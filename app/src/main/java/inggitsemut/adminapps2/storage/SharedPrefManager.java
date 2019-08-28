package inggitsemut.adminapps2.storage;

import android.content.Context;
import android.content.SharedPreferences;

import inggitsemut.adminapps2.model.Admin;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "data";

    private static final String KEY_ADMIN_ID = "id";
    private static final String KEY_ADMIN_NAME = "name";
    private static final String KEY_ADMIN_EMAIL = "user_email";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void loginAdmin(Admin admin) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ADMIN_ID, admin.getId());
        editor.putString(KEY_ADMIN_NAME, admin.getName());
        editor.putString(KEY_ADMIN_EMAIL, admin.getUser_email());
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_ADMIN_ID, 0) != 0;
    }

    public Admin getAdmin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Admin(
                sharedPreferences.getInt(KEY_ADMIN_ID, 0),
                sharedPreferences.getString(KEY_ADMIN_NAME, null),
                sharedPreferences.getString(KEY_ADMIN_EMAIL, null)
        );
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}