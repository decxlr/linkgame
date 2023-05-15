package com.example.linkgame2.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class MyView extends View {

    Paint mPaint;
    int x1,x2,y1,y2;

    public MyView(Context context,int x1,int y1,int x2,int y2,Paint mPaint) {
        super(context);
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.mPaint = mPaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*画直线*/
        canvas.drawLine(x1, y1, x2, y2, mPaint);
    }

}

