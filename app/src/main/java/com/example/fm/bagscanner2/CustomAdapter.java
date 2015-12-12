package com.example.fm.bagscanner2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by FM on 12/12/2015.
 */
public class CustomAdapter extends BaseAdapter {
    private Context ctx;
    private List<Item> itemList;

    public CustomAdapter(Activity ctx, List<Item> itemList) {
        this.ctx = ctx;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public Object getItem(int pos) {
        return itemList == null ? null : itemList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return itemList == null ? 0 : itemList.get(pos).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextHolder th = null;
        if (v == null) {
            LayoutInflater lInf = (LayoutInflater)
                    ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = lInf.inflate(R.layout.item_layout, null);
            TextView dateView = (TextView) v.findViewById(R.id.dateTV);
            TextView itemView = (TextView) v.findViewById(R.id.itemTV);
            th = new TextHolder();
            th.dateView = dateView;
            th.itemView = itemView;
            v.setTag(th);
        }
        else
            th = (TextHolder) v.getTag();
        th.dateView.setText(itemList.get(position).date);
        th.itemView.setText(itemList.get(position).item);
        return v;
    }
    static class TextHolder {
        TextView dateView;
        TextView itemView;
    }
}
