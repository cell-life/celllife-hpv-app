package org.celllife.android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.odk.collect.android.R;
import org.odk.collect.android.preferences.PreferencesActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * This task takes care of automatically updating the app (as a background process). 
 * 
 * It returns an exception if one occurs, otherwise it returns null (on success).
 * 
 * To trigger an update. all you need to do is to create two files at the following URLS:
 *  * default_autoupdate_version_url (e.g. http://hpv.cell-life.org/datacapture/app/version.html - a file that contains just the version number - nothing else!)
 *  * default_autoupdate_url (e.g. http://hpv.cell-life.org/datacapture/app/2/app.apk - the apk in the a folder named after the version.)
 */
public class AutoUpdate extends AsyncTask<Void, Integer, Exception> {
    
    private Application app;
    private Activity activity;
    private SharedPreferences settings;
    private boolean updateDownloaded = false; 
    
    public AutoUpdate(Application app, Activity activity) {
        super();
        Log.d("AutoUpdate", "starting AutoUpdate");
        this.app = app;
        this.activity = activity;
        // Get a handle on the settings file for later use (URLS, etc)
        settings = PreferenceManager.getDefaultSharedPreferences(this.activity.getBaseContext());
    }
    
    /**
     * Indication if an update was downloaded - doesn't indicate if it was successfully installed (unfortunately)
     * @return true if an update was downloaded
     */
    public boolean isUpdateDownloaded() {
        return updateDownloaded;
    }
    
    @Override
    protected Exception doInBackground(Void... params) {
        try {
            checkForUpdate();
        } catch (Exception e) {
            Log.i("AutoUpdate", "An exception occurred=",e);
            return e;
        }
        return null;
    }

    private void checkForUpdate() throws Exception {
        int currentVersion = getCurrentVersionCode(app);
        int targetAppVersion = getLatestVersionCode();
        if (currentVersion < targetAppVersion) {
            final Uri downloadedUpdate = downloadAPK(targetAppVersion);
            updateDownloaded = true;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(activity)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.update)
                            .setMessage(R.string.update_ready)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(downloadedUpdate, "application/vnd.android.package-archive");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    
                                    activity.startActivity(intent);
                                }
                            }).show();
                }
            });
        }
    }
    
    private int getLatestVersionCode() throws MalformedURLException, IOException, NumberFormatException {
        StringBuilder sb = new StringBuilder();

        String server =
            settings.getString(PreferencesActivity.KEY_AUTOUPDATE_VERSION_URL,
                    activity.getResources().getString(R.string.default_autoupdate_version_url));
        URL url = new URL(server);
        InputStreamReader is = null;
        try {
            // read the data
            URLConnection connection = url.openConnection();
            is = new InputStreamReader(connection.getInputStream());
            int data = is.read();
            while (data != -1) {
                sb.append((char) data);
                data = is.read();
            }
            // convert result to a number
            int code = Integer.parseInt(sb.toString().trim());
            Log.i("AutoUpdate", "Checking the latest version on "+server+" "+code);
            return code;

        } finally {
            // close input/output resources
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {}
        }
    }
    
    private int getCurrentVersionCode(Application app) throws PackageManager.NameNotFoundException {
        return app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionCode;
    }

    private Uri downloadAPK(int version) throws FileNotFoundException, MalformedURLException, IOException {
        File file = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;

        try {
            File dir = android.os.Environment.getExternalStorageDirectory();
            file = new File(dir, "app.apk");
            fos = new FileOutputStream(file);

            String autoUpdateUrl =
                    settings.getString(PreferencesActivity.KEY_AUTOUPDATE_URL,
                            activity.getResources().getString(R.string.default_autoupdate_url));
            autoUpdateUrl = String.format(autoUpdateUrl, version);
            Log.i("AutoUpdate", "Downloading app.apk for version "+version+" at URL "+autoUpdateUrl);
            URL url = new URL(autoUpdateUrl);
            URLConnection connection = url.openConnection();
            bis = new BufferedInputStream(connection.getInputStream());

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();

            return Uri.fromFile(file);

        //} catch (Exception e) {
            //Log.e("AutoUpdate", "Error: " + e);
            //displayErrorMessage(String.format(Resources.getSystem().getString(R.string.update_error_apk), version));
            
        } finally {
            // close input/output resources
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception e) {}
        }
    }
}
