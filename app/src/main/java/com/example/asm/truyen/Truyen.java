package com.example.asm.truyen;

import java.util.Arrays;

public class Truyen {
    public int id;
    public String tenTruyen;
    public String moTaNgan;
    public String tenTacGia;
    public String namXuatBan;
    public String anhBia;
    public Image[] listImage;

    public class Image{
        public String link_image;

        @Override
        public String toString() {
            return "Image{" +
                    "link_image='" + link_image + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Truyen{" +
                "id=" + id +
                ", tenTruyen='" + tenTruyen + '\'' +
                ", moTaNgan='" + moTaNgan + '\'' +
                ", tenTacGia='" + tenTacGia + '\'' +
                ", namXuatBan='" + namXuatBan + '\'' +
                ", anhBia='" + anhBia + '\'' +
                ", linkImage=" + Arrays.toString(listImage) +
                '}';
    }
}

