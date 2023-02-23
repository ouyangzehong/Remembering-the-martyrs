package com.example.bottomtabbar;

public class Line {
    private String name;
    private int imageId;
    private String lineEditName;
    public Line(String name, int imageId, String lineEditName){
        this.name = name;
        this.imageId = imageId;
        this.lineEditName=lineEditName;

    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
    public String getLineEditName(){return lineEditName;}
}
