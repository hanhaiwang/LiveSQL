# LiveSQL

#### 版本	[![](https://jitpack.io/v/hanhaiwang/LiveSQL.svg)](https://jitpack.io/#hanhaiwang/LiveSQL)

#### 介绍

LiveSQL是Android移动端的一个简单易用的实用型数据库框架。是基于Sqlite3进行二次开发的数据库架构，只需要简单的几行代码，就能集成到您应用上。

#### 软件架构
方便用户在Android移动端开发中，快速使用数据库存储等操作。不用死记SQL语法，只要给出实体类，会自动根据实体类中的成员变量生成对应的数据表，大大提高了开发效率。


#### 快速集成步骤：

1. 在项目的`build.gradle`中，添加仓库地址

   ```groovy
   allprojects {
       repositories {
           google()
           jcenter()
           maven { url 'https://jitpack.io' }
       }
   }
   ```

   

2. 在`app`的`gradle`文件（`app/build.gradle`）中，引入依赖

   ```groovy
   dependencies {
       implementation 'com.github.hanhaiwang:LiveSQL:1.0.1'
   }
   ```

   

3. 在Application的继承类的onCreate方法初始化

   ```java
   public class APP extends Application {
       @Override
       public void onCreate() {
           super.onCreate();
           //live为数据库名字，根据你的需求填写数据库名
           LiveSQL.init(this,"live");
       }
   }
   ```

   

4. 在AndroidManifest.xml 中，添加读写权限申请

   ```xml
   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
   <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
   ```




按照上述四步操作，就可以完成LiveSQL数据库框架集成了。



#### 使用说明

##### 一、定义实体类

```java
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
```

@DbTable：表示根据用户设定的值生成数据表。

@DbField：表示根据实体类中的成员变量，生成对应的数据库字段名称。



##### 二、LiveSQL数据的增、删、改、查使用方法

1. 插入数据

   ```Java
   User user = new User(1,"admin","admin",1);
   LiveFactory<User> userFactory = LiveSQL.getInstance().create(User.class);
   userFactory.insert(user);
   ```

   

2. 查询数据

   ```java
   [1]. 查询多条记录
        LiveSQL<User> liveSQL = LiveSqlFactory.getInstance().getLiveSQL(User.class);
        List<Map<String, Object>> list = liveSQL.query();
        for (Map<String, Object> map : list) {
           for(Map.Entry<String, Object> entry:map.entrySet()){
                Log.e(TAG,entry.getKey()+"--->"+entry.getValue());
           }
        }
   
   [2]. 按用户给出的字段名，查询数据
       LiveSQL<User> liveSQL = LiveSqlFactory.getInstance().getLiveSQL(User.class);
   	String[] fieldName = new String[]{"username","password"};
   	liveSQL.query(fieldName);
   
   [3]. 按条件查询数据
       LiveSQL<User> liveSQL = LiveSqlFactory.getInstance().getLiveSQL(User.class);
   	String selection = "name=?";	//需要注意：条件必须是种写法,"字段名=?" 相当于where
   	String[] selectionArgs = new String[]{"admin"};	//需要查询的条件参数数组
   	Map<String, Object> list = liveSQL.query(selection,selectionArgs);
   	liveSQL.query(fieldName);
   .........
   ```

    其他查询和单记录查询，不再论述。请详见下面的API。

   

3. 修改数据

   ```java
   LiveSQL<User> liveSQL = LiveSQL.getInstance().create(User.class);
   User user = new user(1,"我是用户名","我是密码",0);	//将需要修改的数据封装到实体类中
   String whereClause = "name=?";
   String[] whereArgs = {"admin"};
   liveSQL.update(user,whereClause,whereArgs);
   ```

   

4. 删除数据

   ```java
   LiveSQL<User> liveSQL = LiveSQL.getInstance().create(User.class);
   String selection = "name=?";
   String[] args = {"admin"};
   liveSQL.delete(selection, args);
   ```

   

##### 三、API说明

