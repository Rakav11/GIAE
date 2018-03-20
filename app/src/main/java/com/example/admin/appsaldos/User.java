package com.example.admin.appsaldos;

/**
 * Created by Admin on 17/09/2017.
 */

public class User {
    //private variables
    int _id;
    String _utilizador;
    String _pass;
    String _escola;

    // Empty constructor
    public User(){

    }
    // constructor
    public User(int id, String utilizador, String _pass , String escola){
        this._id = id;
        this._utilizador = utilizador;
        this._pass = _pass;
        this._escola = escola;
    }

    // constructor
    public User(String  utilzador, String _pass , String escola){
        this._utilizador = utilzador;
        this._pass =_pass;
        this._escola = escola;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }
    public String getEscola(){
        return this._escola;
    }


    public void setEscola(String escola){
        this._escola = escola;
    }

    public String getUser(){
        return this._utilizador;
    }


    public void setUser(String user){
        this._utilizador = user;
    }


    public String getPass(){
        return this._pass;
    }

    public void setPass(String pass){
        this._pass = pass;
    }
}
