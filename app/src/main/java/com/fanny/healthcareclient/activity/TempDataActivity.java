package com.fanny.healthcareclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fanny.healthcareclient.R;
import com.fanny.healthcareclient.dao.DBOpenHelper;
import com.fanny.healthcareclient.fragment.FragmentRecord_Temp;
import com.fanny.healthcareclient.fragment.FragmentRecord_XueYa;
import com.fanny.healthcareclient.fragment.FragmentTendency_Temp;
import com.fanny.healthcareclient.fragment.FragmentTendency_XueYa;
import com.fanny.healthcareclient.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;


public class TempDataActivity extends AppCompatActivity {

    private String TAG = "TempActivity";
    private NavigationTabBar navigationTabBar;
    private String[] colors;
    private ViewPager viewPager;

    public static DBOpenHelper dbopenHelper;
    private Connection connection;

    public static String TempName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiwen);

        dbopenHelper = new DBOpenHelper(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("体温数据分析");

        actionBar.setDisplayHomeAsUpEnabled(true);

        initDateUser();
        initFragment();
        initTopUI();
//        initDate();

    }

    private void initDateUser() {
        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        TempName=extras.getString("username");

    }

    private void initDate() {
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
                        String sql = "select * from TiWen_Info ";//查询表名为“TiWen_Info”的所有内容
                        Statement stmt = null;//创建Statement
                        stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);

                        while (rs.next()) {//<code>ResultSet</code>最初指向第一行
                            Log.e(TAG, rs.getString("UserID"));//输出第n行，列名为“UserID”的值
                            Log.e(TAG, rs.getString("UserName"));
                            Log.e(TAG, rs.getString("Device_ID"));
                            Log.e(TAG, rs.getString("Device_Type"));
                            Log.e(TAG, String.valueOf(rs.getDate("DateTime")));
                            Log.e(TAG, String.valueOf(rs.getFloat("Temp")));
                        }
                        /**
                         * 取值，存储本地数据库
                         */

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

    class myFragmentPagerAdapter extends FragmentPagerAdapter {

        FragmentManager fragmentManager;
        List<Fragment> fragmentList;

        public myFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragmentManager = fm;
            this.fragmentList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private FragmentManager myManager;
    private List<Fragment> myFragmentList = new ArrayList<Fragment>();

    private void initFragment() {
        myManager = getSupportFragmentManager();
        Fragment f1 = new FragmentRecord_Temp();
        Fragment f2 = new FragmentTendency_Temp();
        myFragmentList.add(f1);
        myFragmentList.add(f2);

    }

    private void initTopUI() {

        viewPager = (ViewPager) findViewById(R.id.vp_content_tiwen);
        viewPager.setAdapter(new myFragmentPagerAdapter(myManager, myFragmentList) {


            @Override
            public int getCount() {
                return myFragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return myFragmentList.get(position);
            }
        });
        initTopTabBar();
    }

    private void initTopTabBar() {
        /**
         * 定义底部导航的颜色数组
         */
        colors = getResources().getStringArray(R.array.default_preview);

        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_tiwen);
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(new NavigationTabBar.Model.Builder(null, Color.parseColor(colors[0]))
                .title("纪录").build());
        models.add(new NavigationTabBar.Model.Builder(null, Color.parseColor(colors[1]))
                .title("趋势").build());

        navigationTabBar.setModels(models);

        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ALL);
        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(false);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsTinted(true);
        navigationTabBar.setIsBadgeUseTypeface(true);
        navigationTabBar.setBadgeBgColor(Color.RED);
        navigationTabBar.setBadgeTitleColor(Color.WHITE);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(Color.WHITE);
        navigationTabBar.setTitleSize(35);

        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
//                changeFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.data_putin:
                Intent addIntent = new Intent(this, TempPutInActivity.class);
                startActivity(addIntent);
                break;
            case R.id.data_clean:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否清除数据？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbopenHelper.getWritableDatabase();
                        db.delete("tempData", "name=?", new String[]{"机主"});
                        db.close();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
