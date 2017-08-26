package com.fanny.healthcareclient.activity;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.adapter.ShopAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ShopMarketActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    ShopAdapter Adapter_linearLayout,Adapter_GridLayout,Adapter_FixLayout,Adapter_ScrollFixLayout
            ,Adapter_FloatLayout,Adapter_ColumnLayout,Adapter_SingleLayout,Adapter_onePlusNLayout,
            Adapter_StickyLayout,Adapter_StaggeredGridLayout;

    private ArrayList<HashMap<String,Object>> listItem;
    private Toolbar toolbar;
    private ImageView im_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_market);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        im_return = (ImageView) findViewById(R.id.iv_search_return);
        im_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycle_view);

        VirtualLayoutManager layoutManager=new VirtualLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.RecycledViewPool viewPool=new RecyclerView.RecycledViewPool();
        mRecyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0,10);

        initData();

        initView();

        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
        adapters.add(Adapter_linearLayout);
        adapters.add(Adapter_GridLayout);

        DelegateAdapter delegateAdapter=new DelegateAdapter(layoutManager);
        delegateAdapter.setAdapters(adapters);

        mRecyclerView.setAdapter(delegateAdapter);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(5, 5, 5, 5);
            }
        });
    }


    private void initData() {
        listItem=new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<100;i++){
            HashMap<String ,Object> map=new HashMap<>();
            map.put("ItemTitle", "第" + i + "行");
            map.put("ItemImage", R.mipmap.ic_launcher);
            listItem.add(map);
        }
    }

    private void initView() {
        /**
         * lineary
         */
        LinearLayoutHelper linearLayoutHelper=new LinearLayoutHelper();
        linearLayoutHelper.setItemCount(50);// 设置布局里Item个数
        linearLayoutHelper.setPadding(20, 20, 20, 20);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        linearLayoutHelper.setMargin(20, 20, 20, 20);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        // linearLayoutHelper.setBgColor(Color.GRAY);// 设置背景颜色
        linearLayoutHelper.setAspectRatio(6);// 设置设置布局内每行布局的宽与高的比

        // linearLayoutHelper特有属性
        linearLayoutHelper.setDividerHeight(10);
        // 设置间隔高度
        // 设置的间隔会与RecyclerView的addItemDecoration（）添加的间隔叠加.

        linearLayoutHelper.setMarginBottom(100);
        // 设置布局底部与下个布局的间隔

        /**
         * gridlayout
         */
        GridLayoutHelper gridLayoutHelper=new GridLayoutHelper(3);
        gridLayoutHelper.setItemCount(50);// 设置布局里Item个数
        gridLayoutHelper.setPadding(20, 20, 20, 20);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        gridLayoutHelper.setMargin(20, 20, 20, 20);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        // linearLayoutHelper.setBgColor(Color.GRAY);// 设置背景颜色
        gridLayoutHelper.setAspectRatio(6);// 设置设置布局内每行布局的宽与高的比

        // 设置间隔高度
        // 设置的间隔会与RecyclerView的addItemDecoration（）添加的间隔叠加.

        gridLayoutHelper.setMarginBottom(100);
        // 设置布局底部与下个布局的间隔


        // 创建自定义的Adapter对象 & 绑定数据 & 绑定对应的LayoutHelper进行布局绘制
        Adapter_linearLayout  = new ShopAdapter(listItem,this, linearLayoutHelper, 5) {
            // 参数2:绑定绑定对应的LayoutHelper
            // 参数3:传入该布局需要显示的数据个数
            // 参数4:传入需要绑定的数据

            // 通过重写onBindViewHolder()设置更丰富的布局效果
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                // 为了展示效果,将布局的第一个数据设置为linearLayout
                if (position == 0) {
                    holder.text.setText("Linear");
                }

                //为了展示效果,将布局里不同位置的Item进行背景颜色设置
                if (position < 2) {
                    holder.itemView.setBackgroundColor(0x66cc0000 + (position - 6) * 128);
                } else if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(0xaa22ff22);
                } else {
                    holder.itemView.setBackgroundColor(0xccff22ff);
                }

            }
        };

        Adapter_GridLayout=new ShopAdapter(listItem,this,gridLayoutHelper,5){
            // 通过重写onBindViewHolder()设置更丰富的布局效果
            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                // 为了展示效果,将布局的第一个数据设置为linearLayout
                if (position == 0) {
                    holder.text.setText("grid");
                }

                //为了展示效果,将布局里不同位置的Item进行背景颜色设置
                if (position < 2) {
                    holder.itemView.setBackgroundColor(0x66cc0000 + (position - 6) * 128);
                } else if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(0xaa22ff22);
                } else {
                    holder.itemView.setBackgroundColor(0xccff22ff);
                }

            }
        };



    }


}
