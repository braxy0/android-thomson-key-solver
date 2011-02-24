package org.exobel.routerkeygen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class KeylistAdapter implements ListAdapter {

	private List<String> listKeys; 
	private Context context; 
	public KeylistAdapter(List<String> list, Context context) {
		if ( list != null )
			this.listKeys = list;
		else
			this.listKeys = new ArrayList<String>();
        this.context = context;
    }
	
	public int getCount() {
		return listKeys.size();
	}

	public Object getItem(int position) {
		return listKeys.get(position);
	}

	public long getItemId(int position) {
		return listKeys.get(position).hashCode();
	}

	public View getView(int position, View arg1, ViewGroup parent) {
		RelativeLayout itemLayout;
	    itemLayout= (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_list_keys, parent, false);
	    TextView key = (TextView) itemLayout.findViewById(R.id.key_item);
	     key.setText(listKeys.get(position));
	    return itemLayout;
	}

	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

}
