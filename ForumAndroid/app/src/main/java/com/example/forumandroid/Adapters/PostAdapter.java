package com.example.forumandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.forumandroid.R;

import java.util.ArrayList;

public class PostAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater layoutInflater;
    private ArrayList<String> posts;
    private ArrayList<String> authors;
    private ArrayList<String> dates;
    private boolean flag = true;

    public PostAdapter(Context context, ArrayList<String> posts, ArrayList<String> authors, ArrayList<String> dates) {
        this.context = context;
        this.posts = posts;
        this.authors = authors;
        this.dates = dates;
    }

    @Override
    public int getCount() {
        return posts.size();
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

        if ( convertView == null ) {
            convertView = layoutInflater.inflate(R.layout.post, null);
        }

        TextView post = convertView.findViewById(R.id.textViewPost);
        TextView author = convertView.findViewById(R.id.textViewAuthor);
        TextView date = convertView.findViewById(R.id.textViewDate);
        post.setText(posts.get(position));
        author.setText(authors.get(position));
        date.setText(dates.get(position));

        return convertView;
    }
}
