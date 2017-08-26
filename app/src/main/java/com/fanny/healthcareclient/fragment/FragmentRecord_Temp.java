package com.fanny.healthcareclient.fragment;

import android.content.Intent;
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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Fanny on 17/7/24.
 */

public class FragmentRecord_Temp extends Fragment {

    private int mItemCount = 10;
    private PullToRefreshListView mlistView;
    private BaseAdapter mAdapter;
    private ArrayList<TempData> listItem;

    private Connection connection;
    private String TAG = "TempRecord";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LinearLayout.inflate(getContext(), R.layout.fragment_tiwenrecode, null);

        mlistView = (PullToRefreshListView) view.findViewById(R.id.listview);

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
                    convertView = LinearLayout.inflate(getContext(), R.layout.tiwen_item, null);
                    myListItem.tv_temp = (TextView) convertView.findViewById(R.id.item_temp);
                    myListItem.tv_time = (TextView) convertView.findViewById(R.id.item_time_temp);
                    convertView.setTag(myListItem);
                } else {
                    myListItem = (MyListItem) convertView.getTag();
                }

                myListItem.tv_temp.setText("体温为：" + listItem.get(position).getTempValue());
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initData();
    }

    private Handler myRefreshHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x01:
                    if(listItem.size()>0){
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
        SQLiteDatabase db = TempDataActivity.dbopenHelper.getWritableDatabase();

        if(listItem.size()>0){
            listItem.clear();
        }
        /**
         *  清除本地数据库
         */
        if(db!=null){
            db.delete("tempData",null,null);
        }
        db.close();

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
                        String sql = "select * from TiWen_Info where UserName like '"+TempDataActivity.TempName+"'";
                        Statement stmt = null;//创建Statement
                        stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);


                        while (rs.next()) {//<code>ResultSet</code>最初指向第一行

                            String UserID = rs.getString("UserID");
                            String UserName = rs.getString("UserName");
                            String Device_ID = rs.getString("Device_ID");
//                            String Device_Type = rs.getString("Device_Type");
                            String DateTime = String.valueOf(rs.getDate("DateTime"));
                            Float Temperature = rs.getFloat("Temp");

                            if(UserID!=null && UserName!=null && Device_ID!=null && DateTime!=null && Temperature!=null){
                                Log.e(TAG, UserID);//输出第n行，列名为“UserID”的值
                                Log.e(TAG, UserName);
                                Log.e(TAG, Device_ID);
//                            Log.e(TAG, Device_Type);
                                Log.e(TAG, DateTime);
                                Log.e(TAG, String.valueOf(Temperature));

                                listItem.add(new TempData(Temperature, DateTime, UserID, UserName));
                                /**
                                 * 更新纪录界面列表
                                 */
                                Message msg=new Message();
                                msg.what=0x01;
                                myRefreshHandler.sendMessage(msg);

                                /**
                                 * 去重取值，存储本地数据库
                                 */
                                SQLiteDatabase db = TempDataActivity.dbopenHelper.getWritableDatabase();
                                db.execSQL(
                                        "insert into tempData(name,time,temp) values(?,?,?)",
                                        new Object[]{UserName, DateTime, Temperature});
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


//        /**
//         * 存储本地数据库
//         */
//        if(listItem.size()>0){
//            SQLiteDatabase db = TempDataActivity.dbopenHelper.getWritableDatabase();
//            for (int i = 0; i < listItem.size(); i++) {
//                db.execSQL(
//                        "insert into tempData(name,time,temp) values(?,?,?)",
//                        new Object[]{"机主", listItem.get(i).getTime(), listItem.get(i).getTempValue()});
//            }
//            db.close();
//        }


    }

    public final class MyListItem {
        TextView tv_temp;
        TextView tv_time;
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

            /**
             * 远程刷新
             */
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        JDBCUtils jdbc = new JDBCUtils();
//                        connection = jdbc.getConnection();
//                        if (connection != null) {
//                            Log.e(TAG, "connection不为空");
//                            String sql = "select * from Temperature";//查询表名为“TiWen_Info”的所有内容
//                            Statement stmt = null;//创建Statement
//                            stmt = connection.createStatement();
//                            ResultSet rs = stmt.executeQuery(sql);
//
//                            while (rs.next()) {//<code>ResultSet</code>最初指向第一行
//
//                                String UserID = rs.getString("UserID");
//                                String UserName = rs.getString("UserName");
//                                String Device_ID = rs.getString("Device_ID");
//                                String Device_Type = rs.getString("Device_Type");
//                                String DateTime = rs.getDate("DateTime").toString();
//                                Float Temperature = rs.getFloat("Temperature");
//
//                                Log.e(TAG, UserID);//输出第n行，列名为“UserID”的值
//                                Log.e(TAG, UserName);
//                                Log.e(TAG, Device_ID);
//                                Log.e(TAG, Device_Type);
//                                Log.e(TAG, DateTime);
//                                Log.e(TAG, String.valueOf(Temperature));
//
//                                listItem.add(new TempData(Temperature, DateTime, UserID, UserName));
//                                /**
//                                 * 更新纪录界面列表
//                                 */
//                                Message msg=new Message();
//                                msg.what=0x01;
//                                myRefreshHandler.sendMessage(msg);
//
//                                /**
//                                 * 去重取值，存储本地数据库
//                                 */
////                            db.execSQL(
////                                    "insert into tempData(name,time,temp) values(?,?,?)",
////                                    new Object[]{UserName, DateTime, Temperature});
//                            }
//
////                        db.close();
//                            rs.close();
//                            stmt.close();
//                        } else {
//                            Log.e(TAG, "connection为空");
//                        }
//
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    } finally {
//                        if (connection != null)
//                            try {
//                                connection.close();
//                            } catch (SQLException e) {
//                            }
//                    }
//                }
//            }).start();


            /**
             * 本地刷新
             */
            SQLiteDatabase db = TempDataActivity.dbopenHelper.getWritableDatabase();
            Cursor cursor = db.query("tempData", null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    TempData data = new TempData();
                    data.setTempValue(cursor.getFloat(cursor.getColumnIndex("temp")));
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
