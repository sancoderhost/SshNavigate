package com.example.navigate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class databasehelper {

    public class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "hosts.db";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + sshout.FeedReaderContract.FeedEntry.TABLE_NAME + " (" +

                        sshout.FeedReaderContract.FeedEntry.HOST+ " TEXT PRIMARY KEY," +

                        sshout.FeedReaderContract.FeedEntry.USER+ " TEXT,"+
                        sshout.FeedReaderContract.FeedEntry.PASSWORD + " TEXT,"+
                        sshout.FeedReaderContract.FeedEntry.KEY_LOCATION+ " TEXT,"+

                        sshout.FeedReaderContract.FeedEntry.PORT+ " INTEGER)";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + sshout.FeedReaderContract.FeedEntry.TABLE_NAME;

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }


    public final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private FeedReaderContract() {}

        /* Inner class that defines the table contents */
        public  class FeedEntry implements BaseColumns {

            public static final String TABLE_NAME = "hosts";
            public static final String USER= "username";
            public static final String PASSWORD = "passwords";
            public static final String PORT= "port";
            public  static  final  String HOST="hostname";
            public static final String KEY_LOCATION= "private_key";


        }
    }
}
