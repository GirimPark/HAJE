package com.cookandroid.haje;

public class User {
    String number;
    String email;
    String name;
    String password;

    public User(String number, String email, String name, String password){
        this.number = number;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getNumber(){
        return number;
    }
    public String getEmail(){
        return email;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }

    public void setNumber(String number){
        this.number = number;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
