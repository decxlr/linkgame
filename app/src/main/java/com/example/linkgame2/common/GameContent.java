package com.example.linkgame2.common;

public class GameContent {
    private int imageId;
    private int X;
    private int Y;
    private int index;
    private boolean vis = true;

    public GameContent(int imageId,int index) {
        this.imageId = imageId;
        this.index = index;
    }

    public GameContent() {

    }


    public boolean isVis() {
        return vis;
    }

    public void setVis(boolean vis) {
        this.vis = vis;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        this.X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        this.Y = y;
    }

    public int getIndex() {
        return index;
    }
}
