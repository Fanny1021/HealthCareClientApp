package com.fanny.healthcareclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.bean.TempData;
import com.fanny.healthcareclient.dao.DBOpenHelper;
import com.fanny.healthcareclient.utils.JDBCUtils;
import com.fanny.healthcareclient.view.CalenderDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserMsgActivity extends AppCompatActivity implements View.OnClickListener, CalenderDialog.MyListener {

    private TextView tv_born;
    private EditText et_username;
    private RadioGroup rg;
    private RadioButton rb_man;
    private RadioButton rb_woman;
    private EditText et_idCard;
    private Button btn_edit;
    private Button btn_save;

    private Connection connection;
    private String TAG = "AddPerson";

    private boolean isEdit = false;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private String userName;
    private String userID;
    private Boolean sex;
    private Date birthday;
    private int age;
    private String id_card;
    private String phone;
    private String mobile_phone;
    private String homeID;
    private Boolean alone;
    private String address;
    private Date check_in;
    private String image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("添加人物资料");
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbOpenHelper = new DBOpenHelper(this);
        db = dbOpenHelper.getWritableDatabase();

        initView();
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        rg = (RadioGroup) findViewById(R.id.rg_gentle);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_woman = (RadioButton) findViewById(R.id.rb_woman);
        tv_born = (TextView) findViewById(R.id.tv_born);
        tv_born.setOnClickListener(this);
        et_idCard = (EditText) findViewById(R.id.et_idCard);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

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
            case R.id.tv_born:
                CalenderDialog dialog = new CalenderDialog();
                dialog.show(getFragmentManager(), "calender");
                break;
            case R.id.btn_edit:
                isEdit = true;
                et_username.setEnabled(true);
                rb_man.setEnabled(true);
                rb_woman.setEnabled(true);
                tv_born.setEnabled(true);
                et_idCard.setEnabled(true);
                break;
            case R.id.btn_save:
                userID = "007";
                userName = "dwd";
                sex = false;
                birthday = new Date(2017,8,2);
                age = 30;
                id_card = "110110111010101010";
                phone = "6888888";
                mobile_phone = "18888888888";
                homeID = "6999999";
                alone = true;
                address = "bj";
                check_in = new Date(2020,8,2);
                image_url = "www.baidu.com";

                if (et_username.getText() == null) {
                    Toast.makeText(this, "请输入姓名，例如：张三", Toast.LENGTH_SHORT);
                } else {
                    userName = et_username.getText().toString();
                }
                if (rb_man.isChecked() == false && rb_woman.isChecked() == false) {
                    Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT);
                } else {
                    if (rb_man.isChecked()) {
                        sex = false;
                    } else {
                        sex =true;
                    }
                }
                if (tv_born.getText() == null) {
                    Toast.makeText(this, "请选择出生日期", Toast.LENGTH_SHORT);
                } else {
                    String birthday= tv_born.getText().toString();
                }
                if (et_idCard.getText() == null) {
                    Toast.makeText(this, "请输入身份证号码", Toast.LENGTH_SHORT);
                } else {
                    if (et_idCard.getText().toString().length() != 18) {
                        Toast.makeText(this, "请输入18位身份证号码", Toast.LENGTH_SHORT);
                    } else {
                        id_card = et_idCard.getText().toString();
                    }
                }

//                if (!name.equals("") && !gentle.equals("") && !birthday.equals("") && !idCard.equals("")) {
                isEdit = false;
                /**
                 * 保存用户信息，上传服务器，更新个人信息界面
                 */

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
                                 * 插入数据至远程服务器
                                 */
                                String sql = "insert into User_Profile_Info (ID,UserID,UserName,Sex,Birthday,Age,ID_Card,Phone,Mobile_Phone,HomeID,Alone,Address,Check_In,Image_url) " +
                                        "values(1,'"+ userID +"','"+ userName +"',True,'2017/8/1',"+age+",'"+id_card+"','"+phone+"','"+mobile_phone+"','"+homeID+"',"+alone+",'"+address+"','"+check_in+"','"+image_url+"')";
                                Statement stmt = null;//创建Statement
                                stmt = connection.createStatement();
                                boolean rs = stmt.execute(sql);
                                if (rs) {
                                    Log.e(TAG, "插入数据成功");
                                } else {
                                    Log.e(TAG, "插入数据失败");
                                }

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


//                }

                break;
        }
    }

    @Override
    public void setData(String str) {
        if (str != null) {
            tv_born.setText(str);
        }
    }
}
