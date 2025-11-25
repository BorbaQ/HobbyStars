package com.example.hackheroes;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

public class StarCollection implements Serializable {
    public String name;
    public ArrayList<Star> stars;
    public String bgIco;
    public StarCollection(ArrayList<Star> starsList, String nameofStarCollection, String _bgIco) {
        stars = starsList;
        name = nameofStarCollection;
        bgIco = _bgIco;
    }
    public StarCollection(StarCollection sc){
        stars = sc.stars;
        name = sc.name;
        bgIco = sc.bgIco;
    }
}
