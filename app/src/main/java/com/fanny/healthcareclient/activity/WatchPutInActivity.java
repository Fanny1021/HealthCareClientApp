package com.fanny.healthcareclient.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.bean.WatchData;
import com.fanny.healthcareclient.bean.XueYangData;

public class WatchPutInActivity extends AppCompatActivity {

    private EditText et_add_username;
    private EditText et_add_spo2;
    private EditText et_add_pr;
    private EditText et_add_pi;
    private EditText et_add_time;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_put_in);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("睡眠监测数据录入");
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
    }


    private void initView() {
        et_add_username = (EditText) findViewById(R.id.et_watch_add_username);
        et_add_spo2 = (EditText) findViewById(R.id.et_watch_add_spo2);
        et_add_pr = (EditText) findViewById(R.id.et_watch_add_pr);
        et_add_pi = (EditText) findViewById(R.id.et_watch_add_pi);
        et_add_time = (EditText) findViewById(R.id.et_watch_add_time_a);

        btn_add = (Button) findViewById(R.id.btn_watch_add_putin);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }
    private void initData() {
        if(!et_add_spo2.getText().toString().equals("")
                && !et_add_pr.getText().toString().equals("")
                && !et_add_pi.getText().toString().equals("")
                && !et_add_time.getText().toString().equals(""))
        {
            int add_spo2= Integer.parseInt((et_add_spo2.getText().toString()));
            int add_pr= Integer.parseInt(et_add_pr.getText().toString());
            float add_pi= Float.parseFloat((et_add_pi.getText().toString()));
            String add_time= et_add_time.getText().toString();

            WatchData addData=new WatchData(add_spo2,add_pr,add_pi,add_time);
            Log.e("录入数据",addData.toString());
            /**
             * 录入数据进入数据库
             */
            SQLiteDatabase db=WatchDataActivity.dbopenHelper.getWritableDatabase();
            db.execSQL("insert into shouhuanData(name,time,spo2,pr,pi) values(?,?,?,?,?)",
                    new Object[]{"机主",add_time,add_spo2,add_pr,add_pi});
            db.close();

        }else {
            Toast.makeText(this,"录入信息有误，请核实后录入",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
