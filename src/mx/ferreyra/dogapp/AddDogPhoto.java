package mx.ferreyra.dogapp;

import java.io.ByteArrayOutputStream;
import java.util.List;

import mx.ferreyra.dogapp.ui.UI;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddDogPhoto extends Activity {

    private Bitmap dogImage;
    private ImageView dogPhoto;
    private EditText dogPhotoFoot;

    // Intent results
    private final int ADD_PHOTO_FROM_STORAGE = 0x01;
    private final int ADD_PHOTO_FROM_CAMARA = 0x00;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog_photo);

        // Load view controls
        dogPhoto     = (ImageView)findViewById(R.id.dog_photo);
        dogPhotoFoot = (EditText)findViewById(R.id.dog_photo_foot);
    }

    public void onClickDogPhotoButton(View view) {
        String[] items = {
                getString(R.string.take_from_images_stored),
                getString(R.string.take_from_camera)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.take_image));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0)
                    getPhotofromAlbum();
                else if (item == 1)
                    takePhoto();
            }
        });
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onClickRemoveDogPhotoButton(View view) {
        // TODO finish and verify this method
        dogImage = null;
        dogPhoto.setImageResource(R.drawable.bg_avatar_camera);
    }

    public void onClickAddPhotoButton(View view) {
        // Validate form
        if(!isValidForm())
            return;

        // Invoke webservices using asynctask
        AddDogPhotoTask task = new AddDogPhotoTask(this);
        String[] params = viewToArray();
        task.execute(params);
    }

    public String[] viewToArray() {
        // Dog Image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        dogImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encodedImageStr = Base64.encodeToString(byteArray,Base64.DEFAULT);

        // Dog photo foot
        String foot = dogPhotoFoot.getText().toString();

        return new String[] {
            encodedImageStr,
            foot
        };
    }

    private void getPhotofromAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, ADD_PHOTO_FROM_STORAGE);
    }

    private void takePhoto() {
        final String action = MediaStore.ACTION_IMAGE_CAPTURE;
        Intent intent = new Intent(action);
        List<ResolveInfo> list = this.getPackageManager()
                                     .queryIntentActivities(intent,
                                                            PackageManager.MATCH_DEFAULT_ONLY);
        if(list.size() > 0)
            startActivityForResult(new Intent(action), ADD_PHOTO_FROM_CAMARA );
    }

    private boolean isValidForm() {
        // Check selected image
        if(dogImage == null) {
            UI.showAlertDialog(getString(R.string.validation_alert_dialog_title),
                               getString(R.string.dog_image_not_selected_dialog_message),
                               getString(android.R.string.ok), this, null);
            return false;
        }

        return true;
    }

    private class AddDogPhotoTask extends AsyncTask<String, Integer, Integer> {

        private final Context context;
        private ProgressDialog dialog;

        public AddDogPhotoTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.please_wait_signing_up));
            dialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            WsDogUtils wsDogUtils = new WsDogUtils(context);
            Integer userId = DogUtil.getInstance().getCurrentUserId();
            Integer result = null;
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            // Stop and hide dialog
            dialog.dismiss();

            // Check result
            if(result == null || result < 0) {
                // Something wrong happened
            } else {
                // Notify successful process
            }
        }
    }
}
