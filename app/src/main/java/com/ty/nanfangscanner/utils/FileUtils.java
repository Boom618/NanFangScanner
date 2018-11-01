package com.ty.nanfangscanner.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.ty.nanfangscanner.constant.ConstantUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Administrator on 2017/6/15.
 */

public class FileUtils {


    /**
     * 保存json到本地
     *
     * @param fileName
     * @param fileContent
     */
    public static void writeFile(String fileName, String fileContent) {
        try {
            File f = new File(ConstantUtil.FILE_DIR + fileName + ".txt");
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(
                    new FileOutputStream(f), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
            Log.e("TAG3333333", "缓存成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG222222", e.getMessage());
        }
    }

    /**
     * 从本地读取json
     */
    public static String readFile(String fileName) {
        StringBuffer buffer = new StringBuffer();
        try {
            File f = new File(ConstantUtil.FILE_DIR + fileName + ".txt");
            if (f.isFile() && f.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(f), "utf-8");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static void saveToSDCard(String filename, String content) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(filename);
                OutputStream out = new FileOutputStream(file);
                out.write(content.getBytes());
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //提示用户SDCard不存在或者为写保护状态
        }
    }

    /**
     * 原始File -> 原始Uri，供拍照时使用 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
     *
     * @param context 上下文
     * @param file    原始文件 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     * @return 原始Uri /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     */
    public static Uri getOriginalUriFromFile(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.ty.mbets.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 原始File -> 封装Uri，供裁剪时使用 cropIntent.setDataAndType(uri, "image/*");
     *
     * @param context   上下文
     * @param imageFile 原始文件 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     * @return 封装Uri  /external/images/media/24
     */
    public static Uri getContentUriFromFile(Context context, File imageFile) {
        if (imageFile == null) {
            Log.e("Tag", "aaaaaaaaaaaa");
        }
        String filePath = imageFile.getAbsolutePath();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 封装Uri -> 原始Path 从选择返回的图片 uri（可能是SDK19前返回的原始uri，也可能是SDK19后从文件等地方返回的封装uri）提取出原始path
     *
     * @param context  上下文
     * @param imageUri 选取返回的图片封装uri /external/images/media/24
     * @return 图片原始路径 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     */
    public static String getPathFromContentUri(Context context, Uri imageUri) {

        String imagePath = null;

        if (Build.VERSION.SDK_INT >= 19) {

            if (DocumentsContract.isDocumentUri(context, imageUri)) {
                // 如果是document类型的Uri，则通过document id处理
                String docId = DocumentsContract.getDocumentId(imageUri);
                if (imageUri.getAuthority().equals("com.android.providers.media.documents")) {
                    String id = docId.split(":")[1];// 解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + " = " + id;
                    imagePath = getImagePathNormally(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if (imageUri.getAuthority().equals("com.android.providers.downloads.documents")) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePathNormally(context, contentUri, null);
                }
            } else if ("cotent".equalsIgnoreCase(imageUri.getScheme())) {
                imagePath = getImagePathNormally(context, imageUri, null);
            } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
                imagePath = imageUri.getPath();
            }
        } else {
            imagePath = getImagePathNormally(context, imageUri, null);
        }
        return imagePath;
    }

    /**
     * 封装Uri -> 原始Path
     *
     * @param context 上下文
     * @param uri     选取返回的，封装图片uri /external/images/media/24
     * @return 图片原始路径 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     */
    public static String getImagePathNormally(Context context, Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取图片真实路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * @param fromPath 被复制的文件路径
     * @param toPath   复制的目录文件路径
     * @param rewrite  是否重新创建文件
     *
     *                 <p>文件的复制操作方法
     */
    public static void copyfile(String fromPath, String toPath, Boolean rewrite) {

        File fromFile = new File(fromPath);
        File toFile = new File(toPath);

        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }

        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            //关闭输入、输出流
            fosfrom.close();
            fosto.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean deleteFoder(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 判断是否是文件
            if (file.isFile()) {
                // delete()方法 你应该知道 是删除的意思;
                file.delete();
                // 否则如果它是一个目录
            } else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
//                File files[] = file.listFiles();
                File[] files = file.listFiles();
                if (files != null) {
                    // 遍历目录下所有的文件
                    for (File f : files) {
                        // 把每个文件 用这个方法进行迭代
                        deleteFoder(f);
                    }
                }
            }
            boolean isSuccess = file.delete();
            if (!isSuccess) {
                return false;
            }
        }
        return true;
    }

}
