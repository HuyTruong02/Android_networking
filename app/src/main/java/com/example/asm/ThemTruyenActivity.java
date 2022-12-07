package com.example.asm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.status.HttpStatus;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class ThemTruyenActivity extends AppCompatActivity implements ServerManager {
    Button chonAnh, them;
    ImageView anhBia;
    EditText tenTruyen, moTaNgan, tacGia, namXuatBan;
    Bitmap bitmap;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_truyen_activity);
        chonAnh = findViewById(R.id.chonAnh);
        anhBia = findViewById(R.id.anhBia);
        tenTruyen = findViewById(R.id.tenTruyen);
        moTaNgan = findViewById(R.id.moTaNgan);
        tacGia = findViewById(R.id.tenTacGia);
        namXuatBan = findViewById(R.id.namXuatBan);
        them = findViewById(R.id.them);

        chonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });

        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap == null){
                    Toast.makeText(ThemTruyenActivity.this, "Hãy chọn ảnh bìa", Toast.LENGTH_LONG).show();
                    return;
                }
                AddTruyen(bitmap);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK){
            Uri selectedImage = imageReturnedIntent.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            anhBia.setImageBitmap(bitmap);
        }
    }

    public void AddTruyen(Bitmap bitmap){
        Dialog dialog = ProgressDialog.show(ThemTruyenActivity.this, "Hệ Thống", "Đang thêm truyện vui lòng đợi...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
                String fileName = new Date().getTime() + "-" + (new Random().nextInt(999999999) + 100000000);
                JSONObject jsonObject = new JSONObject();
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    byte[] b = stream.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    jsonObject.put("bitmap", encodedImage);
                    jsonObject.put("filename", fileName);
                    jsonObject.put("tenTruyen", tenTruyen.getText().toString());
                    jsonObject.put("moTaNgan", moTaNgan.getText().toString());
                    jsonObject.put("tenTacGia", tacGia.getText().toString());
                    jsonObject.put("namXuatBan", namXuatBan.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new HttpAsyncTask().execute(
                        HttpLinkTruyen() + "/upload",
                        HttpAsyncTask.REQUEST_METHOD.POST,
                        jsonObject,
                        new AsyncTaskListener() {
                            @Override
                            public void start() {

                            }

                            @Override
                            public void end(Object result) {
                                Log.d("z", String.valueOf(result));
                                TruyenManagerActivity.ins.UpdateList();
                                TruyenActivity.ins.UpdateList();
                                Toast.makeText(ThemTruyenActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
            }
        }).start();
    }
}