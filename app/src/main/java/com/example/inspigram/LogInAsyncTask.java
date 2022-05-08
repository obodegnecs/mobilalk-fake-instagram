package com.example.inspigram;

import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class LogInAsyncTask extends AsyncTask<Void, Void, String> {
    private WeakReference<TextView> textViewWeakReference;

    public LogInAsyncTask(TextView textView) {
        textViewWeakReference = new WeakReference<>(textView);
    }

    @Override
    protected String doInBackground(Void... voids) {
        Random random = new Random();
        int num = random.nextInt(8);
        int time = num * 300;

        try {
            Thread.sleep(time);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        return "Bejelentkezés " + time + " ms után!";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        textViewWeakReference.get().setText(s);

    }
}
