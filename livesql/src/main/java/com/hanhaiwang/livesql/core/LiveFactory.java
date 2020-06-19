package com.hanhaiwang.livesql.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.database.Cursor.FIELD_TYPE_BLOB;
import static android.database.Cursor.FIELD_TYPE_FLOAT;
import static android.database.Cursor.FIELD_TYPE_INTEGER;
import static android.database.Cursor.FIELD_TYPE_NULL;
import static android.database.Cursor.FIELD_TYPE_STRING;

public class LiveFactory<T> implements ILiveBase<T> {

    private SQLiteDatabase sqLiteDatabase;
    private Class<T> entityClass;
    private String tableName;
    private boolean isInit;
    private HashMap<String, Field> cacheMap;
    private List<Map<String, Object>> lists;
    private Map<String, Object> maps;

    public boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClass = entityClass;

        if (!isInit) {
            tableName = entityClass.getAnnotation(DbTable.class).value();
            if (!sqLiteDatabase.isOpen()) {
                return false;
            }
            String createTableSql = getCreateTableSql();
            sqLiteDatabase.execSQL(createTableSql);
            cacheMap = new HashMap<>();
            initCacheMap();

            lists = new ArrayList<>();
            maps = new HashMap<>();

            isInit = true;
        }
        return isInit;
    }

    private void initCacheMap() {
        String sql = "select * from " + tableName + " limit 1,0";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        cursor.close();
        Field[] columnFields = entityClass.getDeclaredFields();
        for (String columnName : columnNames) {
            Field resultField = null;
            for (Field columnField : columnFields) {
                String fieldAnnotationName = columnField.getAnnotation(DbField.class).value();
                if (fieldAnnotationName.equals(columnName)) {
                    resultField = columnField;
                    break;
                }
            }
            if (resultField != null) {
                cacheMap.put(columnName, resultField);
            }
        }

    }

    private String getCreateTableSql() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(tableName + "(");
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Class<?> type = declaredField.getType();
            if (type == String.class) {
                stringBuffer.append(declaredField.getAnnotation(DbField.class).value() + " TEXT,");
            } else if (type == Integer.class) {
                stringBuffer.append(declaredField.getAnnotation(DbField.class).value() + " INTEGER,");
            } else if (type == Double.class) {
                stringBuffer.append(declaredField.getAnnotation(DbField.class).value() + " DOUBLE,");
            } else if (type == Long.class) {
                stringBuffer.append(declaredField.getAnnotation(DbField.class).value() + " LONG,");
            } else if (type == byte[].class) {
                stringBuffer.append(declaredField.getAnnotation(DbField.class).value() + " BLOB,");
            } else {
                //不支持的类型
                continue;
            }
        }
        if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }


    @Override
    public long insert(T entity) {
        Map<String, String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        long result = sqLiteDatabase.insert(tableName, null, values);
        return result;
    }


    @Override
    public List<Map<String, Object>> query() {
        return query(null);
    }


    @Override
    public List<Map<String, Object>> query(String[] columns) {
        return query(columns, null, null);
    }

    @Override
    public List<Map<String, Object>> query(String selection, String[] selectionArgs) {
        return query(null, selection, selectionArgs);
    }

    @Override
    public List<Map<String, Object>> query(String[] columns, String selection, String[] selectionArgs) {
        return query(columns, selection, selectionArgs, null);
    }

    @Override
    public List<Map<String, Object>> query(String[] columns, String selection, String[] selectionArgs, String orderBy) {
        return query(columns, selection, selectionArgs, orderBy, null);
    }


    @Override
    public List<Map<String, Object>> query(String[] columns, String selection, String[] selectionArgs, String orderBy, String limit) {
        return query(columns, selection, selectionArgs, null, null, orderBy, limit);
    }

    @Override
    public List<Map<String, Object>> query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        if (selection != null) {
            if (!selection.contains("=?")) {
                throw new RuntimeException("传入条件的参数不正确，请检查！");
            }
        }
        Cursor cursor = sqLiteDatabase.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (cursor.moveToFirst()) {
            for (int x = 0; x < cursor.getCount(); x++) {
                maps = getFieldValue(cursor);
                lists.add(maps);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return lists;
    }

    @Override
    public Map<String, Object> queryRow() {
        return queryRow(null);
    }

    @Override
    public Map<String, Object> queryRow(String selection, String[] selectionArgs) {
        return queryRow(null, selection, selectionArgs);
    }

    @Override
    public Map<String, Object> queryRow(String[] columns) {
        return queryRow(columns, null, null);
    }


    @Override
    public Map<String, Object> queryRow(String[] columns, String selection, String[] selectionArgs) {
        return queryRow(columns, selection, selectionArgs, null);
    }

    @Override
    public Map<String, Object> queryRow(String[] columns, String selection, String[] selectionArgs, String orderBy) {
        return queryRow(columns, selection, selectionArgs, null, null, orderBy);
    }

    @Override
    public Map<String, Object> queryRow(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        if (selection != null) {
            if (!selection.contains("=?")) {
                throw new RuntimeException("传入条件的参数不正确，请检查！");
            }
        }
        Cursor cursor = sqLiteDatabase.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, "1");
        if (cursor.moveToFirst()) {
            maps = getFieldValue(cursor);
        }
        cursor.close();
        return maps;
    }

    @Override
    public int update(T entity, String whereClause, String[] whereArgs) {
        if (!whereClause.contains("=?")) {
            throw new RuntimeException("传入条件的参数不正确，请检查！");
        }
        Map<String, String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        return sqLiteDatabase.update(tableName, values, whereClause, whereArgs);
    }

    @Override
    public int delete(String whereClause, String[] whereArgs) {
        if (!whereClause.contains("=?")) {
            throw new RuntimeException("传入条件的参数不正确，请检查！");
        }
        int num = sqLiteDatabase.delete(tableName, whereClause, whereArgs);
        if (num > 0x00) {
            return num;
        }
        return 0x00;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    private Map<String, String> getValues(T entity) {
        HashMap<String, String> map = new HashMap<>();
        Iterator<Field> fieldIterator = cacheMap.values().iterator();
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            field.setAccessible(true);
            try {
                Object object = field.get(entity);
                if (object == null) {
                    continue;
                }
                String value = object.toString();
                String key = field.getAnnotation(DbField.class).value();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private Map<String, Object> getFieldValue(Cursor cursor) {
        String[] columnNames = cursor.getColumnNames();
        for (int i = 0; i < columnNames.length; i++) {
            if (cursor.getType(i) == FIELD_TYPE_INTEGER) { //1
                maps.put(columnNames[i], cursor.getLong(i));
            } else if (cursor.getType(i) == FIELD_TYPE_FLOAT) {  //2
                maps.put(columnNames[i], cursor.getFloat(i));
            } else if (cursor.getType(i) == FIELD_TYPE_STRING) { //3
                maps.put(columnNames[i], cursor.getString(i));
            } else if (cursor.getType(i) == FIELD_TYPE_BLOB) {   //4
                maps.put(columnNames[i], cursor.getBlob(i));
            }
        }
        return maps;
    }
}

