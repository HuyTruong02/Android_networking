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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.status.HttpStatus;
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

public class EditTruyenActivity extends AppCompatActivity implements ServerManager {
    Button chonAnh, sua;
    ImageView anhBia;
    EditText tenTruyen, moTaNgan, tacGia, namXuatBan;
    Bitmap bitmap;
    int idTruyen;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_truyen_activity);
        chonAnh = findViewById(R.id.chonAnh);
        anhBia = findViewById(R.id.anhBia);
        tenTruyen = findViewById(R.id.tenTruyen);
        moTaNgan = findViewById(R.id.moTaNgan);
        tacGia = findViewById(R.id.tenTacGia);
        namXuatBan = findViewById(R.id.namXuatBan);
        sua = findViewById(R.id.edit);

        Bundle b = getIntent().getExtras();

        idTruyen = b.getInt("id");

        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTruyen(bitmap);
            }
        });

        new HttpAsyncTask().execute(
                HttpLinkTruyen(idTruyen),
                HttpAsyncTask.REQUEST_METHOD.GET,
                null,
                new AsyncTaskListener() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void end(Object result) {
                        Type type = new TypeToken<List<Truyen>>(){}.getType();
                        List<Truyen> truyen = new Gson().fromJson(String.valueOf(result), type);
                        tenTruyen.setText(truyen.get(0).tenTruyen);
                        moTaNgan.setText(truyen.get(0).moTaNgan);
                        tacGia.setText(truyen.get(0).tenTacGia);
                        namXuatBan.setText(truyen.get(0).namXuatBan);
                        new HttpAsyncTask().execute(
                                truyen.get(0).anhBia,
                                HttpAsyncTask.REQUEST_METHOD.GETIMAGE,
                                null,
                                new AsyncTaskListener() {
                                    @Override
                                    public void start() {

                                    }

                                    @Override
                                    public void end(Object result) {
                                        Bitmap bitmapb = (Bitmap) result;
                                        anhBia.setImageBitmap(bitmapb);
                                        bitmap = bitmapb;
                                    }
                                });
                    }
                });

        chonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
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

    public void EditTruyen(Bitmap bitmap){
        Dialog dialog = ProgressDialog.show(EditTruyenActivity.this, "Hệ Thống", "Đang sửa truyện vui lòng đợi...");
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
                    jsonObject.put("tenTruyen", tenTruyen.getText().toString());
                    jsonObject.put("moTaNgan", moTaNgan.getText().toString());
                    jsonObject.put("tenTacGia", tacGia.getText().toString());
                    jsonObject.put("namXuatBan", namXuatBan.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new HttpAsyncTask().execute(
                        HttpLinkTruyen(idTruyen),
                        HttpAsyncTask.REQUEST_METHOD.PUT,
                        jsonObject,
                        new AsyncTaskListener() {
                            @Override
                            public void start() {

                            }

                            @Override
                            public void end(Object result) {
                                HttpStatus httpStatus = new Gson().fromJson(String.valueOf(result), HttpStatus.class);
                                if(httpStatus.status == 0){
                                    TruyenManagerActivity.ins.UpdateList();
                                    TruyenActivity.ins.UpdateList();
                                    Toast.makeText(EditTruyenActivity.this, "Sửa thành công", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(TruyenInfo.ins, "Lỗi khi sửa: " + httpStatus.message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }).start();
    }
}