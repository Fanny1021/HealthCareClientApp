package com.fanny.healthcareclient.activity;

import android.database.sqlite.SQLiteDatabase;
import android.media.DrmInitData;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.bean.XueYaData;

public class XueYaPutInActivity extends AppCompatActivity {

    private EditText et_add_username;
    private EditText et_add_sys;
    private EditText et_add_dia;
    private EditText et_add_plua;
    private EditText et_add_map;
    private EditText et_add_time;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xue_ya_put_in);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("血压数据录入");
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
    }


    private void initView() {
        et_add_username = (EditText) findViewById(R.id.et_add_username);
        et_add_sys = (EditText) findViewById(R.id.et_add_sys);
        et_add_dia = (EditText) findViewById(R.id.et_add_dia);
        et_add_plua = (EditText) findViewById(R.id.et_add_plus);
        et_add_map = (EditText) findViewById(R.id.et_add_map);
        et_add_time = (EditText) findViewById(R.id.et_add_time_a);

        btn_add = (Button) findViewById(R.id.btn_add_putin);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }
    private void initData() {
        if(!et_add_sys.getText().toString().equals("")
                && !et_add_dia.getText().toString().equals("")
                && !et_add_plua.getText().toString().equals("")
                && !et_add_map.getText().toString().equals("")
                && !et_add_time.getText().toString().equals(""))
        {
            int add_sys= Integer.parseInt(et_add_sys.getText().toString());
            int add_dia= Integer.parseInt(et_add_dia.getText().toString());
            int add_plus= Integer.parseInt(et_add_plua.getText().toString());
            int add_map= Integer.parseInt(et_add_map.getText().toString());
            String add_time= et_add_time.getText().toString();

            XueYaData addData=new XueYaData(add_sys,add_dia,add_time,add_plus,add_map);
            Log.e("录入数据",addData.toString());
            /**
             * 录入数据进入数据库
             */
            SQLiteDatabase db=XueyaDataActivity.dbopenHelper.getWritableDatabase();
            db.execSQL("insert into xueyaData(name,time,sys,dia,plus,map) values(?,?,?,?,?,?)",
                    new Object[]{"机主",add_time,add_sys,add_dia,add_plus,add_map});
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
