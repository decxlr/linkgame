package com.example.linkgame2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.linkgame2.R;
import com.example.linkgame2.common.GameContent;
import com.example.linkgame2.common.Pub;

import java.util.List;

public class GameContentAdapter extends BaseAdapter {

    private List<GameContent> provinceBeanList;
    private LayoutInflater layoutInflater;

    public GameContentAdapter(Context context, List<GameContent> provinceBeanList) {
        this.provinceBeanList = provinceBeanList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return provinceBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return provinceBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_game_content, null);
            holder = new ViewHolder();
            holder.gameImage = (ImageView) convertView.findViewById(R.id.game_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*设置图案的大小，使其合适*/
        AbsListView.LayoutParams param;
        param = new AbsListView.LayoutParams(
                Pub.width / Pub.edgeL,
                Pub.height / Pub.edgeL);
        convertView.setLayoutParams(param);


        GameContent gameContent = provinceBeanList.get(position);
        if (gameContent != null) {
            holder.gameImage.setImageResource(gameContent.getImageId());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView gameImage;
    }

}
