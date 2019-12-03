package app.com.friendstracker.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anand on 06-08-2017.
 */

public class ProfileDatabaseReader  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "user.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProfileModel.Profile.TABLE_NAME + " (" +
                    ProfileModel.Profile.COLUMN_NAME_PHONE + " TEXT," +
                    ProfileModel.Profile.COLUMN_NAME_NAME + " TEXT," +
                    ProfileModel.Profile.COLUMN_NAME_BIRTHDAY + " TEXT," +
                    ProfileModel.Profile.COLUMN_NAME_PHOTO + " BLOB," +
                    ProfileModel.Profile.COLUMN_NAME_EMAIL + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProfileModel.Profile.TABLE_NAME;

    public ProfileDatabaseReader(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
