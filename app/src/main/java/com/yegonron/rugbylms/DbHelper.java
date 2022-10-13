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

    //Game Table
    private static final String GAME_TABLE_NAME = "GAME_TABLE";
    private static final String G_ID = "_GID";
    private static final String GAME_DATE = "GAME_DATE";
    private static final String HOME_TEAM = "HOME_TEAM";
    private static final String AWAY_TEAM = "AWAY_TEAM";
    private static final String HOME_TEAM_SCORE = "HOME_TEAM_SCORE";
    private static final String AWAY_TEAM_SCORE = "AWAY_TEAM_SCORE";

    private static final String CREATE_GAME_TABLE =
            " CREATE TABLE IF NOT EXISTS " + GAME_TABLE_NAME + "(" +
                    G_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    T_ID + " INTEGER NOT NULL, " +
                    GAME_DATE + " DATETIME NOT NULL, " +
                    HOME_TEAM + " INTEGER NOT NULL, " +
                    AWAY_TEAM + " INTEGER NOT NULL, " +
                    HOME_TEAM_SCORE + " INTEGER NOT NULL, " +
                    AWAY_TEAM_SCORE + " INTEGER NOT NULL, " +
                    " FOREIGN KEY ( " + T_ID + ") REFERENCES " + TEAM_TABLE_NAME + "(" + T_ID + ")" +
                    ");";

    private static final String DROP_GAME_TABLE = " DROP TABLE IF EXISTS " + GAME_TABLE_NAME;
    private static final String SELECT_GAME_TABLE = " SELECT * FROM " + GAME_TABLE_NAME;

    //Fixtures Table
    private static final String FIXTURES_TABLE_NAME = "FIXTURES_TABLE";
    public static final String F_ID = "_FID";
    public static final String FIXTURES_NAME_KEY = "FIXTURES_NAME";
    public static final String FIXTURE_DATE = "FIXTURE_DATE";
    public static final String PATH = "PATH";
    public static final String FIXTURE_VENUE = "FIXTURE_VENUE";
    public static final String FIXTURE_TIME = "FIXTURE_TIME";

    private static final String CREATE_FIXTURES_TABLE =
            " CREATE TABLE IF NOT EXISTS " + FIXTURES_TABLE_NAME + "(" +
                    F_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    T_ID + " INTEGER NOT NULL, " +
                    G_ID + " INTEGER NOT NULL, " +
                    FIXTURES_NAME_KEY + " TEXT NOT NULL, " +
                    FIXTURE_DATE + " DATETIME NOT NULL, " +
                    PATH + " VARCHAR NOT NULL, " +
                    HOME_TEAM + " INTEGER NOT NULL, " +
                    AWAY_TEAM + " INTEGER NOT NULL, " +
                    FIXTURE_VENUE + " VARCHAR NOT NULL, " +
                    FIXTURE_TIME + " TIME NOT NULL, " +
                    SEASON_NAME_KEY + " TEXT NOT NULL, " +
                    " FOREIGN KEY ( " + T_ID + ") REFERENCES " + TEAM_TABLE_NAME + "(" + T_ID + ")" +
                    " , FOREIGN KEY ( " + G_ID + ") REFERENCES " + GAME_TABLE_NAME + "(" + T_ID + ")" +
                    ");";


    private static final String DROP_FIXTURES_TABLE = " DROP TABLE IF EXISTS " + FIXTURES_TABLE_NAME;
    private static final String SELECT_FIXTURES_TABLE = " SELECT * FROM " + FIXTURES_TABLE_NAME;
    private static final String UPDATE_GAME_FIXTURES = " SELECT TEAM_NAME_KEY AS Team, Sum(P) AS P,Sum(W) AS W,Sum(D) AS D,Sum(L) AS L,\n" +
            "    SUM(GF) as GF,SUM(GA) AS GA,SUM(GD) AS GD,SUM(Pts) AS Pts\n" +
            "    FROM(\n" +
            "         SELECT\n" +
            "              HOME_TEAM Team,\n" +
            "              1 P,\n" +
            "              IF(HOME_TEAM_SCORE > AWAY_TEAM_SCORE,1,0) W,\n" +
            "              IF(HOME_TEAM_SCORE = AWAY_TEAM_SCORE,1,0) D,\n" +
            "              IF(HOME_TEAM_SCORE < AWAY_TEAM_SCORE,1,0) L,\n" +
            "              HOME_TEAM_SCORE GF,\n" +
            "              AWAY_TEAM_SCORE GA,\n" +
            "              HOME_TEAM_SCORE-AWAY_TEAM_SCORE GD,\n" +
            "              CASE WHEN HOME_TEAM_SCORE > AWAY_TEAM_SCORE THEN 4 " +
            "                   WHEN HOME_TEAM_SCORE = AWAY_TEAM_SCORE THEN 2 " +
            "              ELSE 0 END PTS\n" +
            "         FROM GAME_TABLE_NAME\n" +
            "         UNION ALL\n" +
            "         SELECT\n" +
            "               AWAY_TEAM,\n" +
            "               1,\n" +
            "               IF(HOME_TEAM_SCORE < AWAY_TEAM_SCORE,1,0),\n" +
            "               IF(HOME_TEAM_SCORE = AWAY_TEAM_SCORE,1,0),\n" +
            "               IF(HOME_TEAM_SCORE > AWAY_TEAM_SCORE,1,0),\n" +
            "               AWAY_TEAM_SCORE,\n" +
            "               HOME_TEAM_SCORE,\n" +
            "               AWAY_TEAM_SCORE-HOME_TEAM_SCORE GD,\n" +
            "               CASE WHEN HOME_TEAM_SCORE < AWAY_TEAM_SCORE THEN 2 " +
            "                    WHEN HOME_TEAM_SCORE = AWAY_TEAM_SCORE THEN 1 " +
            "               ELSE 0 END\n" +
            "          FROM GAME_TABLE_NAME\n" +
            "     ) as tot\n" +
            "    JOIN TEAM_TABLE_NAME t ON tot.Team=t.T_ID\n" +
            "    GROUP BY Team\n" +
            "    ORDER BY SUM(Pts) DESC;";


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
        db.execSQL(CREATE_GAME_TABLE);
        db.execSQL(CREATE_FIXTURES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TEAM_TABLE);
            db.execSQL(DROP_PLAYER_TABLE);
            db.execSQL(DROP_STATUS_TABLE);
            db.execSQL(DROP_GAME_TABLE);
            db.execSQL(DROP_FIXTURES_TABLE);

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

    void deleteTeam(long tid) {
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(TEAM_TABLE_NAME, T_ID + "=?", new String[]{String.valueOf(tid)});

    }

    void updateTeam(long tid, String teamName, String season) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEAM_NAME_KEY, teamName);
        values.put(SEASON_NAME_KEY, season);

        database.update(TEAM_TABLE_NAME, values, T_ID + "=?", new String[]{String.valueOf(tid)});

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

    void deletePlayer(long pid) {
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(PLAYER_TABLE_NAME, P_ID + "=?", new String[]{String.valueOf(pid)});
    }

    void updatePlayer(long pid, String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME_KEY, name);

        database.update(PLAYER_TABLE_NAME, values, P_ID + "=?", new String[]{String.valueOf(pid)});

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

    void updateStatus(long pid, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY, status);
        String whereClause = DATE_KEY + "='" + date + "' AND " + P_ID + "=" + pid;

        database.update(STATUS_TABLE_NAME, values, whereClause, null);

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
