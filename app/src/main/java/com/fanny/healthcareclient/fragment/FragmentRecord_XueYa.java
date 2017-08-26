package com.fanny.healthcareclient.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.activity.TempDataActivity;
import com.fanny.healthcareclient.activity.XueyaDataActivity;
import com.fanny.healthcareclient.bean.TempData;
import com.fanny.healthcareclient.bean.XueYaData;
import com.fanny.healthcareclient.utils.JDBCUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Fanny on 17/7/24.
 */

public class FragmentRecord_XueYa extends Fragment {

    private PullToRefreshListView mlistView;
    private BaseAdapter mAdapter;
    private ArrayList<XueYaData> listItem;

    private Connection connection;
    private String TAG = "XueyaRecord";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LinearLayout.inflate(getContext(), R.layout.fragment_xueyarecode, null);

        mlistView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);

        initData();

//        mAdapter=new ArrayAdapter<String>(getActivity(),R.layout.xueya_item,R.id.id_grid_item_text,listItem);

        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listItem.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                MyListItem myListItem = null;
                if (convertView == null) {
                    myListItem = new MyListItem();
                    convertView = LinearLayout.inflate(getContext(), R.layout.xueya_item, null);
                    myListItem.tv_sys = (TextView) convertView.findViewById(R.id.item_sys);
                    myListItem.tv_dia = (TextView) convertView.findViewById(R.id.item_dia);
                    myListItem.tv_time = (TextView) convertView.findViewById(R.id.item_time);
                    myListItem.tv_plus = (TextView) convertView.findViewById(R.id.item_plus);
                    myListItem.tv_map = (TextView) convertView.findViewById(R.id.item_map);
                    convertView.setTag(myListItem);
                } else {
                    myListItem = (MyListItem) convertView.getTag();
                }

                myListItem.tv_sys.setText("收缩压为：" + listItem.get(position).getSys());
                myListItem.tv_dia.setText("舒张压为：" + listItem.get(position).getDia());
                myListItem.tv_plus.setText("脉率为：" + listItem.get(position).getPlus());
                myListItem.tv_map.setText("平均压为：" + listItem.get(position).getMap());
                myListItem.tv_time.setText("时间：" + listItem.get(position).getTime());
                return convertView;
            }
        };

        mlistView.setAdapter(mAdapter);

        mlistView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.e("纪录下拉刷新列表", "onPullDownToRefresh");
                String label = DateUtils.formatDateTime(
                        getContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                String label = DateUtils.formatDateTime(
                        getContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                Log.e("纪录加载刷新列表", "onPullUpToRefresh");
                new GetDataTask().execute();
            }
        });

        return view;

    }

    private Handler myRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    if (listItem.size() > 0) {
                        mAdapter.notifyDataSetChanged();
                        mlistView.onRefreshComplete();
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initData() {

        listItem = new ArrayList<>();
        SQLiteDatabase db = XueyaDataActivity.dbopenHelper.getWritableDatabase();

        if (listItem.size() > 0) {
            listItem.clear();
        }
        /**
         *  清除本地数据库
         */
        if (db != null) {
            db.delete("xueyaData", null, null);
        }
        db.close();


//        /**
//         * 假设数据源
//         */
//        listItem = new ArrayList<>();
//        for(int i=0;i<mItemCount;i++){
//            listItem.add(new XueYaData(100+i,70+i,2008+i,90+i,80+i));
//
//        }
        /**
         * 存储本地数据库
         */
//        SQLiteDatabase db= XueyaDataActivity.dbopenHelper.getWritableDatabase();
//        for(int i=0;i<listItem.size();i++){
//            db.execSQL(
//                    "insert into xueyaData(name,time,sys,dia,plus,map) values(?,?,?,?,?,?)",
//                    new Object[]{"机主",listItem.get(i).getTime(),listItem.get(i).getSys(),listItem.get(i).getDia(),listItem.get(i).getPlus(),listItem.get(i).getMap()});
//        }
//        db.close();

        /**
         * 测试连接远程数据库操作
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JDBCUtils jdbc = new JDBCUtils();
                    connection = jdbc.getConnection();
                    if (connection != null) {
                        Log.e(TAG, "connection不为空");

                        /**
                         * 根据姓名字段查询数据
                         */
                        String sql = "select * from XueYa_Info where UserName like '" + XueyaDataActivity.XueyaName + "'";
                        Statement stmt = null;//创建Statement
                        stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);


                        while (rs.next()) {//<code>ResultSet</code>最初指向第一行

                            String UserID = rs.getString("UserID");
                            String UserName = rs.getString("UserName");
                            String Device_ID = rs.getString("Device_ID");
                            String Device_Type = rs.getString("Device_Type");
                            String DateTime = String.valueOf(rs.getDate("DateTime"));
                            int SSY = rs.getInt("SSY");
                            int SZY = rs.getInt("SZY");
                            int ML = rs.getInt("ML");
                            int PJY = rs.getInt("PJY");

                            if (UserID != null && UserName != null && Device_ID != null && DateTime != null && SSY != 0 && SZY != 0 && ML != 0 && PJY != 0) {
                                Log.e(TAG, UserID);//输出第n行，列名为“UserID”的值
                                Log.e(TAG, UserName);
                                Log.e(TAG, Device_ID);
                                Log.e(TAG, Device_Type);
                                Log.e(TAG, DateTime);
                                Log.e(TAG, String.valueOf(SSY));
                                Log.e(TAG, String.valueOf(SZY));
                                Log.e(TAG, String.valueOf(ML));
                                Log.e(TAG, String.valueOf(PJY));

                                listItem.add(new XueYaData(SSY, SZY,DateTime, ML, PJY));
                                /**
                                 * 更新纪录界面列表
                                 */
                                Message msg = new Message();
                                msg.what = 0x01;
                                myRefreshHandler.sendMessage(msg);

                                /**
                                 * 去重取值，存储本地数据库
                                 */
                                SQLiteDatabase db = XueyaDataActivity.dbopenHelper.getWritableDatabase();
                                db.execSQL(
                                        "insert into xueyaData(name,time,sys,dia,plus,map) values(?,?,?,?,?,?)",
                                        new Object[]{UserName, DateTime, SSY,SZY,ML,PJY});
                                db.close();
                            }

                        }


                        rs.close();
                        stmt.close();
                    } else {
                        Log.e(TAG, "connection为空");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        try {
                            connection.close();
                        } catch (SQLException e) {
                        }
                }
            }
        }).start();


    }

    public final class MyListItem {
        public TextView tv_sys;
        TextView tv_dia;
        TextView tv_time;
        TextView tv_plus;
        TextView tv_map;
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            /**
             * 假设数据源
             */
//            mItemCount++;
//            listItem.add(new XueYaData(mItemCount,mItemCount,mItemCount,mItemCount,mItemCount));

            /**
             * 刷新数据库源
             */
            if (listItem != null) {
                listItem.clear();
            }
            SQLiteDatabase db = XueyaDataActivity.dbopenHelper.getWritableDatabase();
            Cursor cursor = db.query("xueyaData", null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    XueYaData data = new XueYaData();
                    data.setSys(cursor.getInt(cursor.getColumnIndex("sys")));
                    data.setDia(cursor.getInt(cursor.getColumnIndex("dia")));
                    data.setPlus(cursor.getInt(cursor.getColumnIndex("plus")));
                    data.setMap(cursor.getInt(cursor.getColumnIndex("map")));
                    data.setTime(cursor.getString(cursor.getColumnIndex("time")));

                    listItem.add(data);
                }
//                    list.setAdapter(new MyAdapter());
            }
            db.close();
            mAdapter.notifyDataSetChanged();
            mlistView.onRefreshComplete();
        }
    }
}
