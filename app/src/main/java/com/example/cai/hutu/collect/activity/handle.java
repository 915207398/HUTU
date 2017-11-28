package com.example.cai.hutu.collect.activity;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cai.hutu.R;
import com.example.cai.hutu.collect.ItemPO.ItemPO;
import com.example.cai.hutu.collect.tools.CreamTools;
import com.example.cai.hutu.collect.tools.SelectColor;


import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImageHeader;
import static org.bytedeco.javacpp.opencv_core.cvCreateMat;
import static org.bytedeco.javacpp.opencv_core.cvCreateMatHeader;
import static org.bytedeco.javacpp.opencv_core.cvCreateMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvCreateSeq;
import static org.bytedeco.javacpp.opencv_core.cvGetImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvGetSubRect;
import static org.bytedeco.javacpp.opencv_core.cvRect;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_core.cvZero;
import static org.bytedeco.javacpp.opencv_imgproc.CV_FILLED;
import static org.bytedeco.javacpp.opencv_imgproc.CV_HOUGH_GRADIENT;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.cvCircle;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetPerspectiveTransform;
import static org.bytedeco.javacpp.opencv_imgproc.cvHoughCircles;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;
import static org.bytedeco.javacpp.opencv_imgproc.cvWarpPerspective;


/**
 * Created by cai on 2017/5/29.
 */

