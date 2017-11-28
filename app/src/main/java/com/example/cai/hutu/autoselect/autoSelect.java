package com.example.cai.hutu.autoselect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cai.hutu.R;
import com.example.cai.hutu.collect.ItemPO.ItemPO;
import com.example.cai.hutu.collect.activity.cream;
import com.example.cai.hutu.collect.tools.CreamTools;
import com.example.cai.hutu.collect.tools.SelectColor;
import com.example.cai.hutu.tool.SysApplication;


import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.example.cai.hutu.collect.ItemPO.ItemPO.ColorFour;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.CorrectionFour;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.bldB;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.bldG;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.bldR;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.gluB;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.gluG;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.gluR;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.leuB;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.leuG;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.leuR;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.listfour;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.nitB;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.nitG;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.nitR;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.proB;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.proG;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.proR;
import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImageHeader;
import static org.bytedeco.javacpp.opencv_core.cvCreateMat;
import static org.bytedeco.javacpp.opencv_core.cvCreateMatHeader;
import static org.bytedeco.javacpp.opencv_core.cvCreateMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvCreateSeq;
import static org.bytedeco.javacpp.opencv_core.cvGetImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvGetSubRect;
import static org.bytedeco.javacpp.opencv_core.cvRect;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_core.cvZero;
import static org.bytedeco.javacpp.opencv_imgproc.CV_FILLED;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GRAY2RGB;
import static org.bytedeco.javacpp.opencv_imgproc.CV_HOUGH_GRADIENT;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.cvCircle;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetPerspectiveTransform;
import static org.bytedeco.javacpp.opencv_imgproc.cvHoughCircles;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
import static org.bytedeco.javacpp.opencv_imgproc.cvWarpPerspective;


/**
 * Created by chenting on 2017/7/1.
 */

public class autoSelect extends Activity {
    private Dialog dialog;//弹窗
    private Dialog dialog_d;//弹窗
    private Button dialog_button12;//弹窗button
    private  Button dialog_cancol;//弹窗取消
    private TextView textView;//弹窗文本
    private Button probutton;//透视按钮;
    //private Button correction;//手动校正
    private boolean enable=false;//判断手动校正是否可用。
    private Button left;
    private Button right;
    private Button up;
    private Button down;
    private CheckBox NIT;
    private CheckBox BLD;
    private CheckBox PRO;
    private CheckBox LEU;
    private CheckBox GLU;
    private CheckBox Valid;

    private RadioButton Red;
    private RadioButton Gleen;
    private RadioButton Blue;

    private Button dailog_d_confirm;//确定
    private Button dailog_d_concel;//取消
  private opencv_core.CvPoint cvPoint= new opencv_core.CvPoint(99999,99999);
    private EditText vertical_xy;
    private EditText horizontal_xy;
    public ImageView myimageview;//相片
    //判断是点击的哪一个图片按钮
    private  static int  IMAGE1_FLAG = 0;
    private  static int  IMAGE2_FLAG = 0;
    private  static int  IMAGE3_FLAG = 0;
    private  static int  IMAGE4_FLAG = 0;
    //触摸图片按钮
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private Button  report;//生成报告按钮;
    private opencv_core.CvPoint[] myPoint = new opencv_core.CvPoint[4];//触摸事件的4个坐标点
    private boolean finishedPerspective_flag = false;//判断4个点是否都选取
    //创建两个文件流
    private File reportPath = null;
    private File txtFilePath = null;

    opencv_core.IplImage img,img1,img2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handle);
        SysApplication.getInstance().addActivity(this);
        probutton=(Button) findViewById(R.id.button);
        myimageview = (ImageView)findViewById(R.id.imageView3);
        imageView1=(ImageView)findViewById(R.id.imageButton4);
        imageView2=(ImageView)findViewById(R.id.imageButton5);
        imageView3=(ImageView)findViewById(R.id.imageButton6);
        imageView4=(ImageView)findViewById(R.id.imageButton7);
        report=(Button)findViewById(R.id.button3);
       // correction=(Button) findViewById(R.id.button2);
        imageView1.setOnTouchListener(new autoSelect.image1ontouchlistener());
        imageView2.setOnTouchListener(new autoSelect.image2ontouchlistener());
        imageView3.setOnTouchListener(new autoSelect.image3ontouchlistener());
        imageView4.setOnTouchListener(new autoSelect.image4ontouchlistener());
        myimageview.setOnTouchListener(new autoSelect.ontouchListener());
        report.setOnClickListener(new autoSelect.OnreportListener());
       // probutton.setOnClickListener(new autoSelect.proOnclickButton());
        //初始化myPoint
        for(int i = 0;i < 4;i++)
            myPoint[i] = new opencv_core.CvPoint(9999,9999);
     //   myimageview.setImageBitmap(CreamTools.showImage(ItemPO.srcImage));
        //全图找圆函数
        allScren();


     //使用说明弹窗；
        dialog = new Dialog(autoSelect.this,R.style.Dialog);
        dialog.setContentView(R.layout.dialog_instructions);
        dialog.setTitle(null);
        probutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                dialog.show();
            }});
        dialog_button12= (Button) dialog.findViewById(R.id.dialog_button12);
        dialog_cancol=(Button)dialog.findViewById(R.id.dialog_cancol);
        dialog_cancol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();//取消窗口
            }
        });
        textView=(TextView) dialog.findViewById(R.id.dialog_text001);
        textView.setText(R.string.dialog_instructions2);
        dialog_button12.setOnClickListener( new autoSelect.proOnclickButton());//透视变换



        //调整弹窗
        dialog_d = new Dialog(autoSelect.this,R.style.Dialog);
        dialog_d .setContentView(R.layout.dialog_handle);
        dialog_d .setTitle(null);
        //三个颜色通道按钮
        Red=(RadioButton)dialog_d.findViewById(R.id.dialog_handle_R);
        Gleen=(RadioButton)dialog_d.findViewById(R.id.dialog_handle_G);
        Blue=(RadioButton)dialog_d.findViewById(R.id.dialog_handle_B);
        //六个复选框按钮
        NIT= (CheckBox) dialog_d.findViewById(R.id.handle_NIT);
        BLD= (CheckBox) dialog_d.findViewById(R.id.handle_BLD);
        PRO= (CheckBox) dialog_d.findViewById(R.id.handle_PRO);
        LEU= (CheckBox) dialog_d.findViewById(R.id.handle_LEU);
        GLU= (CheckBox) dialog_d.findViewById(R.id.handle_GLU);
        Valid= (CheckBox) dialog_d.findViewById(R.id.handle_VALID);


        left=(Button)dialog_d.findViewById(R.id.xy_direction).findViewById(R.id.collect_left);
        right=(Button)dialog_d.findViewById(R.id.xy_direction).findViewById(R.id.collect_right);
        up=(Button)dialog_d.findViewById(R.id.xy_direction).findViewById(R.id.collect_upward);
        down=(Button)dialog_d.findViewById(R.id.xy_direction).findViewById(R.id.collect_downward);
        horizontal_xy=(EditText)dialog_d.findViewById(R.id.xy_direction).findViewById(R.id.horizontal_xy);
        vertical_xy=(EditText)dialog_d.findViewById(R.id.xy_direction).findViewById(R.id.vertical_xy);
        left.setOnClickListener(new leftchange());
        right.setOnClickListener(new rightchange());
        up.setOnClickListener(new upchange());
        down.setOnClickListener(new downchange());
