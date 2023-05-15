package com.example.linkgame2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linkgame2.R;
import com.example.linkgame2.common.Rank;

import java.util.List;

public class RankAdapter extends ArrayAdapter <Rank>{

    private int resourceId;

    public RankAdapter(Context context, int textViewResourceId, List<Rank> objects) {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Rank rank = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.num = view.findViewById(R.id.num);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.score = view.findViewById(R.id.score);
            viewHolder.time = view.findViewById(R.id.time);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.num.setText(String.valueOf(rank.getNum()));
        viewHolder.name.setText(rank.getName());
        viewHolder.score.setText(String.valueOf(rank.getScore()));
        viewHolder.time.setText(rank.getTime());
        return view;
    }

    class ViewHolder {
        TextView num,name,score,time;
    }
}
