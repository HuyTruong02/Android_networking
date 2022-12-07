package com.example.asm.truyen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asm.R;
import com.example.asm.TruyenInfo;
import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;

import java.util.List;
import java.util.zip.Inflater;

public class TruyenAdapter extends BaseAdapter {

    List<Truyen> truyens;
    Context context;
    Bundle b;

    public TruyenAdapter(List<Truyen> truyens, Context context, Bundle b) {
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
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        ImageView anhBia = v.findViewById(R.id.anhBia);
      //  TextView moTaNgan = v.findViewById(R.id.moTaNgan);
        TextView tacGia = v.findViewById(R.id.tenTacGia);
        TextView name = v.findViewById(R.id.name);
        TextView namXuatBan = v.findViewById(R.id.namXuatBan);

        Truyen truyen = getItem(i);

        //moTaNgan.setText(truyen.moTaNgan);
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
                      //  Log.d("LOAD IMAGE", truyen.anhBia);
                    }

                    @Override
                    public void end(Object result) {
                      //  Log.d("LOAD IMAGE", "DONE " + truyen.anhBia);
                        Bitmap bitmap = (Bitmap) result;
                        anhBia.setImageBitmap(bitmap);
                    }
                });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, TruyenInfo.class);
                i.putExtra("accountId", b.getInt("accountId"));
                i.putExtra("id", truyen.id);
                context.startActivity(i);
            }
        });

        return v;
    }
}
