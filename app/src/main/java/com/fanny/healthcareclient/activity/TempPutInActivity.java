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
import com.fanny.healthcareclient.bean.TempData;
import com.fanny.healthcareclient.bean.XueYaData;

import static com.fanny.healthcareclient.R.id.et_add_dia;
import static com.fanny.healthcareclient.R.id.et_add_map;

public class TempPutInActivity extends AppCompatActivity {

    private EditText et_add_username;
    private EditText et_add_temp;
    private EditText et_add_time;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiwen_put_in);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("体温数据录入");
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
    }


    private void initView() {
        et_add_username = (EditText) findViewById(R.id.et_temp_add_username);
        et_add_temp = (EditText) findViewById(R.id.et_add_temp);
        et_add_time = (EditText) findViewById(R.id.et_temp_add_time_a);

        btn_add = (Button) findViewById(R.id.btn_temp_add_putin);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }
    private void initData() {
        if(!et_add_username.getText().toString().equals("") && !et_add_temp.getText().toString().equals("")
                && !et_add_time.getText().toString().equals(""))
        {
            float add_temp= Float.parseFloat(et_add_temp.getText().toString());
            String add_time= et_add_time.getText().toString();
            String add_username=et_add_username.getText().toString();

            TempData addData=new TempData(add_temp,add_time,"ID",add_username);
            Log.e("录入数据",addData.toString());

            /**
             * 录入数据进入本地数据库
             */
            SQLiteDatabase db=TempDataActivity.dbopenHelper.getWritableDatabase();
            db.execSQL("insert into tempData(name,time,temp) values(?,?,?)",
                    new Object[]{add_username,add_time,add_temp});
            db.close();

            /**
             * 后期：本地录入进入远程数据库
             */


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
