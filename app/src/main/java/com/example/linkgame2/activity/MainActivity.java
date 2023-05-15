package com.example.linkgame2.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.linkgame2.R;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, KeyEvent.Callback{

    Button startGame,exitGame,gameRank,gameRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*创建数据库*/
        LitePal.getDatabase();

        /*开始游戏按钮*/
        startGame = findViewById(R.id.start_game);
        /*退出游戏按钮*/
        exitGame = findViewById(R.id.exit_game);
        /*排行榜按钮*/
        gameRank = findViewById(R.id.rank);
        /*游戏规则按钮*/
        gameRule = findViewById(R.id.rule);

        startGame.setOnClickListener(this);
        exitGame.setOnClickListener(this);
        gameRank.setOnClickListener(this);
        gameRule.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_game:
                Intent intent = new Intent(this,GameActivity.class);
                /*跳转到游戏界面*/
                startActivity(intent);
                break;
            case R.id.exit_game:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.are_you_quit)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        finish();
                                    }
                                }).show();  //退出游戏
                break;
            case R.id.rank:
                Intent intent1  = new Intent(this, RankActivity.class);
                /*跳转到排行榜界面*/
                startActivity(intent1);
                break;
            case R.id.rule:
                /*游戏规则*/
                Dialog fullscreenDialog = new Dialog(this,R.style.DialogFullscreen);
                fullscreenDialog.setContentView(R.layout.dialog_fullscreen);
                ImageView back = fullscreenDialog.findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fullscreenDialog.dismiss();
                    }
                });
                fullscreenDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 按下返回键
     */
    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount()==0) {
            Log.d("GameActivity", "onKeyDown: press back");
            new AlertDialog.Builder(this)
                    .setMessage(R.string.are_you_quit)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    finish();
                                }
                            }).show();
            return true;
        }
        return false;
    }
}