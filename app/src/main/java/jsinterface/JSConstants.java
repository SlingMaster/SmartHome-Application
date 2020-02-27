package jsinterface;

/**
 * Created by Alex Dovby on 14-Dec-17.
 */

public class JSConstants {
    // ---------------------------------------------
    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
    // ---------------------------------------------
    public static final String INTERFACE_NAME = "NativeApplication";


    // events from client --------
    public static final int EVT_MAIN_TEST = 0;
    public static final int EVT_READY = 1;
    public static final int EVT_BACK = 4;
    // events from ui app client --
//    public static final int EVT_UI_PROJECT = 21;
//    public static final int EVT_UI_RETURN = 22;
    // ---------------------------

    // command for client --------
    public static final int CMD_INIT = 1000;
    // command cur app -----------
//    public static final int CMD_MEASUREMENT_START = 5000;
//    public static final int CMD_MEASUREMENT_END = 5001;
    public static final int CMD_MEASUREMENT_RESULT = 5002;
    public static final int CMD_SHOW_LIST = 5003;

    //    public static final int CMD_BACK = 4;
    //    public static final int EVT_TARGET_GEOPOSITION = 5;
//    public static final int CMD_TARGET_COMPLETE = 6;
//    public static final int CMD_UPDATE_GEOPOSITION = 7;
//    public static final int CMD_AUTO_UPDATE_GEOPOSITION = 8;
//    public static final int CMD_TARGET_GEOPOSITION = 9;
//    public static final int EVT_NAVIGATION = 10;
//    public static final int CMD_REDIRECT = 32;
    public static final int EVT_EXIT = 35;

    public static final int EVT_EXO = 777;
    public static final int EVT_EXO_RESPONSE = 888;
}
