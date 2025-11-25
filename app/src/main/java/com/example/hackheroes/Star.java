package com.example.hackheroes;

import java.io.Serializable;

public class Star implements Serializable {
    public String name;
    public Boolean status;
    public String description;
    float position;
    public Star(String _name, String _Description, Boolean _status, float _position){
        name = _name;
        description = _Description;
        status = _status;
        position = _position;
    }
}
