package com.example.admin.appsaldos;

/**
 * Created by Admin on 16/09/2017.
 */

public class Contact {
    //private variables
    int _id;
    String _utilizador;
    String _pass;
    int _escola;

    // Empty constructor
    public Contact(){

    }
    // constructor
    public Contact(int id, String utilizador, String _pass , int escola){
        this._id = id;
        this._utilizador = utilizador;
        this._pass = _pass;
        this._escola = escola;
    }

    // constructor
    public Contact(String  utilzador, String _pass , int escola){
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
    public int getEscola(){
        return this._escola;
    }


    public void setEscola(int escola){
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
