package com.example.cai.hutu.collect.tools;

import android.graphics.Bitmap;


import org.bytedeco.javacpp.opencv_core;

import java.nio.ByteBuffer;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2RGBA;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GRAY2BGRA;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGBA2BGR;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;


/**
 * Created by cai on 2017/5/29.
 */

public class CreamTools {



    //Bitmap格式的图片转换成IplImage格式的图片
    public opencv_core.IplImage Bitmap2IplImage(Bitmap bitmap){
        opencv_core.IplImage iplImage = opencv_core.IplImage.create(bitmap.getWidth(),bitmap.getHeight(),IPL_DEPTH_8U, 4);
        bitmap.copyPixelsToBuffer(iplImage.getByteBuffer());
        opencv_core.IplImage image = opencv_core.IplImage.create(bitmap.getWidth(),bitmap.getHeight(),IPL_DEPTH_8U, 3);
        cvCvtColor(iplImage,image,CV_RGBA2BGR);
        return image;
    }

    /*IplImage Convert to  Bitmap 即IplImage 格式的图片转换成 Bitmap*/
        public static Bitmap showImage(opencv_core.IplImage image){
            opencv_core.IplImage ShowImage = opencv_core.IplImage.create(image.cvSize(), 8, 4);
            if(image.nChannels() == 3){
                cvCvtColor(image,ShowImage,CV_BGR2RGBA);
            }
            if(image.nChannels() == 1){//像素点
                cvCvtColor(image, ShowImage, CV_GRAY2BGRA);  //四通道的
            }
            ByteBuffer buffer = ShowImage.getByteBuffer();
            Bitmap bitmap = Bitmap.createBitmap(image.width(), image.height(), Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            return bitmap;
        }



}
