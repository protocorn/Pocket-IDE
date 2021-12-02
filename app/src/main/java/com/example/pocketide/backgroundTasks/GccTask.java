package com.example.pocketide.backgroundTasks;

import android.app.ProgressDialog;

import androidx.annotation.Nullable;



public class GccTask extends CinaBackgroundTask<Void, Object, Object> {

    protected ProgressDialog loadingDialog;

    public GccTask(@Nullable ProgressDialog loadingDialog) {
        this.loadingDialog = loadingDialog;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        return super.doInBackground(voids);
    }

    @Override
    protected void onPreExecute() {
        if (loadingDialog != null)
            loadingDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object str) {
        super.onPostExecute(str);
        if (loadingDialog != null)
            loadingDialog.setCancelable(true);
    }
}
