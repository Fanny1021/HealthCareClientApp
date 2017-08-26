package com.fanny.healthcareclient.fragment;

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

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.activity.XueTangDataActivity;
import com.fanny.healthcareclient.activity.XueyaDataActivity;
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

public class FragmentTendency_XueTang extends Fragment {

    private LineChart chat_xuetang;
    private Description description;

    private Connection connection;
    private String TAG = "xuetangTendency";
    private ArrayList<Entry> values1;
    private ArrayList<Entry> values2;
    private ArrayList<Entry> values3;

    //LineDataSet每一个对象就是一条连接线
    LineDataSet set1;
    LineDataSet set2;
    LineDataSet set3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= LinearLayout.inflate(getContext(), R.layout.fragment_xuetangtendency,null);

        chat_xuetang = (LineChart) view.findViewById(R.id.chat_xuetang);
        initChatStyle();

        description = new Description();
        initDescription();
        chat_xuetang.setDescription(description);


        initData();


        /**
         * 绘图
         */
//        drawTheChat();

        return view;
    }

    private void initChatStyle() {
        chat_xuetang.setDrawBorders(false);

        chat_xuetang.setDrawGridBackground(true);

        chat_xuetang.setGridBackgroundColor(Color.WHITE & 0x70ffffff);

        chat_xuetang.setTouchEnabled(true);

        chat_xuetang.setDragEnabled(true);

        chat_xuetang.setScaleEnabled(true);

        chat_xuetang.setPinchZoom(false);

        chat_xuetang.setBackgroundColor(Color.GRAY);


        Legend mLegend=chat_xuetang.getLegend();
        mLegend.setForm(Legend.LegendForm.CIRCLE); //样式
        mLegend.setFormSize(6f); //字体
        mLegend.setTextColor(Color.WHITE); //颜色

//        chat1.setVisibleXRange(1, 7);   //x轴可显示的坐标范围
        XAxis xAxis = chat_xuetang.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
//        xAxis.setTextColor(Color.WHITE);    //字体的颜色
//        xAxis.setTextSize(10f); //字体大小
        xAxis.setGridColor(Color.WHITE);//网格线颜色
        xAxis.setDrawGridLines(true); //不显示网格线
//        xAxis.setValueFormatter();
//        xAxis.setTypeface(mTf);

        YAxis axisLeft = chat_xuetang.getAxisLeft(); //y轴左边标示
        YAxis axisRight = chat_xuetang.getAxisRight(); //y轴右边标示
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

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x02:
                    if (chat_xuetang.getData() != null &&
                            chat_xuetang.getData().getDataSetCount() > 0) {
                        //获取数据
                        set1 = (LineDataSet) chat_xuetang.getData().getDataSetByIndex(0);
                        set1.setValues(values1);

                        set2 = (LineDataSet) chat_xuetang.getData().getDataSetByIndex(1);
                        set2.setValues(values2);

                        set3 = (LineDataSet) chat_xuetang.getData().getDataSetByIndex(2);
                        set3.setValues(values3);

                        //刷新数据
                        chat_xuetang.getData().notifyDataChanged();
                        chat_xuetang.notifyDataSetChanged();
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
                        String sql = "select * from XueTang_Info where UserName like '" + XueTangDataActivity.XuetangName + "'";//查询表名为“TiWen_Info”中姓名为龚梦帆的内容
                        Statement stmt = null;//创建Statement
                        stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        int i = 0;
                        while (rs.next()) {//<code>ResultSet</code>最初指向第一行
                            i=i+4;
                            String UserID = rs.getString("UserID");
                            String UserName = rs.getString("UserName");
                            String Device_ID = rs.getString("Device_ID");
                            String Device_Type = rs.getString("Device_Type");
                            String DateTime = String.valueOf(rs.getDate("DateTime"));

                            float glu=rs.getFloat("XueT1");
                            float ua=rs.getFloat("XueT2");
                            float chol=rs.getFloat("XueT3");


                            if (UserID != null && UserName != null && Device_ID != null && DateTime != null && glu != 0 && ua != 0 && chol != 0 ) {
                                Log.e(TAG, UserID);//输出第n行，列名为“UserID”的值
                                Log.e(TAG, UserName);
                                Log.e(TAG, Device_ID);
                                Log.e(TAG, Device_Type);
                                Log.e(TAG, DateTime);
                                Log.e(TAG, String.valueOf(glu));
                                Log.e(TAG, String.valueOf(ua));
                                Log.e(TAG, String.valueOf(chol));


                                values1.add(new Entry(44 + i, glu));
                                values2.add(new Entry(44 + i, ua));
                                values3.add(new Entry(44 + i, chol));


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
        values2 = new ArrayList<>();
        values3 = new ArrayList<>();

        values1.add(new Entry(4, 74));
        values1.add(new Entry(8, 78));
        values1.add(new Entry(12, 76));
        values1.add(new Entry(16, 80));
        values1.add(new Entry(20, 74));
        values1.add(new Entry(24, 72));
        values1.add(new Entry(28, 82));
        values1.add(new Entry(32, 78));
        values1.add(new Entry(36, 70));
        values1.add(new Entry(40, 69));

        values2.add(new Entry(4, 110));
        values2.add(new Entry(8, 115));
        values2.add(new Entry(12, 130));
        values2.add(new Entry(16, 120));
        values2.add(new Entry(20, 110));
        values2.add(new Entry(24, 112));
        values2.add(new Entry(28, 100));
        values2.add(new Entry(32, 111));
        values2.add(new Entry(36, 98));
        values2.add(new Entry(40, 97));

        values3.add(new Entry(4, 10));
        values3.add(new Entry(8, 15));
        values3.add(new Entry(12, 30));
        values3.add(new Entry(16, 20));
        values3.add(new Entry(20, 10));
        values3.add(new Entry(24, 12));
        values3.add(new Entry(28, 10));
        values3.add(new Entry(32, 11));
        values3.add(new Entry(36, 28));
        values3.add(new Entry(40, 27));



        /**
         * 处理图表1
         */
        //判断图表1中原来是否有数据
        if (chat_xuetang.getData() != null &&
                chat_xuetang.getData().getDataSetCount() > 0) {
            //获取数据1
            set1 = (LineDataSet) chat_xuetang.getData().getDataSetByIndex(0);
            set1.setValues(values1);
            set2 = (LineDataSet) chat_xuetang.getData().getDataSetByIndex(1);
            set2.setValues(values2);
            set3 = (LineDataSet) chat_xuetang.getData().getDataSetByIndex(2);
            set3.setValues(values3);
            //刷新数据
            chat_xuetang.getData().notifyDataChanged();
            chat_xuetang.notifyDataSetChanged();
        } else {

            //设置数据1  参数1：数据源 参数2：图例名称
            set1 = new LineDataSet(values1, "血糖数据");
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

            //设置数据2
            set2 = new LineDataSet(values2, "尿酸数据");
            set2.setColor(Color.GREEN);
            set2.setCircleColor(Color.GREEN);
            set2.setLineWidth(1f);
            set2.setCircleRadius(3f);
            set2.setValueTextSize(10f);

            //设置数据3
            set3 = new LineDataSet(values3, "总胆固醇数据");
            set3.setColor(Color.YELLOW);
            set3.setCircleColor(Color.YELLOW);
            set3.setLineWidth(1f);
            set3.setCircleRadius(3f);
            set3.setValueTextSize(10f);

            //保存LineDataSet集合
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);
            dataSets.add(set3);
            //创建LineData对象 属于LineChart折线图的数据集合
            LineData data = new LineData(dataSets);
            // 添加到图表中
            chat_xuetang.setData(data);
            //绘制图表
            chat_xuetang.invalidate();
        }



    }

}
