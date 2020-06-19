package com.hanhaiwang.livesql.core;

import java.util.List;
import java.util.Map;

/**
 * 规范所有的数据库操作
 * insert updata detect select batinsert(批量插入) batupdata(批量更新) batdetect(批量删除) ...等
 */
public interface ILiveBase<T> {

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
}