public class handle extends Activity{
    private Dialog dialog;//弹窗;
    private Button probutton;//透视按钮;
    public int ImageView_height;
    public int ImageView_width;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handle);
        probutton=(Button) findViewById(R.id.button);
        myimageview = (ImageView)findViewById(R.id.imageView3);
        imageView1=(ImageView)findViewById(R.id.imageButton4);
        imageView2=(ImageView)findViewById(R.id.imageButton5);
        imageView3=(ImageView)findViewById(R.id.imageButton6);
        imageView4=(ImageView)findViewById(R.id.imageButton7);
        report=(Button)findViewById(R.id.button3);
        imageView1.setOnTouchListener(new image1ontouchlistener());
        imageView2.setOnTouchListener(new image2ontouchlistener());
        imageView3.setOnTouchListener(new image3ontouchlistener());
        imageView4.setOnTouchListener(new image4ontouchlistener());
        myimageview.setOnTouchListener(new ontouchListener());
        report.setOnClickListener(new OnreportListener());
        probutton.setOnClickListener(new proOnclickButton());
        //初始化myPoint
        for(int i = 0;i < 4;i++)
            myPoint[i] = new opencv_core.CvPoint(9999,9999);
        myimageview.setImageBitmap(CreamTools.showImage(ItemPO.srcImage));
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
                        cvCircle(showImage,point,25,CV_RGB(255, 255, 255),3,CV_FILLED,0);
                    /*创建一个100*100的矩阵头*/
                    opencv_core.CvMat myMat = cvCreateMatHeader(50,50,CV_8UC1);
                    /*以原点中心向左上平移后画一个正方形*/
                    opencv_core.CvRect myRect = cvRect(a-25,b-25,50,50);
                    //以选取点判断是否足够画一个半径25的圆
                    if(a-25 < 0 || b-25 < 0 || a+25 >ItemPO.grayImage.width()||b+25 >ItemPO.grayImage.height()) {
                        Toast.makeText(handle.this, "ROI choose failed", Toast.LENGTH_SHORT).show();
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
                    contour = cvHoughCircles(BinaryImage, storage, CV_HOUGH_GRADIENT,2,ItemPO.grayImage.width()/2,500,8,15,35);

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
                        Toast.makeText(handle.this, "Can not find a circle !", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    for(int i = 0;i < contour.total();i++){
                        //功能上理解，得到截取的原的对象，从而得到圆心和半径。
                        opencv_core.CvPoint3D32f curCircle = new opencv_core.CvPoint3D32f(cvGetSeqElem(contour,i));
                        curRadius = Math.round(curCircle.z());
                        curCenter = new opencv_core.CvPoint(Math.round(curCircle.x()),Math.round(curCircle.y()));
                        //cvCircle(subImage,curCenter,curRadius,CV_RGB(255, 255, 255),3,CV_FILLED,0);
                        cvCircle(showImage_RGB,new opencv_core.CvPoint(a-25+curCenter.x(),b-25+curCenter.y()),2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);

                    }

                    if(ImageNum == 1) {
                        imageView1.setImageBitmap(CreamTools.showImage(sub_RGB));
                        int x = a-25+curCenter.x();
                        int y = b-25+curCenter.y();
                        //TextView1.setText("X1="+ x+"\nY1="+y);//show the center position of the black point
                        myPoint[0] = new opencv_core.CvPoint(x,y);
                        //绘制出其他的圆
                        if(myPoint[1].x() != 9999 && myPoint[1].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[1],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        if(myPoint[2].x() != 9999 && myPoint[2].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[2],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        if(myPoint[3].x() != 9999 && myPoint[3].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[3],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                    }
                    else if(ImageNum == 2) {
                        imageView2.setImageBitmap(CreamTools.showImage(sub_RGB));
                        int x = a-25+curCenter.x();
                        int y = b-25+curCenter.y();
                        myPoint[1] = new opencv_core.CvPoint(x,y);
                        //TextView2.setText("X2="+ x+"\nY2="+y);
                        //绘制出其他的圆
                        if(myPoint[0].x() != 9999 && myPoint[0].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[0],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        if(myPoint[2].x() != 9999 && myPoint[2].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[2],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        if(myPoint[3].x() != 9999 && myPoint[3].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[3],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                    }
                    else if(ImageNum == 3) {
                        imageView3.setImageBitmap(CreamTools.showImage(sub_RGB));
                        int x = a-25+curCenter.x();
                        int y = b-25+curCenter.y();
                        myPoint[2] = new opencv_core.CvPoint(x,y);
                        //绘制出其他的圆
                        if(myPoint[1].x() != 9999 && myPoint[1].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[1],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        if(myPoint[0].x() != 9999 && myPoint[0].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[0],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        if(myPoint[3].x() != 9999 && myPoint[0].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[3],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                    }
                    else {
                        imageView4.setImageBitmap(CreamTools.showImage(sub_RGB));
                        int x = a-25+curCenter.x();
                        int y = b-25+curCenter.y();
                        myPoint[3] = new opencv_core.CvPoint(x,y);
                        //TextView4.setText("X4="+ x+"\nY4="+y);
                        //绘制出其他的圆
                        if(myPoint[1].x() != 9999 && myPoint[1].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[1],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        if(myPoint[2].x() != 9999 && myPoint[2].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[2],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                        if(myPoint[0].x() != 9999 && myPoint[0].y() != 9999)
                            cvCircle(showImage_RGB,myPoint[0],2*curRadius,CV_RGB(255, 0, 0),-1,CV_FILLED,0);
                    }
                    finishedPerspective_flag = false;// not finished perspective
                    myimageview.setImageBitmap(CreamTools.showImage(showImage_RGB));
                    break;
                //
            }//end switch
            return true;
        }
    }
    public  class proOnclickButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //判断是否四个点都选齐了
            if(myPoint[0].x()==9999||myPoint[1].x()==9999||myPoint[2].x()==9999||myPoint[3].x()==9999){
                Toast.makeText(handle.this, "Please chose the four mark point", Toast.LENGTH_SHORT).show();
                return;
            }
            //把四个点的x坐标进行从小到大排序。
            opencv_core.CvPoint p = new opencv_core.CvPoint();
            for(int i = 0; i < 3;i++)
            {
                for(int j = 0;j < 4-i-1;j++)
                {
                    if(myPoint[j].x() > myPoint[j+1].x()){
                        p = myPoint[j];
                        myPoint[j] = myPoint[j+1];
                        myPoint[j+1] = p;
                    }
                }
            }
            //让第0个和第2个点位于下方，1和3位于上方。
            if(myPoint[0].y() > myPoint[1].y()){
                p = myPoint[0];
                myPoint[0] = myPoint[1];
                myPoint[1] = p;
            }
            if(myPoint[2].y() > myPoint[3].y()){
                p = myPoint[2];
                myPoint[2] = myPoint[3];
                myPoint[3] = p;
            }
            opencv_core.CvMat warp_matrix = cvCreateMat(3,3,CV_32FC1);	//透视的变换矩阵，必须是3*3
            cvZero(warp_matrix);//是让矩阵的值都为0，好处是避免系统自动分配值的不确定性对程序的影响
            float distanceyy = (float)Math.pow(Math.pow(myPoint[1].x()-myPoint[0].x(), 2)+Math.pow(myPoint[1].y()-myPoint[0].y(),2), 0.5);//算出0，1点的距离
            float distancexx = (float)Math.pow(Math.pow(myPoint[2].x()-myPoint[0].x(), 2)+Math.pow(myPoint[2].y()-myPoint[0].y(),2), 0.5);//算出0，2点的距离
            float distancey = (float)Math.pow(Math.pow(myPoint[3].x()-myPoint[2].x(), 2)+Math.pow(myPoint[3].y()-myPoint[2].y(),2), 0.5);//算出3，2点的距离
            float distancex = (float)Math.pow(Math.pow(myPoint[3].x()-myPoint[1].x(), 2)+Math.pow(myPoint[3].y()-myPoint[1].y(),2), 0.5);//算出3，1点的距离
            distancex = (distancex + distancexx)/2;
            distancey = (distancey + distanceyy)/2;

            float[] a1 = new float[]{myPoint[0].x(),myPoint[0].y(),myPoint[2].x(),myPoint[2].y(),myPoint[1].x(),myPoint[1].y(),myPoint[3].x(),myPoint[3].y()};
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
        }
    }

    private class OnreportListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(finishedPerspective_flag == false){
                Toast.makeText(handle.this, "Please finished perspective first!", Toast.LENGTH_SHORT).show();
                return;
            }

            String pathString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+"/THReport";
            reportPath = new File(pathString);
            txtFilePath  = new File(reportPath,"Report.txt");//decide file path
            // Make sure the pictures directory exists
            if(!reportPath.exists()){
                Toast.makeText(handle.this,reportPath.toString()+"can not find!", Toast.LENGTH_LONG).show();
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
            String buf = "Urine analysis report nr:#"+"\n"
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
                    + "|  Valid  |"+ItemPO.result[ItemPO.res[1]]+"|"+"\n"
                    + "|  Nit  |"+ItemPO.result[ItemPO.res[0]]+"|"+"\n"
                    + "|  Leu  |"+ItemPO.result[ItemPO.res[5]]+"|"+"\n"
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
            intent.setClass(handle.this,report.class);
            startActivity(intent);
        }
    }

}
