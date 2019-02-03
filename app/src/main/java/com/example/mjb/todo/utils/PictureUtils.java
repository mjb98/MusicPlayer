package com.example.mjb.todo.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

public class PictureUtils {


    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcWidth > destWidth || srcHeight > destHeight) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;
            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }


    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);


        return getScaledBitmap(path, point.x, point.y);
    }
    public static String getImageRealPath(ContentResolver contentResolver, Uri uri)
    {
        if (uri == null)
            return null;
        else {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = contentResolver.query(uri, proj,
                    null, null, null);
            if(cursor.getCount() >=1) {

                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    cursor.moveToNext();

                    return cursor.getString(column_index);
                 }else
                return null;
        }

    }

    public static Bitmap getcover(Uri uri,Activity activity){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art;
        BitmapFactory.Options bfo=new BitmapFactory.Options();

        mmr.setDataSource(activity, uri);
        rawArt = mmr.getEmbeddedPicture();

// if rawArt is null then no cover art is embedded in the file or is not
// recognized as such.
        if (null != rawArt) {
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
            return art;
        }
        return null;
    }

}
