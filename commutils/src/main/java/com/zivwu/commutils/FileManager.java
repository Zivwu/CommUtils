package com.zivwu.commutils;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 1. file  路径提供者
 * 2. file  文件存取
 * 3. APP   空间检查
 */
public class FileManager {
    private Context context;

    private long minSpace;
    /**
     * 外置存储，读写权限
     */
    private String[] permissions;

    private String NO_SPACE_DESC;

    public FileManager(Context context) {
        this.context = context;
        //手机内部最后不使用的空间 MB 为单位
        int DEF_MIN_MB = 100;
        minSpace = 1024 * 1024 * DEF_MIN_MB;
        NO_SPACE_DESC = "您目前的手机存储空间以不足" + DEF_MIN_MB + "MB,请清理后使用";
        permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    /**
     * get Android Cache File
     *
     * @return if sd card && hasPermission @permissions return context.getExternalCacheDir()
     * else return context.getCacheDir()
     */
    public File provideCacheFile() {
        File storageDir = context.getExternalCacheDir();
        if (!PermissionHelper.hasPermission(context, permissions)
                || !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || storageDir == null) {
            storageDir = context.getCacheDir();
        }
        return storageDir;
    }


    public File provideImageStampFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File file = provideCacheFile();
        if (file == null)
            return null;
        String IMAGE_DIR = "images";
        return provideImageCacheFile(file, IMAGE_DIR, imageFileName);

    }


    public File provideImageCacheFile(File dir, String child, String name) {
        File image = null;
        File imageFile = new File(dir, child);
        if (!imageFile.exists()) {
            boolean mkdirs = imageFile.mkdirs();
            if (!mkdirs)
                return null;
        }

        try {
            image = File.createTempFile(
                    name,  /* prefix */
                    ".jpg",         /* suffix */
                    imageFile      /* directory */
            );
        } catch (IOException ignored) {

        }
        return image;
    }


    public static Uri generateImageUri(Context context, File file, String authority) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    /**
     * check file how has low space
     *
     * @param file the file need to write
     * @return has space true else false
     */
    public boolean checkUsableSpace(File file) {
        return checkUsableSpace(file, minSpace);
    }

    /**
     * check file how has low space
     *
     * @param file the file need to write
     * @param size need how match space
     * @return has space true else false
     */
    public boolean checkUsableSpace(File file, long size) {
        return file.getUsableSpace() - size > 0;

    }


}
