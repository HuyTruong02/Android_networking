package com.example.asm.truyen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asm.EditTruyenActivity;
import com.example.asm.ImageEditActivity;
import com.example.asm.R;
import com.example.asm.ServerManager;
import com.example.asm.TruyenActivity;
import com.example.asm.TruyenInfo;
import com.example.asm.TruyenManagerActivity;
import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.status.HttpStatus;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class TruyenManagerAdapter extends BaseAdapter implements ServerManager {

    List<Truyen> truyens;
    Context context;
    Bundle b;

    public TruyenManagerAdapter(List<Truyen> truyens, Context context, Bundle b) {
        this.truyens = truyens;
        this.context = context;
        this.b = b;
    }

    @Override
    public int getCount() {
        return truyens.size();
    }

    @Override
    public Truyen getItem(int i) {
        return truyens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_manager, viewGroup, false);
        ImageView anhBia = v.findViewById(R.id.anhBia);
      //  TextView moTaNgan = v.findViewById(R.id.moTaNgan);
        TextView tacGia = v.findViewById(R.id.tenTacGia);
        TextView name = v.findViewById(R.id.name);
        TextView namXuatBan = v.findViewById(R.id.namXuatBan);
        Button plus = v.findViewById(R.id.add);
        Button delete = v.findViewById(R.id.delete);
        Button edit = v.findViewById(R.id.edit);
        edit.setBackgroundResource(0);
        plus.setBackgroundResource(0);
        delete.setBackgroundResource(0);

        Truyen truyen = getItem(i);

        //moTaNgan.setText(truyen.moTaNgan);
        tacGia.setText("Tác giả: " + truyen.tenTacGia);
        name.setText(truyen.tenTruyen);
        namXuatBan.setText("Năm: " + truyen.namXuatBan);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TruyenManagerActivity.ins, EditTruyenActivity.class);
                i.putExtra("id", truyen.id);
                TruyenManagerActivity.ins.startActivity(i);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TruyenManagerActivity.ins, ImageEditActivity.class);
                i.putExtra("id", truyen.id);
                TruyenManagerActivity.ins.startActivity(i);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAsyncTask().execute(
                        HttpLinkTruyen(truyen.id),
                        HttpAsyncTask.REQUEST_METHOD.DELETE,
                        new JSONObject(),
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
                                    Toast.makeText(TruyenInfo.ins, "Xóa thành công", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(TruyenInfo.ins, "Lỗi khi xóa: " + httpStatus.message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        new HttpAsyncTask().execute(
                truyen.anhBia,
                HttpAsyncTask.REQUEST_METHOD.GETIMAGE,
                null,
                new AsyncTaskListener() {
                    @Override
                    public void start() {
                      //  Log.d("LOAD IMAGE", truyen.anhBia);
                    }

                    @Override
                    public void end(Object result) {
                      //  Log.d("LOAD IMAGE", "DONE " + truyen.anhBia);
                        Bitmap bitmap = (Bitmap) result;
                        anhBia.setImageBitmap(bitmap);
                    }
                });
        return v;
    }
}
