package com.stavl.cordova.plugin.brotherLabelPrinter;

import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BrotherLabelPrinter extends CordovaPlugin {

    private BrotherLabelPrinterIntegration brotherLabelPrinter;

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackctx) throws JSONException {

        if ("init".equals(action)) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        Context context = cordova.getActivity().getApplicationContext();

                        JSONObject params = args.optJSONObject(0);

                        String printerModel = params.optString("printerModel");
                        String orientation = params.optString("orientation");
                        String printMode = params.optString("printMode");
                        int scaleValue = params.optInt("scaleValue");
                        boolean isAutoCut = params.optBoolean("isAutoCut");
                        boolean isLabelEndCut = params.optBoolean("isLabelEndCut");
                        boolean isSpecialTape = params.optBoolean("isSpecialTape");
                        String labelName = params.optString("labelName");

                        brotherLabelPrinter = new BrotherLabelPrinterIntegration(context, printerModel, orientation, printMode, scaleValue, isAutoCut, isLabelEndCut, isSpecialTape, labelName);

                        getPrinterStatus(callbackctx);

                    } catch (Exception e) {
                        PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                        callbackctx.sendPluginResult(result);
                        e.printStackTrace();
                    }
                }
            });

            return true;
        }

        if ("getPrinterStatus".equals(action)) {
            getPrinterStatus(callbackctx);
            return true;
        }

        if ("printBase64Image".equals(action)) {
            printBase64Image(args, callbackctx);
            return true;
        }

        return false;
    }


    private void getPrinterStatus(final CallbackContext callbackctx) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, brotherLabelPrinter.getPrinterStatus());
                    callbackctx.sendPluginResult(result);
                } catch (Exception e) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                    callbackctx.sendPluginResult(result);
                    e.printStackTrace();
                }
            }
        });
    }

    private void printBase64Image(final JSONArray args, final CallbackContext callbackctx) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    String base64 = args.optString(0, null);
                    PluginResult result = new PluginResult(PluginResult.Status.OK, brotherLabelPrinter.printBase64Image(base64));
                    callbackctx.sendPluginResult(result);
                } catch (Exception e) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                    callbackctx.sendPluginResult(result);
                    e.printStackTrace();
                }
            }
        });
    }


}
