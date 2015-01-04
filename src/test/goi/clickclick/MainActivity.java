package test.goi.clickclick;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import test.goi.clickclick.sqlite.LatlongDataSource;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ActionBarActivity  implements
ConnectionCallbacks, OnConnectionFailedListener, LocationListener{
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	private ListView cardList;
	private List<Card> cards;
	private CardDataAdapter cardAdapter;
	private int loaded, limit, len;
	private File[] list;
	private LocationRequest mLocationRequest;
	private GoogleApiClient mGoogleApiClient;
	private boolean mRequestingLocationUpdates;
	private LatlongDataSource dbSource;
	
	
	private static File getOutputMediaFile(){

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "ClickClick");
	    
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("ClickClick", "failed to create directory");
	            return null;
	        }
	    }
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
	    File mediaFile;
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    
	    return mediaFile;
	}
	
	private static Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}
	
	protected synchronized void buildGoogleApiClient() {
	    mGoogleApiClient = new GoogleApiClient.Builder(this)
	        .addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this)
	        .addApi(LocationServices.API)
	        .build();
	}

	protected void createLocationRequest() {
	    mLocationRequest = new LocationRequest();
	    mLocationRequest.setInterval(10000);
	    mLocationRequest.setFastestInterval(5000);
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
	}

	@Override
	public void onConnected(Bundle connectionHint) {
	    Log.e("Connection", "Connected");
	    if (mRequestingLocationUpdates) {
	        startLocationUpdates();
	    }
	}

	protected void startLocationUpdates() {
		if(mGoogleApiClient.isConnected()){	
			LocationServices.FusedLocationApi.requestLocationUpdates(
	            mGoogleApiClient, mLocationRequest, this);
			Toast.makeText(this, "Tracking", Toast.LENGTH_SHORT).show();
		}
		else{
			mGoogleApiClient.connect();
		}
	}
	
	protected void stopLocationUpdates() {
		if(mGoogleApiClient.isConnected()){
	    	LocationServices.FusedLocationApi.removeLocationUpdates(
		            mGoogleApiClient, this);
	    	Toast.makeText(this, "Tracking Stopped", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buildGoogleApiClient();
		createLocationRequest();
		mGoogleApiClient.connect();
		
		mRequestingLocationUpdates = false;
		
		dbSource = new LatlongDataSource(this);
		dbSource.open();
		
		cardList = (ListView)findViewById(R.id.cards);	
		File f = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "ClickClick");
		
		cards = new ArrayList<Card>();
		cardAdapter = new CardDataAdapter(this, cards);
		cardList.setAdapter(cardAdapter);
		
		if(f.exists())
		{
			TextView tv = (TextView)findViewById(R.id.textView1);
			tv.setVisibility(View.GONE);
			limit = 5;
			list = f.listFiles();
			len = list.length;
			
			loaded = (limit > len) ? len : limit;
				
			for(int i=0; i<loaded; i++){
				cards.add(new Card(list[len-1-i]));
			}
			cardAdapter.notifyDataSetChanged();
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_click) {
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUri = getOutputMediaFileUri();

			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			
		}
		if(id == R.id.action_track){
			if (!mRequestingLocationUpdates){
				mRequestingLocationUpdates = true;
				startLocationUpdates();
			}
		}
		if(id == R.id.action_untrack){
			if(mRequestingLocationUpdates){
				mRequestingLocationUpdates = false;
				stopLocationUpdates();
			}
		}
		if (id == R.id.action_load) {
			limit += 5;
			if(limit > len)
				limit = len;
			
			int k = loaded;
			
			loaded = (limit > len) ? len : limit;
			
			for(int i=k; i<loaded; i++){
				cards.add(new Card(list[len-1-i]));
			}
			cardAdapter.notifyDataSetChanged();
		}
		if (id == R.id.count){
			Toast.makeText(this, String.valueOf(dbSource.getAllEntries()), Toast.LENGTH_SHORT).show();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {	
	        	limit += 1;
	        	File f = new File(Environment.getExternalStoragePublicDirectory(
	  	              Environment.DIRECTORY_PICTURES), "ClickClick");
	        	
	        	list = f.listFiles();
	        	len = list.length;
	        	
	        	cards.add(0, new Card(list[len-1]));
	        	cardAdapter.notifyDataSetChanged();
				
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    if(mRequestingLocationUpdates){
	    	mRequestingLocationUpdates = false;
	    	stopLocationUpdates();
	    }
	}
	
	@Override
    protected void onStop() {
		Log.d("Connection","disconnected");
        mGoogleApiClient.disconnect();
        super.onStop();
    }	
	
	@Override
	public void onResume() {
	    super.onResume();
	    if (!mRequestingLocationUpdates) {
	        startLocationUpdates();
	    }
	}

	@Override
	public void onLocationChanged(Location location) {
		String latlong = String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude());
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		dbSource.addLatLong(timestamp, location.getLatitude(), location.getLongitude());
		Toast.makeText(this, latlong, Toast.LENGTH_SHORT).show();
		Log.e("Got Location", latlong);
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Log.e("Connection","Failed");
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		Log.e("Connection","Suspeneded");
	}
}
