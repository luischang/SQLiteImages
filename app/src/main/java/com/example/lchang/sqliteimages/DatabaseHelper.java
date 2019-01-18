package com.example.lchang.sqliteimages;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Nombre de la tabla
    public static final String TABLE_NAME = "PLATO";

    // Columnas de la tabla
    public static final String CODIGO = "codigo";
    public static final String NOMBRE = "nombre";
    public static final String PRECIO = "precio";
    public static final String IMAGEN = "imagen";

    // Nombre de la base de datos
    static final String DB_NAME = "RESTAURANT.DB";

    // Versión de la base de datos(importante)
    static final int DB_VERSION = 3;

    // Script para la creación de la tabla
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + CODIGO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOMBRE + " TEXT NOT NULL, "
            + PRECIO + " REAL NOT NULL, "
            + IMAGEN + " BLOB NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
