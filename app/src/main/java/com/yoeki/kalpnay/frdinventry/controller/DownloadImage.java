package com.yoeki.kalpnay.frdinventry.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    String src;
    private Context context;


    public DownloadImage(Context context, String src) {
        this.src = src;
        this.context = context;

    }

    protected void onPreExecute() {

    }

    public Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Disconnect the http url connection
            // connection.disconnect();
        }
        return null;

    }

    protected void onPostExecute(Bitmap result) {



    }


}