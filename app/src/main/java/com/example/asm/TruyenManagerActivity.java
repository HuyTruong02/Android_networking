package com.example.asm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.truyen.Truyen;
import com.example.asm.truyen.TruyenAdapter;
import com.example.asm.truyen.TruyenManagerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TruyenManagerActivity extends AppCompatActivity implements ServerManager {

    ListView listView;
    Button button;
    public static TruyenManagerActivity ins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truyen_manager_activity);
        button = findViewById(R.id.them);
        ins = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TruyenManagerActivity.this, ThemTruyenActivity.class);
                startActivity(i);
            }
        });

        listView = findViewById(R.id.listTruyen);

        UpdateList();
    }

    public void UpdateList(){
        Bundle b = getIntent().getExtras();
        new HttpAsyncTask().execute(
                HttpLinkTruyen(),
                HttpAsyncTask.REQUEST_METHOD.GET,
                null,
                new AsyncTaskListener() {
                    @Override
                    public void start() {
                    }
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void end(Object result) {
                        Type type = new TypeToken<List<Truyen>>(){}.getType();
                        List<Truyen> truyen = new Gson().fromJson(String.valueOf(result), type);

                        TruyenManagerAdapter truyenAdapter = new TruyenManagerAdapter(truyen, TruyenManagerActivity.this, b);
                        listView.setAdapter(truyenAdapter);
                    }
                });
    }
}