//        correction.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View arg0){
//                GetCoordinates();
//                dialog_d .show();
//            }});
        dailog_d_confirm=(Button)dialog_d.findViewById(R.id.dialog_handle_confirm) ;//校正确定按钮；
        dailog_d_concel=(Button)dialog_d.findViewById(R.id.dialog_handle_cancel);//校正取消按钮；
        dailog_d_confirm.setOnClickListener(new proOnclickButton());//校正确认按钮
        dailog_d_concel.setOnClickListener(new correctionCF());
        NIT.setOnCheckedChangeListener(new checkNIT());
        BLD.setOnCheckedChangeListener(new checkBLD());
        PRO.setOnCheckedChangeListener(new checkPRO());
        LEU.setOnCheckedChangeListener(new checkLEU());
        GLU.setOnCheckedChangeListener(new checkGLU());
        Valid.setOnCheckedChangeListener(new checkValid());

        //颜色通道改变
        Red.setOnClickListener(new RGBchangeButton());
        Gleen.setOnClickListener(new RGBchangeButton());
        Blue.setOnClickListener(new RGBchangeButton());
  }


//颜色通道改变方法
  public  class RGBchangeButton implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_handle_R:
                if(NIT.isChecked()){
                    if(nitR){
                        nitR=false;
                        Red.setChecked(nitR);
                    }else{
                        nitR=true;
                        Red.setChecked(nitR);
                    }
                }else if(BLD.isChecked()){
                    if(bldR){
                        bldR=false;
                        Red.setChecked(bldR);
                    }else{
                        bldR=true;
                        Red.setChecked(bldR);
                    }

                }else if(PRO.isChecked()){
                    if(proR){
                        proR=false;
                        Red.setChecked(proR);


                    }else{
                        proR=true;
                        Red.setChecked(proR);
                    }
                }else if(Valid.isChecked()){

                }else if(GLU.isChecked()){
                    if(gluR){
                        gluR=false;
                        Red.setChecked(gluR);
                    }else{
                        gluR=true;
                        Red.setChecked(gluR);
                    }
                }else if(LEU.isChecked()){
                    if(leuR){
                        leuR=false;
                        Red.setChecked(leuR);
                    }else{
                        leuR=true;
                        Red.setChecked(leuR);
                    }
                }

                break;
            case R.id.dialog_handle_G:
                if(NIT.isChecked()){
                    if(nitG){
                        nitG=false;
                        Gleen.setChecked(nitG);

                    }else{
                        nitG=true;
                        Gleen.setChecked(nitG);
                    }
                }else if(BLD.isChecked()){
                    if(bldG){
                        bldG=false;
                        Gleen.setChecked(bldG);
                    }else{
                        bldG=true;
                        Gleen.setChecked(bldG);
                    }

                }else if(PRO.isChecked()){
                    if(proG){
                        proG=false;
                        Gleen.setChecked(proG);
                    }else{
                        proG=true;
                        Gleen.setChecked(proG);
                    }
                }else if(Valid.isChecked()){

                }else if(GLU.isChecked()){
                    if(gluG){
                        gluG=false;
                        Gleen.setChecked(gluG);
                    }else{
                        gluG=true;
                        Gleen.setChecked(gluG);
                    }
                }else if(LEU.isChecked()){
                    if(leuG){
                        leuG=false;
                        Gleen.setChecked(leuG);
                    }else{
                        leuG=true;
                        Gleen.setChecked(leuG);
                    }
                }
                break;
            case R.id.dialog_handle_B:
                if(NIT.isChecked()){
                    if(nitB){
                        nitB=false;
                        Blue.setChecked(nitB);
                    }else{
                        nitB=true;
                        Blue.setChecked(nitB);
                    }
                }else if(BLD.isChecked()){
                    if(bldB){
                        bldB=false;
                        Blue.setChecked(bldB);
                    }else{
                        bldB=true;
                        Blue.setChecked(bldB);
                    }

                }else if(PRO.isChecked()){
                    if(proB){
                        proB=false;
                        Blue.setChecked(proB);
                    }else{
                        proB=true;
                        Blue.setChecked(proB);
                    }
                }else if(Valid.isChecked()){

                }else if(GLU.isChecked()){
                    if(gluB){
                        gluB=false;
                        Blue.setChecked(gluB);
                    }else{
                        gluB=true;
                        Blue.setChecked(gluB);
                    }
                }else if(LEU.isChecked()){
                    if(leuB){
                        leuB=false;
                        Blue.setChecked(leuB);
                    }else{
                        leuB=true;
                        Blue.setChecked(leuB);
                    }
                }
                break;
        }
    }
}



