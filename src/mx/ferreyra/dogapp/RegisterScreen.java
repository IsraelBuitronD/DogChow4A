package mx.ferreyra.dogapp;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import mx.ferreyra.dogapp.AppData.USER_LOGIN_TYPE;
import mx.ferreyra.dogapp.SessionEvents.AuthListener;
import mx.ferreyra.dogapp.SessionEvents.LogoutListener;
import mx.ferreyra.dogapp.org.ksoap2.SoapEnvelope;
import mx.ferreyra.dogapp.org.ksoap2.serialization.SoapObject;
import mx.ferreyra.dogapp.org.ksoap2.serialization.SoapSerializationEnvelope;
import mx.ferreyra.dogapp.org.ksoap2.transport.HttpTransportSE;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class RegisterScreen extends Activity{

	private Button fbBtn,manualBtn;
	Intent i; 
	Facebook authenticatedFacebook = new Facebook(DogUtil.FACEBOOK_APPID);

	public Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;

	private ProgressBar pb;
	String fbId, fbName, fbEmail;
	private TextView txtTitle;
	private Button txtBackBtn, txtHomeBtn;
	private boolean isWindowOpen = true;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.register); 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);


		txtBackBtn = (Button)findViewById(R.id.tbutton_left);
		txtTitle = (TextView)findViewById(R.id.title_txt);
		txtHomeBtn = (Button)findViewById(R.id.tbutton_right);

		txtHomeBtn.setVisibility(View.INVISIBLE);
		txtTitle.setText(getResources().getString(R.string.register));

		fbBtn = (Button)findViewById(R.id.loginBtn);
		manualBtn = (Button)findViewById(R.id.registerBtn);
		pb = (ProgressBar)findViewById(R.id.register_progress);
		pb.setVisibility(View.INVISIBLE);

		txtBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		fbBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				fbLogin();
			}
		});

		manualBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				i =new Intent(RegisterScreen.this,ManualRegister.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		});


	} 

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isWindowOpen = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();

		if(pb != null)
			pb.setVisibility(View.INVISIBLE);		
		
	}

	//Send facebook login to server and 
	private class RegisterTask implements Runnable{

		private String errorMsg;
		private String resultData;
		private String response;
		@Override
		public void run() {
			try {
				isWindowOpen = true;
				//Log.i("Register-Screen", " resultData"+resultData);
				// process the response here: executed in background thread)				
				final JSONObject json = Util.parseJson(resultData);

				final String email = json.getString("email"); 

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."

				//showAlert("Hello there, " + name + "!");

				SoapObject request = new SoapObject(AppData.NAMESPACE,
						AppData.METHOD_NAME_USER_REGSTRATION);       

				final String is_facebook = "1";
				final String first_name = json.getString("first_name");


				request.addProperty("username", email);

				request.addProperty("password", first_name);
				request.addProperty("isFacebook", is_facebook);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet=true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(AppData.URL);

				androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

				// Make the soap call.
				androidHttpTransport.debug=true;

				androidHttpTransport.call(AppData.SOAP_ACTION+AppData.METHOD_NAME_USER_REGSTRATION, envelope);

				response = envelope.getResponse().toString();



			}
			catch (UnknownHostException e) {
				progressTitle();
				errorMsg = getResources().getString(R.string.no_connection);
				Log.e(this.getClass().getSimpleName(), errorMsg);				
			}catch (UnknownServiceException e) {
				progressTitle();
				errorMsg = getResources().getString(R.string.service_unavailable);				
				Log.e(this.getClass().getSimpleName(),  getResources().getString(R.string.service_unavailable));
			}catch (MalformedURLException e) {
				progressTitle();
				errorMsg = getResources().getString(R.string.url_malformed);	
				e.printStackTrace();
				Log.e(this.getClass().getSimpleName(), errorMsg );
			}catch (JSONException e) {
				progressTitle();
				errorMsg = getResources().getString(R.string.parse_error) ;	
				Log.e(this.getClass().getSimpleName(), errorMsg);
				e.printStackTrace();
			} catch (FacebookError e) {
				progressTitle();
				errorMsg = getResources().getString(R.string.fb_error);	
				Log.e(this.getClass().getSimpleName(), errorMsg);
				e.printStackTrace();
			}catch (XmlPullParserException e) {
				progressTitle();
				errorMsg = getResources().getString(R.string.unable_to_parse);	
				Log.e(this.getClass().getSimpleName(), errorMsg);
			} catch (IOException e) {
				progressTitle();
				errorMsg = getResources().getString(R.string.io_error);	
				Log.e(this.getClass().getSimpleName(), errorMsg);
			} catch (Exception e) {
				progressTitle();
				errorMsg = getResources().getString(R.string.error_in_register) ;	
				Log.e(this.getClass().getSimpleName(), errorMsg);
			}

			new Handler().post(new Runnable() {

				@Override
				public void run() {
					if(errorMsg == null){
						Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
						if(response != null){
							
							progressTitle();
							
							SharedPreferences pref = getSharedPreferences(Utilities.DOGCHOW, 0);
							SharedPreferences.Editor edit = pref.edit();
							edit.putString(Utilities.USER_ID,response); 
							edit.commit();

							AppData.USER_ID = response.trim();
							AppData.assignType(USER_LOGIN_TYPE.FACEBOOK); 


							SharedPreferences sharedPreferences = getSharedPreferences(Utilities.DOGAPP, MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPreferences.edit();
							editor.putString(Utilities.ROUTE_TIMING, getResources().getString(R.string.route_time));
							editor.putString(Utilities.ROUTE_DISTANCE, getResources().getString(R.string.route_distance));
							editor.putString(Utilities.ROUTE_SPEED, getResources().getString(R.string.route_speed));
							editor.commit();

							Intent signinPage = new Intent(RegisterScreen.this, LoginMainScreen.class);
							AppData.IS_FIRST = true;
							startActivity(signinPage);
							finish();
						}
					}else{
						progressTitle();
						Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
					}
				}
			});

		}

	}



	private void fbLogin() {
		try{
			mx.ferreyra.dogapp.DogUtil ac = (mx.ferreyra.dogapp.DogUtil) getApplication();
			ac.initFacebook();  

			mFacebook = ac.getFacebook();
			mAsyncRunner = ac.getAsyncFacebookRunner();

			SessionStore.restore(mFacebook, this);
			SessionEvents.addAuthListener(new SampleAuthListener());
			SessionEvents.addLogoutListener(new SampleLogoutListener());
			//Log.i("Register-Screen", "Expires: "+mFacebook.getAccessExpires());
			//Log.i("Register-Screen", "Token "+mFacebook.getAccessToken());
			//Log.i("Register-Screen", "Session valid "+mFacebook.isSessionValid());
			
			if(!mFacebook.isSessionValid()){
				//Log.i("Register-Screen", "Start to authorize "+mFacebook);
				isWindowOpen = false;
				SessionStore.clear(getApplicationContext());
				mFacebook.authorize(RegisterScreen.this, ac.getPermissions(),Facebook.FORCE_DIALOG_AUTH, new UserInfoListener());
				//Log.i("Register-Screen", "Start to authorized "+ac.getPermissions());
			}else{
				i =new Intent(RegisterScreen.this, LoginMainScreen.class); 
				startActivity(i);
				finish();
			}
		}catch (Exception e) {
			progressTitle();
			e.printStackTrace();
		}


	}


	public class SampleAuthListener implements AuthListener {

		public void onAuthSucceed() {
			Log.i("Register-Screen", "Authendication success");
			progressTitle();
			isWindowOpen = true;
		}

		public void onAuthFail(String error) {
			Log.i("Register-Screen", "Authendication fails : "+error);
			progressTitle();
			isWindowOpen = true;
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			Log.i("Register-Screen", "Login begins");
		}

		public void onLogoutFinish() {
			Log.i("Register-Screen", "Login finished");
			progressTitle();
			isWindowOpen = true;
		}
		
		
	}

	public class UserInfoListener implements DialogListener {

		public void onComplete(Bundle values) {
			try{
				isWindowOpen = true;
				//Log.i("Register-Screen", "Values: "+values);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						pb.setVisibility(View.VISIBLE);
						
					}
				});
				mAsyncRunner.request("me", new FacebookUserRequestListener());				
			}
			catch (Exception e) {
				isWindowOpen = true;
				progressTitle();
				e.printStackTrace();
			}
		}
		public void onCancel() {
			isWindowOpen = true;
			progressTitle();
			Log.i("Register-Screen", "Fb cancelled in authentication");
		}

		public void onError(DialogError e) {
			isWindowOpen = true;
			progressTitle();
			Log.i("Register-Screen", "Fb Dialog error in authentication");
			e.printStackTrace();
		}

		public void onFacebookError(FacebookError e) {
			isWindowOpen = true;
			progressTitle();
			Log.i("Register-Screen", "Fb Facebook error in authentication");
			e.printStackTrace();			
		}
		
		
	}

	public class FacebookUserRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {	
			//Log.i("Register-Screen", "response: "+response + " state"+state);
			isWindowOpen = true;
			RegisterTask registerTask = new RegisterTask();
			registerTask.resultData = response;
			runOnUiThread(registerTask);
		}
		
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			super.onFacebookError(e, state);
			isWindowOpen = true;
			progressTitle();
			Log.e("Register-Screen", "Error in Facebook authorization");
			e.printStackTrace();
		}
		
		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub
			super.onFileNotFoundException(e, state);
			isWindowOpen = true;
			progressTitle();
			Log.e("Register-Screen", "Error in Facebook authorization due to file doesn\'t exist");
			e.printStackTrace();
		}
		
		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub
			super.onIOException(e, state);
			isWindowOpen = true;
			progressTitle();
			Log.e("Register-Screen", "Error in Facebook authorization due to network");
			e.printStackTrace();
		}
		
		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub
			super.onMalformedURLException(e, state);
			isWindowOpen = true;
			progressTitle();
			Log.e("Register-Screen", "Error in Facebook authorization due to url");
			e.printStackTrace();
		}
		
	}


	public void postServer(String response) {
		isWindowOpen = true;
		RegisterTask registerTask = new RegisterTask();
		registerTask.resultData = response;
		runOnUiThread(registerTask);


	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("Register-Screen", ""+resultCode + " "+resultCode + " "+data);
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}
	
	Handler handler = new Handler();

	private void progressTitle(){
		handler.post(new Runnable() {

			@Override
			public void run() {
				pb.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(isWindowOpen){
				progressTitle();
				return super.onKeyDown(keyCode, event);
			}else{
				progressTitle();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	} 
}