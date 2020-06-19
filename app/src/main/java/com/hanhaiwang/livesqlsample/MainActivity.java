package com.hanhaiwang.livesqlsample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.hanhaiwang.livesql.LiveSQL;
import com.hanhaiwang.livesql.core.LiveFactory;
import com.hanhaiwang.livesqlsample.bean.Auth;
import com.hanhaiwang.livesqlsample.bean.User;

import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button insert;
    private Button query;
    private Button update;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();
        initView();
    }

    private void initView() {
        insert = findViewById(R.id.insert);
        query = findViewById(R.id.query);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        insert.setOnClickListener(this);
        query.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.insert:
                /*Auth auth =new Auth("admin","jldkjaojudlfjoainncuxlqjroiq4646qe3218368746!$#!$89kkjalkjf");
                LiveFactory<Auth> sql = LiveSQL.getInstance().create(Auth.class);
                sql.insert(auth);*/

                User user = new User(1,"admin","admin",1);
                LiveFactory<User> userFactory = LiveSQL.getInstance().create(User.class);
                userFactory.insert(user);
                break;

            case R.id.query:
                /*LiveSQL<User> liveSQL = LiveSqlFactory.getInstance().getLiveSQL(User.class);
                //List<Map<String, Object>> list = liveSQL.query(new String[]{});
                List<Map<String, Object>> list = liveSQL.query();
                for (Map<String, Object> map : list) {
                    for(Map.Entry<String, Object> entry:map.entrySet()){
                        Log.e(TAG,entry.getKey()+"--->"+entry.getValue());
                    }
                }
                Log.e(TAG,"得到的数据是>>>>>>>>>>"+list.toString());*/

                //获取单条记录测试
                /*LiveFactory<Auth> baseDao = LiveSQL.getInstance().create(Auth.class);
                Map<String, Object> list = baseDao.queryRow();
                //Map<String, Object> list = baseDao.queryRow(new String[]{},"name=?",new String[]{"admin"});
                for(Map.Entry<String,Object> entry:list.entrySet()){
                    Log.e(TAG,entry.getKey()+"--->"+entry.getValue());
                }
                Log.e(TAG,"得到的数据是>>>>>>>>>>"+list.toString());*/

                LiveFactory<User> userLiveFactory = LiveSQL.getInstance().create(User.class);
                List<Map<String, Object>> list = userLiveFactory.query();
                for (Map<String, Object> map : list) {
                    for(Map.Entry<String, Object> entry:map.entrySet()){
                        Log.e(TAG,entry.getKey()+"--->"+entry.getValue());
                    }
                }
                Log.e(TAG,"得到的数据是>>>>>>>>>>"+list.toString());
                break;

            case R.id.update:
                LiveFactory<Auth> updateDao = LiveSQL.getInstance().create(Auth.class);
                Auth u = new Auth("andy","0000000000000000000000000000000000000000");
                String[] whereArgs = {"admin"};
                updateDao.update(u, "name=?", whereArgs);
                break;

            case R.id.delete:
                LiveFactory<Auth> deleteDao = LiveSQL.getInstance().create(Auth.class);
                String[] args = {"andy"};
                deleteDao.delete("name=?", args);
                break;
        }
    }



    /**
     * 权限检查
     */
    private void initPermission(){
        //小于Android 6.0
        if(Build.VERSION.SDK_INT<23){
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
    }

}
