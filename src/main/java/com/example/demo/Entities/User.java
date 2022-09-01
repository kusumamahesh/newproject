package com.example.demo.Entities;

public class User {
    private String username;
    private String password;
    private String confirm_password;

    public User(){}

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public boolean confirmPassword() {return this.password.equals(this.confirm_password);}
}
