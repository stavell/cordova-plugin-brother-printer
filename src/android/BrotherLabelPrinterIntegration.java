package com.stavl.cordova.plugin.brotherLabelPrinter;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Base64;

import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by stavel on 8.02.18.
 */

public class BrotherLabelPrinterIntegration {

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private Printer mPrinter;
    private PrinterInfo mPrinterInfo;
    private Context context;


    BrotherLabelPrinterIntegration(Context ctx, String printerModel, String orientation, String printMode, int scaleValue, boolean isAutoCut, boolean isLabelEndCut, boolean isSpecialTape, String labelName) {
        this.context = ctx;
        mPrinter = new Printer();

        mPrinterInfo = mPrinter.getPrinterInfo();

        mPrinterInfo.port = PrinterInfo.Port.USB;
        mPrinterInfo.workPath = context.getCacheDir().getAbsolutePath();

        mPrinterInfo.printerModel = PrinterInfo.Model.valueOf(printerModel);
        mPrinterInfo.paperSize = PrinterInfo.PaperSize.CUSTOM;
        mPrinterInfo.orientation = PrinterInfo.Orientation.valueOf(orientation);
        mPrinterInfo.printMode = PrinterInfo.PrintMode.valueOf(printMode);
        mPrinterInfo.scaleValue = scaleValue;

        mPrinterInfo.isAutoCut = isAutoCut;
        mPrinterInfo.isLabelEndCut = isLabelEndCut;
        mPrinterInfo.isSpecialTape = isSpecialTape;

        mPrinterInfo.labelNameIndex = LabelInfo.QL700.valueOf(labelName).ordinal();
    }

    public JSONObject getPrinterStatus() {
        PrinterStatus printerStatus = new PrinterStatus();

        if (!checkUSB()) {
            printerStatus.errorCode = PrinterInfo.ErrorCode.ERROR_COMMUNICATION_ERROR;
        } else {
            setPrinterInfo();
            printerStatus = mPrinter.getPrinterStatus();
        }
        return statusToJSON(printerStatus);
    }


    public JSONObject printBase64Image(String base64) {
        if (!checkUSB()) return getPrinterStatus();
        setPrinterInfo();

        Bitmap bitmap = bmpFromBase64(base64);
        PrinterStatus printerStatus = mPrinter.printImage(bitmap);
        return statusToJSON(printerStatus);
    }


    private void setPrinterInfo() {
        mPrinter.setPrinterInfo(mPrinterInfo);
    }

    private static Bitmap bmpFromBase64(String base64) {
        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JSONObject statusToJSON(PrinterStatus printerStatus) {
        JSONObject result = new JSONObject();
        try {
            result.put("status", printerStatus.errorCode.name());
            result.put("code", printerStatus.errorCode.ordinal());
        } catch (JSONException ignored) {
        }
        return result;
    }

    private boolean checkUSB() {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = mPrinter.getUsbDevice(usbManager);

        if (usbDevice == null) {
            return false;
        }

        PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        }, new IntentFilter(ACTION_USB_PERMISSION));

        assert usbManager != null;

        while (true) {
            if (!usbManager.hasPermission(usbDevice)) {
                usbManager.requestPermission(usbDevice, permissionIntent);
            } else {
                break;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
        }

        return true;
    }
}
