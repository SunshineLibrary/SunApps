package com.sunshine.support.installer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class DatabaseQueue<T extends JSONSerializable> {

    private DatabaseQueueDBHandler dbHandler;
    private String queueName;
    private JSONSerializable.Factory<T> factory;


    public DatabaseQueue(Context context, String queueName, JSONSerializable.Factory<T> factory) {
        dbHandler = new DatabaseQueueDBHandler(context, queueName);
        this.factory = factory;
    }

    public void add(T object) {
        dbHandler.insert(object.toJSON().toString());
    }

    public T peek() {
        String value = dbHandler.getFirst();
        if (value != null) {
            try {
                return factory.createNewFromJSON(new JSONObject(value));
            } catch (JSONException e) {
                Log.e(getClass().getName(), "Failed to parse JSON for value: " + value);
                dbHandler.getAndRemoveFirst();
                return peek();
            }
        }
        return null;
    }

    public T pop() {
        String value = dbHandler.getAndRemoveFirst();
        if (value != null) {
            try {
                return factory.createNewFromJSON(new JSONObject(value));
            } catch (JSONException e) {
                Log.e(getClass().getName(), "Failed to parse JSON for value: " + value);
                return pop();
            }
        }
        return null;
    }

    private static class DatabaseQueueDBHandler extends SQLiteOpenHelper {

        private static String[] COLUMNS = {"id", "value"};
        private static String ID = COLUMNS[0];
        private static String VALUE = COLUMNS[1];

        private String queueName;

        public DatabaseQueueDBHandler(Context context, String queueName) {
            super(context, queueName, null, 1);
            this.queueName = queueName;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + queueName + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    VALUE + " TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

        public void insert(String value) {
            if (value != null) {
                getWritableDatabase().insert(queueName, null, getNewContentValues(value));
            }
        }

        private ContentValues getNewContentValues(String value) {
            ContentValues values = new ContentValues();
            values.put("value", value);
            return values;
        }

        public String getFirst() {
            Cursor cursor = getWritableDatabase().query(queueName, COLUMNS, null, null, null, null, null);
            String value = null;
            if(cursor.moveToFirst()) {
                value = cursor.getString(cursor.getColumnIndex(VALUE));
            }
            cursor.close();

            return value;
        }

        public String getAndRemoveFirst() {
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.query(queueName, COLUMNS, null, null, null, null, null);
            String value = null;
            if(cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                value = cursor.getString(cursor.getColumnIndex(VALUE));
                cursor.close();
                db.delete(queueName, ID + "=?", new String[]{String.valueOf(id)});
            } else {
                cursor.close();
            }
            return value;
        }
    }
}