```java
 /**
     * 插入数据
     * @param entity    实体类型
     * @return          long类型
     */
    long insert(T entity);


    /**
     * 查询全部记录
     * @return
     */
    List<Map<String,Object>> query();


    /**
     * 查询全部记录
     * @param columns
     * @return
     */
    List<Map<String,Object>> query(String[] columns);

    /**
     * 按条件查询全部记录
     * @param selection         条件字段，相当于where
     * @param selectionArgs     条件字段，参数数组
     * @return
     */
    List<Map<String,Object>> query(String selection, String[] selectionArgs);

    /**
     * 按条件查询全部记录
     * @param columns           列名称数组
     * @param selection         条件字段，相当于where
     * @param selectionArgs     条件字段，参数数组
     * @return
     */
    List<Map<String,Object>> query(String[] columns, String selection, String[] selectionArgs);

    /**
     * 按条件查询全部记录，并可以设置排序方式
     * @param columns           列名称数组
     * @param selection         条件字段，相当于where
     * @param selectionArgs     条件字段，参数数组
     * @param orderBy           排序列，默认值设为null
     * @return
     */
    List<Map<String,Object>> query(String[] columns, String selection, String[] selectionArgs, String orderBy);

    /**
     * 按条件查询记录，并可以设置排序方式和查询记录的数量
     * @param columns           列名称数组
     * @param selection         条件字段，相当于where
     * @param selectionArgs     条件字段，参数数组
     * @param orderBy           排序列，默认值设为null
     * @param limit             分页查询限制，默认值设为null
     * @return
     */
    List<Map<String,Object>> query(String[] columns, String selection, String[] selectionArgs, String orderBy, String limit);

    /**
     * 按条件查询记录，可根据指定的条件并行分组、筛选或排序，同时也可以设置查询记录的数量
     * @param columns           列名称数组
     * @param selection         条件字段，相当于where
     * @param selectionArgs     条件字段，参数数组
     * @param groupBy           分组列，默认值设为null
     * @param having            分组条件，默认值设为null
     * @param orderBy           排序列，默认值设为null
     * @param limit             分页查询限制，默认值设为null
     * @return                  游标返回值，相当于RETSULT
     */
    List<Map<String,Object>> query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);

    /**
     * 查询一条记录
     * @return
     */
    Map<String,Object> queryRow();

    /**
     * 查记一条记录
     * @param selection         条件字段，相当于where
     * @param selectionArgs     条件字段，参数数组
     * @return
     */
    Map<String,Object> queryRow(String selection, String[] selectionArgs);

    /**
     * 查询一条记录
     * @param columns           列名称数组
     * @return
     */
    Map<String,Object> queryRow(String[] columns);

    /**
     * 按条件查询一条记录
     * @param columns           列名称数组
     * @param selection         条件字段，相当于where
     * @param selectionArgs     条件字段，参数数组
     * @return
     */
    Map<String,Object> queryRow(String[] columns, String selection, String[] selectionArgs);

    /**
     * 按条件查询一条记录,并进行排序
     * @param columns           列名称数组
     * @param selection         条件字段，相当于where
     * @param selectionArgs     条件字段，参数数组
     * @param orderBy           排序列，默认值设为null
     * @return
     */
    Map<String,Object> queryRow(String[] columns, String selection, String[] selectionArgs, String orderBy);

    /**
     * 按条件查询一条记录，并进分组、筛选或排序操作
     * @param columns            列名称数组
     * @param selection          条件字段，相当于where
     * @param selectionArgs      条件字段，参数数组
     * @param groupBy            分组列，默认值设为null
     * @param having             分组条件，默认值设为nul
     * @param orderBy            排序列，默认值设为null
     * @return
     */
    Map<String,Object> queryRow(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);



    /**
     * 更新数据
     * @param entity        实体类型
     * @param whereClause   条件
     * @param whereArgs     条件参数数组
     * @return              long类型
     */
    int update(T entity, String whereClause, String[] whereArgs);

    /**
     * 删除数据
     * @param whereClause   条件
     * @param whereArgs     条件参数数组
     * @return              long类型
     */
    int delete(String whereClause, String[] whereArgs);
```

如果觉得此框架对您有所帮助，麻烦收藏下，点个星。谢谢！