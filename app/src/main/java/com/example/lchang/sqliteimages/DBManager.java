package com.example.lchang.sqliteimages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {
    static private DatabaseHelper dbHelper;
    static private SQLiteDatabase database;
    private Context context;

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

     public ArrayList<Integer> getData(String sql) {
        Cursor cursor;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();

        this.open();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        cursor = database.rawQuery(sql, null);
         while (cursor.moveToNext()){
             arrayList.add(cursor.getInt(0));
         }
        this.close();
        return arrayList;

    }

    public void insert(String nombre, Double precio, byte[] imagen) {
        this.open();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NOMBRE, nombre);
        contentValue.put(DatabaseHelper.PRECIO, precio);
        contentValue.put(DatabaseHelper.IMAGEN, imagen);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        this.close();
    }

    public ArrayList<Plato> fetch() {
        this.open();
        ArrayList<Plato> arrayList = new ArrayList<>();
        String[] columns = new String[]{DatabaseHelper.CODIGO, DatabaseHelper.NOMBRE
                , DatabaseHelper.PRECIO, DatabaseHelper.IMAGEN};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns
                , null, null
                , null, null, null);
        while (cursor.moveToNext()) {
            int codigo = cursor.getInt(0);
            String nombre = cursor.getString(1);
            Double precio = cursor.getDouble(2);
            byte[] imagen = cursor.getBlob(3);

            arrayList.add(new Plato(codigo,nombre,precio,imagen));
        }
        this.close();
        return arrayList;
    }

     public void update(long codigo, String nombre, Double precio, byte[] imagen) {
        this.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NOMBRE, nombre);
        contentValues.put(DatabaseHelper.PRECIO, precio);
        contentValues.put(DatabaseHelper.IMAGEN, imagen);
        int i = database.update(DatabaseHelper.TABLE_NAME
                , contentValues
                , DatabaseHelper.CODIGO + " = " + codigo
                , null);
        this.close();
    }

    public void delete(int codigo) {
        this.open();
        database.delete(DatabaseHelper.TABLE_NAME
                , DatabaseHelper.CODIGO + "=" + codigo
                , null);
        this.close();
    }
}
