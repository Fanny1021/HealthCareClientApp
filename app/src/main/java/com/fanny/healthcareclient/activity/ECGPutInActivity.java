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
import com.fanny.healthcareclient.bean.ECGData;
import com.fanny.healthcareclient.bean.TempData;

public class ECGPutInActivity extends AppCompatActivity {

    private EditText et_add_username;
    private EditText et_add_ecg;
    private EditText et_add_time;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_put_in);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("心电数据录入");
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
    }


    private void initView() {
        et_add_username = (EditText) findViewById(R.id.et_ecg_add_username);
        et_add_ecg = (EditText) findViewById(R.id.et_add_ecg);
        et_add_time = (EditText) findViewById(R.id.et_ecg_add_time_a);

        btn_add = (Button) findViewById(R.id.btn_ecg_add_putin);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }
    private void initData() {
        if(!et_add_ecg.getText().toString().equals("")
                && !et_add_time.getText().toString().equals(""))
        {
            int add_ecg= Integer.parseInt(et_add_ecg.getText().toString());
            String add_time= et_add_time.getText().toString();

            ECGData addData=new ECGData(add_ecg,add_time);
            Log.e("录入数据",addData.toString());
            /**
             * 录入数据进入数据库
             */
            SQLiteDatabase db=ECGDataActivity.dbopenHelper.getWritableDatabase();
            db.execSQL("insert into xindianData(name,time,ecg) values(?,?,?)",
                    new Object[]{"机主",add_time,add_ecg});
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
