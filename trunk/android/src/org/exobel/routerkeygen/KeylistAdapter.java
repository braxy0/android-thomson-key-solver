package org.exobel.routerkeygen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class KeylistAdapter extends BaseAdapter {

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

}
