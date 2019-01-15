package com.example.lchang.sqliteimages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DBManager {

    static private DatabaseHelper dbHelper;
    private Context context;
    static private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    static public Cursor getData(String sql) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void insert(String nombre, Double precio, byte[] imagen) {

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NOMBRE, nombre);
        contentValue.put(DatabaseHelper.PRECIO, precio);
        contentValue.put(DatabaseHelper.IMAGEN, imagen);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

    }

    public Cursor fetch() {
        String[] columns = new String[]{DatabaseHelper.CODIGO, DatabaseHelper.NOMBRE
                , DatabaseHelper.PRECIO, DatabaseHelper.IMAGEN};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns
                , null, null
                , null, null, null);
        return cursor;
    }

    public void queryData(String sql) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.execSQL(sql);
    }

    static public void update(long codigo, String nombre, Double precio, byte[] imagen) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NOMBRE, nombre);
        contentValues.put(DatabaseHelper.PRECIO, precio);
        contentValues.put(DatabaseHelper.IMAGEN, imagen);
        int i = database.update(DatabaseHelper.TABLE_NAME
                , contentValues
                , DatabaseHelper.CODIGO + " = " + codigo
                , null);

    }

    static public void delete(int codigo) {

        database.delete(DatabaseHelper.TABLE_NAME
                , DatabaseHelper.CODIGO + "=" + codigo
                , null);
    }


}
