package org.celllife.hpv;

import org.odk.collect.android.provider.FormsProviderAPI.FormsColumns;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

public class LoadHPVFormTask extends AsyncTask<String, Integer, Uri> {
    
    private Context context;
    
    public LoadHPVFormTask(Context context) {
        this.context = context;
    }

    @Override
    protected Uri doInBackground(String... params) {
        String formPrefix = params[0];
        
        // find the latest form with the specified key (formPrefix)
        long formId = -1;
        long formDate = -1;
        String sortOrder = FormsColumns.DISPLAY_NAME + " DESC, " + FormsColumns.JR_VERSION + " DESC";
        Cursor c = context.getContentResolver().query(FormsColumns.CONTENT_URI, null, null, null, sortOrder);
        try {
            if (c.moveToFirst()) {
                do {
                    int displayNameIndex = c.getColumnIndexOrThrow(FormsColumns.DISPLAY_NAME);
                    int dateIndex = c.getColumnIndexOrThrow(FormsColumns.DATE);
                    String thisDisplayName = c.getString(displayNameIndex);
                    long thisFormDate = c.getLong(dateIndex);
                    if (thisDisplayName.startsWith(formPrefix) && thisFormDate > formDate) {
                        int idIndex = c.getColumnIndexOrThrow(FormsColumns._ID);
                        formId = c.getLong(idIndex);
                        formDate = thisFormDate;
                    }
                } while (c.moveToNext());
            }
         } finally {
             c.close();
         }
        
        // load the form URI
        Uri formUri = null;
        if (formId != -1) {
            formUri = ContentUris.withAppendedId(FormsColumns.CONTENT_URI, formId);
        }
        return formUri;
    }
}
