package com.fanny.healthcareclient.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.activity.TempDataActivity;
import com.fanny.healthcareclient.bean.TempData;
import com.fanny.healthcareclient.utils.JDBCUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Fanny on 17/7/24.
 */

public class FragmentTendency_Temp extends Fragment {

    private String TAG = "tempTendency";
    private LineChart chat_temp;
    private Description description;
    private ArrayList<Entry> values1;
    private LineDataSet set1;

    private Connection connection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LinearLayout.inflate(getContext(), R.layout.fragment_tiwentendency, null);

        chat_temp = (LineChart) view.findViewById(R.id.chat_tiwen);
        initChatStyle();

        description = new Description();
        initDescription();
        chat_temp.setDescription(description);

        initData();


        /**
         * 绘图
         */
//        drawTheChat();

        return view;
    }

    private void initChatStyle() {
        chat_temp.setDrawBorders(false);

        chat_temp.setDrawGridBackground(true);

        chat_temp.setGridBackgroundColor(Color.WHITE & 0x70ffffff);

        chat_temp.setTouchEnabled(true);

        chat_temp.setDragEnabled(true);

        chat_temp.setScaleEnabled(true);

        chat_temp.setPinchZoom(false);

        chat_temp.setBackgroundColor(Color.GRAY);


        Legend mLegend = chat_temp.getLegend();
        mLegend.setForm(Legend.LegendForm.CIRCLE); //样式
        mLegend.setFormSize(6f); //字体
        mLegend.setTextColor(Color.WHITE); //颜色

//        chat1.setVisibleXRange(1, 7);   //x轴可显示的坐标范围
        XAxis xAxis = chat_temp.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
//        xAxis.setTextColor(Color.WHITE);    //字体的颜色
//        xAxis.setTextSize(10f); //字体大小
        xAxis.setGridColor(Color.WHITE);//网格线颜色
        xAxis.setDrawGridLines(true); //不显示网格线
//        xAxis.setValueFormatter();
//        xAxis.setTypeface(mTf);

        YAxis axisLeft = chat_temp.getAxisLeft(); //y轴左边标示
        YAxis axisRight = chat_temp.getAxisRight(); //y轴右边标示
//        axisLeft.setTextColor(Color.WHITE); //字体颜色
//        axisLeft.setTextSize(10f); //字体大小
//        axisLeft.setAxisMaxValue(1000f); //最大值
        axisLeft.setLabelCount(6, true); //显示格数
        axisLeft.setGridColor(Color.WHITE); //网格线颜色
//        axisLeft.setTypeface(mTf);

        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

    }


    private void initDescription() {
        description.setText("数据分析图表");
        description.setTextColor(Color.RED);
        description.setTextSize(10);

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        /**
//         * 获取本地数据库数据
//         */
//        SQLiteDatabase db = TempDataActivity.dbopenHelper.getWritableDatabase();
//        Cursor cursor = db.query("tempData", null, null, null, null, null, null);
//        if (cursor != null && cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//
////                TempData data = new TempData();
////                data.setTempValue(cursor.getFloat(cursor.getColumnIndex("temp")));
////                data.setTime(cursor.getString(cursor.getColumnIndex("time")));
//
//                float tempValue=cursor.getFloat(cursor.getColumnIndex("temp"));
//                String dateStr=cursor.getString(cursor.getColumnIndex("time"));
//                int date= Integer.parseInt(dateStr);
//
//                Log.e("onResume+图标新添加数据",tempValue+"");
//                values1.add(new Entry(date,tempValue));
//            }
////                    list.setAdapter(new MyAdapter());
//        }
//        db.close();
//
//        /**
//         * 刷新数据
//         */
//        if (chat_temp.getData() != null &&
//                chat_temp.getData().getDataSetCount() > 0) {
//            //获取数据1
//            set1 = (LineDataSet) chat_temp.getData().getDataSetByIndex(0);
//            set1.setValues(values1);
//
//            //刷新数据
//            chat_temp.getData().notifyDataChanged();
//            chat_temp.notifyDataSetChanged();
//        } else {
//            Toast.makeText(getActivity(),"没有最新数据",Toast.LENGTH_SHORT).show();
//        }
//
//    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x02:
                    if (chat_temp.getData() != null &&
                            chat_temp.getData().getDataSetCount() > 0) {
                        //获取数据1
                        set1 = (LineDataSet) chat_temp.getData().getDataSetByIndex(0);
                        set1.setValues(values1);

                        //刷新数据
                        chat_temp.getData().notifyDataChanged();
                        chat_temp.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * 获取服务器数据库数据
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JDBCUtils jdbc = new JDBCUtils();
                    connection = jdbc.getConnection();
                    if (connection != null) {
                        Log.e(TAG, "connection不为空");
//                        String sql = "select * from TiWen_Info";//查询表名为“TiWen_Info”的所有内容
                        String sql = "select * from TiWen_Info where UserName like '"+TempDataActivity.TempName+"'";//查询表名为“TiWen_Info”中姓名为龚梦帆的内容
                        Statement stmt = null;//创建Statement
                        stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        int i = 0;
                        while (rs.next()) {//<code>ResultSet</code>最初指向第一行
                            i = i + 4;
                            String UserID = rs.getString("UserID");
                            String UserName = rs.getString("UserName");
                            String Device_ID = rs.getString("Device_ID");
//                            String Device_Type = rs.getString("Device_Type");
                            String DateTime = String.valueOf(rs.getDate("DateTime"));
                            Float Temperature = rs.getFloat("Temp");

                            if (UserID != null && UserName != null && Device_ID != null && DateTime != null && Temperature != null) {
                                Log.e(TAG, UserID);//输出第n行，列名为“UserID”的值
                                Log.e(TAG, UserName);
                                Log.e(TAG, Device_ID);
//                            Log.e(TAG, Device_Type);
                                Log.e(TAG, DateTime);
                                Log.e(TAG, String.valueOf(Temperature));


                                values1.add(new Entry(44 + i, Temperature));
                                Message msg = new Message();
                                msg.what = 0x02;
                                myHandler.sendMessage(msg);
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

    private void initData() {

        /**
         * Entry 坐标点对象  构造函数 第一个参数为x点坐标 第二个为y点
         */
        values1 = new ArrayList<>();
        /**
         * 前期虚拟数据
         */
        values1.add(new Entry(4, (float) 24.5));
        values1.add(new Entry(8, (float) 25.5));
        values1.add(new Entry(12, (float) 23.6));
        values1.add(new Entry(16, (float) 27.8));
        values1.add(new Entry(20, (float) 27.4));
        values1.add(new Entry(24, (float) 27.2));
        values1.add(new Entry(28, (float) 28.2));
        values1.add(new Entry(32, (float) 27.8));
        values1.add(new Entry(36, (float) 27.0));
        values1.add(new Entry(40, (float) 26.9));


        //LineDataSet每一个对象就是一条连接线

        /**
         * 处理图表1
         */
        //判断图表1中原来是否有数据
        if (chat_temp.getData() != null &&
                chat_temp.getData().getDataSetCount() > 0) {
            //获取数据1
            set1 = (LineDataSet) chat_temp.getData().getDataSetByIndex(0);
            set1.setValues(values1);

            //刷新数据
            chat_temp.getData().notifyDataChanged();
            chat_temp.notifyDataSetChanged();
        } else {

            //设置数据1  参数1：数据源 参数2：图例名称
            set1 = new LineDataSet(values1, "体温数据");
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLUE);
            set1.setLineWidth(1f);//设置线宽
            set1.setCircleRadius(3f);//设置焦点圆心的大小
            set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
            set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
            set1.setHighlightEnabled(true);//是否禁用点击高亮线
            set1.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
            set1.setValueTextSize(9f);//设置显示值的文字大小
            set1.setDrawFilled(false);//设置禁用范围背景填充

            //格式化显示数据
            final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
                }
            });
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
//                set1.setFillDrawable(drawable);//设置范围背景填充
                set1.setFillColor(Color.BLACK);
            } else {
                set1.setFillColor(Color.BLACK);
            }


            //保存LineDataSet集合
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets
            //创建LineData对象 属于LineChart折线图的数据集合
            LineData data = new LineData(dataSets);
            // 添加到图表中
            chat_temp.setData(data);
            //绘制图表
            chat_temp.invalidate();
        }


    }

}
