package jsinterface;
/*
 * Copyright (c) 2017. Jeneral Samopal Company
 * Design and Programming by Alex Dovby
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
    // ---------------------------

    // command for client --------
    public static final int CMD_INIT = 1000;
    // command cur app -----------
    public static final int CMD_MEASUREMENT_RESULT = 5002;
    public static final int CMD_SHOW_LIST = 5003;
    public static final int EVT_EXIT = 35;

    public static final int EVT_EXO = 777;
    public static final int EVT_EXO_RESPONSE = 888;
}
