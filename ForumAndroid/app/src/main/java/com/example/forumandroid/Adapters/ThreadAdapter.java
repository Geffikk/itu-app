package com.example.forumandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.forumandroid.R;

import java.util.ArrayList;

public class ThreadAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater layoutInflater;
    private ArrayList<String> arrayList;

    public ThreadAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.thread, null);
        }

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(arrayList.get(position));

        return convertView;
    }
}