//校正取消按钮
    public class correctionCF implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        dialog_d.cancel();
    }
}




  //判断复选框
  public class checkNIT implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
           if(isChecked){
              BLD.setChecked(false);
               PRO.setChecked(false);
              LEU.setChecked(false);
              GLU.setChecked(false);
               Valid.setChecked(false);
               GetCoordinates();
           }
        }
    }
    public class checkBLD implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
           if(isChecked){
              NIT.setChecked(false);
               PRO.setChecked(false);
             LEU.setChecked(false);
               GLU.setChecked(false);
              Valid.setChecked(false);
               GetCoordinates();
           }
        }
    }
    public class checkPRO implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
           if(isChecked){
              NIT.setChecked(false);
              BLD.setChecked(false);
              LEU.setChecked(false);
              GLU.setChecked(false);
              Valid.setChecked(false);
               GetCoordinates();
          }
        }
    }
    public class checkLEU implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
           if(isChecked){
               NIT.setChecked(false);
              PRO.setChecked(false);
              BLD.setChecked(false);
              GLU.setChecked(false);
               Valid.setChecked(false);
               GetCoordinates();
          }
        }
    }
    public class checkGLU implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
           if(isChecked){
              NIT.setChecked(false);
              PRO.setChecked(false);
             LEU.setChecked(false);
              BLD.setChecked(false);
              Valid.setChecked(false);
               GetCoordinates();
           }
        }
    }
    public class checkValid implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
          if(isChecked){
            NIT.setChecked(false);
            PRO.setChecked(false);
             LEU.setChecked(false);
             GLU.setChecked(false);
              BLD.setChecked(false);
              GetCoordinates();
          }
        }
    }
//改变坐标函数
public  class leftchange implements View.OnClickListener{
    @Override
    public void onClick(View v) {
int k=5;
        if(Integer.valueOf(cvPoint.x()-k)<= 0) {
            Toast.makeText(autoSelect .this, "The result cannot be zero！", Toast.LENGTH_LONG).show();
            return;
        }
        horizontal_xy.setText(Integer.valueOf(cvPoint.x()-k).toString());
        changeagainleft();
    }
}
    public  class rightchange implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int k=5;

            if(Integer.valueOf(cvPoint.x()+k)>=1440) {
                Toast.makeText(autoSelect .this, "The result cannot be 1440！", Toast.LENGTH_LONG).show();
                return;
            }
            horizontal_xy.setText(Integer.valueOf(cvPoint.x()+k).toString());
          changeagainrighet();
        }
    }
    public  class upchange implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int k=5;

            if(Integer.valueOf(cvPoint.y()-k)<=0) {
                Toast.makeText(autoSelect .this, "The result cannot be 600！", Toast.LENGTH_LONG).show();
                return;
            }
            vertical_xy.setText(Integer.valueOf(cvPoint.y()-k).toString());
        changeagainup();
        }
    }
    public  class downchange implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int k=5;

            if(Integer.valueOf(cvPoint.y()+k)>=600) {
                Toast.makeText(autoSelect .this, "The result cannot be zero！", Toast.LENGTH_LONG).show();
                return;
            }
            vertical_xy.setText(Integer.valueOf(cvPoint.y()+k).toString());
        changeagaindown();
        }
    }
//显示坐标函数
    public void GetCoordinates(){
           if(NIT.isChecked()){
               Red.setChecked(nitR);
               Gleen.setChecked(nitG);
               Blue.setChecked(nitB);
              cvPoint = ItemPO.selectArray[0];
        }else if(BLD.isChecked()){
               Red.setChecked(bldR);
               Gleen.setChecked(bldG);
               Blue.setChecked(bldB);
             cvPoint = ItemPO.selectArray[2];
        }else if(PRO.isChecked()){
               Red.setChecked(proR);
               Gleen.setChecked(proG);
               Blue.setChecked(proB);
               cvPoint = ItemPO.selectArray[4];
        }else if(Valid.isChecked()){
            cvPoint = ItemPO.selectArray[5];
        }else if(GLU.isChecked()){
               Red.setChecked(gluR);
               Gleen.setChecked(gluG);
               Blue.setChecked(gluB);
            cvPoint = ItemPO.selectArray[3];
        }else if(LEU.isChecked()){
               Red.setChecked(leuR);
               Gleen.setChecked(leuG);
               Blue.setChecked(leuB);
            cvPoint = ItemPO.selectArray[1];
        }

        horizontal_xy.setText(Integer.valueOf(cvPoint.x()).toString());
        vertical_xy.setText(Integer.valueOf(cvPoint.y()).toString());
    }



