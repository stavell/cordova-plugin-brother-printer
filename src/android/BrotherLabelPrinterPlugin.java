package com.stavl.cordova.plugin.brotherLabelPrinter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.app.PendingIntent;

import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.NetPrinter;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;
import com.sun.jdi.InternalException;

public class BrotherPrinterPlugin extends CordovaPlugin {
 
    //token to make it easy to grep logcat
    private static final String TAG = "print";

    private CallbackContext callbackctx;
    private BrotherLabelPrinter brotherLabelPrinter;

    @Override
    public boolean execute (String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if ("init".equals(action)) {
            Context context = cordova.getActivity().getApplicationContext();


            // brotherLabelPrinter = BrotherLabelPrinter(context, args);

            return true;
        }

        if ("getPrinterStatus".equals(action)) {

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        JSONObject r = brotherLabelPrinter.getPrinterStatus();
                        JSONArray args = new JSONArray();
                        args.put(r);

                        PluginResult result = new PluginResult(PluginResult.Status.OK, args);
                        callbackctx.sendPluginResult(result);
                    } catch(Exception e){    
                        e.printStackTrace();
                    }
                }
            });

            return true;
        }

        if ("printBase64Image".equals(action)) {

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        
                        String base64 = args.optString(0, null);
                        JSONObject r = brotherLabelPrinter.printBase64Image(base64);

                        JSONArray args = new JSONArray();
                        args.put(r);

                        PluginResult result = new PluginResult(PluginResult.Status.OK, args);
                        callbackctx.sendPluginResult(result);

                    } catch(Exception e){    
                        e.printStackTrace();
                    }
                }
            });
            
            return true;
        }

        return false;
    } 
 

}
