package com.fleetlabs.library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static final String SD_PATH = Environment.getExternalStorageDirectory().toString();
    public static String APP_FILE_PATH = SD_PATH + "/ImagePicker/";
    public static String TEMP_FOLDER = APP_FILE_PATH + "temp/";
    public static String TEMP_CAMERA_IMAGE = TEMP_FOLDER + "temp.jpg";

    public static int ScreenWidth = 900;
    public static int ScreenHeight = 900;

    private static final String TAG = "FileUtils";

    public static void initDir(String packageName) {
        APP_FILE_PATH = SD_PATH + "/Android/data/" + packageName + "/";
        TEMP_FOLDER = APP_FILE_PATH + "temp/";
        TEMP_CAMERA_IMAGE = TEMP_FOLDER + "temp.jpg";

        File rootDir = new File(TEMP_FOLDER);
        if (isSDCardAvailable()) {
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }
        }
    }

    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String saveImage(FileDescriptor fd, String imagePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, opt);

        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        int screenWidth = ScreenWidth;
        int screenHeight = ScreenHeight;

        opt.inSampleSize = 1;

        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)

                opt.inSampleSize = picHeight / screenHeight;
        }

        opt.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFileDescriptor(fd, null, opt);
        String realPathString = savePhotoToSDCard(bm, imagePath, String.valueOf(System.currentTimeMillis()));
        if (bm != null && !bm.isRecycled())
            bm.recycle();
        return realPathString;
    }

    public static String saveImage(String imageTempPath, String imagePath) {
        String result = "";
        try {

            BitmapFactory.Options opt = new BitmapFactory.Options();

            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageTempPath, opt);

            int picWidth = opt.outWidth;
            int picHeight = opt.outHeight;

            int screenWidth = ScreenWidth;
            int screenHeight = ScreenHeight;

            opt.inSampleSize = computeSampleSize(opt, ScreenWidth > ScreenHeight ? ScreenHeight : ScreenWidth, ScreenWidth*ScreenHeight);
            /*
            if (picWidth > picHeight) {
                if (picWidth > screenWidth)
                    opt.inSampleSize = (picWidth / screenWidth + 1) * 2;
            } else {
                if (picHeight > screenHeight)
                    opt.inSampleSize = (picHeight / screenHeight + 1) * 2;
            }
            */
            opt.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(imageTempPath, opt);

            int angle = readPictureDegree(imageTempPath);

            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            Log.e(TAG, "angle2=" + angle);
            if(angle == 0)
            {
                result = savePhotoToSDCard(bm, imagePath, String.valueOf(System.currentTimeMillis()));
            }
            else
            {
                Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                result = savePhotoToSDCard(resizedBitmap, imagePath, String.valueOf(System.currentTimeMillis()));
            }

        } catch (Exception e) {
            Log.e(TAG, "Error when save image " + e);
        }

        return result;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static String savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        String pathString = "";
        if (isSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName + ".jpg");
            pathString = photoFile.getAbsolutePath();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pathString;
    }

    public static int computeSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        //原始图片的宽
        double w = options.outWidth;
        //原始图片的高
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}