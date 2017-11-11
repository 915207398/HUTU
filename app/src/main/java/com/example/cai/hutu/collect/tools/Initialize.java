package com.example.cai.hutu.collect.tools;

import com.example.cai.hutu.collect.ItemPO.ItemPO;


import org.bytedeco.javacpp.opencv_core;

import java.util.ArrayList;

/**
 * Created by chenting on 2017/7/28.
 */

public class Initialize {

    //初始化自动选点变量与默认RGB通道
    public void InitPoint(){
        ItemPO.showString="";
        ItemPO.listfour=new ArrayList<opencv_core.CvPoint>();
        ItemPO.CorrectionFour=new opencv_core.CvPoint[4];
        ItemPO.nitR=false;
        ItemPO.nitG=true;
        ItemPO.nitB=false;
        ItemPO.bldR=true;
        ItemPO.bldG=false;
        ItemPO.bldB=false;
        ItemPO.proR=true;
        ItemPO.proG=false;
        ItemPO.proB=false;
        ItemPO.leuR=true;
        ItemPO.leuG=true;
        ItemPO.leuB=true;
        ItemPO.gluR=true;
        ItemPO.gluG=true;
        ItemPO.gluB=true;
    }
//清除重复显示数据
    public void cleardate(){
        ItemPO.showString="";
    }

    //初始化坐标
    public void Initdistance(){
         ItemPO.selectArray =new opencv_core.CvPoint[]{
                 new opencv_core.CvPoint(370,80), new opencv_core.CvPoint(460,80),//NIT,LEU
                 new opencv_core.CvPoint(370,180), new opencv_core.CvPoint(460,180),//BLD,GLU
                 new opencv_core.CvPoint(370,280), new opencv_core.CvPoint(460,280)//PRO,Valid
        };
    }
}
