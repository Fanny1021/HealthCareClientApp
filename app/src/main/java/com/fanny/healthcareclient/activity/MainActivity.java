package com.fanny.healthcareclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.dao.DBOpenHelper;
import com.fanny.healthcareclient.utils.SpUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView username;
    private TextView usersex;
    private TextView userage;
    private SQLiteDatabase db;

    private String loginUser;
    private String loginsex;
    private String loginage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("健康监测管家");

//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
        initview();
        initData();
    }

    private void initData() {
        /**
         * 去数据库中查询最后一次登录的用户
         */
//        db = TempDataActivity.dbopenHelper.getWritableDatabase();
//        Boolean flag=true;
//        Cursor cursor=db.rawQuery("select * from usetlogin where isLast like ?",new String[]{"true"});
//        while (cursor.moveToNext()){
//
//        }
//        db.close();
        loginUser = username.getText().toString();
        loginsex = usersex.getText().toString();
        loginage = userage.getText().toString();

    }

    private void initview() {
        LinearLayout ll_temp = (LinearLayout) findViewById(R.id.ll0);
        ll_temp.setOnClickListener(this);
        LinearLayout ll_xueya = (LinearLayout) findViewById(R.id.ll1);
        ll_xueya.setOnClickListener(this);
        LinearLayout ll_xuetang = (LinearLayout) findViewById(R.id.ll2);
        ll_xuetang.setOnClickListener(this);
        LinearLayout ll_xueyang = (LinearLayout) findViewById(R.id.ll3);
        ll_xueyang.setOnClickListener(this);
        LinearLayout ll_xindian = (LinearLayout) findViewById(R.id.ll4);
        ll_xindian.setOnClickListener(this);
        LinearLayout ll_watch = (LinearLayout) findViewById(R.id.ll5);
        ll_watch.setOnClickListener(this);

        Typeface customFont = Typeface.createFromAsset(this.getAssets(), "fonts/SourceHanSansCN-Light.otf");

        username = (TextView) findViewById(R.id.main_username);
        usersex = (TextView) findViewById(R.id.main_usersex);
        userage = (TextView) findViewById(R.id.main_userage);
        username.setTypeface(customFont);
        usersex.setTypeface(customFont);
        userage.setTypeface(customFont);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll1:
                Intent xueyaIntent = new Intent(MainActivity.this, XueyaDataActivity.class);
                xueyaIntent.putExtra("username",loginUser);
                startActivity(xueyaIntent);
                break;
            case R.id.ll0:
                Intent tempIntent = new Intent(MainActivity.this, TempDataActivity.class);
                tempIntent.putExtra("username",loginUser);
                startActivity(tempIntent);
                break;
            case R.id.ll2:
                Intent xuetangIntent = new Intent(MainActivity.this, XueTangDataActivity.class);
                xuetangIntent.putExtra("username",loginUser);
                startActivity(xuetangIntent);
                break;
            case R.id.ll3:
                Intent xueyangIntent = new Intent(MainActivity.this, XueYangDataActivity.class);
                xueyangIntent.putExtra("username",loginUser);
                startActivity(xueyangIntent);
                break;
            case R.id.ll4:
                Intent ecgIntent = new Intent(MainActivity.this, ECGDataActivity.class);
                ecgIntent.putExtra("username",loginUser);
                startActivity(ecgIntent);
                break;
            case R.id.ll5:
                Intent watchIntent = new Intent(MainActivity.this, WatchDataActivity.class);
                watchIntent.putExtra("username",loginUser);
                startActivity(watchIntent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public final static int RESULT_CODE = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                finish();
                break;
            case R.id.user_edit:
                Intent moreMsgIntent = new Intent(this, MoreMsgActivity.class);
                moreMsgIntent.putExtra("userInfo",loginUser);
                startActivityForResult(moreMsgIntent, RESULT_CODE);
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE) {

            if (resultCode == MoreMsgActivity.RESULT_CODE) {
                Bundle bundle = data.getBundleExtra("returnMsg");
                loginUser = bundle.getString("name");
                loginsex = bundle.getString("sex");
                loginage = bundle.getString("age");

                username.setText(loginUser);
                usersex.setText(loginsex);
                userage.setText(loginage);

//                /**
//                 * 保存sp本地数据，便于下次登录
//                 */
//                SpUtil.putString(getApplicationContext(),"username",name);
//                SpUtil.putString(getApplicationContext(),"usersex",sex);
//                SpUtil.putString(getApplicationContext(),"userage",age);
//                SpUtil.putBoolean(getApplicationContext(),"isLastLogin",true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