//再次调用checkbook显示
    public void changeagainleft(){
        int k=5;
            if(NIT.isChecked()){
                ItemPO.selectArray[0]=new opencv_core.CvPoint(cvPoint.x()-k,cvPoint.y());
               cvPoint =ItemPO.selectArray[0] ;
            }else if(BLD.isChecked()){
                ItemPO.selectArray[2]=new opencv_core.CvPoint(cvPoint.x()-k,cvPoint.y());
                cvPoint =ItemPO.selectArray[2] ;
            }else if(PRO.isChecked()){
                ItemPO.selectArray[4]=new opencv_core.CvPoint(cvPoint.x()-k,cvPoint.y());
                cvPoint =ItemPO.selectArray[4] ;
            }else if(Valid.isChecked()){
                ItemPO.selectArray[5]=new opencv_core.CvPoint(cvPoint.x()-k,cvPoint.y());
                cvPoint =ItemPO.selectArray[5] ;
            }else if(GLU.isChecked()){
                ItemPO.selectArray[3]=new opencv_core.CvPoint(cvPoint.x()-k,cvPoint.y());
                cvPoint =ItemPO.selectArray[3] ;
            }else if(LEU.isChecked()){
                ItemPO.selectArray[1]=new opencv_core.CvPoint(cvPoint.x()-k,cvPoint.y());
                cvPoint =ItemPO.selectArray[1] ;
            }
    }
    public void changeagainrighet(){
        int k=5;
       if(NIT.isChecked()){
            ItemPO.selectArray[0]=new opencv_core.CvPoint(cvPoint.x()+k,cvPoint.y());
            cvPoint =ItemPO.selectArray[0] ;
        }else if(BLD.isChecked()){
            ItemPO.selectArray[2]=new opencv_core.CvPoint(cvPoint.x()+k,cvPoint.y());
            cvPoint =ItemPO.selectArray[2] ;
        }else if(PRO.isChecked()){
            ItemPO.selectArray[4]=new opencv_core.CvPoint(cvPoint.x()+k,cvPoint.y());
            cvPoint =ItemPO.selectArray[4] ;
        }else if(Valid.isChecked()){
            ItemPO.selectArray[5]=new opencv_core.CvPoint(cvPoint.x()+k,cvPoint.y());
            cvPoint =ItemPO.selectArray[5] ;
        }else if(GLU.isChecked()){
            ItemPO.selectArray[3]=new opencv_core.CvPoint(cvPoint.x()+k,cvPoint.y());
            cvPoint =ItemPO.selectArray[3] ;
        }else if(LEU.isChecked()){
            ItemPO.selectArray[1]=new opencv_core.CvPoint(cvPoint.x()+k,cvPoint.y());
            cvPoint =ItemPO.selectArray[1] ;
        }
    }
    public void changeagainup(){
        int k=5;
        if(NIT.isChecked()){
            ItemPO.selectArray[0]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()-k);
            cvPoint =ItemPO.selectArray[0] ;
        }else if(BLD.isChecked()){
            ItemPO.selectArray[2]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()-k);
            cvPoint =ItemPO.selectArray[2] ;
        }else if(PRO.isChecked()){
            ItemPO.selectArray[4]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()-k);
            cvPoint =ItemPO.selectArray[4] ;
        }else if(Valid.isChecked()){
            ItemPO.selectArray[5]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()-k);
            cvPoint =ItemPO.selectArray[5] ;
        }else if(GLU.isChecked()){
            ItemPO.selectArray[3]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()-k);
            cvPoint =ItemPO.selectArray[3] ;
        }else if(LEU.isChecked()){
            ItemPO.selectArray[1]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()-k);
            cvPoint =ItemPO.selectArray[1] ;
        }
    }
    public void changeagaindown(){
        int k=5;
        if(NIT.isChecked()){
            ItemPO.selectArray[0]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()+k);
            cvPoint =ItemPO.selectArray[0] ;
        }else if(BLD.isChecked()){
            ItemPO.selectArray[2]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()+k);
            cvPoint =ItemPO.selectArray[2] ;
        }else if(PRO.isChecked()){
            ItemPO.selectArray[4]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()+k);
            cvPoint =ItemPO.selectArray[4] ;
        }else if(Valid.isChecked()){
            ItemPO.selectArray[5]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()+k);
            cvPoint =ItemPO.selectArray[5] ;
        }else if(GLU.isChecked()){
            ItemPO.selectArray[3]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()+k);
            cvPoint =ItemPO.selectArray[3] ;
        }else if(LEU.isChecked()){
            ItemPO.selectArray[1]=new opencv_core.CvPoint(cvPoint.x(),cvPoint.y()+k);
            cvPoint =ItemPO.selectArray[1] ;
        }
    }
