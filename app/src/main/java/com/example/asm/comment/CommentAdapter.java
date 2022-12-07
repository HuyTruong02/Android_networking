package com.example.asm.comment;

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

import com.example.asm.LoginActivity;
import com.example.asm.R;
import com.example.asm.TruyenActivity;
import com.example.asm.TruyenInfo;
import com.example.asm.httpasynctask.AsyncTaskListener;
import com.example.asm.httpasynctask.HttpAsyncTask;
import com.example.asm.status.HttpStatus;
import com.example.asm.truyen.Truyen;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class CommentAdapter extends BaseAdapter {

    List<Comment> comments;
    Context context;
    Bundle b;

    public CommentAdapter(List<Comment> comments, Context context, Bundle b) {
        this.comments = comments;
        this.context = context;
        this.b = b;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_comment, viewGroup, false);

        TextView name = v.findViewById(R.id.name);
        TextView cm = v.findViewById(R.id.comment);
        Button delete = v.findViewById(R.id.delete);
        delete.setBackgroundResource(0);

        Comment comment = getItem(i);

        name.setText(comment.fullname + " [" + comment.time + "]");
        cm.setText(comment.comment);

        if(comment.accountId != b.getInt("accountId")){
            delete.setVisibility(View.INVISIBLE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAsyncTask().execute(
                        TruyenInfo.ins.HttpLinkComment(comment.id),
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
                                    TruyenInfo.ins.UpdateListComment(comment.truyenId);
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(context, "Lỗi khi xóa: " + httpStatus.message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        return v;
    }
}
