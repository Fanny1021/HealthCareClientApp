package com.fanny.healthcareclient.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.adapter.UserChoiceSpinnerAdapter;
import com.fanny.healthcareclient.bean.TempData;
import com.fanny.healthcareclient.bean.UserInfo;
import com.fanny.healthcareclient.dao.DBOpenHelper;
import com.fanny.healthcareclient.utils.JDBCUtils;
import com.fanny.healthcareclient.utils.SpUtil;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserChoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private Connection connection;
    private String TAG = "UserChoice";
    private Spinner spn_username;
    private ArrayList<String> userList;
    private UserChoiceSpinnerAdapter mySpinAdapter;
    private RadioButton rb_man;
    private RadioButton rb_woman;
    private TextView tv_age;
    private TextView tv_idCard;
    private Button btn_choiceUser;
    //    private Button btn_choiceSave;

    private SQLiteDatabase db;
    private DBOpenHelper dbopenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choice);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("个人资料");
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbopenHelper=new DBOpenHelper(this);

        initView();
    }

    private Handler myRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    mySpinAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void initView() {

        userList = new ArrayList<String>();
        spn_username = (Spinner) findViewById(R.id.spn_username);
        /**
         * 虚拟数据
         */

        ArrayList<UserInfo> userInfolist = new ArrayList<>();
        userList.add("龚梦帆");
        userList.add("邓为东");
        userInfolist.add(new UserInfo("龚梦帆", true, 26, "7", "7"));
        userInfolist.add(new UserInfo("邓为东", false, 32, "8", "8"));
        mySpinAdapter = new UserChoiceSpinnerAdapter(this, userList);
        spn_username.setAdapter(mySpinAdapter);


//        for(int i=0;i<10;i++){
//            UserInfo userInfo=new UserInfo("gmf"+i,false,20+i,1+i+"",1+i+"");
//            userInfolist.add(userInfo);
//            userList.add(userInfo.getUserName());
//        }


        /**
         * 远程数据
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JDBCUtils jdbc = new JDBCUtils();
                    connection = jdbc.getConnection();
                    if (connection != null) {
                        Log.e(TAG, "connection不为空");
                        String sql = "select * from User_Profile_Info";//查询表名为“TiWen_Info”的所有内容
                        Statement stmt = null;//创建Statement
                        stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        while (rs.next()) {//<code>ResultSet</code>最初指向第一行

                            String UserName = rs.getString("UserName");
                            Boolean UserSex = rs.getBoolean("Sex");
                            int UserAge = rs.getInt("Age");
                            String UserID = rs.getString("UserID");
                            String UserID_Card = rs.getString("ID_Card");

                            Log.e(TAG, UserID);//输出第n行，列名为“UserID”的值
                            Log.e(TAG, UserName);
                            Log.e(TAG, UserAge + "");
                            Log.e(TAG, UserID_Card);

                            UserInfo userInfo = new UserInfo(UserName, UserSex, UserAge, UserID_Card, UserID);
                            userInfolist.add(userInfo);
                            userList.add(userInfo.getUserName());

                            /**
                             * 更新纪录界面列表
                             */
                            Message msg = new Message();
                            msg.what = 0x01;
                            myRefreshHandler.sendMessage(msg);

//                            /**
//                             * 去重取值，存储本地数据库
//                             */
//                            SQLiteDatabase db = TempDataActivity.dbopenHelper.getWritableDatabase();
//                            db.execSQL(
//                                    "insert into tempData(name,time,temp) values(?,?,?)",
//                                    new Object[]{UserName, DateTime, Temperature});
//                            db.close();
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

//        String user[] = getResources().getStringArray(R.array.user);
//        ArrayAdapter<String> myAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, user);
//        myAdapter.setDropDownViewResource(R.layout.my_drop_down_item);


        rb_man = (RadioButton) findViewById(R.id.c_rb_man);
        rb_woman = (RadioButton) findViewById(R.id.c_rb_woman);
        tv_age = (TextView) findViewById(R.id.c_tv_born);
        tv_idCard = (TextView) findViewById(R.id.c_tv_idCard);
        btn_choiceUser = (Button) findViewById(R.id.c_btn_edit);
//        btn_choiceSave = (Button) findViewById(R.id.c_btn_save);

        spn_username.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userList.size() > 0) {
                    String selectName = (String) mySpinAdapter.getItem(position);
                    if (userInfolist.get(position) != null && userInfolist.get(position).getUserName().equals(selectName)) {
                        Log.e(TAG, "查询到结果");
                        if (userInfolist.get(position).getSex() == true) {
                            rb_man.setSelected(true);
                            rb_woman.setSelected(false);
                        }else {
                            rb_man.setSelected(false);
                            rb_woman.setSelected(true);
                        }
                        tv_age.setText(userInfolist.get(position).getAge() + "");
                        tv_idCard.setText(userInfolist.get(position).getUserID_Card());
                    } else {
                        Log.e(TAG, "未查询到结果");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_choiceUser.setOnClickListener(this);
//        btn_choiceSave.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.userchoice_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.user_add:
                Intent userMsgIntent = new Intent(this, UserMsgActivity.class);
                startActivity(userMsgIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public final static int RESULT_CODE = 1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.c_btn_edit:
                Boolean isLast=true;
                String name = (String) spn_username.getSelectedItem();
                String sex = "";
                if (rb_man.isChecked()) {
                    sex = "男";
                } else {
                    sex = "女";
                }
                String age = tv_age.getText().toString();
                String idCard = tv_idCard.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("sex", sex);
                bundle.putString("age", age);
                bundle.putString("idCard", idCard);

                Intent retrunIntent = new Intent();
                retrunIntent.putExtra("returnMsg", bundle);
                setResult(RESULT_CODE, retrunIntent);

                /**
                 * 保存本地数据库
                 */
                /**
                 * 初始化本地数据库
                 */

//                db = dbopenHelper.getWritableDatabase();
//                /**
//                 * 将数据库中除了保存用户的islast之外所有用户的islast值置为false
//                 */
//
////                db.execSQL("update userlogin set isLast='false' where ")
//                db.execSQL("insert into userlogin(name,sex,age,idcardno,isLast) values(?,?,?,?,?)",
//                        new Object[]{name, sex, age,idCard,isLast});
//
//                db.close();


                finish();
                break;

        }
    }


}
