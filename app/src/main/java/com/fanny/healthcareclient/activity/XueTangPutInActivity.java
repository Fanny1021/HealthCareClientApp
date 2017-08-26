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
import com.fanny.healthcareclient.bean.XueTangData;
import com.fanny.healthcareclient.bean.XueYaData;

import static com.fanny.healthcareclient.R.id.et_add_dia;

public class XueTangPutInActivity extends AppCompatActivity {

    private EditText et_add_username;
    private EditText et_add_glu;
    private EditText et_add_ua;
    private EditText et_add_chol;
    private EditText et_add_time;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuetang_put_in);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("血糖数据录入");
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
    }


    private void initView() {
        et_add_username = (EditText) findViewById(R.id.et_xuetang_add_username);
        et_add_glu = (EditText) findViewById(R.id.et_add_glu);
        et_add_ua = (EditText) findViewById(R.id.et_add_ua);
        et_add_chol = (EditText) findViewById(R.id.et_add_chol);
        et_add_time = (EditText) findViewById(R.id.et_xuetang_add_time_a);

        btn_add = (Button) findViewById(R.id.btn_xuetang_add_putin);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }
    private void initData() {
        if(!et_add_glu.getText().toString().equals("")
                && !et_add_ua.getText().toString().equals("")
                && !et_add_chol.getText().toString().equals("")
                && !et_add_time.getText().toString().equals(""))
        {
            float add_glu= Float.parseFloat((et_add_glu.getText().toString()));
            float add_ua= Float.parseFloat(et_add_ua.getText().toString());
            float add_chol= Float.parseFloat(et_add_chol.getText().toString());
            String add_time= et_add_time.getText().toString();

            XueTangData addData=new XueTangData(add_glu,add_ua,add_chol,add_time);
            Log.e("录入数据",addData.toString());
            /**
             * 录入数据进入数据库
             */
            SQLiteDatabase db=XueTangDataActivity.dbopenHelper.getWritableDatabase();
            db.execSQL("insert into xuetangData(name,time,glu,ua,chol) values(?,?,?,?,?)",
                    new Object[]{"机主",add_time,add_glu,add_ua,add_chol});
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
