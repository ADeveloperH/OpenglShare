package com.noxgroup.app.learnopengl.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.noxgroup.app.learnopengl.MyApplication;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author huangjian
 * @create 2021/7/30
 * @Description
 */
public class Utils {
    public static Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = MyApplication.getContext().getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static String loadFromAssetsFile(String glShaderPath) {
        AssetManager am = MyApplication.getContext().getResources().getAssets();
        StringBuilder result = new StringBuilder();
        try {
            InputStream is = am.open(glShaderPath);
            int ch;
            byte[] buffer = new byte[1024];
            while (-1 != (ch = is.read(buffer))) {
                result.append(new String(buffer, 0, ch));
            }
            is.close();
        } catch (Exception e) {
            return null;
        }
        return result.toString().replaceAll("\\r\\n", "\n");
    }
}
