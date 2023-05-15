package com.example.linkgame2.activity;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.linkgame2.R;
import com.example.linkgame2.common.Rank;
import com.example.linkgame2.adapter.RankAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {

    List<Rank> list = new ArrayList<>();
    /**
     * back:返回图标
     * search:搜索图标
     */
    ImageView back,search;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        /*返回图标*/
        back = findViewById(R.id.back);
        /*搜索图标*/
        search = findViewById(R.id.search);
        
        allRank(back);
        searchByName(back,search);

    }

    /**
     * 显示全部排行
     */
    private void allRank(ImageView back) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list = LitePal.order("score desc").find(Rank.class);
        Log.d("RankActivity", "onCreate: list.size="+list.size());
        for(int i = 0;i < list.size();i++) {
            //更新排名
            if (i != 0 ) {
                if(list.get(i).getScore() != list.get(i - 1).getScore()) {
                    list.get(i).setNum(i+1);
                } else {
                    list.get(i).setNum(list.get(i-1).getNum());
                }
            } else {
                list.get(i).setNum(1);
            }
            list.get(i).save();
            Log.d("RankActivity", "onCreate: list"+list.get(i).getNum()
                    +" "+list.get(i).getName()+" "+list.get(i).getScore()+" "+list.get(i).getTime());
        }

        RankAdapter rankAdapter = new RankAdapter(RankActivity.this,R.layout.item_rank,list);
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(rankAdapter);
    }

    /**
     * 通过昵称搜索排名
     */
    private void searchByName(ImageView back,ImageView search) {
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        /*最大输入长度为6*/
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        /*排行榜中的搜索功能*/
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        allRank(back);
                    }
                });

                new AlertDialog.Builder(context)
                        .setMessage(getResources().getString(R.string.input_searchname))
                        .setView(input)
                        .setNegativeButton(getResources().getString(R.string.cancel), null)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String inputName = input.getText().toString();
                                list = LitePal.where("name=?",inputName).order("score desc").find(Rank.class);
                                RankAdapter rankAdapter = new RankAdapter(RankActivity.this,R.layout.item_rank,list);
                                ListView listView = findViewById(R.id.listview);
                                listView.setAdapter(rankAdapter);
                                searchByName(back,search);
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }
}