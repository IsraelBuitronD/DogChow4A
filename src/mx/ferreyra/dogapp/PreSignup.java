package mx.ferreyra.dogapp;

import mx.ferreyra.dogapp.ui.UI;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PreSignup extends Activity {

	public static final Integer SIGN_UP = 10;
	public static final Integer LOGIN = 11;
	public static final Integer RECOVER = 12;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre_signup);
	}

	public void onClickSignupButton(View view) {
		startActivityForResult(new Intent(this, SignupActivity.class), SIGN_UP);
	}

	public void onClickLoginButton(View view) {
		startActivityForResult(new Intent(this, LoginActivity.class), LOGIN);
	}

	public void onClickRecoverPasswordButton(View view) {
		startActivityForResult(new Intent(this, RetrievePassword.class), RECOVER);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == Activity.RESULT_OK && intent != null){

			if (requestCode == SIGN_UP || requestCode == LOGIN){

				Bundle extras = intent.getExtras();
				Integer idUser = (Integer) extras.get("ID_USER");


				if (idUser < 0){
					UI.showAlertDialog(getString(R.string.ups),
							getString(R.string.an_error_ocurred),
							getString(android.R.string.ok),
							this, null);
				}
				else {

					Intent intent1 = new Intent();
					intent1.putExtra("ID_USER", idUser);

					setResult(RESULT_OK, intent1);
					finish();
				}



			}

			else if (requestCode == RECOVER){

			}

		}


	}





}
