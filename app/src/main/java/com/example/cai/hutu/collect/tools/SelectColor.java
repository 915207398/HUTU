package com.example.cai.hutu.collect.tools;

import android.content.Context;
import android.support.design.internal.NavigationMenuItemView;

import com.example.cai.hutu.collect.ItemPO.ItemPO;


import org.bytedeco.javacpp.opencv_core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.cai.hutu.collect.ItemPO.ItemPO.CorrectionFour;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.listfour;

import static java.lang.Boolean.TRUE;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_core.cvSet2D;

/**
 * Created by dell on 2017/6/6.
 */

public class SelectColor {

    public int[] ref;
    public double[] distance;
    public double[] distance2;
    public Map<String,opencv_core.CvPoint> mapa=new HashMap<String,opencv_core.CvPoint>();
    public int X;
    public int Y;
    public Context mcontex;
    public SelectColor(Context m) {
        // TODO Auto-generated constructor stub
        ref = new int[]{  1,1,0,0,1,// 0-Negative   1-"+" 2-"0" 3-"25-10" 4-"80" 5-"200" 6-"NonValid" 7-"0.2+" 8-"1++" 9-"3+++"
                     6,  5,4,3,2,0,1,1,
                         9,8,7,2};
        distance = new double[18];
        mcontex = m;
    }


    public opencv_core.CvScalar GetPixCalculateAverageValue(opencv_core.IplImage dst, opencv_core.CvPoint point,int S){
        //get the test and reference data
        //创建RGB颜色空间值
        int r_value = 0;
        int g_value = 0;
        int b_value = 0;
        //创建一个4通道的Scalar
        opencv_core.CvScalar scalar = new opencv_core.CvScalar(0,0,0,0);
        int G=7;
        int L=11;
        if(S==2){
            G=12;
            L=12;
        }
        int F=(2*G-1)*(2*L-1);
        //画一个矩形区域，并把该点颜色变为红色。
        opencv_core.CvPoint[]  rectangle=new opencv_core.CvPoint[F];
        int count=0;

        for(int i=0;i<G;i++){
            for(int j=0;j<L;j++){
                rectangle[count]=new opencv_core.CvPoint(point.x()-i,point.y()-j);
                count++;

            }
           for(int n=1;n<L;n++){
               rectangle[count]=new opencv_core.CvPoint(point.x()-i,point.y()+n);
               count++;
           };
        }
        for(int k=1;k<G;k++){
            for(int j=0;j<L;j++){
                rectangle[count]=new opencv_core.CvPoint(point.x()+k,point.y()-j);
                count++;

            }
            for(int n=1;n<L;n++){
                rectangle[count]=new opencv_core.CvPoint(point.x()+k,point.y()+n);
                count++;
            };
        }
        for(int l=0;l<F;l++){
            scalar = cvGet2D(dst,rectangle[l].y(),rectangle[l].x());
           // System.out.println(rectangle[l].y()+"++++++++++++++"+rectangle[l].x());
            b_value += scalar.val(0);
            g_value += scalar.val(1);
            r_value += scalar.val(2);

            scalar.val(0,0);
            scalar.val(1,0);
            scalar.val(2,255);
            //把这个的颜色在对应图片上显示。
            cvSet2D(dst,rectangle[l].y(),rectangle[l].x(),scalar);
        };

        scalar.val(0,b_value/=F);
        scalar.val(1,g_value/=F);
        scalar.val(2,r_value/=F);
        //标准差计算公式。
        StandardDeviation(rectangle, dst, scalar);
        return scalar;
    }



