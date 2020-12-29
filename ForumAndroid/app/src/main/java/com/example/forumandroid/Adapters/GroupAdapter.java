package com.example.forumandroid.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.forumandroid.Activities.HomeActivity;
import com.example.forumandroid.R;

import java.util.ArrayList;

public class GroupAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater layoutInflater;
    private ArrayList<String> arrayList;

    // For debugging
    private final String TAG = HomeActivity.class.getSimpleName();

    public GroupAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        // size is half of string array size
        return arrayList.size() / 2;
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
            convertView = layoutInflater.inflate(R.layout.group, null);
        }

        TextView textViewLeft = convertView.findViewById(R.id.textViewLeft);
        TextView textViewRight = convertView.findViewById(R.id.textViewRight);

        textViewLeft.setText(arrayList.get(position * 2));

        if ( ! arrayList.get(position * 2 + 1).equals("") ) {
            textViewRight.setText(arrayList.get(position * 2 + 1));
            textViewRight.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
