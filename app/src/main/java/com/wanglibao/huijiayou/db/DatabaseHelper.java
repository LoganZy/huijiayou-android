package com.wanglibao.woodtrade.woodtrade.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wanglibao.woodtrade.woodtrade.config.Constant;
import com.wanglibao.woodtrade.woodtrade.model.City;
import com.wanglibao.woodtrade.woodtrade.model.Region;
import com.wanglibao.woodtrade.woodtrade.model.ShopOrder;
import com.wanglibao.woodtrade.woodtrade.model.Unit;
import com.wanglibao.woodtrade.woodtrade.model.User;
import com.wanglibao.woodtrade.woodtrade.model.WoodType;
import com.wanglibao.woodtrade.woodtrade.model.WoodTypeDetail;
import com.wanglibao.woodtrade.woodtrade.model.WoodTypeDetailThreePlace;

import java.sql.SQLException;

/**
 * Created by lugg on 2016/3/31.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDBUtil(Context context){
        context = context.getApplicationContext();
        if(instance == null){
            synchronized (DatabaseHelper.class){
                if (instance == null){
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    private DatabaseHelper(Context context){
        super(context, Constant.DB_NAME,null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Region.class);
            TableUtils.createTable(connectionSource, City.class);
            TableUtils.createTable(connectionSource, Unit.class);
            TableUtils.createTable(connectionSource, WoodType.class);
            TableUtils.createTable(connectionSource, WoodTypeDetail.class);
            TableUtils.createTable(connectionSource, ShopOrder.class);
            TableUtils.createTable(connectionSource, WoodTypeDetailThreePlace.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,User.class,true);
            TableUtils.dropTable(connectionSource,Region.class,true);
            TableUtils.dropTable(connectionSource,City.class,true);
            TableUtils.dropTable(connectionSource,Unit.class,true);
            TableUtils.dropTable(connectionSource,WoodType.class,true);
            TableUtils.dropTable(connectionSource,WoodTypeDetail.class,true);
            TableUtils.dropTable(connectionSource,ShopOrder.class,true);
            TableUtils.dropTable(connectionSource,WoodTypeDetailThreePlace.class,true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database,connectionSource);
    }
}
