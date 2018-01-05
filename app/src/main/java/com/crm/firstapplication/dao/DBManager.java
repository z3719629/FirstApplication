package com.crm.firstapplication.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.crm.firstapplication.vo.User;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

public class DBManager {

    public static DbManager db;
    public static void initDb(){
        //本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("test_db") //设置数据库名
                .setDbVersion(1) //设置数据库版本,每次启动应用时将会检查该版本号,
                //发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(true)//设置是否开启事务,默认为false关闭事务
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {

                    }
                })//设置数据库创建时的Listener
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //balabala...
                    }
                });//设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        //.setDbDir(null);//设置数据库.db文件存放的目录,默认为包名下databases目录下
        db = x.getDb(daoConfig);
    }

    public static void dbFindAll() throws DbException {
        List<User> users = db.findAll(User.class);
    }

    public static void dbFindSelector() throws DbException {
        //List<User> users = db.findAll(User.class);
        //showDbMessage("【dbFind#findAll】第一个对象:"+users.get(0).toString());

        //User user = db.findById(User.class, 1);
        //showDbMessage("【dbFind#findById】第一个对象:" + user.toString());

        //long count = db.selector(User.class).where("name","like","%kevin%").and("email","=","caolbmail@gmail.com").count();//返回复合条件的记录数
        //showDbMessage("【dbFind#selector】复合条件数目:" + count);

        List<User> users = db.selector(User.class)
                .where("name","like","%admin%")
                .and("name", "=", "admin")
                .orderBy("id",true)
                .limit(2) //只查询两条记录
                //.offset(2) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        if(users == null || users.size() == 0){
            return;//请先调用dbAdd()方法
        }
        //showDbMessage("【dbFind#selector】复合条件数目:" + users.size());
    }

    public static void dbDelete() throws DbException {
        List<User> users = db.findAll(User.class);
        if(users == null || users.size() == 0){
            return;//请先调用dbAdd()方法
        }
        //db.delete(users.get(0)); //删除第一个对象
        //db.delete(User.class);//删除表中所有的User对象【慎用】
        //db.delete(users); //删除users对象集合
        //users =  db.findAll(User.class);
        // showDbMessage("【dbDelete#delete】数据库中还有user数目:" + users.size());

        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("id",">","5").or("id","=","1").expr(" and name > '2015-12-29 00:00:01' ");
        db.delete(User.class,whereBuilder);
        users =  db.findAll(User.class);
        //showDbMessage("【dbDelete#delete】数据库中还有user数目:" + users.size());
    }

    public static void dbInsert(Object o) throws DbException {
        db.saveOrUpdate(o);
    }

    public static void dbUpdate() throws DbException {
        List<User> users = db.findAll(User.class);
        if(users == null || users.size() == 0){
            return;//请先调用dbAdd()方法
        }
        User user = users.get(0);
        user.setName("admin");
        //db.replace(user);
        //db.update(user);
        //db.update(user,"email");//指定只对email列进行更新

        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("id",">","5").or("id","=","1"); //.expr(" and name > '2015-12-29 00:00:01' ");
        db.update(User.class,whereBuilder,
                new KeyValue("name",System.currentTimeMillis() / 1000 + "@email.com")
                ,new KeyValue("age",15));//对User表中复合whereBuilder所表达的条件的记录更新email和mobile
    }

    public static void selectBySql(String sql) throws DbException {
        db.execQuery(sql);
    }

    public static <T> T findById(Class<T> clazz, int id) throws DbException {
        return db.findById(clazz, id);
    }
}
