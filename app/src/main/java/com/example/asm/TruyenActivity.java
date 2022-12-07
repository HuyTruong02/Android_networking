package com.example.asm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.truyen.Truyen;
import com.example.asm.truyen.TruyenAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class TruyenActivity extends AppCompatActivity implements ServerManager {

    ListView listView;
    Button manager;
    public static TruyenActivity ins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ins = this;

        listView = findViewById(R.id.listTruyen);
        manager = findViewById(R.id.manager);

        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TruyenActivity.this, TruyenManagerActivity.class);
                startActivity(i);
            }
        });

/*        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", "bum1");
            jsonObject.put("password", "bum");
            jsonObject.put("email", "bum@gmail.com");
            jsonObject.put("fullname", "nguyen van bum");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
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

                        TruyenAdapter truyenAdapter = new TruyenAdapter(truyen, TruyenActivity.this, b);
                        listView.setAdapter(truyenAdapter);

                    }
                });
    }
}