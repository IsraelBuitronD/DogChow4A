package mx.ferreyra.dogapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import mx.ferreyra.dogapp.AppData.USER_LOGIN_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

/*
 * Draws route directions but obtain location information
 */
public class ExerciseMenu extends Activity{

	public String mapurl;
	public String actualkm;

	private Intent i;
	private Button title_right;
	private TextView title_txt;
	private ProgressBar progress_title;
	private FacebookConnector facebookConnector;
	private GoogleAnalyticsTracker analyticsTracker;
	private DogUtil app;

	//get gps strength in android
	LocationManager locMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.exercisemenu);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		app = DogUtil.getInstance();
		titleBar = (ProgressBar)findViewById(R.id.progress_title);
		titleBar.setVisibility(View.INVISIBLE);

		title_right= (Button)findViewById(R.id.tbutton_right);

		title_right.setText(getResources().getString(R.string.logout));
		title_right.setTextColor(Color.WHITE);
		title_right.setBackgroundResource(R.drawable.custom_button);	
		title_right.setVisibility(View.VISIBLE);

		title_right.setTextSize(12);
		progress_title = (ProgressBar)findViewById(R.id.progress_title);
		analyticsTracker = ((DogUtil)getApplication()).getTracker();
		title_txt = (TextView)findViewById(R.id.title_txt);

		title_txt.setText(getString(R.string.exercise_title));
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(analyticsTracker == null)
			analyticsTracker = ((DogUtil)getApplication()).getTracker();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(analyticsTracker != null)
			analyticsTracker.dispatch();
	}

	private Facebook facebook;
	public void logoutFb(){
		if(facebook.isSessionValid()){			
			AsyncFacebookRunner asyncFacebookRunner = new AsyncFacebookRunner(facebook);
			asyncFacebookRunner.logout(getApplicationContext(), new RequestListener() {

				@Override
				public void onMalformedURLException(MalformedURLException e, Object state) {
					Log.e("DogChowMX", "Error : "+e.getMessage());
					setTitleInvisible();
				}

				@Override
				public void onIOException(IOException e, Object state) {
					Log.e("DogChowMX", "Network unreachable: "+e.getMessage());
					setTitleInvisible();

				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e, Object state) {
					Log.e("DogChowMX", "onFileNotFoundException: "+e.getMessage());
					setTitleInvisible();

				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
					Log.e("DogChowMX", "Facebook error: "+e.getMessage());
					setTitleInvisible();
				}

				@Override
				public void onComplete(String response, Object state) {
					setTitleInvisible();
					if(response.equals(true) || response.equals("true") ){
						handler.post(new Runnable() {
							@Override
							public void run() {
								SessionStore.clear(getApplicationContext());
								Toast.makeText(getApplicationContext(), getString(R.string.fb_user_logout_success), Toast.LENGTH_SHORT).show();

								SharedPreferences pref = getSharedPreferences(Utilities.DOGCHOW, 0);
								SharedPreferences.Editor edit = pref.edit();
								edit.putString(Utilities.USER_ID,""); 
								edit.commit();

								i =new Intent(ExerciseMenu.this,MainActivity.class); 
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
								i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);				
								startActivity(i);
								finish();

							}
						});
					}else
						try {
							JSONObject myjson = new JSONObject(response);
							final String errorMessage = myjson.getString("error_msg");								
							handler.post(new Runnable() {
								@Override
								public void run() {
									if(errorMessage != null )
										Toast.makeText(getApplicationContext(),  getString(R.string.fb_user_logout_failure), Toast.LENGTH_SHORT).show();
									finish();
								}
							});


						} catch (JSONException e) {
							setTitleInvisible();
							e.printStackTrace();
						}catch (Exception e) {
							setTitleInvisible();
							e.printStackTrace();
						}

				}
			});

		}else{
			SessionStore.clear(getApplicationContext());
			setTitleInvisible();
		}
	}

	private boolean isNLP = false;

	/*
	 * Check Wi-Fi and GPS connection exist to show map
	 */
	private boolean isRouteShown(){
		boolean connection = false;
		String provider =Settings.Secure.getString(getContentResolver(),Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if(!provider.contains("gps")){ //if gps is disabled			
			isNLP = true;
			connection = false;
			return connection;
		}


		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		//mobile
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

		//wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();


		if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
			//mobile
			connection = true;
		} else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
			//wifi
			connection = true;
		}

		if(!connection)
			return isNLP = connection;

		return connection;
	}

	/*
	 * Display settings page to enable connection for Maps API Implementation
	 */
	public void testalert(){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		String message;
		if(isNLP)
			message = getResources().getString(R.string.gps_disabled);
		else
			message = getResources().getString(R.string.mobile_newtork_disabled);
		alt_bld.setMessage(message)
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if(isNLP){
					startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
				}
				else{
					final Intent intent = new Intent(Intent.ACTION_MAIN, null);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
					intent.setComponent(cn);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity( intent);
				}
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(getResources().getString(R.string.app_name));
		alert.setIcon(R.drawable.icon);
		alert.show();
	}
	private ProgressBar titleBar;
	Handler handler = new Handler();
	public void setTitleInvisible(){
		handler.post(new Runnable() {

			@Override
			public void run() {
				titleBar.setVisibility(View.INVISIBLE);
			}
		});
	}


	@Override
	protected void onStop() {
		super.onStop();

		if(analyticsTracker != null)
			analyticsTracker.stopSession();
	}

	@SuppressWarnings("unused")
	private void clearCredentials() {
		try {
			if(facebookConnector != null)
				facebookConnector.getFacebook().logout(getApplicationContext());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onClickNewRouteButton(View v) {
		boolean isRouteShow = false;
		boolean isError = false;

		try{
			isRouteShow = isRouteShown();
		} catch(Exception e) {
			Log.e(this.getClass().getSimpleName(), ""+e.getMessage());
			isError = true;
		}

		if(!isRouteShow && !isError){
			testalert();
		} else {
			analyticsTracker.trackEvent("New Route",            // Category, i.e. New Route Button
					"Button",               // Action, i.e. New Route
					"clicked",              // Label    i.e. New Route
					DogUtil.TRACKER_VALUE); // Value

			DogUtil.TRACKER_VALUE++;

			if(app.getCurrentUserId()==null) {
				startActivityForResult(new Intent(this, PreSignup.class), DogUtil.NEW_ROUTE);
			} else {
				Intent i = new Intent(this, Starting.class);
				i.putExtra("loadroute", 2);
				startActivity(i);
			}
		}
	}

	public void onClickLoadRouteButton(View v) {
		if(app.getCurrentUserId()==null) {
			startActivityForResult(new Intent(this, PreSignup.class), DogUtil.LOAD_ROUTE);
		} else {
			Intent i = new Intent(this, Starting.class);
			i.putExtra("loadroute", 1);
			startActivity(i);
		}
		analyticsTracker.trackEvent("Load Route",            // Category, i.e. New Route Button
				"Button",                // Action, i.e. New Route
				"clicked",               // Label    i.e. New Route
				DogUtil.TRACKER_VALUE);  // Value,

		DogUtil.TRACKER_VALUE++;
	}

	public void onClickStatisticsButton(View v) {
		if(app.getCurrentUserId()==null) {
			startActivityForResult(new Intent(this, PreSignup.class), DogUtil.STATISTICS);
		} else {
			startActivity(new Intent(this, Report.class));
		}
		analyticsTracker.trackEvent("Statictics",           // Category, i.e. Statictics Button
				"Button",               // Action, i.e. New Route
				"clicked",              // Label    i.e. New Route
				DogUtil.TRACKER_VALUE); // Value,
		DogUtil.TRACKER_VALUE++;
	}

	public void onClickDogWelfare(View v) {
		if(app.getCurrentUserId()==null) {
			startActivityForResult(new Intent(this, PreSignup.class), DogUtil.DOGWELFARE);
		} else {
			startActivity(new Intent(this, DogRegister.class));
		}
	}

	public void onClickTButtonLeftButton(View v) {
		finish();
	}

	public void onClickTButtonRightButton(View v) {
		progress_title.setVisibility(View.VISIBLE);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(AppData.getLoginType() == USER_LOGIN_TYPE.APPLICATION){
					SharedPreferences pref = getSharedPreferences(Utilities.DOGCHOW, 0);
					SharedPreferences.Editor edit = pref.edit();
					edit.putString(Utilities.USER_ID,"");
					edit.commit();

					i =new Intent(ExerciseMenu.this,MainActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
					finish();
					progress_title.setVisibility(View.INVISIBLE);
				} else if (AppData.getLoginType() == USER_LOGIN_TYPE.FACEBOOK){
					title_right.setVisibility(View.INVISIBLE);
					titleBar.setVisibility(View.VISIBLE);

					facebook = ((DogUtil)getApplication()).getFacebook();
					logoutFb();
				} else {
					SharedPreferences pref = getSharedPreferences(Utilities.DOGCHOW, 0);
					SharedPreferences.Editor edit = pref.edit();
					edit.putString(Utilities.USER_ID,"");
					edit.commit();

					i =new Intent(ExerciseMenu.this,MainActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
					finish();
					progress_title.setVisibility(View.INVISIBLE);
				}
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == Activity.RESULT_OK && intent != null){

			if ( requestCode == DogUtil.NEW_ROUTE){

				Bundle extras = intent.getExtras();
				Integer idUser = (Integer) extras.get("ID_USER");

				if (idUser>0){
					Intent i = new Intent(this, Starting.class);
					i.putExtra("loadroute", 2);
					startActivity(i);
				}



			}
			else if (requestCode == DogUtil.LOAD_ROUTE){
				Intent i = new Intent(this, Starting.class);
				i.putExtra("loadroute", 1);
				startActivity(i);
			}
			else if (requestCode == DogUtil.DOGWELFARE){
				startActivity(new Intent(this, DogRegister.class));
			}

		}
	}

}
