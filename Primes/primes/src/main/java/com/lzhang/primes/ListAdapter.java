package com.lzhang.primes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Linda on 3/1/14.
 */
public class ListAdapter extends CursorAdapter {

    public ListAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder == null) {
            holder = new ViewHolder();
            holder.index = (TextView) view.findViewById(R.id.index);
            holder.value = (TextView) view.findViewById(R.id.value);
            view.setTag(holder);
        }

        int countCol = cursor.getColumnIndex(DatabaseHelper.COL_COUNT);
        int valueCol = cursor.getColumnIndex(DatabaseHelper.COL_VALUE);
        holder.index.setText(cursor.getString(countCol));
        holder.value.setText(cursor.getString(valueCol));

    }

    class ViewHolder {
        TextView index;
        TextView value;
    }
}
