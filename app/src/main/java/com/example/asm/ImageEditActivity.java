package com.example.asm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.status.HttpStatus;
import com.example.asm.truyen.Image;
import com.example.asm.truyen.ImageEditAdapater;
import com.example.asm.truyen.Truyen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ImageEditActivity extends AppCompatActivity implements ServerManager {
    Button plus;
    Bitmap bitmap;
    ListView lv;
    int idTruyen;

    public static ImageEditActivity ins;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_edit_activity);
        plus = findViewById(R.id.plus);
        lv = findViewById(R.id.list);
        ins = this;

        Bundle b = getIntent().getExtras();

        idTruyen = b.getInt("id");

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });
        UpdateList();
        plus.setBackgroundResource(0);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK){
            Uri selectedImage = imageReturnedIntent.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                AddImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void UpdateList(){
        new HttpAsyncTask().execute(
                HttpLinkImage(idTruyen),
                HttpAsyncTask.REQUEST_METHOD.GET,
                null,
                new AsyncTaskListener() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void end(Object result) {
                        Type type = new TypeToken<List<Image>>(){}.getType();
                        List<Image> images = new Gson().fromJson(String.valueOf(result), type);
                        ImageEditAdapater imageEditAdapater = new ImageEditAdapater(images, ImageEditActivity.this, getIntent().getExtras());
                        lv.setAdapter(imageEditAdapater);
                    }
                });
    }

    public void AddImage(){
        Dialog dialog = ProgressDialog.show(ImageEditActivity.this, "Hệ Thống", "Đang thêm ảnh vui lòng đợi...");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = new Date().getTime() + "-" + (new Random().nextInt(999999999) + 100000000);
                JSONObject jsonObject = new JSONObject();
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    byte[] b = stream.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    jsonObject.put("bitmap", encodedImage);
                    jsonObject.put("filename", fileName);
                    jsonObject.put("id_truyen", idTruyen);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new HttpAsyncTask().execute(
                        HttpLinkImage(),
                        HttpAsyncTask.REQUEST_METHOD.POST,
                        jsonObject,
                        new AsyncTaskListener() {
                            @Override
                            public void start() {

                            }

                            @Override
                            public void end(Object result) {
                                HttpStatus httpStatus = new Gson().fromJson(String.valueOf(result), HttpStatus.class);
                                if(httpStatus.status == 0){
                                    UpdateList();
                                    Toast.makeText(ImageEditActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(TruyenInfo.ins, "Lỗi khi thêm: " + httpStatus.message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }).start();
    }
}