package com.example.asm.httpasynctask;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.asm.ServerManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpAsyncTask implements ServerManager {

    public enum REQUEST_METHOD {GET, POST, PUT, DELETE, GETIMAGE, OPTIONS};

    public void execute(String url, REQUEST_METHOD request_method, JSONObject data, AsyncTaskListener asyncTaskListener){
        switch (request_method){
            case GET:
                Get(url, asyncTaskListener);
                break;
            case GETIMAGE:
                GetImage(url, asyncTaskListener);
                break;
            default:
                Post(url, request_method, data, asyncTaskListener);
                break;
        }
    }

    private void Post(String url, REQUEST_METHOD request_method,JSONObject data, AsyncTaskListener asyncTaskListener) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                asyncTaskListener.start();
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);

                    //Open Connection
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setDoOutput(true);

                    httpURLConnection.setDoInput(true);

                    httpURLConnection.setRequestMethod(request_method.name());

                    httpURLConnection.setRequestProperty("Content-Type", "application/json");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    writer.append(data.toString());

                    writer.flush();
                    writer.close();
                    outputStream.close();

                    if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader read = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();

                        String dong;

                        while ((dong = read.readLine()) != null){
                            stringBuilder.append(dong).append("\n");
                        }
                        return stringBuilder.toString();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                asyncTaskListener.end(s);
            }
        }.execute(url);
    }

/*    public static AsyncTask Get(String url, AsyncTaskListener asyncTaskListener) {
        return new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                asyncTaskListener.start();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    //Open Connection
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //Tao doi tuong doc du lieu tu GET
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //Tao bien do nho dem
                    BufferedReader read = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder builder = new StringBuilder();
                    String dong;

                    while ((dong = read.readLine()) != null){
                        builder.append(dong);
                    }
                    read.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return builder.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                asyncTaskListener.end(s);
            }
        }.execute(url);
    }*/
    private void Get(String url, AsyncTaskListener asyncTaskListener) {
         new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                asyncTaskListener.start();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    //Open Connection
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //Tao doi tuong doc du lieu tu GET
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //Tao bien do nho dem
                    BufferedReader read = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder builder = new StringBuilder();
                    String dong;

                    while ((dong = read.readLine()) != null){
                        builder.append(dong);
                    }
                    read.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return builder.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String v) {
                super.onPostExecute(v);
                asyncTaskListener.end(v);
            }
        }.execute(url);
    }

    private void GetImage(String url, AsyncTaskListener asyncTaskListener) {
        new AsyncTask<String, Void, Bitmap>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                asyncTaskListener.start();
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                return loadImageFromUrl(strings[0]);
            }

            @Override
            protected void onPostExecute(Bitmap v) {
                super.onPostExecute(v);
                asyncTaskListener.end(v);
            }
        }.execute(url);
    }

    private Bitmap loadImageFromUrl(String link){
        String imageLink = HttpLinkImageUpload(link);

        URL url;
        Bitmap bmp = null;
        try{
            url = new URL(imageLink);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        }catch (Exception e){
            e.fillInStackTrace();
        }
        return bmp;
    }
}
