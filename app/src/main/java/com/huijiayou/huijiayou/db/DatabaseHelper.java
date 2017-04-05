//package com.huijiayou.huijiayou.db;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
//import com.j256.ormlite.support.ConnectionSource;
//
///**
// * Created by lugg on 2016/3/31.
// */
//public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
//
//    private static DatabaseHelper instance;
//    public static final String DB_NAME = "huijiayou.db";
//    public static final int DB_VERSION = 1;
//
//    public static synchronized DatabaseHelper getDBUtil(Context context){
//        context = context.getApplicationContext();
//        if(instance == null){
//            synchronized (DatabaseHelper.class){
//                if (instance == null){
//                    instance = new DatabaseHelper(context);
//                }
//            }
//        }
//        return instance;
//    }
//
//    private DatabaseHelper(Context context){
//        super(context, DB_NAME,null, DB_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
////        try {
////            TableUtils.createTable(connectionSource, User.class);
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
////        try {
//////            TableUtils.dropTable(connectionSource,User.class,true);
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//        onCreate(database,connectionSource);
//    }
//}
