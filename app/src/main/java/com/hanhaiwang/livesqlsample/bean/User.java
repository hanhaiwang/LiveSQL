package com.hanhaiwang.livesqlsample.bean;

import com.hanhaiwang.livesql.core.DbField;
import com.hanhaiwang.livesql.core.DbTable;

@DbTable("user")
public class User {
    @DbField("id")
    private Integer id;
    @DbField("username")
    private String username;
    @DbField("password")
    private String password;
    @DbField("status")
    private Integer status;

    public User(int id, String username, String password, Integer status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
    }
}
