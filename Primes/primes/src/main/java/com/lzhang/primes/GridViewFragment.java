package com.lzhang.primes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Linda on 3/1/14.
 */
public class GridViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grid_view, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        // register receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_PRIME_FOUND);
        filter.addAction(Constants.INTENT_CLEAR);
        getActivity().registerReceiver(mPrimeReceiver, filter);

        setupGrid();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mPrimeReceiver);
    }

    private void setupGrid() {
        int largestPrime = DatabaseHelper.getInstance(getActivity()).getLargestPrime();
        if(largestPrime != -1) {
            Integer[] numArray = new Integer[largestPrime];
            for(int i = 1; i <= largestPrime; i++) {
                numArray[i - 1] = i;
            }

            GridView grid = (GridView) getView().findViewById(R.id.grid);
            GridAdapter adapter = new GridAdapter(getActivity(), R.layout.grid_item, numArray);
            grid.setAdapter(adapter);
        }
    }

    BroadcastReceiver mPrimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setupGrid();
        }
    };
}