//全图片找圆函数。
    public  void allScren (){

       //img = cvLoadImage("D:\ii.jpg",1); //原图
       img=ItemPO.srcImage;
        img1 = cvCreateImage (cvGetSize(img), IPL_DEPTH_8U, 1);//处理的图像必须是八位单通道的
        if (img. nChannels()== 1)
        {
            img1 = cvCloneImage (img);
        }
        else
        {
            cvCvtColor (img, img1, CV_RGB2GRAY); //转为单通道
        }

        opencv_core.CvMemStorage storage=cvCreateMemStorage(0);
        opencv_core.CvSeq circle = cvCreateSeq(0, Loader.sizeof(opencv_core.CvSeq.class), Loader.sizeof(opencv_core.CvPoint.class), storage);

//cvSmooth( img1, img1, CV_GAUSSIAN, 5, 5 ); //看了很多事例，要降噪处理，但测试的结果是
//找圆会有一定偏差，说明用这个要因图而异，噪声高的图才要用
        circle = cvHoughCircles( //cvHoughCircles函数需要估计每一个像素梯度的方向，
//因此会在内部自动调用cvSobel,而二值边缘图像的处理是比较难的
                img1,
                storage,
                CV_HOUGH_GRADIENT,
                1, //累加器图像的分辨率,增大则分辨率变小
                250, //很重要的一个参数，告诉两个圆之间的距离的最小距离，如果已知一副图像，可以先行计
//算出符合自己需要的两个圆之间的最小距离。
                250, //canny算法的阈值上限，下限为一半（即100以上为边缘点，50以下抛弃，中间视是否相连而
//定）
                15, //决定成圆的多寡 ，一个圆上的像素超过这个阈值，则成圆，否则丢弃
                15,//最小圆半径，这个可以通过图片确定你需要的圆的区间范围
                40 //最大圆半径
        );

        img2 = cvCreateImage (cvGetSize(img), IPL_DEPTH_8U, 3); //用一个三通道的图片来显示红色的
//圆圈
        cvCvtColor (img1, img2, CV_GRAY2RGB); //转为3通道

        for( int i = 0; i < circle.total(); i++ )
        {
        //Pointer p = cvGetSeqElem( circle, i );
            opencv_core.CvPoint3D32f p = new opencv_core.CvPoint3D32f(cvGetSeqElem(circle,i));
//霍夫圆变换
            int pt = Math.round(p.z());
            opencv_core.CvPoint curCenter = new opencv_core.CvPoint(Math.round(p.x()),Math.round(p.y()));
            cvCircle(img2,curCenter,pt,CV_RGB(255, 255, 255),3,CV_FILLED,0);
         //  cvCircle(showImage_RGB,new opencv_core.CvPoint(a-25+curCenter.x(),b-25+curCenter.y()),2*pt,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
            cvCircle(
                    img2,
                    new opencv_core.CvPoint(curCenter.x(),curCenter.y()), //确定圆心
                    pt, //确定半径
                    CV_RGB( 255, 0, 0 ),
                    -1,
                    CV_FILLED,
                    0
            ); //画圆函数
       listfour.add(curCenter);//采集画圆的点。

        }

        SelectColor algorthim = new SelectColor(getApplicationContext());
        algorthim.autoSel(img);
       myimageview.setImageBitmap(CreamTools.showImage(DefaultFP(img)));//把找到的圆点进行加工处理，返回四个最有可能的点


    }

