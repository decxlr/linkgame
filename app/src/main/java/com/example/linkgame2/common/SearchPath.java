package com.example.linkgame2.common;

import static java.lang.Math.*;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SearchPath {
    //两个图案通过一条线就能相连
    private static boolean findByOne(boolean[][] grid, GameContent start, GameContent end) {

        int minValue,maxValue;

        if(start.getX() != end.getX() && start.getY() != end.getY()) {
            return false;
        }

        //X-行,Y-列,grid=true为存在
        if(start.getX() == end.getX()) {
            minValue = min(start.getY(),end.getY());
            maxValue = max(start.getY(),end.getY());
            for(int i = minValue+1;i < maxValue;i++) {
                if(grid[i][end.getX()]) {
                    return false;
                }
            }
        } else {
            minValue = min(start.getX(),end.getX());
            maxValue = max(start.getX(),end.getX());
            for(int i = minValue+1;i < maxValue;i++) {
                if(grid[end.getY()][i]) {
                    return false;
                }
            }
        }

        return true;
    }

    //两个图案通过两条线相连
    private static GameContent findByTwo(boolean[][] grid,GameContent start,GameContent end) {

        boolean one,two;

        if(start.getY() == end.getY() || start.getX() == end.getX()) {
            return null;
        }

        GameContent pt = new GameContent();
        pt.setX(start.getX());
        pt.setY(end.getY());
        if (!grid[pt.getY()][pt.getX()]) {
            one = findByOne(grid,start,pt);
            two = one ? findByOne(grid,pt,end) : one;
            if(one && two) {
                return pt;
            }
        }

        pt.setY(start.getY());
        pt.setX(end.getX());
        if (!grid[pt.getY()][pt.getX()]) {
            one = findByOne(grid,start,pt);
            two = one ? findByOne(grid,pt,end) : one;
            if(one && two) {
                return pt;
            }
        }

        return null;
    }

    //两个图案通过三条线相连
    public static List<GameContent> findByAll(boolean[][] grid, GameContent start, GameContent end, int length) {

        List<GameContent> list = new ArrayList<>();
        GameContent temp,one = new GameContent(),two;

        if(findByOne(grid,start,end)) {
            return new ArrayList<>();
        }

        if((temp = findByTwo(grid,start,end)) != null) {
            list.add(temp);
            return list;
        }

        for(int i = start.getX()+1;i <= length+1;i++) {
            Log.d("Search", "findByAll: int i = start.getX()+1");
            if(!grid[start.getY()][i]) {
                Log.d("Search", "findByAll: in if1");
                one.setY(start.getY());
                one.setX(i);
                two = findByTwo(grid,one,end);
                if(two != null) {
                    Log.d("Search", "findByAll: success1");
                    list.add(one);
                    list.add(two);
                    return list;
                }
            } else {
                break;
            }
        }

        for(int i = start.getX()-1;i >= 0;i--) {
            Log.d("Search", "findByAll: int i = start.getX()-1");
            if(!grid[start.getY()][i]) {
                Log.d("Search", "findByAll: in if2");
                one.setY(start.getY());
                one.setX(i);
                two = findByTwo(grid,one,end);
                if(two != null) {
                    Log.d("Search", "findByAll: success2");
                    list.add(one);
                    list.add(two);
                    return list;
                }
            } else {
                break;
            }
        }

        for(int i = start.getY()+1;i <= length+1;i++) {
            Log.d("Search", "findByAll: int i = start.getY()+1");
            if(!grid[i][start.getX()]) {
                Log.d("Search", "findByAll: in if3");
                one.setY(i);
                one.setX(start.getX());
                Log.d("Search", "findByAll: one.X="+one.getX()+" one.Y="+one.getY());
                two = findByTwo(grid,one,end);
                if(two != null) {
                    Log.d("Search", "findByAll: success3");
                    list.add(one);
                    list.add(two);
                    return list;
                }
            } else {
                break;
            }
        }

        for(int i = start.getY()-1;i >= 0;i--) {
            Log.d("Search", "findByAll: int i = start.getY()-1");
            if(!grid[i][start.getX()]) {
                Log.d("Search", "findByAll: in if4");
                one.setY(i);
                one.setX(start.getX());
                two = findByTwo(grid,one,end);
                if(two != null) {
                    Log.d("Search", "findByAll: success4");
                    list.add(one);
                    list.add(two);
                    return list;
                }
            } else {
                break;
            }
        }

        return null;
    }
}
