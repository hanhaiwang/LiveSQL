package com.hanhaiwang.livesqlsample.bean;


import com.hanhaiwang.livesql.core.DbField;
import com.hanhaiwang.livesql.core.DbTable;

@DbTable("auth")
public class Auth {
    /*@DbField("_id")
    private Integer id;*/
    @DbField("name")
    private String name;
    @DbField("token")
    private String token;

    public Auth(String name, String token) {
        this.name = name;
        this.token = token;
    }
}
