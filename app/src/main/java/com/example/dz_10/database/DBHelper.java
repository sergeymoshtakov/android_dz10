package com.example.dz_10.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shopping_list.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Создание таблиц
        sqLiteDatabase.execSQL("CREATE TABLE Lists (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, date INTEGER NOT NULL, description TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE Type (_id INTEGER PRIMARY KEY AUTOINCREMENT, label TEXT NOT NULL, rule TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE Product (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, count REAL NOT NULL, list_id INTEGER NOT NULL, checked INTEGER NOT NULL, count_type INTEGER NOT NULL, FOREIGN KEY(list_id) REFERENCES Lists(_id), FOREIGN KEY(count_type) REFERENCES Type(_id));");

        // Инициализация данных (только при создании базы данных)
        initializeData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Удаление старых таблиц и создание новых
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Lists");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Type");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Product");
        onCreate(sqLiteDatabase);
    }

    // Метод для инициализации данных (добавления типов и списков)
    public void initializeData(SQLiteDatabase db) {
        // Добавление типов
        ContentValues values = new ContentValues();
        values.put("label", "шт");
        values.put("rule", "int");
        long type1Id = db.insert("Type", null, values); // Сохраняем ID для использования в Products

        values.put("label", "кг");
        values.put("rule", "float");
        long type2Id = db.insert("Type", null, values); // Сохраняем ID для использования в Products

        values.put("label", "л");
        values.put("rule", "float");
        long type3Id = db.insert("Type", null, values); // Сохраняем ID для использования в Products

        // Добавление списков
        values.clear();
        values.put("name", "List 1");
        values.put("description", "Lorem ipsum dolor sit amet");
        values.put("date", System.currentTimeMillis() / 1000); // текущее время
        long list1Id = db.insert("Lists", null, values); // Сохраняем ID для использования в Products

        values.clear();
        values.put("name", "List 2");
        values.put("description", "Shopping for groceries");
        values.put("date", System.currentTimeMillis() / 1000); // текущее время
        long list2Id = db.insert("Lists", null, values); // Сохраняем ID для использования в Products

        values.clear();
        values.put("name", "List 3");
        values.put("description", "Party supplies");
        values.put("date", System.currentTimeMillis() / 1000); // текущее время
        long list3Id = db.insert("Lists", null, values); // Сохраняем ID для использования в Products

        // Добавление продуктов
        addProductIfNotExists(db, "Product 1", 0.5, (int)list1Id, 1, (int) type2Id); // Ссылаемся на созданные ID
        addProductIfNotExists(db, "Product 2", 1, (int)list1Id, 0, (int) type1Id);
        addProductIfNotExists(db, "Product 3", 2, (int)list2Id, 0, (int) type1Id);
    }

    // Метод для добавления продукта, если его нет в базе данных
    private void addProductIfNotExists(SQLiteDatabase db, String name, double count, int listId, int checked, int countType) {
        // Проверяем, существует ли продукт с таким же именем и listId
        Cursor cursor = db.query("Product",
                new String[]{"_id"},
                "name = ? AND list_id = ?",
                new String[]{name, String.valueOf(listId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Продукт с таким именем и list_id уже существует
            Log.d("DBHelper", "Product already exists: " + name);
            cursor.close();
        } else {
            // Продукт не найден, добавляем его
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("count", count);
            values.put("list_id", listId);
            values.put("checked", checked);
            values.put("count_type", countType);

            long result = db.insert("Product", null, values);
            if (result == -1) {
                Log.e("DBHelper", "Failed to insert product: " + name); // Логирование ошибки
            } else {
                Log.d("DBHelper", "Product added: " + name); // Логирование успешной вставки
            }
        }
        if (cursor != null) {
            cursor.close(); // Закрываем курсор, чтобы избежать утечек памяти
        }
    }

    // Дальнейшие методы остаются без изменений
    public void addList(String name, String description) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("date", System.currentTimeMillis() / 1000);  // Сохраняем время в секундах
        db.insert("Lists", null, values);
    }

    public void addType(String label, String rule) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("label", label);
        values.put("rule", rule);
        db.insert("Type", null, values);
    }

    public Cursor getLists() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Lists", new String[]{"_id", "name", "date", "description"}, null, null, null, null, null);
    }

    public Cursor getProducts() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Product", new String[]{"_id", "name", "count", "list_id", "checked", "count_type"}, null, null, null, null, null);
    }

    public Cursor getProductsForList(int listId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Product",
                new String[]{"_id", "name", "count", "list_id", "checked", "count_type"},
                "list_id = ?",
                new String[]{String.valueOf(listId)},
                null, null, null);
    }

    public Cursor getTypes() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Type", new String[]{"_id", "label", "rule"}, null, null, null, null, null);
    }

    public void addProduct(String name, double count, int listId, int checked, int countType) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("count", count);
        values.put("list_id", listId);
        values.put("checked", checked);
        values.put("count_type", countType);
        db.insert("Product", null, values);
        Log.d("DB", "Product saved: " + name + ", " + count + ", " + listId + ", " + checked + ", " + countType);
    }
}