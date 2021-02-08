package com.genedev.sqlitesample;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NamesDB";
    private static final String TABLE_NAME = "NamesTB";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String[] COLUMNS = { KEY_ID, KEY_NAME };

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE NamesTB (  id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT  )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteOne(Names names) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(names.getId()) });
        db.close();
    }

    public Names getName(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Names player = new Names();
        player.setId(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)));
        player.setName(cursor.getString(1));

        return player;
    }

    public List<Names> allNames() {

        List<Names> players = new LinkedList<Names>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        Names names = null;

        if (cursor.moveToFirst()) {
            do {
                names = new Names();
                names.setId(Integer.parseInt(cursor.getString(0)));
                names.setName(cursor.getString(1));
                players.add(names);
            } while (cursor.moveToNext());
        }

        return players;
    }

    public void addPlayer(Names names) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, names.getName());
        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public int updatePlayer(Names names) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, names.getName());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "id = ?", // selections
                new String[] { String.valueOf(names.getId()) });

        db.close();

        return i;
    }


}
