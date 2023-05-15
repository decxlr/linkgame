package com.example.linkgame2.fragment;

import static com.example.linkgame2.common.SearchPath.findByAll;
import static java.lang.Math.abs;
import static java.util.Collections.sort;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkgame2.common.MyView;
import com.example.linkgame2.common.Rank;
import com.example.linkgame2.common.Pub;
import com.example.linkgame2.common.GameContent;
import com.example.linkgame2.adapter.GameContentAdapter;
import com.example.linkgame2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class GameFragment extends Fragment{

    /**
     * fragment范围为整个屏幕
     */
    FrameLayout frameLayout;
    Paint mPaint = new Paint();
    /**
     * w:图案的宽
     * h:图案的高
     */
    int w = Pub.width /Pub.edgeL,h = Pub.height /Pub.edgeL;
    /**
     * list中存的是折点
     */
    List<GameContent> list = new ArrayList<>();
    int i1;
    /**
     * grid：表格，用来标记图案是否存在
     */
    boolean[][] grid = new boolean[20][20];
    boolean flag = true,canGo = false,stop = false;
    /**
     * cnt:图案数量
     * beginValue:初始宽度
     * beginTime:初始时间
     * stage:关卡
     */
    private int cnt,beginValue = 4,beginTime = 15,stage = 1;
    /**
     * clicked:是否有图案已被点击
     * remove1:要移除的图案编号
     */
    private int clicked,remove1;
    private int imageId1,imageId2;
    /**
     * c:标记，暂停后只弹出一次对话框
     */
    int c = 1;
    /**
     * willRemove:要移除的图案
     */
    private List<Integer> willRemove = new ArrayList<>();
    /**
     * cardView1:卡片
     */
    CardView cardView1;
    View view1,view2,lineView1,lineView2,myView;
    /**
     * gameContent：图案对象
     */
    GameContent gameContent1,gameContent2;
    /**
     * picture:水果图片
     */
    int[] picture = {R.drawable.apple_pic,R.drawable.banana_pic,R.drawable.cherry_pic,
            R.drawable.grape_pic,R.drawable.mango_pic,R.drawable.orange_pic,
    R.drawable.pear_pic,R.drawable.pineapple_pic,R.drawable.watermelon_pic,R.drawable.strawberry_pic};
    HashMap<Integer,Integer> map = new HashMap<>();
    List<GameContent> gameContentList = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        /*网格布局*/
        GridView gridView = view.findViewById(R.id.grid_view);

        frameLayout = view.findViewById(R.id.frame);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        /*初始化变量*/
        Pub.flag = true;
        Pub.score = 0;
        Pub.edgeL = 16;
        canGo = false;
        stage = 1;
        stop = false;
        Log.d("GameFragment", "onCreateView: score="+Pub.score);
        Log.d("Pub", "main: height "+Pub.height +" width:"+Pub.width);

        /*倒计时*/
        TextView myTime = view.findViewById(R.id.time);
        /*分数*/
        TextView myScore = view.findViewById(R.id.score);
        /*剩余图案的数量*/
        TextView cntPic = view.findViewById(R.id.pic_cnt);
        /*关卡数*/
        TextView stageText = view.findViewById(R.id.stage);

        playGame(gridView,myTime,myScore,cntPic,stageText);

        /*重排*/
        ImageView reset = view.findViewById(R.id.reset);
        /*游戏暂停*/
        ImageView stopImg = view.findViewById(R.id.stop);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(gridView);    //重排功能
            }
        });

        stopImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop = true;  //实现暂停功能
            }
        });

        return view;
    }

    /**
     *  倒计时功能
     */
    private void countdown(TextView myTime,int allTime,TextView myScore) {
        Log.d("GameFragment", "countdown: allTIme="+allTime);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int t = allTime;
                for (int i = allTime; i >= 0; i--) {
                    try {
                        if(stop) {  //游戏暂停中
                            i++;
                            c--;
                            if(getActivity()!=null && c >= 0) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(getContext())
                                                .setMessage(getResources().getString(R.string.stoping))
                                                .setPositiveButton(getResources().getString(R.string.go), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        stop = false;
                                                        c = 1;
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    }
                                });
                            }
                        }

                        t = i;
                        int finalT = t;
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {  //显示剩余时间
                                    myTime.setText(getResources().getString(R.string.surplus) + finalT + "s");
                                }
                            });
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("GameFragment", "run: in for flag = "+flag+",i = "+i);
                    if (!flag || !Pub.flag) {
                        Pub.flag = true;
                        flag = true;
                        Pub.score += i*10;  //增加分数
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /*将分数显示更新*/
                                    myScore.setText(getResources().getString(R.string.now_score) + "" + Pub.score);
                                }
                            });
                        }
                        break;
                    }
                }
                if (t <= 0) {
                    Log.d("GameFragment", "run: exit , flag = "+flag);
                    if (getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!canGo) {
                                    Date curDate = new Date(System.currentTimeMillis());
                                    String date = format.format(curDate);

                                    final EditText input = new EditText(getContext());
                                    input.setText("无名");  // 默认昵称
                                    input.setSelectAllOnFocus(true);
                                    input.setSingleLine(true);
                                    input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                                    Log.d("GameFragment", "onClick: your score=" + Pub.score);

                                    new AlertDialog.Builder(getContext())  //游戏结束，退出游戏
                                            .setTitle(getResources().getString(R.string.game_over))
                                            .setMessage(getResources().getString(R.string.input_name))
                                            .setView(input)
                                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    getActivity().finish();
                                                    Pub.score = 0;
                                                    Log.d("GameFragment", "onClick: score=" + Pub.score);
                                                    Log.d("GameFragment", "onClick: date="+date);
                                                }
                                            })
                                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Rank rank = new Rank();//将分数保存到排行榜中
                                                    String name = input.getText().toString();
                                                    rank.setName(name);
                                                    rank.setScore(Pub.score);
                                                    rank.setTime(date);
                                                    rank.save();
                                                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();

                                                    getActivity().finish();
                                                    Pub.score = 0;
                                                }
                                            })
                                            .setCancelable(false)
                                            .show();
                                }
                            }
                        });
                    }
                }
            }
        }).start();
    }

    /**
     * 初始化图片
     */
    private void initPicture(@NonNull GridView gridView, int size, int kindOfPic, int numOfPic){
        gameContentList.clear();
        gridView.setColumnWidth(size);
        Pub.edgeL = kindOfPic;
        Log.d("GameFragment", "initPicture: score = "+Pub.score);

        //初始化表格的值
        for(int i = 0;i < kindOfPic+2;i++) {
            for(int j = 0;j < kindOfPic+2;j++) {
                if(i == 0 || j == 0 || i == kindOfPic+1 || j == kindOfPic+1) {
                    grid[i][j] = false;
                } else {
                    grid[i][j] = true;
                }
                Log.d("GameFragment", "initPicture: grid["+i+"]["+j+"]="+grid[i][j]);
            }
        }

        cnt = 0;
        for (int j : picture) {
            map.put(j, 0);
        }
        Log.d("GamFragment", "initPicture: kindOfPic = "+kindOfPic);
        for(int i = 0;i < numOfPic;i++) {
            Random random = new Random();
            int x = random.nextInt();
            x %= kindOfPic;
            x = abs(x);
            if(map.get(picture[x]) != (numOfPic/kindOfPic)) {
                cnt++;
                map.put(picture[x], map.get(picture[x])+1);
                GameContent gameContent = new GameContent(picture[x],i);
                /*图片的列数*/
                gameContent.setX(i%kindOfPic+1);
                /*图片的行数*/
                gameContent.setY(i/kindOfPic+1);
                Log.d("GameFragment", "initPicture: gameContent.indexX="
                        +gameContent.getX()+" indexY="+gameContent.getY());
                gameContentList.add(gameContent);
            } else {
                i--;
            }
        }
            
        GameContentAdapter adapter = new GameContentAdapter(getActivity(),gameContentList);
        gridView.setAdapter(adapter);
    }

    private void reset(GridView gridView) {
        Log.d("Gamefragment", "reset: in");
        int len = gameContentList.size();
        int edgeLen = beginValue;  //边长

        int t = 0;
        if(!willRemove.isEmpty()) {
            sort(willRemove);
            for (int i : willRemove) {
                if(i-t >= 0) {
                    gameContentList.remove(i - t);  //删除被消掉的图案
                    t++;
                }
            }
        }


        List<GameContent> temp = new ArrayList<>(gameContentList);
        len = gameContentList.size();  // 更新gameContentList的长度
        boolean[] vis = new boolean[len+5];
        int n = len;

        for(int i = 0;i < len;i++) {
            vis[i] = false;
        }
        for(int i = 0;i < edgeLen+2;i++) {
            for(int j = 0;j < edgeLen+2;j++) {
                grid[i][j] = false;  //将所有的格子初始化为没有图片
            }
        }

        /*清空*/
        gameContentList.clear();
        willRemove.clear();

        /*打乱顺序*/
        int newIndex = 0;
        while (n > 0){
            Random random = new Random();
            int y = random.nextInt();
            y %= len;
            y = abs(y);
            if(!vis[y]) {
                vis[y] = true;
                GameContent gameContent = temp.get(y);
                gameContent.setX(newIndex%edgeLen+1);
                gameContent.setY(newIndex/edgeLen+1);

                grid[gameContent.getY()][gameContent.getX()] = true;
                gameContentList.add(gameContent);
                newIndex++;
                n--;
            }
            Log.d("Gamefragment", "reset: n="+n);
        }

        willRemove.clear();  //清空

        GameContentAdapter adapter = new GameContentAdapter(getActivity(),gameContentList);
        gridView.setAdapter(adapter);
    }

    private void playGame(GridView gridView,TextView myTime,TextView myScore,TextView cntPic,TextView stageText) {

        /*显示第几关*/
        stageText.setText("第"+stage+"关");

        willRemove.clear();
        Log.d("GameFragment", "playGame: score="+Pub.score);
        initPicture(gridView, 900/beginValue, beginValue, beginValue*beginValue);
        cntPic.setText(getResources().getString(R.string.need_pic)+""+beginValue*beginValue);
        /*倒计时*/
        countdown(myTime,beginTime,myScore);
        clicked = 0;
        canGo = false;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                myView = view;

                if(clicked == 0) {
                    i1 = i;
                    remove1 = i;  //将要删除的
                    clicked = 1;
                    view1 = view;
                    view.getParent();
                    cardView1 = view.findViewById(R.id.cardview);
                    lineView1 = view.findViewById(R.id.view1);  //直线
                    lineView2 = view.findViewById(R.id.view2);  //直线
                    gameContent1 = gameContentList.get(i);
                    imageId1 = gameContent1.getImageId();
                    Log.d("GameFragment", "onItemClick: imageId1="+imageId1);
                    lineView1.setBackgroundColor(getResources().getColor(R.color.teal_200));  //改变线段颜色
                    lineView2.setBackgroundColor(getResources().getColor(R.color.teal_200));  //改变线段颜色
                } else {
                    clicked = 0;
                    gameContent2 = gameContentList.get(i);
                    imageId2 = gameContent2.getImageId();
                    view2 = view;

                    if(imageId1 == imageId2 && view1 != view2
                            && (list = findByAll(grid,gameContent1,gameContent2,beginValue)) != null) {

                        int[] position1 = new int[2];
                        int[] position2 = new int[2];
                        view1.getLocationOnScreen(position1);
                        view2.getLocationOnScreen(position2);

                        if(position1[0] == 0 && position1[1] == 0) {
                            position1[0] = 30;
                            position1[1] = 541;
                        }
                        if(position2[0] == 0 && position2[1] == 0) {
                            position2[0] = 30;
                            position2[1] = 541;
                        }

                        w = cardView1.getWidth();
                        h = cardView1.getHeight();

                        mPaint.setColor(getResources().getColor(R.color.blue));
                        MyView v1 = null,v2 = null,v3 = null,v4 = null,v5 = null,myView = null;
                        /*使用局部变量防止连线无法清除*/

                        position1[0] += w / 2;
                        position2[0] += w / 2;
                        position1[1] += h / 6;
                        position2[1] += h / 6;

                        /*连线位置太低 */
                        if(stage == 3) {
                            position1[1] -= h/6;
                            position2[1] -= h/6;
                        } else if(stage >= 4) {
                            position1[1] -= h/4;
                            position2[1] -= h/4;
                        }

                        /*画线-两个图案之间的轨迹*/
                        int dx1 = 0,dx2 = 0,dy1 = 0,dy2 = 0;
                        if(list.size() == 2) {
                            dx1 = list.get(0).getX()-gameContent1.getX();
                            dy1 = list.get(0).getY()-gameContent1.getY();
                            dx2 = list.get(1).getX()-gameContent2.getX();
                            dy2 = list.get(1).getY()-gameContent2.getY();
                            v1 = new MyView(getContext(),position1[0],position1[1],position1[0]+dx1*w,position1[1]+dy1*h,mPaint);
                            frameLayout.addView(v1);
                            v2 = new MyView(getContext(),position1[0]+dx1*w,position1[1]+dy1*h,position2[0]+dx2*w,position2[1]+dy2*h,mPaint);
                            frameLayout.addView(v2);
                            v3 = new MyView(getContext(),position2[0]+dx2*w,position2[1]+dy2*h,position2[0],position2[1],mPaint);
                            frameLayout.addView(v3);
                        } else if (list.size() == 1) {
                            dx1 = list.get(0).getX()-gameContent1.getX();
                            dy1 = list.get(0).getY()-gameContent1.getY();
                            v4 = new MyView(getContext(),position1[0],position1[1],position1[0]+dx1*w,position1[1]+dy1*h,mPaint);
                            frameLayout.addView(v4);
                            v5 = new MyView(getContext(),position1[0]+dx1*w,position1[1]+dy1*h,position2[0],position2[1],mPaint);
                            frameLayout.addView(v5);
                        } else {
                            myView = new MyView(getContext(),position1[0],position1[1],position2[0],position2[1],mPaint);
                            frameLayout.addView(myView);
                        }

                        Log.d("GameContentFragment", "onItemClick: line"+" position1[0]="+position1[0]+" position1[1]="+position1[1]);
                        Log.d("GameContentFragment", "onItemClick: line"+" position2[0]="+position2[0]+" position2[1]="+position2[1]);
                        Log.d("GameContentFragment", "onItemClick: line: dx1="+dx1+" dx2="+dx2+" dy1="+dy1+" dy2="+dy2);

                        MyView finalV1 = v1,finalV2 = v2,finalV3 = v3,finalV4 = v4,finalV5 = v5;
                        MyView finalMyView = myView;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(200);
                                    /*连线显示时间为0.2秒*/
                                    if(getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                /*将连线清除*/
                                                frameLayout.removeView(finalMyView);
                                                frameLayout.removeView(finalV1);
                                                frameLayout.removeView(finalV2);
                                                frameLayout.removeView(finalV3);
                                                frameLayout.removeView(finalV4);
                                                frameLayout.removeView(finalV5);
                                            }
                                        });
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        willRemove.add(remove1);
                        willRemove.add(i);  //将要删除的放入Arraylist中

                        /*消掉的图案设置为不可见*/
                        view1.setVisibility(View.INVISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        gameContent1.setVis(false);
                        gameContent2.setVis(false);

                        grid[gameContent1.getY()][gameContent1.getX()] = false;
                        grid[gameContent2.getY()][gameContent2.getX()] = false;

                        cnt-=2;  // 图案的数量减2
                        Pub.score += 20;  // 分数加20
                        /*更新分数和剩余图案数量*/
                        myScore.setText(getResources().getString(R.string.now_score)+""+Pub.score);
                        cntPic.setText(getResources().getString(R.string.need_pic)+""+cnt);
                        list.clear();
                    } else {
                        lineView1.setBackgroundColor(getResources().getColor(R.color.white));//改变线段颜色
                        lineView2.setBackgroundColor(getResources().getColor(R.color.white));//改变线段颜色
                    }
                }

                Log.d("GameFragment", "onItemClick: cnt="+cnt);
                if(cnt == 0) {//所有图片被消完
                    flag = false;
                    canGo = true;//防止通关后弹出退出对话框

                    /*防止点下一关过快，来不及结束countdown中线程->2个线程同时运行*/
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(getActivity() != null) {
                                //Log.d("GameFragment", "run: 500ms pass");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        new AlertDialog.Builder(getContext())//恭喜游戏通关，下一关
                                                .setTitle(getResources().getString(R.string.congratulations))
                                                .setMessage(getResources().getString(R.string.game_pass))
                                                .setPositiveButton(getResources().getString(R.string.next), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        if(beginValue + 2 <= picture.length) {
                                                            beginValue += 2;
                                                            beginTime += 15;
                                                            flag = true;
                                                            Pub.flag = true;
                                                        } else {
                                                            if(beginTime >= 10) {
                                                                beginTime -= 5;
                                                            }
                                                        }
                                                        stage++;
                                                        playGame(gridView,myTime,myScore,cntPic,stageText);
                                                    }
                                                })
                                                .setNegativeButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        getActivity().finish();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
    }
}