public opencv_core.IplImage DefaultFP(opencv_core.IplImage img){
    //复制一张图片出来。
    opencv_core.IplImage showImage_RGB = img.clone();
   for(int i=0;i<4;i++){
    cvCircle(showImage_RGB,CorrectionFour[i],30,ColorFour[i],-1,CV_FILLED,0);
   }
//    cvCircle(showImage_RGB,CorrectionFour[0],10,CV_RGB( 255,0,0 ),-1,CV_FILLED,0);
//    cvCircle(showImage_RGB,CorrectionFour[1],10,CV_RGB( 0,255,0 ),-1,CV_FILLED,0);
//    cvCircle(showImage_RGB,CorrectionFour[2],10,CV_RGB( 0,0,255),-1,CV_FILLED,0);
//    cvCircle(showImage_RGB,CorrectionFour[3],10,CV_RGB( 255,0,255 ),-1,CV_FILLED,0);
return showImage_RGB;
}









    public final class image1ontouchlistener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            IMAGE1_FLAG = 1;
            IMAGE2_FLAG = 0;
            IMAGE3_FLAG = 0;
            IMAGE4_FLAG = 0;
            Log.i("-------------","IMAGE1_FLAG");
            return true;
        }

    }
    //2
    public final class image2ontouchlistener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            IMAGE1_FLAG = 0;
            IMAGE2_FLAG = 2;
            IMAGE3_FLAG = 0;
            IMAGE4_FLAG = 0;
            return true;
        }

    }
    //3
    public final class image3ontouchlistener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            IMAGE1_FLAG = 0;
            IMAGE2_FLAG = 0;
            IMAGE3_FLAG = 3;
            IMAGE4_FLAG = 0;
            return true;
        }

    }
    //4
    public final class image4ontouchlistener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            IMAGE1_FLAG = 0;
            IMAGE2_FLAG = 0;
            IMAGE3_FLAG = 0;
            IMAGE4_FLAG = 4;
            return true;
        }

    }
    //图片触摸事件
    private final class ontouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            int ImageNum = IMAGE1_FLAG|IMAGE2_FLAG|IMAGE3_FLAG|IMAGE4_FLAG;//检测被选中的ImageView方块
            if(ImageNum == 0 || ItemPO.grayImage == null || ItemPO.srcImage == null) return true;   //not chose define location
            int myAction = event.getAction();//获取触发事件。
            //获取控件位置坐标
            int[] location = new int[2];
            myimageview.getLocationOnScreen(location);//获取ImageView触摸点坐标
            switch(myAction)
            {
                case MotionEvent.ACTION_DOWN:
                    int ImageCentorX =(myimageview.getRight()-myimageview.getLeft() )/2;//得到myImageView的中心
                    int ImageCentorY =(myimageview.getBottom() -myimageview.getTop())/2;
                    Matrix m = myimageview.getImageMatrix();
                    float[] values = new float[10];
                    m.getValues(values);
                    float com_x = values[0];//压缩系数，图片放到新的框里会按比例压缩。
                    float com_y = values[4];
                    //计算压缩后图片在imageView中的实际的宽和高
                    int trueWidth = (int)(ItemPO.grayImage.width()*com_x+0.5f);  //实际的宽并取整
                    int trueHeight = (int)(ItemPO.grayImage.height()*com_y+0.5f); //实际的高
                    int halftrueWidth = (int)(trueWidth*0.5);
                    int halftrueHeight = (int)(trueHeight*0.5);
                    //判断触摸点是否在ImageView图片内
                    if(event.getX() < (ImageCentorX-halftrueWidth) || event.getX() >(ImageCentorX+halftrueWidth)||
                            event.getY()< (ImageCentorY-halftrueHeight)||event.getY()>(ImageCentorY+halftrueHeight))  return true;
                    //就在这个地方画一个圆圈，这里是原图片的位置上对应的位置。通过压缩位置除以压缩系数。
                    int a = (int)(ItemPO.grayImage.width()*(event.getX()+halftrueWidth-ImageCentorX)/trueWidth+0.5f);
                    int b = (int)(ItemPO.grayImage.height()*(event.getY()+halftrueHeight-ImageCentorY)/trueHeight+0.5f);
                    opencv_core.CvPoint point = new opencv_core.CvPoint(a,b);
                    opencv_core.IplImage showImage = ItemPO.grayImage.clone();
                    if(showImage != null)
                        //画圆函数，cvCircle是指绘制圆形的一个程序函数。
                        //void cvCircle( CvArr* img, CvPoint center, int radius, CvScalar color, int thickness=1, int line_type=8, int shift=0 );
                        //img 图像,center 圆心坐标,radius 圆形的半径,color 线条的颜色,thickness 如果是正数，表示组成圆的线条的粗细程度。否则，表示圆是否被填充
                        //line_type 线条的类型。见 cvLine 的描述,shift 圆心坐标点和半径值的小数点位数
                        cvCircle(showImage,point,35,CV_RGB(255, 255, 255),3,CV_FILLED,0);
                    /*创建一个100*100的矩阵头*/
                    opencv_core.CvMat myMat = cvCreateMatHeader(50,50,CV_8UC1);
                    /*以原点中心向左上平移后画一个正方形*/
                    opencv_core.CvRect myRect = cvRect(a-25,b-25,50,50);
                    //以选取点判断是否足够画一个半径25的圆
                    if(a-25 < 0 || b-25 < 0 || a+25 >ItemPO.grayImage.width()||b+25 >ItemPO.grayImage.height()) {
                        Toast.makeText(autoSelect.this, "ROI choose failed", Toast.LENGTH_SHORT).show();
                        return true;                  //ROI  超过图像边界
                    }
                    /*pImg为指向图像的指针，pMat指向存储所接图像的矩阵，返回值和pMat相等*/
                    cvGetSubRect(ItemPO.grayImage,myMat,myRect);
                    //创建一个100*100的图像头,subImage为要截取的一个矩形图片
                    opencv_core.IplImage subImage = cvCreateImageHeader(cvSize(50,50), 8, 1);
                    /*pMat为存储数据的矩阵，pSubImg指向图像，返回值与pSubImg相等也可以简化为：*/
                    cvGetImage(myMat,subImage);



                    //Important  transform the image and tot the circle point
                    //分配一个图片头文件,用来放二值化后的图片。
                    opencv_core.IplImage BinaryImage = cvCreateImage(subImage.cvSize(), IPL_DEPTH_8U, 1);
                    //二值化处理阙值为100.
                    cvThreshold(subImage, BinaryImage,100, 255, CV_THRESH_BINARY_INV);
                    //申请一块内存放临时的图片（是用来显示再图片上的红点吗？）
                    opencv_core.CvMemStorage storage = cvCreateMemStorage(0); //默认的是64kb

                    /*
                    创建了一个空的CvSeq对象,参数seq_flags表示序列元素的数据类型，
                    一般设置为0即可，即数据类型未指定；参数header_size表示CvSeq结构体的大小，
                    一般设置为sizeof(CvSeq)；参数elem_size表示每个元素所占用的字节数，一般设置为sizeof(type)，
                    type可取int、float、char、CvPoint等；
                    参数storage表示由函数cvCreateMemStorage创建的内存存储指针。*/

                    opencv_core.CvSeq contour = cvCreateSeq(0, Loader.sizeof(opencv_core.CvSeq.class), Loader.sizeof(opencv_core.CvPoint.class), storage);

                    /*霍夫圆变换的函数为：利用 Hough 变换在灰度图像中找圆
                    （3.Hough 变换方式，目前只支持CV_HOUGH_GRADIENT,
                      4.累加器图像的分辨率，如果dp设置为1，则分辨率是相同的；如果设置为更大的值（比如2），累加器的分辨率受此影响会变小（此情况下为一半）。dp的值不能比1小）
                      5.探测到的圆的中心之间的最小距离。如果参数太小，多个相邻的圆可能会被错误地检测到一个真实的圆。如果它太大，可能会漏掉一些圆。
                      6.用于Canny的边缘阀值上限，下限被置为上限的一半。
                      7.累加器的阀值。
                      8.最小圆半径。9.最大圆半径。
                    */

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        contour = cvHoughCircles(BinaryImage, storage, CV_HOUGH_GRADIENT,2,ItemPO.grayImage.width()/2,500,8,3,15);

                    //subImage  showImage convert to RGB把图片恢复为彩色
                    opencv_core.IplImage showImage_RGB = ItemPO.srcImage.clone();
                    opencv_core.CvMat myMat2 = cvCreateMatHeader(50,50,CV_8UC1);
                    opencv_core.CvRect myRect2 = cvRect(a-25,b-25,50,50);
                    cvGetSubRect(ItemPO.srcImage,myMat2,myRect2);
                    opencv_core.IplImage sub_RGB = cvCreateImageHeader(cvSize(50,50), 8, 3);
                    cvGetImage(myMat2,sub_RGB);//截取了出彩色的图片
                    opencv_core.CvPoint curCenter = null; //圆心
                    int curRadius = 0;    //半径
                    if(contour.total() != 1) {
                        Toast.makeText(autoSelect.this, "Can not find a circle !", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    for(int i = 0;i < contour.total();i++){
                        //功能上理解，得到截取的原的对象，从而得到圆心和半径。
                        opencv_core.CvPoint3D32f curCircle = new opencv_core.CvPoint3D32f(cvGetSeqElem(contour,i));
                        curRadius = Math.round(curCircle.z())+3;
                        curCenter = new opencv_core.CvPoint(Math.round(curCircle.x()),Math.round(curCircle.y()));
                        //cvCircle(subImage,curCenter,curRadius,CV_RGB(255, 255, 255),3,CV_FILLED,0);
                       // cvCircle(showImage_RGB,new opencv_core.CvPoint(a-25+curCenter.x(),b-25+curCenter.y()),2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);

                    }

                    if(ImageNum == 1) {
                        imageView1.setImageBitmap(CreamTools.showImage(sub_RGB));
                        int x = a-25+curCenter.x();
                        int y = b-25+curCenter.y();
                        //TextView1.setText("X1="+ x+"\nY1="+y);//show the center position of the black point
                        CorrectionFour[0] = new opencv_core.CvPoint(x,y);
                        //绘制出其他的圆
                            cvCircle(showImage_RGB,CorrectionFour[0],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                            cvCircle(showImage_RGB,CorrectionFour[1],2*curRadius,CV_RGB(0, 255, 0),-1,CV_FILLED,0);
                            cvCircle(showImage_RGB,CorrectionFour[2],2*curRadius,CV_RGB(0, 0, 255),-1,CV_FILLED,0);
                            cvCircle(showImage_RGB,CorrectionFour[3],2*curRadius,CV_RGB(255, 0, 255),-1,CV_FILLED,0);
                    }
                    else if(ImageNum == 2) {
                        imageView2.setImageBitmap(CreamTools.showImage(sub_RGB));
                        int x = a-25+curCenter.x();
                        int y = b-25+curCenter.y();
                        CorrectionFour[1] = new opencv_core.CvPoint(x,y);
                        //TextView2.setText("X2="+ x+"\nY2="+y);
                        //绘制出其他的圆
                        cvCircle(showImage_RGB,CorrectionFour[0],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        cvCircle(showImage_RGB,CorrectionFour[1],2*curRadius,CV_RGB(0, 255, 0),-1,CV_FILLED,0);
                        cvCircle(showImage_RGB,CorrectionFour[2],2*curRadius,CV_RGB(0, 0, 255),-1,CV_FILLED,0);
                        cvCircle(showImage_RGB,CorrectionFour[3],2*curRadius,CV_RGB(255, 0, 255),-1,CV_FILLED,0);
                    }
                    else if(ImageNum == 3) {
                        imageView3.setImageBitmap(CreamTools.showImage(sub_RGB));
                        int x = a-25+curCenter.x();
                        int y = b-25+curCenter.y();
                        CorrectionFour[2] = new opencv_core.CvPoint(x,y);
                        //绘制出其他的圆
                        cvCircle(showImage_RGB,CorrectionFour[0],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        cvCircle(showImage_RGB,CorrectionFour[1],2*curRadius,CV_RGB(0, 255, 0),-1,CV_FILLED,0);
                        cvCircle(showImage_RGB,CorrectionFour[2],2*curRadius,CV_RGB(0, 0, 255),-1,CV_FILLED,0);
                        cvCircle(showImage_RGB,CorrectionFour[3],2*curRadius,CV_RGB(255, 0, 255),-1,CV_FILLED,0);
                    }
                    else {
                        imageView4.setImageBitmap(CreamTools.showImage(sub_RGB));
                        int x = a-25+curCenter.x();
                        int y = b-25+curCenter.y();
                        CorrectionFour[3] = new opencv_core.CvPoint(x,y);
                        //TextView4.setText("X4="+ x+"\nY4="+y);
                        //绘制出其他的圆
                        cvCircle(showImage_RGB,CorrectionFour[0],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        cvCircle(showImage_RGB,CorrectionFour[1],2*curRadius,CV_RGB(0, 255, 0),-1,CV_FILLED,0);
                        cvCircle(showImage_RGB,CorrectionFour[2],2*curRadius,CV_RGB(0, 0, 255),-1,CV_FILLED,0);
                        cvCircle(showImage_RGB,CorrectionFour[3],2*curRadius,CV_RGB(255, 0, 255),-1,CV_FILLED,0);
                    }
                    finishedPerspective_flag = false;// not finished perspective
                    myimageview.setImageBitmap(CreamTools.showImage(showImage_RGB));
                    break;
                //
            }//end switch
            return true;
        }
    }

    //透视变换方法。
    public  class proOnclickButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {


            //把四个点的x坐标进行从小到大排序。
            opencv_core.CvPoint p = new opencv_core.CvPoint();
            for(int i = 0; i < 3;i++)
            {
                for(int j = 0;j < 4-i-1;j++)
                {
                    if(CorrectionFour[j].x() > CorrectionFour[j+1].x()){
                        p = CorrectionFour[j];
                        CorrectionFour[j] = CorrectionFour[j+1];
                        CorrectionFour[j+1] = p;
                    }
                }
            }
            //让第0个和第2个点位于下方，1和3位于上方。
            if(CorrectionFour[0].y() > CorrectionFour[1].y()){
                p = CorrectionFour[0];
                CorrectionFour[0] = CorrectionFour[1];
                CorrectionFour[1] = p;
            }
            if(CorrectionFour[2].y() > CorrectionFour[3].y()){
                p = CorrectionFour[2];
                CorrectionFour[2] = CorrectionFour[3];
                CorrectionFour[3] = p;
            }
            opencv_core.CvMat warp_matrix = cvCreateMat(3,3,CV_32FC1);	//透视的变换矩阵，必须是3*3
            cvZero(warp_matrix);//是让矩阵的值都为0，好处是避免系统自动分配值的不确定性对程序的影响
            float distanceyy = (float)Math.pow(Math.pow(CorrectionFour[1].x()-CorrectionFour[0].x(), 2)+Math.pow(CorrectionFour[1].y()-CorrectionFour[0].y(),2), 0.5);//算出0，1点的距离
            float distancexx = (float)Math.pow(Math.pow(CorrectionFour[2].x()-CorrectionFour[0].x(), 2)+Math.pow(CorrectionFour[2].y()-CorrectionFour[0].y(),2), 0.5);//算出0，2点的距离
            float distancey = (float)Math.pow(Math.pow(CorrectionFour[3].x()-CorrectionFour[2].x(), 2)+Math.pow(CorrectionFour[3].y()-CorrectionFour[2].y(),2), 0.5);//算出3，2点的距离
            float distancex = (float)Math.pow(Math.pow(CorrectionFour[3].x()-CorrectionFour[1].x(), 2)+Math.pow(CorrectionFour[3].y()-CorrectionFour[1].y(),2), 0.5);//算出3，1点的距离
            distancex = (distancex + distancexx)/2;
            distancey = (distancey + distanceyy)/2;

            float[] a1 = new float[]{CorrectionFour[0].x(),CorrectionFour[0].y(),CorrectionFour[2].x(),CorrectionFour[2].y(),CorrectionFour[1].x(),CorrectionFour[1].y(),CorrectionFour[3].x(),CorrectionFour[3].y()};
            float[] a2 = new float[]{0,0,distancex,0,0,distancey,distancex,distancey};
            //创建一个图片头文件。
            opencv_core.IplImage dst = opencv_core.IplImage.create(((int)(distancex+1)),(int)(distancey+1), 8, 3);
            //1.输入图像的四边形顶点坐标。2.输出图像的相应的四边形顶点坐标。3.指向3×3输出矩阵的指针。
            cvGetPerspectiveTransform(a1,a2,warp_matrix);
            //1.输入图像.2.输出图像.3.3×3 变换矩阵
            cvWarpPerspective(ItemPO.srcImage, dst, warp_matrix);

            SelectColor algorthim = new SelectColor(getApplicationContext());
            algorthim.calculateDistance(dst);
            myimageview.setImageBitmap(CreamTools.showImage(dst));
            finishedPerspective_flag = true;
            dialog.cancel();//关闭弹窗
            dialog_d.cancel();
              enable=true;//手动校正可用
            //correction.setEnabled(enable);
        }
    }

    private class OnreportListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(finishedPerspective_flag == false){
                Toast.makeText(autoSelect.this, "Please finished perspective first!", Toast.LENGTH_SHORT).show();
                return;
            }

            String pathString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+"/THReport";
            reportPath = new File(pathString);
            txtFilePath  = new File(reportPath,"Report.txt");//decide file path
            // Make sure the pictures directory exists
            if(!reportPath.exists()){
                Toast.makeText(autoSelect.this,reportPath.toString()+"can not find!", Toast.LENGTH_LONG).show();
                reportPath.mkdir();
            }
            try{
                if(txtFilePath.exists())txtFilePath.delete();
                //create a new file under this file
                txtFilePath.createNewFile();
            }catch(Exception e){};
            //write data to this file
            BufferedWriter bWrite = null;
            FileWriter fw = null;
            SimpleDateFormat tempData = new SimpleDateFormat("MM/dd/yyyy"+" "+"hh:mm:ss");
            String datatime = tempData.format(new java.util.Date()).toString();
            String buf = "Urine analysis report "+"\n"
                    + "Date:"+datatime+"\n"
                    + "-----------------------------"+"\n"
                    + "| Marker |result |"+"\n"
                    + "-------------xxxxxx------------"+"\n"
                    + "-------------------------------"+"\n"
		    			   /*+ "| 肌酐         |  -    |  xx |  15   |"+"\n"
		    			   + "| 尿蛋白     |  -    |  xx |  12   |"+"\n"
		    			   + "| 白细胞     |  -    |  xx |  6    |"+"\n"
		    			   + "| 尿比重     |  +    |  xx |  32   |"+"\n"
		    			   + "| 亚硝酸盐 |  ++   |  xx |  20   |"+"\n"
		    			   + "| 尿胆原     |  +    |  xx |  7    |"+"\n"
		    			   + "| 酸碱度     |  -    |  xx |  18   |"+"\n"
		    			   + "| 抗坏血酸 |  -    |  xx |  25   |"+"\n"
		    			   + "| 隐血         |  +    |  xx |  31   |"+"\n"
		    			   + "| 酮体         |  -    |  xx |  9    |"+"\n"
		    			   + "| 胆红素     |  -    |  xx |  12   |"+"\n"
		    			   + "| 尿糖         |  -    |  xx |  6    |\n";*/
                   // + "|  Valid  |"+ItemPO.result[ItemPO.res[1]]+"|"+"\n"
                    + "|  Nit  |"+ItemPO.result[ItemPO.res[0]]+"|"+"\n"
                    + "|  Leu  |"+ItemPO.result[ItemPO.res[1]]+"|"+"\n"
                    + "|  Pro  |"+ItemPO.result[ItemPO.res[4]]+"|"+"\n"
                    + "|  Glu  |"+ItemPO.result[ItemPO.res[3]]+"|"+"\n"
                    + "|  Blo  |"+ItemPO.result[ItemPO.res[2]]+"|"+"\n";

            try{
                fw = new FileWriter(txtFilePath.toString(),true);
                bWrite = new BufferedWriter(fw);
                bWrite.write(buf);
                bWrite.close();
                fw.close();
            }catch(IOException e){
                e.printStackTrace();
                try {
                    bWrite.close();

                    fw.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            //active Intent
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("filePath", txtFilePath.toString());
            intent.putExtras(bundle);
            intent.setClass(autoSelect.this, com.example.cai.hutu.collect.activity.report.class);
            startActivity(intent);
            finish();
        }
    }







}