    /*
    2017.7.2
      画圆后判断该圆颜色是否接近黑色
     */
    public opencv_core.CvScalar GetPixCalculateAverageValue2(opencv_core.IplImage dst, opencv_core.CvPoint point){
        //get the test and reference data
        //创建RGB颜色空间值
        int r_value = 0;
        int g_value = 0;
        int b_value = 0;
        //创建一个4通道的Scalar
        opencv_core.CvScalar scalar = new opencv_core.CvScalar(0,0,0,0);
        //以输入点为中心提取他周围的点，后面来取平均值。
        opencv_core.CvPoint[] cvPoint = new opencv_core.CvPoint[]{new opencv_core.CvPoint(point.x()-3,point.y()-1),new opencv_core.CvPoint(point.x()-2,point.y()-1),new opencv_core.CvPoint(point.x()-1,point.y()-1),new opencv_core.CvPoint(point.x(),point.y()-1),new opencv_core.CvPoint(point.x()+1,point.y()-1),new opencv_core.CvPoint(point.x()+2,point.y()-1),new opencv_core.CvPoint(point.x()+3,point.y()-1),
                new opencv_core.CvPoint(point.x()-3,point.y()),  new opencv_core.CvPoint(point.x()-2,point.y()),  new opencv_core.CvPoint(point.x()-1,point.y()),  new opencv_core.CvPoint(point.x(),point.y()),  new opencv_core.CvPoint(point.x()+1,point.y()),  new opencv_core.CvPoint(point.x()+2,point.y()),  new opencv_core.CvPoint(point.x()+3,point.y()),
                new opencv_core.CvPoint(point.x()-3,point.y()+1),new opencv_core.CvPoint(point.x()-2,point.y()+1),new opencv_core.CvPoint(point.x()-1,point.y()+1),new opencv_core.CvPoint(point.x(),point.y()+1),new opencv_core.CvPoint(point.x()+1,point.y()+1),new opencv_core.CvPoint(point.x()+2,point.y()+1),new opencv_core.CvPoint(point.x()+3,point.y()+1)};

        for(int i = 0;i < 21;i++){
            scalar = cvGet2D(dst,cvPoint[i].y(),cvPoint[i].x());
            b_value += scalar.val(0);
            g_value += scalar.val(1);
            r_value += scalar.val(2);
        }
        scalar.val(0,b_value/=21);
        scalar.val(1,g_value/=21);
        scalar.val(2,r_value/=21);

        return scalar;
    }







