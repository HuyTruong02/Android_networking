package com.example.asm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm.comment.Comment;
import com.example.asm.comment.CommentAdapter;
import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.status.HttpStatus;
import com.example.asm.truyen.Truyen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TruyenInfo extends AppCompatActivity implements ServerManager {

    TextView moTaNgan, tacGia, name, namXuatBan;
    EditText comment;
    ImageView anhBia;
    ListView lv;
    Button read, gui;

    public static TruyenInfo ins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truyen_info);
        ins = this;

        read = findViewById(R.id.readTruyen);
        gui = findViewById(R.id.comment);
        comment= findViewById(R.id.commemtTextEdit);

        anhBia = findViewById(R.id.anhBia);
        moTaNgan = findViewById(R.id.moTaNgan);
        tacGia = findViewById(R.id.tenTacGia);
        name = findViewById(R.id.name);
        namXuatBan = findViewById(R.id.namXuatBan);
        lv = findViewById(R.id.commentLv);

        Bundle i = getIntent().getExtras();

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TruyenInfo.this, ReadTruyenActivity.class);
                it.putExtra("id", i.getInt("id"));
                startActivity(it);
            }
        });
        gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostCommemt(i.getInt("accountId"), i.getInt("id"));
            }
        });

        new HttpAsyncTask().execute(
                HttpLinkTruyen(i.getInt("id")),
                HttpAsyncTask.REQUEST_METHOD.GET,
                null,
                new AsyncTaskListener() {
                    @Override
                    public void start() {
                        Log.d("INFO TRUYEN", "loading...");
                    }

                    @Override
                    public void end(Object result) {
                        List<Truyen> truyens = new Gson().fromJson(String.valueOf(result), new TypeToken<List<Truyen>>(){}.getType());
                        Truyen truyen = truyens.get(0);
                        Log.d("INFO TRUYEN", "end data: " + truyen.toString());

                        moTaNgan.setText(truyen.moTaNgan);
                        tacGia.setText("Tác giả: " + truyen.tenTacGia);
                        name.setText(truyen.tenTruyen);
                        namXuatBan.setText("Năm: " + truyen.namXuatBan);
                        new HttpAsyncTask().execute(
                                truyen.anhBia,
                                HttpAsyncTask.REQUEST_METHOD.GETIMAGE,
                                null,
                                new AsyncTaskListener() {
                                    @Override
                                    public void start() {
                                    }

                                    @Override
                                    public void end(Object result) {
                                        Bitmap bitmap = (Bitmap) result;
                                        anhBia.setImageBitmap(bitmap);
                                    }
                                });
                    }
                });
        UpdateListComment(i.getInt("id"));
    }

    public void PostCommemt(int accountId, int truyenId){
        String commentPost = comment.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountId", accountId);
            jsonObject.put("truyenId", truyenId);
            jsonObject.put("comment", commentPost);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpAsyncTask().execute(
                HttpLinkComment(),
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
                            UpdateListComment(truyenId);
                            Toast.makeText(TruyenInfo.ins, "Bình luận thành công", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(TruyenInfo.ins, "Lỗi khi Bình luận: " + httpStatus.message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void UpdateListComment(int id){
        new HttpAsyncTask().execute(
                HttpLinkComment(id),
                HttpAsyncTask.REQUEST_METHOD.GET,
                null,
                new AsyncTaskListener() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void end(Object result) {
                        Log.d("Comment", String.valueOf(result));
                        List<Comment> comments = new Gson().fromJson(String.valueOf(result), new TypeToken<List<Comment>>(){}.getType());
                        CommentAdapter commentAdapter = new CommentAdapter( comments, TruyenInfo.this, getIntent().getExtras());
                        lv.setAdapter(commentAdapter);
                    }
                });
    }
}