package com.example.asm;

import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity implements ServerManager {

    EditText user, pass, email, fullname;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.email);
        fullname = findViewById(R.id.fullname);

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userText = user.getText().toString();
                String passText = pass.getText().toString();
                String emailText = email.getText().toString();
                String fullnameText = fullname.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", userText);
                    jsonObject.put("password", passText);
                    jsonObject.put("email", emailText);
                    jsonObject.put("fullname", fullnameText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new HttpAsyncTask().execute(
                        HttpLinkUsers(),
                        HttpAsyncTask.REQUEST_METHOD.POST,
                        jsonObject,
                        new AsyncTaskListener() {
                            @Override
                            public void start() {
                                Log.d("REGISTER >>> ", "Register...");
                            }

                            @Override
                            public void end(Object result) {
                                HttpStatus httpStatus = new Gson().fromJson(String.valueOf(result), HttpStatus.class);
                                if(httpStatus.status == 0){
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                                }else if(httpStatus.status == 1){
                                    Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_LONG).show();
                                }else if(httpStatus.status == 2){
                                    Toast.makeText(RegisterActivity.this, "Đăng ký không thành công (" + httpStatus.getMessage() + ")", Toast.LENGTH_LONG).show();
                                }
                                Log.d("LOGIN >>> ", "Login Result " + String.valueOf(result));
                            }
                        });
            }
        });
    }
}