    public int[] calculateDistance(opencv_core.IplImage dst){
        ItemPO.showString="";
        int width = dst.width();
        int height = dst.height();
       // System.out.println("1111111111111111width="+width+"--------height="+height);
        opencv_core.CvScalar[] store = new opencv_core.CvScalar[ItemPO.pointArray.length];//store average value pix
        for(int i = 0;i < ItemPO.pointArray.length;i++){
            opencv_core.CvPoint point = new opencv_core.CvPoint((int)(width*ItemPO.pointArray[i].x()/756),(int)(height*ItemPO.pointArray[i].y()/356));
            opencv_core.CvScalar scalar = GetPixCalculateAverageValue(dst,point,1);//CvScalar  B G R value not R G B value
            if(i==13){
                scalar.val(2,scalar.val(2)+23);
            }
            if(i==14){
                scalar.val(2,scalar.val(2)+23);
            }
            if(i==6){
                scalar.val(2,scalar.val(2)+25);
            }
            if(i==7){
                scalar.val(2,scalar.val(2)+25);
            }
            store[i] = scalar;
            ItemPO.showString+=i+"--:b="+scalar.val(0)+"--:g="+scalar.val(1)+"--:r="+scalar.val(2)+"\n";
            // store the result in the array
        }//end for
		/*next will use the Euclidean distance calculate test Biomarker to reference Biomarker distance
		* before calculate,we should  perpare test biomarker data
		*/
        opencv_core.CvScalar[] select =new opencv_core.CvScalar[ItemPO.selectArray.length];
        for(int i = 0;i < ItemPO.selectArray.length;i++){
            opencv_core.CvPoint point = new opencv_core.CvPoint((int)(width*ItemPO.selectArray[i].x()/756),(int)(height*ItemPO.selectArray[i].y()/356));
            //MyAlgorithm algorthim = new MyAlgorithm();
            opencv_core.CvScalar scalar = GetPixCalculateAverageValue(dst,point,2);//CvScalar  B G R value not R G B value
            if(i==0){
              //  scalar.val(2,scalar.val(2)+10);
                scalar.val(1,scalar.val(1)+10);
            }

            select[i] = scalar; // store the result in the array
            ItemPO.showString+=i+"--reaction block--:b="+scalar.val(0)+"--:g="+scalar.val(1)+"--:r="+scalar.val(2)+"\n";;
        }

        opencv_core.CvScalar Nit = select[0];//suppose this is Nit pix value,I think you can select from arrat store
        opencv_core.CvScalar Leu = select[1];
        opencv_core.CvScalar Blo = select[2];
        opencv_core.CvScalar Glu = select[3];
        opencv_core.CvScalar Pro = select[4];
        opencv_core.CvScalar Valid = select[5];
        //calculate Nit
        double value = 999999999;
        for(int i = 0;i < 3;i++){
           // distance[i] = myEuclideanDistance(Nit,store[i]);
            distance[i]=eightalgorithm(ItemPO.nitR,ItemPO.nitG,ItemPO.nitB,Nit,store[i],i);
            if(value > distance[i]){
                value = distance[i];
                ItemPO.res[0] = ref[i];
            }
        }//end for
        value = 999999999;
        for(int i = 3;i < 5;i++){
            //distance[i] = myEuclideanDistance(Leu,store[i]);
            distance[i]=eightalgorithm(ItemPO.leuR,ItemPO.leuG,ItemPO.leuB,Leu,store[i],i);
            if(value > distance[i]){
                value = distance[i];
                ItemPO.res[1] = ref[i];
            }
        }//end for
        value = 999999999;
        for(int i = 5;i < 10;i++){
          //  distance[i] = myEuclideanDistance(Blo,store[i]);
            distance[i]=eightalgorithm(ItemPO.bldR,ItemPO.bldG,ItemPO.bldB,Blo,store[i],i);
            if(value > distance[i]){
                value = distance[i];
                ItemPO.res[2] = ref[i];
            }
        }//end for
        value = 999999999;
        for(int i = 10;i < 13;i++){
           // distance[i] = myEuclideanDistance(Glu,store[i]);
            distance[i]=eightalgorithm(ItemPO.gluR,ItemPO.gluG,ItemPO.gluB,Glu,store[i],i);
            if(value > distance[i]){
                value = distance[i];
                ItemPO.res[3] = ref[i];
            }
        }//end for
        value = 999999999;
        for(int i = 13;i < 17;i++){
          //  distance[i] = myEuclideanDistance(Pro,store[i]);
            distance[i]=eightalgorithm(ItemPO.proR,ItemPO.proG,ItemPO.proB,Pro,store[i],i);
            System.out.println(i+"-------"+Pro.val(2)+"------------"+store[i].val(2));
            if(value > distance[i]){
                value = distance[i];
                ItemPO.res[4] = ref[i];
            }
        }//end for
      //  value = 999999999;
//        for(int i = 22;i < 27;i++){
//            distance[i] = myEuclideanDistance(Leu,store[i]);
//            if(value > distance[i]){
//                value = distance[i];
//                ItemPO.res[5] = ref[i];
//            }
//        }//end for
        return ItemPO.res;
    }



