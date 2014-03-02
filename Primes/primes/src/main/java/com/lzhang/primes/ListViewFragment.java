package com.lzhang.primes;

/**
 * Created by Linda on 3/1/14.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListViewFragment extends Fragment {

    private ListView mList;
    private ListAdapter mAdapter;
    private Cursor mCursor;

    public ListViewFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_PRIME_FOUND);
        filter.addAction(Constants.INTENT_CLEAR);
        getActivity().registerReceiver(mPrimeReceiver, filter);

        setupList();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mPrimeReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    private void setupList() {
        mList = (ListView) getView().findViewById(R.id.list);
        mCursor = DatabaseHelper.getInstance(getActivity()).getPrimesCursor();
        mAdapter = new ListAdapter(getActivity(), mCursor);
        mList.setAdapter(mAdapter);
    }

    private void updateList() {
        mCursor.close();
        mCursor = DatabaseHelper.getInstance(getActivity()).getPrimesCursor();
        mAdapter.changeCursor(mCursor);
    }

    BroadcastReceiver mPrimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateList();
        }
    };
}