package jsinterface;

import android.util.Log;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by Alex Dovby on 14-Dec-17.
 */

public class JSOut {
    protected static final String TAG = JSOut.class.getSimpleName();
    protected WeakReference<WebView> _webView;

    // ----------------------------------------
    public JSOut(WebView appView) {
        _webView = new WeakReference<WebView>(appView);
    }

    protected final WebView getWebView(){
        WebView wv = _webView.get();
        return wv;
    }

    // Send String ----------------------------
    public void callJavaScript(int target, final String data) {
        final String s = "javascript:JavaScriptCallback(" + target + ", " + data + " );";
        final WebView wv = getWebView();
        wv.post(new Runnable() {
            @Override
            public void run() {
                wv.loadUrl(s);
            }
        });
        Log.d(TAG, "JavaScriptCallback String | " + s);
    }

    // Send JSON Object -----------------------
    public void callJavaScript(int target, final JSONObject json) {
        JSONObject json_data = json;
        if(json_data==null)
            json_data = new JSONObject();

        final String s = "javascript:JavaScriptCallback(" + target + ", " + json_data.toString() + " );";
        final WebView wv = getWebView();
        wv.post(new Runnable() {
            @Override
            public void run() {
                wv.loadUrl(s);
            }
        });
        Log.d(TAG, ">>>>>>>>>> JavaScriptCallback JSONObject | " + s);
    }

    // Send JSON Array ------------------------
    public void callJavaScript(int target, final JSONArray json) {
        String jsonString = "{}";
        if( json != null)
            jsonString = json.toString();
        final String s = "javascript:JavaScriptCallback(" + target + ", " + jsonString + " );";
        final WebView wv = getWebView();
        wv.post(new Runnable() {
            @Override
            public void run() {
                wv.loadUrl(s);
            }
        });
        // Log.d(TAG, "JavaScriptCallback JSONArray | " + s);
    }

}