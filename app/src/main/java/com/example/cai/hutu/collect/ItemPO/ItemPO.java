package com.example.cai.hutu.collect.ItemPO;



import org.bytedeco.javacpp.opencv_core;

import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;


/**
 * Created by cai on 2017/5/29.
 */

public class ItemPO {

    public static opencv_core.IplImage srcImage ;
    public static opencv_core.IplImage grayImage ;
    public static opencv_core.CvPoint[] pointArray = new opencv_core.CvPoint[]{
                                                                                                                 new opencv_core.CvPoint(145,80),new opencv_core.CvPoint(185,80),new opencv_core.CvPoint(225,80),new opencv_core.CvPoint(610,80),new opencv_core.CvPoint(650,80),
            new opencv_core.CvPoint(20,180),                                  new opencv_core.CvPoint(105,180),new opencv_core.CvPoint(145,180),new opencv_core.CvPoint(185,180),new opencv_core.CvPoint(225,180),new opencv_core.CvPoint(610,180),new opencv_core.CvPoint(650,180),new opencv_core.CvPoint(690,180),
                                                                              new opencv_core.CvPoint(105,280),new opencv_core.CvPoint(145,280),new opencv_core.CvPoint(185,280),new opencv_core.CvPoint(225,280)
    };
    public static opencv_core.CvPoint[] selectArray =new opencv_core.CvPoint[]{
            new opencv_core.CvPoint(370,80), new opencv_core.CvPoint(460,80),//NIT,LEU
            new opencv_core.CvPoint(370,180), new opencv_core.CvPoint(460,180),//BLD,GLU
            new opencv_core.CvPoint(370,280), new opencv_core.CvPoint(460,280)//PRO,Valid
    };
    public static int[] res = new int[6];
    public static String[] result = new String[]{"-","+","0","25-10","80","200","NonValid","0.2","1","3"};//0-Negative   1-"+" 2-"0" 3-"25-10" 4-"80" 5-"200" 6-"NonValid" 7-"0.2+" 8-"1++" 9-"3+++"
    public static  String showString="";
    public static List<opencv_core.CvPoint> listfour=new ArrayList<opencv_core.CvPoint>();
    public static opencv_core.CvPoint[] CorrectionFour=new opencv_core.CvPoint[4];
    public static opencv_core.CvScalar[] ColorFour=new opencv_core.CvScalar[]{
            CV_RGB(255, 0, 0),CV_RGB(0, 255, 0),CV_RGB(0, 0, 255),CV_RGB(255, 0, 255)
    };
    //颜色通道判断。
    public static boolean nitR=false;
    public static boolean nitG=true;
    public static boolean nitB=false;
    public static boolean bldR=true;
    public static boolean bldG=false;
    public static boolean bldB=false;
    public static boolean proR=true;
    public static boolean proG=false;
    public static boolean proB=false;
    public static boolean leuR=true;
    public static boolean leuG=true;
    public static boolean leuB=true;
    public static boolean gluR=true;
    public static boolean gluG=true;
    public static boolean gluB=true;



}
