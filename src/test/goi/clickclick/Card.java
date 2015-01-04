package test.goi.clickclick;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class Card {

	private String  path, dateTime;
	
	@SuppressWarnings("deprecation")
	public Bitmap makeBitmap(){
		
		int targetW = 350;
		int targetH = 175;
		
		
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(this.path, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	    
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
	    
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
	    
	    return BitmapFactory.decodeFile(this.path, bmOptions);
	}
	
	public Card(File file)
	{
		this.path = file.getAbsolutePath();
		String delim = "[/]+";
		String[] tmp = this.path.split(delim);
		delim = tmp[tmp.length-1];
		this.dateTime = delim.substring(10, 12) + "/" +delim.substring(8, 10) + "/" + delim.substring(4, 8) + 
				" " + delim.substring(13,15) + ":" + delim.substring(15,17) + ":" + delim.substring(17,19);
		Log.e("DATE",dateTime);
		
	}
	
	public String dateTime(){
		return this.dateTime;
	}
	
}
