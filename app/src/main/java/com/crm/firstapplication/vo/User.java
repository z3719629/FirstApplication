package com.crm.firstapplication.vo;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/1/2.
 */
@Table(name="user")
public class User {

    @Column(name="id",isId=true,autoGen=true)
    private int id;

    //姓名
    @Column(name="name",property = "NOT NULL")
    private String name;

    @Column(name="password")
    private String password;

    //年龄
    @Column(name="age")
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