    // Euclidean distance algorithm
    public double myEuclideanDistance(opencv_core.CvScalar test, opencv_core.CvScalar ref){
        double dis = 0;
        dis = Math.sqrt(Math.pow(test.val(0)-ref.val(0), 2)+Math.pow(test.val(1)-ref.val(1), 2)+Math.pow(test.val(2)-ref.val(2), 2));
        return dis;
    }
   //Single channel
    public double mySinglechannelDistance(opencv_core.CvScalar test, opencv_core.CvScalar ref){
        double dis = 0;
        dis=Math.abs(test.val(2)-ref.val(2));
        return dis;
    }
//计算取值区域的标准差
public double StandardDeviation(opencv_core.CvPoint[] test,opencv_core.IplImage dst,opencv_core.CvScalar scalar){
    double dis = 0;
    double R=0;
    double G=0;
    double B=0;
    opencv_core.CvScalar rgb = new opencv_core.CvScalar(0,0,0,0);
      for(int i=0;i<test.length;i++){
          rgb = cvGet2D(dst,test[i].y(),test[i].x());
         B+=Math.abs(Math.pow(rgb.val(0)-scalar.val(0),2));
         G+=Math.abs(Math.pow(rgb.val(1)-scalar.val(1),2));
         R+=Math.abs(Math.pow(rgb.val(2)-scalar.val(2),2));
      }
    dis=R+G+B;
 return dis;
}



//判断调用通道方法
    public double  eightalgorithm(boolean R,boolean G,boolean B,opencv_core.CvScalar test, opencv_core.CvScalar ref,int i){
        double dis = 0;

          if(R==true&&G==true&&B==true){
              dis = Math.sqrt(Math.pow(test.val(0)-ref.val(0), 2)+Math.pow(test.val(1)-ref.val(1), 2)+Math.pow(test.val(2)-ref.val(2), 2));
          }else if(R==true&&G==true&&B==false){
              dis = Math.sqrt(Math.pow(test.val(1)-ref.val(1), 2)+Math.pow(test.val(2)-ref.val(2), 2));
          }else if(R==true&&G==false&&B==false){
              dis = Math.sqrt(Math.pow(test.val(2)-ref.val(2), 2));
          }else if(R==false&&G==false&&B==false){
              dis = Math.sqrt(Math.pow(test.val(0)-ref.val(0), 2)+Math.pow(test.val(1)-ref.val(1), 2)+Math.pow(test.val(2)-ref.val(2), 2));
          }else if(R==false&&G==true&&B==true){
              dis = Math.sqrt(Math.pow(test.val(0)-ref.val(0), 2)+Math.pow(test.val(1)-ref.val(1), 2));
          }else if(R==false&&G==true&&B==false){
              dis = Math.sqrt(Math.pow(test.val(1)-ref.val(1), 2));
          }else if(R==false&&G==false&&B==true){
              dis = Math.sqrt(Math.pow(test.val(0)-ref.val(0), 2));
          }else if(R==true&&G==false&&B==true){
              dis = Math.sqrt(Math.pow(test.val(0)-ref.val(0), 2)+Math.pow(test.val(2)-ref.val(2), 2));
          }
      //  System.out.println("R="+R+"G="+G+"B="+B+"dis="+dis);
        ItemPO.showString+=i+"--R="+R+"G="+G+"B="+B+"dis="+dis+"\n";
        return dis;

    }







    /*
     *2017.7.1
     *
     */
    //图片自动选取点变换
    public opencv_core.IplImage autoSel(opencv_core.IplImage dst){
        int width = dst.width();
        int height = dst.height();
        opencv_core.CvScalar[] store = new opencv_core.CvScalar[listfour.size()];//store average value pix
        opencv_core.CvScalar black = new opencv_core.CvScalar(0,0,0,0);//store average value pix
        black.val(0,0);
        black.val(1,0);
        black.val(2,0);
        distance2=new double[listfour.size()];
        for(int i = 0;i < listfour.size();i++){
            opencv_core.CvScalar scalar = GetPixCalculateAverageValue2(dst,listfour.get(i));//CvScalar  B G R value not R G B value
            store[i] = scalar;
            double d=myEuclideanDistance(black,scalar);
            String dis=String.valueOf(d);
            mapa.put(dis,listfour.get(i));
           distance2[i]=d;
            // store the result in the array
        }
       double temp;
        for (int i = 0; i < distance2.length-1; i++) {
            for (int j = 0; j < distance2.length-i-1; j++) {
                if (distance2[j] > distance2[j+1]) {
                    temp = distance2[j+1];
                    distance2[j+1] = distance2[j];
                    distance2[j] = temp;  // 两个数交换位置
                }
            }
        }
        opencv_core.CvScalar colora = new opencv_core.CvScalar(0,0,0,0);
        for(int i=0;i<4;i++){
            opencv_core.CvPoint point = new opencv_core.CvPoint((int)(mapa.get( String.valueOf(distance2[i])).x()),(int)(mapa.get( String.valueOf(distance2[i])).y()));
           //  System.out.println("___________-----选取的四个最黑的定位点---距离---____x___y__"+distance2[i]+"++++++"+point.y()+"+++++"+point.x());
           // opencv_core.CvScalar scalar2 = GetPixCalculateAverageValue(dst,point);//CvScalar  B G R value not R G B value
            CorrectionFour[i]=point;
        }
return dst;
    }
}
