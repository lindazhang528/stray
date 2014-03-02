package com.lzhang.primes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PrimeService extends Service {
    private static final String TAG = "PrimeService";

    private final IBinder mBinder = new Binder();
    private boolean mIsBound = false;

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(findRequestReceiver, new IntentFilter(Constants.INTENT_FIND_PRIMES));
    }

    @Override
    public IBinder onBind(Intent intent) {
        mIsBound = true;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mIsBound = false;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(findRequestReceiver);
    }

    private BroadcastReceiver findRequestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.INTENT_FIND_PRIMES) && intent.getExtras() != null) {
                int count = intent.getExtras().getInt(Constants.INTENT_EXTRA_COUNT);
                PrimesTask primesTask = new PrimesTask();
                primesTask.execute(count);
            }
        }
    };

    // find the first n primes and save to db
    private class PrimesTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            int count = integers[0];

            findPrimes(count);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "found primes");

            if(!mIsBound) {
                // app is not in foreground. show notification then stop service
                showNotification();
                stopSelf();
            }
            else {
                // show toast
                Toast.makeText(PrimeService.this, R.string.toast_text, 2000).show();
            }
        }
    }

    private void findPrimes(int n) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        for(int i = 1; i <= n; i++) {
            int ithPrime = dbHelper.getPrimeAt(i);
            if(ithPrime == -1) {
                ithPrime = nthPrime(i);
            }

            broadcastPrime(ithPrime);
            dbHelper.insertPrime(i, ithPrime);
        }
    }

    // send broadcast when number is found
    private void broadcastPrime(int n) {
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_PRIME_FOUND);
        intent.putExtra(Constants.INTENT_EXTRA_PRIME, n);
        sendBroadcast(intent);
    }


    // finds the nth prime number
    private int nthPrime(int n) {
        if (n < 2) return 2;
        if (n == 2) return 3;
        int limit, root, count = 1;
        limit = (int)(n*(Math.log(n) + Math.log(Math.log(n)))) + 3;
        root = (int)Math.sqrt(limit) + 1;
        limit = (limit-1)/2;
        root = root/2 - 1;
        boolean[] sieve = new boolean[limit];
        for(int i = 0; i < root; ++i) {
            if (!sieve[i]) {
                ++count;
                for(int j = 2*i*(i+3)+3, p = 2*i+3; j < limit; j += p) {
                    sieve[j] = true;
                }
            }
        }
        int p;
        for(p = root; count < n; ++p) {
            if (!sieve[p]) {
                ++count;
            }
        }
        return 2*p+1;
    }

    // show notification
    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Prime numbers")
                .setContentText("Prime numbers found!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.getNotification());
     }
}
