/*
 * Copyright (c) 2017. Jeneral Samopal Company
 * Design and Programming by Alex Dovby
 */

package com.jsc.smarthome;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jsinterface.JSConstants;
import jsinterface.JSOut;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected static final String TAG = "tag" + MainActivity.class.getSimpleName();
    final String DIR_SD = "SmartHomeDB";
    final String FILENAME_LOG = "data_base.json";
    final static int REQUEST_CODE_CLEAR = 1;
    public static final int PERMISSION_REQUEST_CODE = 101;
    public static final int PERMISSION_CHANGE_TIMEOUT = 10000;

    private static Boolean dev_mode = false;
    DrawerLayout drawer;
    NavigationView navigationView;
    SharedPreferences preference;
    WebView webView;


    // stop screen from dimming
    PowerManager powerManager;
    private static PowerManager.WakeLock wakeLock = null;


    // js interface ------------
    protected JSOut jsOut;
    public JSONObject uiRequest;
    public static JSONArray jsonDataBaseArray = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comparePermission();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

//        toggle = new ActionBarDrawerToggle(this, drawer,
//                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                // Do whatever you want here
//                System.out.println("onDrawerClosed");
//            }
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                // Do whatever you want here
//                System.out.println("onDrawerOpened");
//                View item = (View) findViewById(R.id.nav_sh);
//                System.out.println("onDrawerOpened : " + item);
//            }
//        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // ------------------------------------
        // PowerManager
        // Stop screen from dimming
        // ------------------------------------
        if (wakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            // Create a bright wake lock
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, TAG);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // ------------------------------------
        // WebView
        // ------------------------------------
        webView = (WebView) findViewById(R.id.web_view);
        // set WebView Style background and scrollbar ----
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        //webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //webView.setScrollbarFadingEnabled(false);
        // set default black color ------------
        webView.setBackgroundColor(0);

        // web settings --------------------------------------
        WebSettings webSettings = webView.getSettings();
        // включаем поддержку JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        // определим экземпляр MyWebViewClient.
        // Он может находиться в любом месте после инициализации объекта WebView
        webView.setWebViewClient(new MyWebViewClient());
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                //change your progress bar
//                Log.w(TAG, "onProgressChanged : " + newProgress + "%");
//                if (newProgress > 50) {
//                    //progressBar.setVisibility(View.GONE);
//                    webView.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setAllowFileAccess(true);

        // set cash app ---------------------------------------
        webSettings.setAppCacheEnabled(true);
        // set wiewport scale ---------------
        // webSettings.setUseWideViewPort(isWideViewPortRequired());
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // ---------------------------------------------------
        // load html
        loadHtml(getResources().getString(R.string.sh_url));
        // ---------------------------------------------------

        // load json BD results ------------------------------
        jsonDataBaseArray = parseFileDataBase(FileUtils.readFileSD(DIR_SD, FILENAME_LOG));
        // ---------------------------------------------------
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    // ----------------------------------------
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }

    // ----------------------------------------
    protected void loadHtml(String url) {
        WebView webView = findViewById(R.id.web_view);
        webView.clearCache(true);
        webView.loadUrl(url);

        // js interface --------------------------------------
        jsOut = new JSOut(webView);
        JSIn jsIn = new JSIn();
        webView.addJavascriptInterface(jsIn, JSConstants.INTERFACE_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        dev_mode = preference.getBoolean("sw_dev_mode", false);

        // show developer mode ----------------
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_dev_mode).setVisible(dev_mode);
        // menu.findItem(R.id.nav_dev_mode).setIcon(R.drawable.ic_delete);
        // System.out.println("dev_mode : " + dev_mode);
        // ------------------------------------.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            wakeLock.release();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("wakeLock | release", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_br) {
            loadHtml(
                    dev_mode
                            ? getResources().getString(R.string.default_debug_br_url)
                            : getResources().getString(R.string.br_url)
            );
        } else if (id == R.id.nav_sh) {
            loadHtml(
                    dev_mode
                            ? getResources().getString(R.string.default_debug_sh_url)
                            : getResources().getString(R.string.sh_url)

            );
        } else if (id == R.id.nav_stats) {
            loadHtml(
                    dev_mode
                            ? getResources().getString(R.string.default_debug_stats_url)
                            : getResources().getString(R.string.stats_url)

            );
        } else if (id == R.id.nav_test) {
            loadHtml(
                    dev_mode
                            ? getResources().getString(R.string.default_debug_test_url)
                            : getResources().getString(R.string.test_url)

            );
        } else if (id == R.id.nav_settings) {
            showConfig(getApplicationContext());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // System.out.println("requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CLEAR:
                    String idClear = data.getStringExtra("clear");
                    // System.out.println("REQUEST_CODE_CLEAR | idClear : " + idClear);
                    if (idClear.equalsIgnoreCase("all_records")) {
                        jsonDataBaseArray = new JSONArray();
                        //writeFile();
                    } else {
                        jsonDataBaseArray.remove(jsonDataBaseArray.length() - 1);
                        // writeFile();

                    }
                    FileUtils.writeFileSD(DIR_SD, FILENAME_LOG, jsonDataBaseArray.toString());
                    break;
                default:
                    break;
            }
        }
    }

    // ----------------------------------------
    public void showListBD(Context context) {
        if (jsonDataBaseArray.length() > 0) {
            Intent bdInten = new Intent(context, ListDataBaseActivity.class);
            bdInten.putExtra("jsonList", jsonDataBaseArray.toString());
            startActivityForResult(bdInten, REQUEST_CODE_CLEAR);
        } else {
            Toast.makeText(this, R.string.msg_list_records, Toast.LENGTH_LONG).show();
        }
    }

    // ----------------------------------------
    public void showConfig(Context context) {
        Intent configIntent = new Intent(context, SettingsActivity.class);
        configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(configIntent);
    }

    // =========================================================
    // Create response for HTML UI
    // =========================================================
    // ----------------------------------------
    protected JSONObject createResponse(JSONObject request, JSONObject response) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JSConstants.REQUEST, request);
            obj.put(JSConstants.RESPONSE, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    // ----------------------------------------

    public JSONObject initData() {
        JSONObject obj = new JSONObject();
        String home_ssid = preference.getString("edit_ssid", "dev");
        String cur_ssid = getCurrentSsid(getApplicationContext());
        Boolean is_home_network = home_ssid.equalsIgnoreCase(cur_ssid);
        // System.out.println("cur_ssid:" + cur_ssid + "| home_ssid:" + home_ssid + "|" + home_ssid.equalsIgnoreCase(cur_ssid));
        try {
            obj.put("android_os", android.os.Build.VERSION.SDK_INT);
            obj.put("language", "en");
            obj.put("esp_ip", preference.getString("esp_ip", getResources().getString(R.string.edit_esp_ip_default)));
            obj.put("measurement_interval", ((int) Integer.parseInt(preference.getString("edit_measurement", "120"))));
            obj.put("is_home_network", is_home_network);
            obj.put("network", (is_home_network ? getResources().getString(R.string.home_network) : getResources().getString(R.string.guest_network)));
            obj.put("ssid", cur_ssid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // System.out.println("initData : " + obj.toString());
        return obj;
    }

    // =========================================================
    // Client request events
    // =========================================================

    // ----------------------------------------
    public void serviceEvents(int request, final String jsonString) {
        // System.out.println("Request ID:" + String.valueOf(request) + " | jsonString:" + jsonString);
        JSONObject requestContent = new JSONObject();

        try {
            uiRequest = new JSONObject(jsonString);
            if (uiRequest.has("request")) {
                requestContent = uiRequest.getJSONObject("request");
            } else {
                requestContent = uiRequest;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");

        switch (request) {
            case JSConstants.EVT_MAIN_TEST:
                callbackToUI(JSConstants.EVT_MAIN_TEST, createResponse(requestContent, null));
                break;
            case JSConstants.EVT_READY:
                callbackToUI(JSConstants.CMD_INIT, createResponse(requestContent, initData()));
                break;
            case JSConstants.CMD_MEASUREMENT_START:
                try {
                    wakeLock.acquire();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("wakeLock | acquire", e.getMessage());
                }
                break;
            case JSConstants.CMD_MEASUREMENT_END:
                break;
            case JSConstants.CMD_MEASUREMENT_RESULT:
                jsonDataBaseArray.put(jsonString);
                FileUtils.writeFileSD(DIR_SD, FILENAME_LOG, jsonDataBaseArray.toString());
                try {
                    wakeLock.release();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e("wakeLock | release", e.getMessage());
                }
                break;
            case JSConstants.CMD_SHOW_LIST:
                //System.out.println("[ html > android ] tag | NativeAppResponseData:" + jsonString);
                jsonDataBaseArray = parseFileDataBase(FileUtils.readFileSD(DIR_SD, FILENAME_LOG));
                showListBD(getApplicationContext());
                break;
            case JSConstants.EVT_BACK:
                break;
            case JSConstants.EVT_EXIT:
                break;
            case JSConstants.EVT_EXO:
            case JSConstants.EVT_EXO_RESPONSE:
                break;
            default:
                // redirect data to application emulator
                // callbackToUI(JSConstants.CMD_REDIRECT, createResponse(requestContent, requestContent));
                break;
        }
    }

    // =========================================================
    // Interface HTML > Application
    // =========================================================
    private class JSIn {

        private JSIn() {
        }

        @JavascriptInterface
        public final void callNative(int request, final String jsonString) {
            // HTML function send data to application -----------------------------
            serviceEvents(request, jsonString);
        }
    }


    // ----------------------------------------
    protected void callbackToUI(int target, JSONObject json) {
        if (jsOut != null) {
            jsOut.callJavaScript(target, json);
        } else {
            Log.d(TAG, "Error Missing JSInterface");// process error
        }
    }

    // =========================================================
    // utils
    // =========================================================
    private String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                ssid = connectionInfo.getSSID().replaceAll("\"", "");
            } else {
                ssid = "developer wifi";
            }
        } else {
            ssid = "emulator wifi";
        }
        // System.out.println("ssid : " + ssid);
        return ssid;
    }


    // =========================================================
    public static JSONArray parseFileDataBase(String jsonList) {
        // System.out.println("parseReadFile:" + jsonList);
        JSONArray json;
        try {
            json = new JSONArray(jsonList);
            // System.out.println("parseLogFile:" + json);
        } catch (JSONException e) {
            json = new JSONArray();
            e.printStackTrace();
        }
        // System.out.println("parseLogFile:" + json);
        return json;
    }

    // ===================================================
    // Permissions
    // ===================================================
    public void requestMultiplePermissions() {
        // вызов dialog на получение доступа к SD Card
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {


            }
        }, PERMISSION_CHANGE_TIMEOUT);
    }

    // ===================================================
    private void comparePermission() {
        boolean mobile_data_permission =
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!mobile_data_permission) {
            requestMultiplePermissions();
        }
    }
}
