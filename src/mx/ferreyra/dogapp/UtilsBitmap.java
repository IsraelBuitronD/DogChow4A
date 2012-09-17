package mx.ferreyra.dogapp;

import android.graphics.Bitmap;

public class UtilsBitmap {

	public static Bitmap resize (Bitmap bitmap, int width, int height){

	
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);



		return scaledBitmap;
	}


}

