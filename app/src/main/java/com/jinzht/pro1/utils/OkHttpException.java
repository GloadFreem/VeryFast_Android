package com.jinzht.pro1.utils;

import android.util.Log;

import com.jinzht.pro1.callback.ErrorException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * okHttp的异常
 */
public class OkHttpException {
    public ErrorException errorException;

    public OkHttpException(ErrorException errorException) {
        this.errorException = errorException;
    }

    public void httpException(Exception e) {
        if (e instanceof UnknownHostException) {
            Log.e("UnknownHostException", e.getStackTrace() + "");
            errorException.errorPage();
        } else if (e instanceof ConnectException) {
            Log.e("ConnectException", e.getStackTrace() + "");
            errorException.errorPage();
        } else if (e instanceof SocketTimeoutException) {
            errorException.errorPage();
            Log.e("SocketTimeoutException", e.getStackTrace() + "");
        } else if (e instanceof ProtocolException) {
            errorException.errorPage();
            Log.e("ProtocolException", e.getStackTrace() + "");
        } else if (e instanceof IOException) {
            errorException.errorPage();
            Log.e("IOException", e.getStackTrace() + "");
        } else {
            errorException.errorPage();
            Log.e("Exception", e.getStackTrace() + "");
        }
    }
}
