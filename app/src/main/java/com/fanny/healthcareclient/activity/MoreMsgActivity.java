package com.fanny.healthcareclient.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.utils.JDBCUtils;
import com.fanny.healthcareclient.utils.SpUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;

public class MoreMsgActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_user;
    private LinearLayout ll_webHealth;
    private LinearLayout ll_market;
    private LinearLayout ll_equipment;
    private LinearLayout ll_set;
    private Connection connection;
    private TextView online_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_msg);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("更多");
        actionBar.setDisplayHomeAsUpEnabled(true);

        initview();

        initData();
    }

    private void initData() {

        /**
         * 传递初始界面用户信息，此处为main界面传来的最后一次登录用户的个人资料
         */
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String name=bundle.getString("userInfo");
        online_user.setText(name);
    }

    private void initview() {
        ll_user = (LinearLayout) findViewById(R.id.ll_userMsg);
        ll_webHealth = (LinearLayout) findViewById(R.id.ll_web_health);
        ll_market = (LinearLayout) findViewById(R.id.ll_market);
        ll_equipment = (LinearLayout) findViewById(R.id.ll_equipment);
        ll_set = (LinearLayout) findViewById(R.id.ll_setup);

        ll_user.setOnClickListener(this);
        ll_webHealth.setOnClickListener(this);
        ll_market.setOnClickListener(this);
        ll_equipment.setOnClickListener(this);
        ll_set.setOnClickListener(this);

        online_user = (TextView) findViewById(R.id.online_user);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_userMsg:
                Intent userMsgChoiceIntent = new Intent(this, UserChoiceActivity.class);
                startActivityForResult(userMsgChoiceIntent,RESULT_CODE);
//                startActivity(userMsgChoiceIntent);
                break;
            case R.id.ll_setup:
                /**
                 * 测试连接远程数据库操作
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JDBCUtils jdbc = new JDBCUtils();
                            connection = jdbc.getConnection();
                            if(connection!=null){
                                Log.e("jdbc","connection不为空");
                                String sql = "select * from UserInfo";//查询表名为“table_test”的所有内容
                                Statement stmt = null;//创建Statement
                                stmt = connection.createStatement();
                                ResultSet rs = stmt.executeQuery(sql);

                                while (rs.next()) {//<code>ResultSet</code>最初指向第一行
                                    Log.e("jdbc", rs.getString("UserID"));//输出第n行，列名为“test_id”的值
//                                    Log.e("jdbc", rs.getInt("id")id);

                                }
                                rs.close();
                                stmt.close();
                            }else {
                                Log.e("jdbc","connection为空");
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }finally {
                            if (connection != null)
                                try {
                                    connection.close();
                                } catch (SQLException e) {
                                }
                        }
                    }
                }).start();
                break;
            case R.id.ll_web_health:

                /**
                 * 用淘宝商城的布局框架做
                 */
                Intent shopIntent=new Intent(MoreMsgActivity.this,ShopMarketActivity.class);
                startActivity(shopIntent);
                break;

            case R.id.ll_equipment:

                /**
                 * 测试百度地图
                 */

                break;


        }
    }

    public final static int RESULT_CODE=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_CODE){

            if(resultCode==UserChoiceActivity.RESULT_CODE){
                /**
                 * 获取bundle
                 */
                Bundle bundle=data.getBundleExtra("returnMsg");
                String name=bundle.getString("name");
                online_user.setText(name);

                /**
                 *传送bundle
                 */
                Intent retrunIntent = new Intent();
                retrunIntent.putExtra("returnMsg",bundle);
                setResult(RESULT_CODE,retrunIntent);


            }
        }


    }
}
