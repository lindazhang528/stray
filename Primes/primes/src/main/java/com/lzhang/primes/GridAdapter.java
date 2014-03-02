package com.lzhang.primes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
* Created by Linda on 3/1/14.
*/
public class GridAdapter extends ArrayAdapter<Integer> {

    int mResource;
    Integer[] mNumbers;

    public GridAdapter(Context context, int resource, Integer[] objects) {
        super(context, resource, objects);
        mResource = resource;
        mNumbers = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(mResource, null);

            holder = new ViewHolder();
            holder.number = (TextView) view.findViewById(R.id.number);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }

        int number = mNumbers[position];
        holder.number.setText(String.valueOf(number));
        if(DatabaseHelper.getInstance(getContext()).havePrime(number)){
            holder.number.setBackgroundColor(Color.parseColor("#00CCFF"));
        }
        else {
            holder.number.setBackgroundColor(Color.parseColor("#DEDEDE"));
        }

        return view;
    }

//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        ViewHolder holder = (ViewHolder) view.getTag();
//
//        if(holder == null) {
//            holder = new ViewHolder();
//            holder.number = (TextView) view.findViewById(R.id.number);
//            view.setTag(holder);
//        }
//
////        holder.number;
//    }

    static class ViewHolder {
        TextView number;
    }
}
