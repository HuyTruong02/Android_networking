package com.example.asm;

public interface ServerManager {
    final String IP_ADDRESS = "http://192.168.1.87/";
    final String USERS = "apiv2/index.php/users";
    final String TRUYEN = "apiv2/index.php/truyen";
    final String COMMENT = "apiv2/index.php/binhluan";
    final String IMAGE_UPLOAD = "apiv2/image-upload";
    final String IMAGE = "apiv2/index.php/image";

    default String HttpLinkUsers(){
        return IP_ADDRESS + USERS;
    }
    default String HttpLinkUsers(int id){
        return IP_ADDRESS + USERS + "/" + id;
    }

    default String HttpLinkTruyen(){
        return IP_ADDRESS + TRUYEN;
    }
    default String HttpLinkTruyen(int id){
        return IP_ADDRESS + TRUYEN + "/" + id;
    }
    default String HttpLinkComment(int id){
        return IP_ADDRESS + COMMENT + "/" + id;
    }
    default String HttpLinkComment(){
        return IP_ADDRESS + COMMENT;
    }
    default String HttpLinkImageUpload(String name){
        return IP_ADDRESS + IMAGE_UPLOAD + "/" + name;
    }
    default String HttpLinkImage(int id){
        return IP_ADDRESS + IMAGE + "/" + id;
    }
    default String HttpLinkImage(){
        return IP_ADDRESS + IMAGE;
    }

}
