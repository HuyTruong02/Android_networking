package com.example.asm.truyen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class ReadTruyenAdapater extends BaseAdapter {

    List<Truyen.Image> images;
    Context context;

    public ReadTruyenAdapater(List<Truyen.Image> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Truyen.Image getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_image, viewGroup, false);
        ImageView anhBia = v.findViewById(R.id.read);
        Truyen.Image image = getItem(i);
        new HttpAsyncTask().execute(
                image.link_image,
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
        return v;
    }
}
