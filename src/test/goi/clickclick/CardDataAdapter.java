package test.goi.clickclick;

import java.util.List;

import test.goi.clickclick.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CardDataAdapter extends ArrayAdapter<Card> {

	private Context context;
	private List<Card> cards;
	
	
	public CardDataAdapter(Context context, List<Card> cards){
		super(context,R.layout.listlayout,cards);
		this.context = context;
		this.cards = cards;
	}

	static class ViewHolder {
		  TextView timestamp;
		  ImageView click;
		  int position;
		}
	
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		
		ViewHolder holder;
		if(view == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listlayout, parent, false);
			
			holder = new ViewHolder();
			holder.click = (ImageView)view.findViewById(R.id.click);
			holder.timestamp = (TextView)view.findViewById(R.id.dateTime);
			view.setTag(holder);
		}
		else{
			holder = (ViewHolder)view.getTag();
		}
		
		holder.timestamp.setText(cards.get(position).dateTime());
		
		new AsyncTask<ViewHolder, Void, Bitmap>(){
			
			private ViewHolder v;
			
			@Override
			protected Bitmap doInBackground(ViewHolder... params) {
				v = params[0];
				return cards.get(position).makeBitmap();
			}
			
			@Override
			protected void onProgressUpdate(Void...progress) {
				v.click.setImageBitmap(null);
			}
			
			@Override
		    protected void onPostExecute(Bitmap result) {
					v.click.setImageBitmap(result);
		    }
			
		}.execute(holder);
		
		return view;
	}
	
	
}
