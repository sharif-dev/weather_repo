package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * a simple database for saving history of Weather Forecast
 * https://developer.android.com/training/data-storage/sqlite
 */

public class WeatherForecastDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WeatherForecast.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WeatherForecastContract.FeedEntry.TABLE_NAME + " (" +
                    WeatherForecastContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    WeatherForecastContract.FeedEntry.COLUMN_NAME_DATE + " TIMESTAMP," +
                    WeatherForecastContract.FeedEntry.COLUMN_NAME_JSON + " TEXT," +
                    WeatherForecastContract.FeedEntry.CITY_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WeatherForecastContract.FeedEntry.TABLE_NAME;


    public WeatherForecastDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}

final class WeatherForecastContract{
    private WeatherForecastContract() {}
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather_forecast";
        public static final String COLUMN_NAME_DATE = "title";
        public static final String COLUMN_NAME_JSON = "subtitle";
        public static final String CITY_NAME = "city";
    }
}