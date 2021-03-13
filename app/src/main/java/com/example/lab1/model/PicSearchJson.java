package com.example.lab1.model;

import java.util.ArrayList;

public class PicSearchJson {
    int total;
    int totalHits;
    ArrayList<PicItemJson> hits;

    public ArrayList<PicItemJson> getPics() {return hits;}
}

