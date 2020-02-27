package com.jsc.smarthome;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * A {@link android.preference.PreferenceActivity} which implements and proxies the necessary calls
 * to be used with AppCompat.
 */
public abstract class AppCompatPreferenceActivity extends PreferenceActivity {
    public static String curVersion;
    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        curVersion = getVersionApp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // SourceTree
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    @NonNull
    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetPreferencesDefault();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    // ----------------------------------------
    private void resetPreferencesDefault() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean reset_default = preference.getBoolean("sw_reset_default", false);
        if (reset_default) {
            // reset default settings
            preference.edit().clear().apply();
            // System.out.println("resetPreferencesDefault");
            // reset last project options ---------
            SharedPreferences.Editor editor = preference.edit();
            editor.putString("dev_br_url", getResources().getString(R.string.default_debug_br_url));
            editor.putString("dev_sh_url", getResources().getString(R.string.default_debug_sh_url));
            editor.putString("dev_stats_url", getResources().getString(R.string.default_debug_stats_url));
            editor.putString("dev_test_url", getResources().getString(R.string.default_debug_test_url));

            editor.putString("home_ssid", getResources().getString(R.string.edit_home_ssid_default));
            editor.putString("edit_esp_ip", getResources().getString(R.string.edit_esp_ip_default));
            editor.putString("edit_measurement", getResources().getString(R.string.edit_interval_default));
            // editor.putString("debug_stats_url", getResources().getString(R.string.default_debug_stats_url));
            editor.apply();
        }
    }

    // ----------------------------------------
    public String getVersionApp() {

        String versionName = "***";
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionCode + "." + pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

}
