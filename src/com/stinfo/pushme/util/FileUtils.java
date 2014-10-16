package com.stinfo.pushme.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
	// 图片转为文件
	public static boolean saveBitmap2file(Bitmap bmp,String FILE_PATH)
	{
	    CompressFormat format = Bitmap.CompressFormat.JPEG;
	    int quality = 100;
	    OutputStream stream = null;
	    try
	    {
	        // 判断SDcard状态
	        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
	        {
	            // 错误提示
	            return false;
	        }
	                                                                                                                                                                                                                                                                                                        
	        // 检查SDcard空间
	        File SDCardRoot = Environment.getExternalStorageDirectory();
	        if (SDCardRoot.getFreeSpace() < 10000)
	        {
	            // 弹出对话框提示用户空间不够
	            Log.e("FileUtils", "存储空间不够");
	            return false;
	        }
	                                                                                                                                                                                                                                                                                                        
	        stream = new FileOutputStream(SDCardRoot.getPath() +  File.separator+FILE_PATH);// "/sdcard/"
	    }
	    catch (FileNotFoundException e)
	    {
	        e.printStackTrace();
	    }
	    return bmp.compress(format, quality, stream);
	}
	
	
	//read native pic and convert it to drawable
	//test done
	public static Drawable readNativePic(String sFilePath) {
		try {
			//declare
			File file;
			boolean boolTemp;
			Bitmap bitmapTemp;

			//init
			file = new File(sFilePath);
			boolTemp = file.exists();
			bitmapTemp = BitmapFactory.decodeFile(sFilePath);

			return (Drawable) new BitmapDrawable(bitmapTemp);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
