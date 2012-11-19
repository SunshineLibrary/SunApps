package com.ssl.support.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class DatabaseQueue<T extends JSONSerializable> {

    private DatabaseQueueDBHandler dbHandler;
    private JSONSerializable.Factory<T> factory;
    private T peeked;

    public DatabaseQueue(Context context, String queueName, JSONSerializable.Factory<T> factory) {
        dbHandler = new DatabaseQueueDBHandler(context, queueName);
        this.factory = factory;
    }

    public void add(T object) {
        dbHandler.insert(object.toJSON().toString());
    }

    public void add(String json) {
        dbHandler.insert(json);
    }

    public T peek() {
        if (peeked != null)
            return peeked;
        String value = dbHandler.getFirst();
        if (value != null) {
            try {
                peeked = factory.createNewFromJSON(new JSONObject(value));
                return peeked;
            } catch (JSONException e) {
                Log.e(getClass().getName(), "Failed to parse JSON for value: " + value);
                dbHandler.getAndRemoveFirst();
                return peek();
            }
        }
        return null;
    }

    public List<T> peekBatch(int batchSize) {
        List<T> batch = new LinkedList<T>();
        List<String> stringBatch = dbHandler.getFirstBatch(batchSize);
        for (String str: stringBatch) {
            try {
                batch.add(factory.createNewFromJSON(new JSONObject(str)));
            } catch (JSONException e) {
                Log.e(getClass().getName(), "Failed to parse JSON for value: " + str);
            }
        }
        return batch;
    }

    public T pop() {
        peeked = null;
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

    public List<T> popBatch(int batchSize) {
        List<T> batch = new LinkedList<T>();
        List<String> stringBatch = dbHandler.getAndRemoveFirstBatch(batchSize);
        for (String str: stringBatch) {
            try {
                batch.add(factory.createNewFromJSON(new JSONObject(str)));
            } catch (JSONException e) {
                Log.e(getClass().getName(), "Failed to parse JSON for value: " + str);
            }
        }
        return batch;
    }

    public void release() {
        dbHandler.close();
    }

    private static class DatabaseQueueDBHandler extends SQLiteOpenHelper {

        private static final String DB_QUEUES = "db_queues";

        private static String[] COLUMNS = {"id", "value"};
        private static String ID = COLUMNS[0];
        private static String VALUE = COLUMNS[1];

        private String queueName;

        public DatabaseQueueDBHandler(Context context, String queueName) {
            super(context, DB_QUEUES, null, 1);
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

        public synchronized void insert(String value) {
            if (value != null) {
                getWritableDatabase().insert(queueName, null, getNewContentValues(value));
            }
        }

        private ContentValues getNewContentValues(String value) {
            ContentValues values = new ContentValues();
            values.put("value", value);
            return values;
        }

        public synchronized String getFirst() {
            List<String> batch = getFirstBatch(1);
            if (batch.size() == 0) {
                return null;
            }
            return batch.get(0);
        }

        public synchronized List<String> getFirstBatch(int batchSize) {
            List<String> batch = new LinkedList<String>();
            Cursor cursor = getWritableDatabase().query(queueName, COLUMNS, null, null, null, null, null);

            try {
                if(cursor.moveToFirst()) {
                    for (int i = 0; i < batchSize; i ++) {
                        batch.add(cursor.getString(cursor.getColumnIndex(VALUE)));
                        if (!cursor.moveToNext()) {
                            break;
                        }
                    }
                }
            } finally {
               cursor.close();
            }
            return batch;
        }

        public synchronized String getAndRemoveFirst() {
            List<String> batch = getAndRemoveFirstBatch(1);
            if (batch.size() == 0) {
                return null;
            }
            return batch.get(0);
        }

        public synchronized List<String> getAndRemoveFirstBatch(int batchSize) {
            List<String> batch = new LinkedList<String>();

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.query(queueName, COLUMNS, null, null, null, null, null);

            try {
                if(cursor.moveToFirst()) {
                    for (int i = 0; i < batchSize; i++) {
                        int id = cursor.getInt(cursor.getColumnIndex(ID));
                        batch.add(cursor.getString(cursor.getColumnIndex(VALUE)));
                        db.delete(queueName, ID + "=?", new String[]{String.valueOf(id)});
                        if (!cursor.moveToNext()) {
                            break;
                        }
                    }
                }
            } finally {
                cursor.close();
            }
            return batch;
        }
    }
}
