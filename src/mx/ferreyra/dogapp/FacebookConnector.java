package mx.ferreyra.dogapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import mx.ferreyra.dogmap.UserInfoFB;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.gson.Gson;

/*
 * Post information on Facebook wall
 */
public class FacebookConnector {	

	/** 
	 * Debug Tag for use logging debug output to LogCat 
	 */  
	protected static final String TAG = "Facebook-DogChow";
	private Facebook facebook;
	private Context context;
	private String[] permissions;

	private RouteNaming activity;
	private AsyncFacebookRunner  asyncFacebookRunner;
	private String requestParameter; 

	/*
	 * Constructor to initiate calls
	 */
	public FacebookConnector(String appId,Activity activity,Context context,String[] permissions) {
		this.facebook = new Facebook(appId);

		asyncFacebookRunner = new AsyncFacebookRunner(facebook);
		SessionStore.restore(facebook, context);
		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionLogoutListener);

		this.context=context;
		this.permissions=permissions;		
		if(activity instanceof RouteNaming)
			this.activity=(RouteNaming) activity;

	}

	public Object getData(){

		new Thread(getDataFacebook).start();


		return null;
	} 

	private Runnable getDataFacebook = new Runnable() {
		@Override
		public void run() {
			try {
				String me = facebook.request("me");
				UserInfoFB user = new Gson().fromJson(me, UserInfoFB.class);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/*
	 * Returns access token for authentication 
	 */
	public String getAccessToken(){
		return requestParameter;
	}

	/*
	 * Returns Facebook object
	 */
	public Facebook getFacebook() {
		return facebook;
	}

	/*
	 * Check the validity of the session
	 */
	public boolean isSessionValid(){
		if(facebook != null)
			return facebook.isSessionValid();
		return false;

	}

	/*
	 * Listens Facebook login and handles errors if any 
	 */
	private final class LoginDialogListener implements DialogListener {

		@Override
		public void onCancel() {			
			showMessage(context.getResources().getString(R.string.login_cancel));
		}

		@Override
		public void onComplete(Bundle values) {			
			requestParameter = values.getString("access_token");
			postMessageInThread(requestParameter);
		}

		@Override
		public void onError(DialogError e) {
			showMessage(context.getResources().getString(R.string.error_in_dialog));
		}

		@Override
		public void onFacebookError(FacebookError e) {
			showMessage(context.getResources().getString(R.string.fb_error_msg));
		}
	}

	/*
	 * Call to post message on Facebook wall
	 */
	private void postMessageInThread(final String token) {
		Thread t = new Thread() {
			public void run() {
				try {					
					postMessageOnWall(activity.getFacebookMsg(), token);
				} catch (Exception ex) {
					showMessage(context.getResources().getString(R.string.error_sending_message)+ex.getMessage());					
				}
			}
		};
		t.start();
	}

	/*
	 * Initiate and request to post data on Facebook wall
	 */
	public void postMessageOnWall(String msg, final String token) {
		if (facebook.isSessionValid()) {
			//Check the message limit
			if(msg.length()>420)
				msg = msg.substring(0, 419);

			Bundle params = new Bundle();			
			params.putString("message", msg);
			params.putString(Facebook.TOKEN, token);				
			params.putString("name", context.getString(R.string.dogchow));
			asyncFacebookRunner.request("me/feed", params, "POST", new WallPostRequestListener(), new String("dogchow"));

		} else {
			login();
		}
	}	

	/*
	 * Initiate and request to post data on Facebook wall with stipulated token along with description
	 */
	public void postMessageOnWall(String msg, final String token, final String desc) {
		if (facebook.isSessionValid()) {

			//Check the message limit
			if(msg.length()>420)
				msg = msg.substring(0, 419);

			Bundle params = new Bundle();			
			params.putString("message", msg);
			params.putString(Facebook.TOKEN, token);				
			params.putString("name", context.getString(R.string.dogchow));
			params.putString("description", desc);
			try {
				facebook.request("me/feed", params, "POST");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


		} else {
			login();
		}
	}	

	/*
	 * Login Facebook
	 */
	public void login() {
		if (!facebook.isSessionValid()) {	
			facebook.authorize(this.activity, this.permissions,Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
		}
	}

	private String routeName;
	/*
	 * 
	 */
	public void loginOnly(Context context, String routeName){
		this.context =  context;
		this.routeName = routeName;
		if (!facebook.isSessionValid()) {
			facebook.authorize(this.activity, this.permissions,Facebook.FORCE_DIALOG_AUTH, new AuthenticateListener());
		}
	}

	private final class AuthenticateListener implements DialogListener {

		@Override
		public void onCancel() {			
			Log.d("FacebookConnector","onCancel");
			showMessage(context.getResources().getString(R.string.login_cancel));
		}

		@Override
		public void onComplete(Bundle values) {
			Log.d("FacebookConnector","success");
			requestParameter = values.getString("access_token");	
			SessionStore.save(facebook, context);
			Intent intent = new Intent(context, PublishFacebookScreen.class);
			intent.putExtra("com.dci.dogapp.route_name", routeName);				
			context.startActivity(intent);

		}

		@Override
		public void onError(DialogError e) {
			Log.d("FacebookConnector","dialog error "+e.getMessage());
			showMessage(context.getResources().getString(R.string.error_in_dialog));
		}

		@Override
		public void onFacebookError(FacebookError e) {
			Log.d("FacebookConnector","facebook error "+e.getMessage());
			showMessage(e.getMessage());
		}
	}


	Handler handler = new Handler();
	public class WallPostRequestListener implements AsyncFacebookRunner.RequestListener {

		@Override
		public void onComplete(String response, Object state) {

			if(response.equals(true)){
				handler.post(new Runnable() {
					@Override
					public void run() {
						showMessage(context.getString(R.string.fb_posted_successfully));
					}
				});
			}else{
				showMessage(context.getString(R.string.fb_not_posted_successfully));
			}
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			showMessage(context.getResources().getString(R.string.error_in_save));	

		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			showMessage(context.getResources().getString(R.string.error_in_save));	

		}

		@Override
		public void onIOException(IOException e, Object state) {
			showMessage(context.getResources().getString(R.string.error_in_save));	

		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			showMessage(context.getResources().getString(R.string.error_in_save));	
		}

	}


	SessionEvents.AuthListener mSessionListener = new SessionEvents.AuthListener() {

		@Override
		public void onAuthSucceed() {

		}

		@Override
		public void onAuthFail(String error) {
			showMessage(context.getResources().getString(R.string.login_fails));
		}
	};

	SessionEvents.LogoutListener mSessionLogoutListener = new SessionEvents.LogoutListener() {

		@Override
		public void onLogoutBegin() {			
		}

		@Override
		public void onLogoutFinish() {
			showMessage(context.getResources().getString(R.string.logout_success));
		}


	};

	private void showMessage(final String message){	
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
			}
		});			
	}
}