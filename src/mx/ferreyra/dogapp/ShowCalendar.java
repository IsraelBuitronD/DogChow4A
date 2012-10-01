package mx.ferreyra.dogapp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.ferreyra.dogapp.pojos.FotosMascotaByUsuarioMesAnoResponse;
import mx.ferreyra.dogapp.recursos.Recursos;
import mx.ferreyra.dogapp.ui.UI;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class ShowCalendar extends Activity {


    private final int idButtons[] ={

            R.id.bt_1, R.id.bt_2, R.id.bt_3, R.id.bt_4, R.id.bt_5,
            R.id.bt_6, R.id.bt_7, R.id.bt_8, R.id.bt_9, R.id.bt_10,

            R.id.bt_11, R.id.bt_12, R.id.bt_13, R.id.bt_14, R.id.bt_15,
            R.id.bt_16, R.id.bt_17, R.id.bt_18, R.id.bt_19, R.id.bt_20,

            R.id.bt_21, R.id.bt_22, R.id.bt_23, R.id.bt_24, R.id.bt_25,
            R.id.bt_26, R.id.bt_27, R.id.bt_28, R.id.bt_29, R.id.bt_30,
            R.id.bt_31
    };

    ArrayList<Button>buttons;

    //private Button buttons[]={};

    private View activityRootView;
    private Context context;

    private Button calMonth;
    private Button calYear;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_show_calendar);

        activityRootView = findViewById(R.id.activityRootView);

        //gridview = (GridView) findViewById(R.id.gridview);
        calMonth = (Button) findViewById(R.id.cal_month);
        calYear = (Button) findViewById(R.id.cal_year);

        buttons = new ArrayList<Button>();
        for (int i=0; i< idButtons.length ;i++){
            buttons.add( (Button) findViewById(idButtons[i]));

        }

    }


    public void onclickPhoto (View v){

        if (month == -1 || year == -1 ){
            return;
        }


        if ( v.getTag() != null){
            new DialogPhotoOptions(  (FotosMascotaByUsuarioMesAnoResponse)v.getTag(), context).buildShow();
        }

        else {



            int day =-1;
            for(int i=0; i< idButtons.length ; i++){

                if(idButtons[i] == v.getId()){
                    day =i;
                    break;
                }

            }


            if (day == -1 )
                return;

            String tmpYear =  String.valueOf(2012 - year);
            String tmpMonth =   ((month+1) < 10 ? "0" +(month+1) : "" + (month+1));
            String tmpDay =   ((day+1) < 10 ? "0" +(day+1) : "" + (day+1));


            String tmpDate = tmpYear + "-" + tmpMonth + "-" + tmpDay ;

            Intent intent = new Intent(context, AddDogPhoto.class);
            intent.putExtra("DATE", tmpDate);

            context.startActivity(intent);
        }


    }


    private int month = -1;
    private int year = -1;

    @SuppressLint("NewApi")
    public void  onClickCalMonthButton (View v){

        AlertDialog.Builder builder;
        checkAndHideKeyboard(null);
        builder = Build.VERSION.SDK_INT>=11 ?
                new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT) :
                    new AlertDialog.Builder(context);
                builder.setTitle("Mes");
                builder.setSingleChoiceItems(Recursos.MONTHS_CAL,  year, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        calMonth.setHint(Recursos.MONTHS_CAL[item]);
                        month = item;
                        dialog.dismiss();
                    }
                });
                builder.create().show();


    }

    @SuppressLint("NewApi")
    public void onClickCalYearButton (View v){

        AlertDialog.Builder builder;
        checkAndHideKeyboard(null);
        builder = Build.VERSION.SDK_INT>=11 ?
                new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT) :
                    new AlertDialog.Builder(context);
                builder.setTitle("A\u00f1o");
                builder.setSingleChoiceItems(Recursos.DOG_YEARS,  year, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        calYear.setHint(Recursos.DOG_YEARS[item]);
                        year = item;
                        dialog.dismiss();
                    }
                });
                builder.create().show();



    }

    public void  onClickConsultCalButton (View v){

        if (month == -1 || year == -1 ){
            UI.showAlertDialog("Upps", "Por favor selecione una fecha v\u00e1lida", "ok", context, null);
        }
        else{
            new MyCalAsyncTask().execute();
        }
    }




    static final int MONTHS_LENGHT[]={31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private class MyCalAsyncTask extends AsyncTask<Void, Void,  List<FotosMascotaByUsuarioMesAnoResponse>> {


        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress =ProgressDialog.show(context, "Espere de favor", "Actualizando informaci\u00f3n", true, false);

        }

        @Override
        protected List<FotosMascotaByUsuarioMesAnoResponse> doInBackground(Void... arg0) {


            String bestProvider;
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);

            List<FotosMascotaByUsuarioMesAnoResponse> ans = null;

            WsDogUtils dogUtils = new WsDogUtils();

            Integer userId = DogUtil.getInstance().getCurrentUserId();
            Date date= new Date();

            userId = 10;
            String tmpYear =  String.valueOf(2012 - year);
            String tmpMonth =   ((month+1) < 10 ? "0" +(month+1) : "" + (month+1));

            String tmpDate = tmpYear + "-" + tmpMonth + "-01" ;



            try {
                ans = dogUtils.fotosMascotaByUsuarioMesAnoToPojo(dogUtils.getFotosMascotaByUsuarioMesAno( userId.toString(), tmpDate));
            } catch (Exception e) {
                e.printStackTrace();
                ans = null;
            }

            return ans;
        }


        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute( List<FotosMascotaByUsuarioMesAnoResponse> fotosMascotas) {
            super.onPostExecute(fotosMascotas);

            progress.dismiss();

            for (int i=0; i< idButtons.length ;i++){
                buttons.get(i).setBackgroundDrawable(null);
            }


            if(fotosMascotas != null && fotosMascotas.size() > 0){

                //fotosMascotaAdapter.clear();
                // fotosMascotaAdapter.add(MONTHS_LENGHT[month], null);

                for ( FotosMascotaByUsuarioMesAnoResponse fotoMascota : fotosMascotas){

                    Button button = buttons.get(fotoMascota.getDate().getDay()-1);

                    button.setTag(fotoMascota);

                    button.setBackgroundDrawable(getDrawable(fotoMascota.getPhotoAsBase64Binary()));
                    //fotosMascotaAdapter.add(fotoMascota.getDate().getDay()-1, fotoMascota);
                }

            }
            //adapter.notifyDataSetChanged();
        }


        //TODO kill me
        Drawable getDrawable (String str){
            byte[] bytes = Base64.decode(str, Base64.DEFAULT);
            InputStream is = new ByteArrayInputStream(bytes);
            Bitmap bmp = BitmapFactory.decodeStream(is);

            Drawable icon = new BitmapDrawable(getResources(),bmp);

            icon.setBounds(
                    0 - icon.getIntrinsicWidth() / 2, 0 - icon.getIntrinsicHeight(),
                    icon.getIntrinsicWidth() / 2, 0);

            return  icon;
        }

    }

    public void checkAndHideKeyboard(View view){
        if(getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO){
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activityRootView.getWindowToken(), 0);
        }
    }


}
