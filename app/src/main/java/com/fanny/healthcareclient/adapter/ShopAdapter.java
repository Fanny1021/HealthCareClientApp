package com.fanny.healthcareclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.bean.TempData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fanny on 17/8/25.
 */

public class ShopAdapter extends DelegateAdapter.Adapter<ShopAdapter.MainViewHolder> {


    private ArrayList<HashMap<String,Object>> listItem;//存放数据列表

    private Context context;
    private LayoutHelper layoutHelper;
    private RecyclerView.LayoutParams layoutParams;
    private int count=0;

    public ShopAdapter(ArrayList<HashMap<String, Object>> listItem, Context context, LayoutHelper layoutHelper, int count) {
        this(listItem,context,layoutHelper,new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300),count);
    }

    public ShopAdapter(ArrayList<HashMap<String, Object>> listItem, Context context, LayoutHelper layoutHelper, RecyclerView.LayoutParams layoutParams, int count) {
        this.listItem = listItem;
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutParams = layoutParams;
        this.count = count;
    }

    /**
     * 与普通的adapter相差的方位即入下：onCreateLayoutHelper()
     * @return
     */
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.text.setText((String) listItem.get(position).get("ItemTitle"));
        holder.image.setImageResource((Integer) listItem.get(position).get("ItemImage"));
    }

    @Override
    public int getItemCount() {
        return count;
    }


//    private MyItemClickListenet myItemClickListenet;
public class MainViewHolder extends RecyclerView.ViewHolder{

    public TextView text;
    public ImageView image;

        public MainViewHolder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.Item);
            image= (ImageView) itemView.findViewById(R.id.Image);

        }
    }
}
