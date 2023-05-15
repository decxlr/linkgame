package com.example.linkgame2.common;

import android.content.res.Resources;

/**
 * @author 徐
 */
public class Pub {
    public static boolean flag = true;
    public static int score = 0;
    public static int edgeL = 16;
    /**
     * 屏幕高度
     */
    public static int screeHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static int height = screeHeight*2/3;
    /**
     * 屏幕宽度
     */
    public static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static int width = screenWidth*14/15;
}
