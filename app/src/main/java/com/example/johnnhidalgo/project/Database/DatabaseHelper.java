package com.example.johnnhidalgo.project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.johnnhidalgo.project.modelos.Cliente;
import com.example.johnnhidalgo.project.modelos.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Project.db";

    // User table name
    private static final String TABLE_USER = "user";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // create table sql query
    private String CREATE_USER_TABLE =
            "CREATE TABLE " + TABLE_USER +
                    "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_NAME + " TEXT,"
                    + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    // Cliente table name
    private static final String TABLE_CLIENTE = "cliente";

    private static final String COLUMN_CLIENTE_ID = "cliente_id";
    private static final String COLUMN_CLIENTE_NAME = "cliente_name";
    private static final String COLUMN_CLIENTE_PASSWORD = "cliente_password";


    // create table sql query
    private String CREATE_CLIENTE_TABLE =
            "CREATE TABLE " + TABLE_CLIENTE +
                    "(" + COLUMN_CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CLIENTE_NAME + " TEXT,"
                    + COLUMN_CLIENTE_PASSWORD + " TEXT" + ")";

    // drop table sql query
    private String DROP_CLIENTE_TABLE = "DROP TABLE IF EXISTS " + TABLE_CLIENTE;



/////////////////////////////////
    //public void insertData(String name, String age, String phone, byte[] image){
    //

    // Cafeteria table name
    private static final String TABLE_CAFETERIA = "cafeteria";

    private static final String COLUMN_CAFETERIA_ID = "cafeteria_id";
    private static final String COLUMN_CAFETERIA_NAME = "cafeteria_name";
    private static final String COLUMN_CAFETERIA_DESCRPCION = "cafeteria_descripcion";
    private static final String COLUMN_CAFETERIA_PRECIO = "cafeteria_precio";
    private static final String COLUMN_CAFETERIA_IMAGEN = "cafeteria_imagen";


    private String CREATE_CAFETERIA_TABLE =
            "CREATE TABLE " + TABLE_CAFETERIA +
                    "(" + COLUMN_CAFETERIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CAFETERIA_NAME + " TEXT,"
                    + COLUMN_CAFETERIA_DESCRPCION + " TEXT,"
                    + COLUMN_CAFETERIA_PRECIO + " DOUBLE,"
                    + COLUMN_CAFETERIA_IMAGEN + " BLOB"   + ")";

    // drop table sql query
    private String DROP_CAFETERIA_TABLE = "DROP TABLE IF EXISTS " + TABLE_CAFETERIA;






    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CLIENTE_TABLE);
        db.execSQL(CREATE_CAFETERIA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CLIENTE_TABLE);
        db.execSQL(DROP_CAFETERIA_TABLE);

        // Create tables again
        onCreate(db);


    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_PASSWORD, user.getUserPass());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setIdUser(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setUserPass(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }

    public void updateUser(User user,String nombre,String pass) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, nombre);
        values.put(COLUMN_USER_PASSWORD, pass);


        db.update(TABLE_USER, values, COLUMN_USER_NAME + " = ?",
                new String[]{String.valueOf(user.getUserName())});
        db.close();
    }


    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_NAME + " = ?",
                new String[]{String.valueOf(user.getUserName())});
        db.close();
    }

    public boolean checkUser(String username) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


    public boolean checkUser(String name, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {name, password};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }



    public void addCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLIENTE_NAME, cliente.getClienteName());
        values.put(COLUMN_CLIENTE_PASSWORD, cliente.getClientePass());

        // Inserting Row
        db.insert(TABLE_CLIENTE, null, values);
        db.close();
    }



    public List<Cliente> getAllCliente() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CLIENTE_ID,
                COLUMN_CLIENTE_NAME,
                COLUMN_CLIENTE_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_CLIENTE_NAME + " ASC";
        List<Cliente> clienteList = new ArrayList<Cliente>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLIENTE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CLIENTE_ID))));
                cliente.setClienteName(cursor.getString(cursor.getColumnIndex(COLUMN_CLIENTE_NAME)));
                cliente.setClientePass(cursor.getString(cursor.getColumnIndex(COLUMN_CLIENTE_PASSWORD)));
                clienteList.add(cliente);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return clienteList;
    }


    public void updateCliente(Cliente cliente,String nombre,String pass) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLIENTE_NAME, nombre);
        values.put(COLUMN_CLIENTE_PASSWORD, pass);

        db.update(TABLE_CLIENTE, values, COLUMN_CLIENTE_NAME + " = ?",
                new String[]{String.valueOf(cliente.getClienteName())});
        db.close();
    }

    public void deleteCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLIENTE, COLUMN_CLIENTE_NAME + " = ?",
                new String[]{String.valueOf(cliente.getClienteName())});
        db.close();
    }



    public boolean checkCliente(String clientename) {
        String[] columns = {
                COLUMN_CLIENTE_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_CLIENTE_NAME + " = ?";
        String[] selectionArgs = {clientename};
        Cursor cursor = db.query(TABLE_CLIENTE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


    public boolean checkCliente(String name, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_CLIENTE_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_CLIENTE_NAME + " = ?" + " AND " + COLUMN_CLIENTE_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {name, password};

        Cursor cursor = db.query(TABLE_CLIENTE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }





}
