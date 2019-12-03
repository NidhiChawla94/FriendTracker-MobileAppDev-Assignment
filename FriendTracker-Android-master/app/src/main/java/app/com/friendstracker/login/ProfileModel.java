package app.com.friendstracker.login;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anand on 06-08-2017.
 */

public class ProfileModel {

    public static final String PHONE = "accountID";
    private final ProfileDatabaseReader dbReader;
        public static final int ACCOUNT_NOT_FOUND = 1;
        public static final int REQUIRED = 0;
        public static final int INVALID = 1;

        public  ProfileModel(ProfileDatabaseReader dbReader){
            this.dbReader = dbReader;

        }

        public void createAccount(Profile profile, RegistrationListener regListener){
            SQLiteDatabase dbw = this.dbReader.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Profile.COLUMN_NAME_NAME, profile.getName());
            values.put(Profile.COLUMN_NAME_EMAIL, profile.getEmail());
            values.put(Profile.COLUMN_NAME_PHONE, profile.getPhone());
            values.put(Profile.COLUMN_NAME_BIRTHDAY, profile.getBirthday());
            values.put(Profile.COLUMN_NAME_PHOTO, profile.getPhoto());
            long newRowId = dbw.insert(Profile.TABLE_NAME, null, values);
            if(newRowId==-1){
                regListener.onCreateFailure(newRowId);
            } else {
                regListener.onCreateSuccess(profile.getPhone());
            }
        }

        public void authenticate(final String phoneNo, final AuthListener authListener) {
            SQLiteDatabase db = this.dbReader.getReadableDatabase();
            String[] projection = {
                    Profile.COLUMN_NAME_PHONE,
            };

            String selection = Profile.COLUMN_NAME_PHONE + " = ?";
            String[] selectionArgs = { phoneNo};

            Cursor cursor = db.query(Profile.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            List profiles = new ArrayList<>();
            while(cursor.moveToNext()) {
                String item = cursor.getString(
                        cursor.getColumnIndexOrThrow(Profile.COLUMN_NAME_PHONE));
                profiles.add(item);
            }
            cursor.close();
            if(profiles.size()>0){
                authListener.onAuthSuccess((String)profiles.get(0));
            } else {
                authListener.onAuthFailure(ProfileModel.ACCOUNT_NOT_FOUND);
            }

        }

    public Profile getAccountData(String phoneNo) {
        Profile profile = null;
        SQLiteDatabase db = this.dbReader.getReadableDatabase();
        String[] projection = {
                Profile.COLUMN_NAME_PHONE,
                Profile.COLUMN_NAME_NAME,
                Profile.COLUMN_NAME_EMAIL,
                Profile.COLUMN_NAME_BIRTHDAY,
                Profile.COLUMN_NAME_PHOTO,
        };

        String selection = Profile.COLUMN_NAME_PHONE + " = ?";
        String[] selectionArgs = { phoneNo};

        Cursor cursor = db.query(Profile.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        List profiles = new ArrayList<>();
        while(cursor.moveToNext()) {
            profile = new Profile();
            profile.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(Profile.COLUMN_NAME_PHONE)));
            profile.setName(cursor.getString(cursor.getColumnIndexOrThrow(Profile.COLUMN_NAME_NAME)));
            profile.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(Profile.COLUMN_NAME_BIRTHDAY)));
            profile.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Profile.COLUMN_NAME_EMAIL)));
            profile.setPhoto(cursor.getBlob(cursor.getColumnIndexOrThrow(Profile.COLUMN_NAME_PHOTO)));
        }
        cursor.close();
        return profile;
    }

    public interface AuthListener {
            void onAuthSuccess(String username);
            void onAuthFailure(int type);
        }
        public interface RegistrationListener{
            void onCreateFailure(long type);
            void onCreateSuccess(String id);
        }

        public static class Profile implements BaseColumns {
            public static final String TABLE_NAME = "profile";
            public static final String COLUMN_NAME_PHONE = "phone";
            public static final String COLUMN_NAME_EMAIL = "email";
            public static final String COLUMN_NAME_NAME = "name";
            public static final String COLUMN_NAME_BIRTHDAY = "birthday";
            public static final String COLUMN_NAME_PHOTO = "photo";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public byte[] getPhoto() {
                return photo;
            }

            public void setPhoto(byte[] photo) {
                this.photo = photo;
            }

            private String name = "";
            private String phone = "";
            private String email = "";
            private String birthday = "";
            private byte[] photo = null;

        }
}
