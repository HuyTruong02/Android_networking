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

public class ImageEditAdapater extends BaseAdapter implements ServerManager {

    List<Image> images;
    Context context;
    Bundle b;

    public ImageEditAdapater(List<Image> images, Context context, Bundle b) {
        this.images = images;
        this.context = context;
        this.b = b;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Image getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_image_edit, viewGroup, false);
        ImageView image = v.findViewById(R.id.img);
        Button delete = v.findViewById(R.id.delete);
        delete.setBackgroundResource(0);

        Image img = getItem(i);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAsyncTask().execute(
                        HttpLinkImage(img.id),
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
                                    ImageEditActivity.ins.UpdateList();
                                    Toast.makeText(ImageEditActivity.ins, "Xóa thành công", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(ImageEditActivity.ins, "Lỗi khi xóa: " + httpStatus.message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        new HttpAsyncTask().execute(
                img.link_image,
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
                        image.setImageBitmap(bitmap);
                    }
                });
        return v;
    }
}
