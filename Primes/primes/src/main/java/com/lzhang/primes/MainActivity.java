package com.lzhang.primes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.fragment, new ListViewFragment())
//                    .commit();
//        }

        setupTabs();
        setupFindView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, PrimeService.class), mPrimeServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mPrimeServiceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_clear:
                // clear db
                DatabaseHelper.getInstance(this).clearAllPrimes();

                // send broadcast to tabs
                Intent intent = new Intent();
                intent.setAction(Constants.INTENT_CLEAR);
                sendBroadcast(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTabs() {
        final FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        TabHost.TabSpec listTabSpec = tabHost.newTabSpec("list");
        listTabSpec.setIndicator("List");
        TabHost.TabSpec gridTabSpec = tabHost.newTabSpec("grid");
        gridTabSpec.setIndicator("Grid");

        tabHost.addTab(listTabSpec, ListViewFragment.class, null);
        tabHost.addTab(gridTabSpec, GridViewFragment.class, null);
    }

    private void setupFindView() {
        final EditText findEditText = (EditText) findViewById(R.id.find_edittext);
        Button findButton = (Button) findViewById(R.id.find_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int count = Integer.valueOf(findEditText.getText().toString());

                    Intent intent =  new Intent();
                    intent.setAction(Constants.INTENT_FIND_PRIMES);
                    intent.putExtra(Constants.INTENT_EXTRA_COUNT, count);
                    sendBroadcast(intent);

                } catch (NumberFormatException e) {
                    // not a number!
                    // show dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.alert_text)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                    // Create the AlertDialog object and return it
                    Dialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private ServiceConnection mPrimeServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };



}
