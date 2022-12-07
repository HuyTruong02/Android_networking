package com.example.asm.comment;

public class Comment {
    public int id;
    public int truyenId;
    public int accountId;
    public String comment;
    public String time;
    public String fullname;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", truyenId=" + truyenId +
                ", accountId=" + accountId +
                ", comment='" + comment + '\'' +
                ", time='" + time + '\'' +
                ", fullname='" + fullname + '\'' +
                '}';
    }
}
