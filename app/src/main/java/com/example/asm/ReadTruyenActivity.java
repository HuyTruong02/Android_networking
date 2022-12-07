package com.example.asm;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.truyen.ReadTruyenAdapater;
import com.example.asm.truyen.Truyen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReadTruyenActivity extends AppCompatActivity implements ServerManager {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_truyen);
        lv = findViewById(R.id.readLv);

        Bundle b = getIntent().getExtras();

        new HttpAsyncTask().execute(
                HttpLinkTruyen(b.getInt("id")),
                HttpAsyncTask.REQUEST_METHOD.GET,
                null,
                new AsyncTaskListener() {
                    @Override
                    public void start() {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void end(Object result) {
                        List<Truyen> truyens = new Gson().fromJson(String.valueOf(result), new TypeToken<List<Truyen>>(){}.getType());
                            List<Truyen.Image> images = Arrays.stream(truyens.get(0).listImage).collect(Collectors.toList());
                            ReadTruyenAdapater readTruyenAdapater = new ReadTruyenAdapater(images, ReadTruyenActivity.this);
                            lv.setAdapter(readTruyenAdapater);

                    }
                });

    }
}