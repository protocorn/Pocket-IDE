package com.example.pocketide.backgroundTasks;


import android.app.ProgressDialog;

import androidx.annotation.Nullable;



public class ActivityTask extends CinaBackgroundTask<Void, Void, Void> {

    private ProgressDialog loadingDialog;

    public ActivityTask(@Nullable ProgressDialog loadingDialog) {
        this.loadingDialog = loadingDialog;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return super.doInBackground(voids);
    }

    @Override
    protected void onPreExecute() {
        if (loadingDialog != null)
            loadingDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }
}
