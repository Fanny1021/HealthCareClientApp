package com.fanny.healthcareclient.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.bean.TempData;

import java.util.List;

/**
 * Created by Fanny on 17/7/31.
 */

public class UserChoiceSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;

    public UserChoiceSpinnerAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(mContext).inflate(R.layout.my_drop_down_item,null);
        if(convertView!=null){
            TextView name= (TextView) convertView.findViewById(R.id.tv_name);
            name.setText(mList.get(position));
        }
        return convertView;
    }
}
