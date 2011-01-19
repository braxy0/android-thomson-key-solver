package com.thomson;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WifiListAdapter extends BaseAdapter {
	private List<String> listESSID; 
	private Context context; 
	private List<Integer> listStrength;
	public WifiListAdapter(List<String> List, Context context) {
        this.listESSID = List;
        this.context = context;
        this.listStrength = new ArrayList<Integer>();
    }
	
	public void setStrenght( List<Integer> listStrength ){
		this.listStrength = listStrength;
	}
	@Override
	public int getCount() {
		return listESSID.size();
	}

	@Override
	public Object getItem(int position) {
		return listESSID.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listESSID.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 RelativeLayout itemLayout;
		 String wifi = listESSID.get(position);
		 int strenght = listStrength.get(position);
	     itemLayout= (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.list_wifi, parent, false);
	 
	     TextView tvUser = (TextView) itemLayout.findViewById(R.id.wifiName);
	     tvUser.setText(wifi);
	     ImageView strenghtPic = (ImageView)itemLayout.findViewById(R.id.strenght);
	     if ( strenght < 1 )
	    	 strenghtPic.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_delete));
		return  itemLayout;
	}

}
