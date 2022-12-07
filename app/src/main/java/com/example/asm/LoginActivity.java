package com.example.asm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.status.HttpStatus;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements ServerManager {

    EditText user, pass;
    Button login, register;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userText = user.getText().toString();
                String passText = pass.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", userText);
                    jsonObject.put("password", passText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new HttpAsyncTask().execute(
                        HttpLinkUsers(),
                        HttpAsyncTask.REQUEST_METHOD.OPTIONS,
                        jsonObject,
                        new AsyncTaskListener() {
                            @Override
                            public void start() {
                                Log.d("LOGIN >>> ", "Login...");
                            }

                            @Override
                            public void end(Object result) {
                                HttpStatus httpStatus = new Gson().fromJson(String.valueOf(result), HttpStatus.class);
                                if(httpStatus.status == 0){
                                    Intent i = new Intent(LoginActivity.this, TruyenActivity.class);
                                    i.putExtra("accountId", Integer.parseInt(httpStatus.message));
                                    startActivity(i);
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                                }
                                Log.d("LOGIN >>> ", "Login Result " + String.valueOf(result));
                            }
                        });
            }
        });
    }
}