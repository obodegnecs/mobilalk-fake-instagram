package com.example.inspigram;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.Random;

public class LogInAsyncLoader extends AsyncTaskLoader<String> {

    public LogInAsyncLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
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
}
