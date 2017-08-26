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
import com.fanny.healthcareclient.activity.WatchDataActivity;
import com.fanny.healthcareclient.activity.XueTangDataActivity;
import com.fanny.healthcareclient.activity.XueYangDataActivity;
import com.fanny.healthcareclient.bean.WatchData;
import com.fanny.healthcareclient.bean.XueTangData;
import com.fanny.healthcareclient.bean.XueYangData;
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

public class FragmentRecord_Watch extends Fragment {

    private int mItemCount = 10;
    private PullToRefreshListView mlistView;
    private BaseAdapter mAdapter;
    private ArrayList<WatchData> listItem;

    private Connection connection;
    private String TAG = "WatchRecord";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LinearLayout.inflate(getContext(), R.layout.fragment_watchrecode, null);

        mlistView = (PullToRefreshListView) view.findViewById(R.id.watch_listview);

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
                    convertView = LinearLayout.inflate(getContext(), R.layout.watch_item, null);
                    myListItem.tv_spo2 = (TextView) convertView.findViewById(R.id.item_watch_spo2);
                    myListItem.tv_pr = (TextView) convertView.findViewById(R.id.item_watch_pr);
                    myListItem.tv_pi = (TextView) convertView.findViewById(R.id.item_watch_pi);
                    myListItem.tv_time = (TextView) convertView.findViewById(R.id.item_time_watch);
                    convertView.setTag(myListItem);
                } else {
                    myListItem = (MyListItem) convertView.getTag();
                }

                myListItem.tv_spo2.setText("血氧饱和度：" + listItem.get(position).getSpo2());
                myListItem.tv_pr.setText("脉率值：" + listItem.get(position).getPr());
                myListItem.tv_pi.setText("灌注指数：" + listItem.get(position).getPi());
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


//        /**
//         * 假设数据源
//         */
//        listItem = new ArrayList<>();
//        for(int i=0;i<mItemCount;i++){
//            listItem.add(new WatchData(35+i,30+i,(float) 5.1+i,2008+i));
//        }
//        /**
//         * 存储本地数据库
//         */
//        SQLiteDatabase db= WatchDataActivity.dbopenHelper.getWritableDatabase();
//        for(int i=0;i<listItem.size();i++){
//            db.execSQL(
//                    "insert into shouhuanData(name,time,spo2,pr,pi) values(?,?,?,?,?)",
//                    new Object[]{"机主",listItem.get(i).getTime(),listItem.get(i).getSpo2(),listItem.get(i).getPr(),listItem.get(i).getPi()});
//        }
//        db.close();

        listItem = new ArrayList<>();
        SQLiteDatabase db = WatchDataActivity.dbopenHelper.getWritableDatabase();

        if (listItem.size() > 0) {
            listItem.clear();
        }
        /**
         *  清除本地数据库
         */
        if (db != null) {
            db.delete("shouhuanData", null, null);
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
                        String sql = "select * from Sleep_Watch_Info where UserName like '" + WatchDataActivity.WatchName + "'";
                        Statement stmt = null;//创建Statement
                        stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);


                        while (rs.next()) {//<code>ResultSet</code>最初指向第一行

                            String UserID = rs.getString("UserID");
                            String UserName = rs.getString("UserName");
                            String Device_ID = rs.getString("Device_ID");
                            String Device_Type = rs.getString("Device_Type");
                            String DateTime = String.valueOf(rs.getDate("DateTime"));

                            int spO2 = rs.getInt("SpO2");
                            int pr = rs.getInt("PR");
                            float pi = rs.getFloat("PI");


                            if (UserID != null && UserName != null && Device_ID != null && DateTime != null && spO2 != 0 && pr != 0 && pi != 0) {
                                Log.e(TAG, UserID);//输出第n行，列名为“UserID”的值
                                Log.e(TAG, UserName);
                                Log.e(TAG, Device_ID);
                                Log.e(TAG, Device_Type);
                                Log.e(TAG, DateTime);
                                Log.e(TAG, String.valueOf(spO2));
                                Log.e(TAG, String.valueOf(pr));
                                Log.e(TAG, String.valueOf(pi));

                                listItem.add(new WatchData(spO2, pr, pi, DateTime));
                                /**
                                 * 更新纪录界面列表
                                 */
                                Message msg = new Message();
                                msg.what = 0x01;
                                myRefreshHandler.sendMessage(msg);

                                /**
                                 * 去重取值，存储本地数据库
                                 */
                                SQLiteDatabase db = WatchDataActivity.dbopenHelper.getWritableDatabase();
                                db.execSQL(
                                        "insert into shouhuanData(name,time,spo2,pr,pi) values(?,?,?,?,?)",
                                        new Object[]{UserName, DateTime, spO2, pr, pi});
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
        TextView tv_spo2;
        TextView tv_pr;
        TextView tv_pi;
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
            SQLiteDatabase db = WatchDataActivity.dbopenHelper.getWritableDatabase();
            Cursor cursor = db.query("shouhuanData", null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    WatchData data = new WatchData();
                    data.setSpo2(cursor.getInt(cursor.getColumnIndex("spo2")));
                    data.setPr(cursor.getInt(cursor.getColumnIndex("pr")));
                    data.setPi(cursor.getFloat(cursor.getColumnIndex("pi")));
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
