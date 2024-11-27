package com.icaller.callscreen.icalldialer.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;


public class FavoriteContactRemoverItemTask {
    public static void removeFromFavorites(ContentResolver contentResolver, String str) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put("starred", (Integer) 0);
        contentResolver.update(uri, contentValues, "_id = ?", new String[]{str});
    }

    public static void removeRecentCallLogs(ContentResolver contentResolver, String str) {
        contentResolver.delete(CallLog.Calls.CONTENT_URI, "number=?", new String[]{str});
    }

    public static String getContactID(ContentResolver contentResolver, String str, String str2) {
        Uri uri;
        if (str == null) {
            uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str2));
        } else {
            uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str));
        }
        Cursor query = contentResolver.query(uri, new String[]{"_id"}, null, null, null);
        String str3 = null;
        if (query != null) {
            if (query.moveToFirst()) {
                str3 = query.getString(query.getColumnIndex("_id"));
            }
            query.close();
        }
        return str3;
    }

    public static void removeAllFavorites(ContentResolver contentResolver) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor query = contentResolver.query(uri, new String[]{"_id"}, "starred = ?", new String[]{"1"}, null);
        if (query != null) {
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex("_id"));
                ContentValues contentValues = new ContentValues();
                contentValues.put("starred", (Integer) 0);
                contentResolver.update(uri, contentValues, "_id = ?", new String[]{string});
            }
            query.close();
        }
    }

    public static void removeRecentAllCallLogs(ContentResolver contentResolver) {
        contentResolver.delete(CallLog.Calls.CONTENT_URI, null, null);
    }
}
