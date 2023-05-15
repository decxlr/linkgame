package com.example.linkgame2.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.example.linkgame2.common.Pub;
import com.example.linkgame2.fragment.GameFragment;
import com.example.linkgame2.R;

public class GameActivity extends AppCompatActivity implements KeyEvent.Callback {

    Fragment fragment = new GameFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.game_fragment,fragment);
        transaction.commit();
    }

    /*按下返回键*/
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
                                    Pub.flag = false;
                                    Pub.score = 0;
                                    Log.d("GameActivity", "onClick: score="+Pub.score);
                                    finish();
                                }
                            }).show();
            return true;
        }
        return false;
    }
}