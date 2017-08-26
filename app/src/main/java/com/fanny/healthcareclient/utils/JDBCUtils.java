package com.fanny.healthcareclient.utils;


import net.sourceforge.jtds.jdbc.Driver;

import java.io.FileInputStream;
import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Fanny on 17/7/23.
 */

public class JDBCUtils {
    //数据库的登录账号和密码
    private static String userName="sa";
    private static String pwd="200chang-yanfa";
//    private static String userName="gmf";
//    private static String pwd="15701695175";
    //jdbc驱动程序
//    private static String driver;
//    private static String className="com.mysql.jdbc.Driver";
    private static String className="net.sourceforge.jtds.jdbc.Driver";
    //数据库地址
//    private static String url="jdbc:mysql://localhost:3306/heima";
    private static String url="jdbc:jtds:sqlserver://192.168.0.100:1433/ZNHLsys";

    //三个重要类的对象
    private Connection connection;
    private Statement ps;
    private ResultSet resultSet;

//    static {
//
//        try {
//            Properties properties = new Properties();
//            properties.load(new FileInputStream("db.properties"));
//            className = properties.getProperty("className");
//            url = properties.getProperty("url");
//            userName = properties.getProperty("userName");
//            pwd = properties.getProperty("pwd");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public JDBCUtils(){

        try {
            Class.forName(className);
            connection = DriverManager.getConnection(url, userName, pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public  Connection getConnection(){
        return connection;
    }





    /**
     * query操作,返回的是List套个Map，数据库是以键值对的形式存储的
     */
//    public List<Map<String, Object>> query(String sql, List<Object> params) {
//        List<Map<String, Object>> list = new ArrayList<>();
//        try {
//            //创建一个Statement，添加相关参数
//            ps = connection.prepareStatement(sql);
//            if (params != null && !params.isEmpty()) {
//                for (int i = 0; i < params.size(); i++) {
//                    ps.setObject(i + 1, params.get(i));
//                }
//            }
//            // 执行SQL语句
//            resultSet = ps.executeQuery();
//            // 处理执行结果
//            // 获取此ResultSet对象的列的属性
//            ResultSetMetaData metaData=resultSet.getMetaData();
//            int col_len=metaData.getColumnCount();
//            while (resultSet.next()){
//                Map<String,Object> map=new HashMap<>();
//                for(int i=0;i<col_len;i++){
//                    String col_name=metaData.getCatalogName(i+1);
//                    Object col_value=resultSet.getObject(col_name);
//                    map.put(col_name,col_value);
//                }
//                list.add(map);
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    /**
     * 关闭jdbc对象
     */
    public void release(){
//        if(resultSet!=null){
//            try {
//                resultSet.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        if(ps!=null){
//            try {
//                ps.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
