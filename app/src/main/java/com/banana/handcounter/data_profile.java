package com.banana.handcounter;

public class data_profile {
    private String idUser,fullname,email,password;
    public String getIdUser(){
        return  idUser;
    }
    public String getFullname(){
        return fullname;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public data_profile(){
    //konstruktor kosong untuk membaca data snapshot
    }
    public data_profile(String idUser,String fullname,String email, String password){
        this.idUser = idUser;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }
}
