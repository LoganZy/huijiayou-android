package com.huijiayou.huijiayou.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by lugg on 2016/4/1.
 */
public abstract class BaseDao<T,ID> {
    public Dao<T,ID> dao;
    public DatabaseHelper databaseHelper;

    public BaseDao(Context context, Class<T> clazz){
        databaseHelper = DatabaseHelper.getDBUtil(context);
        try {
            dao = databaseHelper.getDao(clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<T,ID> getDao(){
        return dao;
    }

    public int insert(T item){
        int id;
        try {
            id = dao.create(item);
        } catch (SQLException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    public int deleteById(ID primayKey){
        int id;
        try {
            id = dao.deleteById(primayKey);
        } catch (SQLException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    public void deleteAll(){
        try {
            List<T> lists = dao.queryForAll();
            dao.delete(lists);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int update(T item){
        int id;
        try {
            id = dao.update(item);
        } catch (SQLException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    public T getById(ID id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> getAll(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T maxOfFieldItem(String field) throws Exception {
        return dao.queryBuilder().orderBy(field,false).limit(1L).query().get(0);
    }

    public T minOfFieldItem(String field) throws Exception {
        return dao.queryBuilder().orderBy(field, true).limit(1L).query().get(0);
    }

    /**
     * get all data
     *
     * @param isAsc true asc, false desc
     * @return List<T> sort by id
     */
    public List<T> getAll(boolean isAsc) {
        try {
            return dao.queryBuilder().orderBy("id", isAsc).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * get all data
     *
     * @param field sort by field
     * @param isAsc true asc, false desc
     * @return List<T> sort by field
     */
    public List<T> getAll(String field, boolean isAsc) {
        try {
            return dao.queryBuilder().orderBy(field, isAsc).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public QueryBuilder<T, ID> getQueryBuilder() {
        return dao.queryBuilder();
    }

    public void insertWithTransaction(final List<T> items) {
        try {
            TransactionManager.callInTransaction(databaseHelper.getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T t : items) {
                        dao.create(t);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createWithTransaction(final List<T> items) {
        try {
            TransactionManager.callInTransaction(databaseHelper.getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T t : items) {
                        dao.update(t);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWithTransaction(final List<ID> items) {
        try {
            TransactionManager.callInTransaction(databaseHelper.getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    dao.deleteIds(items);
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

