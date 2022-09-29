package com.yegonron.rugbylms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    //Team Table
    private static final String TEAM_TABLE_NAME = "TEAM_TABLE";
    public static final String T_ID = "_TID";
    public static final String TEAM_NAME_KEY = "TEAM_NAME";
    public static final String SEASON_NAME_KEY = "SEASON";

    private static final String CREATE_TEAM_TABLE =
            " CREATE TABLE IF NOT EXISTS " + TEAM_TABLE_NAME + "(" +
                    T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    TEAM_NAME_KEY + " TEXT NOT NULL, " +
                    SEASON_NAME_KEY + " TEXT NOT NULL, " +
                    " UNIQUE (" + TEAM_NAME_KEY + "," + SEASON_NAME_KEY + ")" +
                    ");";

    private static final String DROP_TEAM_TABLE = " DROP TABLE IF EXISTS " + TEAM_TABLE_NAME;
    private static final String SELECT_TEAM_TABLE = " SELECT * FROM " + TEAM_TABLE_NAME;

    //Player Table
    private static final String PLAYER_TABLE_NAME = "PLAYER_TABLE";
    public static final String P_ID = "_PID";
    public static final String PLAYER_NAME_KEY = "PLAYER_NAME";
    public static final String PLAYER_ROLL_KEY = "ROLL";

    private static final String CREATE_PLAYER_TABLE =
            " CREATE TABLE IF NOT EXISTS " + PLAYER_TABLE_NAME + "(" +
                    P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    T_ID + " INTEGER NOT NULL, " +
                    PLAYER_NAME_KEY + " TEXT NOT NULL, " +
                    PLAYER_ROLL_KEY + " INTEGER, " +
                    " FOREIGN KEY ( " + T_ID + ") REFERENCES " + TEAM_TABLE_NAME + "(" + T_ID + ")" +
                    ");";

    private static final String DROP_PLAYER_TABLE = " DROP TABLE IF EXISTS " + PLAYER_TABLE_NAME;
    private static final String SELECT_PLAYER_TABLE = " SELECT * FROM " + PLAYER_TABLE_NAME;

    //Status Table
    private static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ID = "_STATUS_ID";
    public static final String DATE_KEY = "STATUS_DATE";
    public static final String STATUS_KEY = "STATUS";

    private static final String CREATE_STATUS_TABLE =
            " CREATE TABLE IF NOT EXISTS " + STATUS_TABLE_NAME + "(" +
                    STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    P_ID + " INTEGER NOT NULL, " +
                    T_ID + " INTEGER NOT NULL, " +
                    DATE_KEY + " DATE NOT NULL, " +
                    STATUS_KEY + " TEXT NOT NULL, " +
                    " UNIQUE (" + P_ID + "," + DATE_KEY + ")," +
                    " FOREIGN KEY (" + P_ID + ") REFERENCES " + PLAYER_TABLE_NAME + "( " + P_ID + ")," +
                    " FOREIGN KEY (" + T_ID + ") REFERENCES " + TEAM_TABLE_NAME + "(" + T_ID + ")" +
                    ");";

    private static final String DROP_STATUS_TABLE = " DROP TABLE IF EXISTS " + STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = " SELECT * FROM " + STATUS_TABLE_NAME;

    public DbHelper(@Nullable Context context) {
        super(context, "Attendance.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TEAM_TABLE);
        db.execSQL(CREATE_PLAYER_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TEAM_TABLE);
            db.execSQL(DROP_PLAYER_TABLE);
            db.execSQL(DROP_STATUS_TABLE);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    long addTeam(String teamName, String season) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEAM_NAME_KEY, teamName);
        values.put(SEASON_NAME_KEY, season);

        return database.insert(TEAM_TABLE_NAME, null, values);

    }

    Cursor getTeamTable() {

        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_TEAM_TABLE, null);

    }

    int deleteTeam(long tid) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(TEAM_TABLE_NAME, T_ID + "=?", new String[]{String.valueOf(tid)});

    }

    long updateTeam(long tid, String teamName, String season) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEAM_NAME_KEY, teamName);
        values.put(SEASON_NAME_KEY, season);

        return database.update(TEAM_TABLE_NAME, values, T_ID + "=?", new String[]{String.valueOf(tid)});

    }

    long addPlayer(long tid, int roll, String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T_ID, tid);
        values.put(PLAYER_ROLL_KEY, roll);
        values.put(PLAYER_NAME_KEY, name);

        return database.insert(PLAYER_TABLE_NAME, null, values);

    }

    Cursor getPlayerTable(long tid) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(PLAYER_TABLE_NAME, null, T_ID + "=?", new String[]{String.valueOf(tid)}, null, null, PLAYER_ROLL_KEY);

    }

    int deletePlayer(long pid) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(PLAYER_TABLE_NAME, P_ID + "=?", new String[]{String.valueOf(pid)});
    }

    long updatePlayer(long pid, String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME_KEY, name);

        return database.update(PLAYER_TABLE_NAME, values, P_ID + "=?", new String[]{String.valueOf(pid)});

    }

    long addStatus(long pid, long tid, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(P_ID, pid);
        values.put(T_ID, tid);
        values.put(DATE_KEY, date);
        values.put(STATUS_KEY, status);

        return database.insert(STATUS_TABLE_NAME, null, values);

    }

    long updateStatus(long pid, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY, status);
        String whereClause = DATE_KEY + "='" + date + "' AND "+ P_ID + "=" + pid;

        return database.update(STATUS_TABLE_NAME, values, whereClause, null);

    }

    String getStatus(long pid, String date) {
        String status = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String whereClause = DATE_KEY + "='" + date + "' AND " + P_ID + "=" + pid;
        Cursor cursor = database.query(STATUS_TABLE_NAME, null, whereClause, null, null, null, null);
        if (cursor.moveToFirst())
            status = cursor.getString(cursor.getColumnIndexOrThrow(STATUS_KEY));
        return status;
    }

    Cursor getDistinctMonths(long tid) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(STATUS_TABLE_NAME, new String[]{DATE_KEY}, T_ID + "=" + tid, null, "substr(" + DATE_KEY + ",4,7)", null, null);
    }


}
