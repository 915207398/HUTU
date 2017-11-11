package com.example.cai.hutu.bottomnavigation.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cai.hutu.R;
import com.example.cai.hutu.autoselect.autoSelect;
import com.example.cai.hutu.bottomnavigation.tools.Constants;
import com.example.cai.hutu.collect.activity.cream;
import com.example.cai.hutu.collect.activity.handle;
import com.example.cai.hutu.collect.tools.CreamTools;
import com.example.cai.hutu.collect.tools.Initialize;


import org.bytedeco.javacpp.opencv_core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.cai.hutu.bottomnavigation.tools.Constants.mBottomNavigationBar;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.grayImage;
import static com.example.cai.hutu.collect.ItemPO.ItemPO.srcImage;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;


/**
 * Created by dell on 2017/6/7.
 */

public class FragmentTwo extends Fragment {
    private Button dialog_button12;//弹窗button
    private Dialog dialog;//弹窗
    private  Button dialog_cancol;//弹窗取消
    private TextView textView;//弹窗文本
    private File pathFile = null;  //照片存储位置，机身内存
    private ImageView myImageView =null;//放照片的ImageView;
    private ImageButton Takephoto=null;//照相按钮;
    private ImageButton Selectphoto=null;//相册按钮;
    private ImageButton GoNext=null;//进入下一步按钮;
    private String filename = null;   //文件夹名字
    //    public static opencv_core.IplImage srcImage ;
//    public static opencv_core.IplImage grayImage ;
//    public static Bitmap bitmap;
    /*判断请求是那个Activity返回回来的*/
    private static final int TAKE_PHOTO = 1;
    private static final int CHOSE_PHOTO = 2;

    private String imagePathToOpencv = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cream,container,false);
        CheckBuildinSDCard();                                    //检测SD卡状态
        Takephoto=(ImageButton)view .findViewById(R.id.imageButton3);
      //  Takephoto.setOnClickListener(new myTakePhotosButtonListener());
        Selectphoto=(ImageButton)view .findViewById(R.id.imageButton);
        Selectphoto.setOnClickListener(new mySelectPhotoListener());
        myImageView=(ImageView)view .findViewById(R.id.imageView);
        GoNext=(ImageButton)view .findViewById(R.id.imageButton2);
        GoNext.setOnClickListener(new onClickGoLisntener());
//        myImageView.setOnClickListener(new onClickHide());
        dialog = new Dialog(getActivity(),R.style.Dialog);
        dialog.setContentView(R.layout.dialog_instructions);
        dialog.setTitle(null);
        textView=(TextView) dialog.findViewById(R.id.dialog_text001);
        Takephoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                textView.setText(R.string.dialog_instructions1);
                dialog.show();
            }});
        dialog_cancol=(Button)dialog.findViewById(R.id.dialog_cancol);
        dialog_cancol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();//取消窗口
            }
        });


        dialog_button12= (Button) dialog.findViewById(R.id.dialog_button12);
        dialog_button12.setOnClickListener( new myTakePhotosButtonListener());


        return view;
    }
   //隐藏底部导航栏
//    public class onClickHide implements View.OnClickListener{
//
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.imageView) {
//                if (mBottomNavigationBar != null) {
//                    mBottomNavigationBar.toggle();
//                }
//            }
//        }
//    }


    /* 拍摄照片  并且把照片加载到ImageView-------------------------------------------------------------------*/
    public class myTakePhotosButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 启动照相  intent
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            filename ="/"+format.format(date)+".jpg";
            Uri imageUri = Uri.fromFile(new File(pathFile,filename));//图片保存的路径。
            Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 创建一个获取图像的Intent
            intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//保存原图片
            startActivityForResult(intentTakePhoto,TAKE_PHOTO);
        }

    }

    /*选取图片--------------------------------------------------------------------------------------------------*/
    public class mySelectPhotoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, CHOSE_PHOTO);
        }

    }

    /*onActivityResult function--------------------------------------------------------------------------------*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String pathString = null;
        if(resultCode != RESULT_OK){//
            filename = null;
            Toast.makeText(getActivity(), "ActivityResult resultCode error",Toast.LENGTH_LONG).show();
            return;
        }
        switch(requestCode){
            case TAKE_PHOTO:                            //利用pathFile  filename 加载图片到ImagView
                pathString = pathFile.toString()+filename;
                break;
            case CHOSE_PHOTO:
                Uri chosePhotoUri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(chosePhotoUri,proj, null, null, null);
                int idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                pathString  = cursor.getString(idx);
                cursor.close();
                break;
        }//end switch
        imagePathToOpencv = pathString;                          //这个路径传到其他Activity 进行该路径下的图片处理
        //加载大图片
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //不读取像素到内存，仅读取图片信息
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathString,opt);
        //从opt中获取原图像的分辨率
        int srcHeight = opt.outHeight;
        int srcWidth = opt.outWidth;
        //获取安卓屏幕的服务
        //WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int windHeight = myImageView.getHeight();
        int windWidth  =  myImageView.getWidth();


        int scaleX = srcWidth/windWidth;
        int scaleY = srcHeight/windHeight;
        int scale = 1;
        if(scaleX > scaleY && scaleY > 1) scale = scaleX;
        if(scaleX < scaleY && scaleX > 1) scale = scaleY;
        opt.inJustDecodeBounds = false;
        //opt.inSampleSize = scale +1;
        opt.inSampleSize = 1 +1;
        //加载图片到ImageView

        if(pathString == null){
            Toast.makeText(getActivity(), "Can not find the image!", Toast.LENGTH_LONG).show();
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(pathString,opt);
        myImageView.setImageBitmap(bitmap);
        //下面将Bitma 转换成srcImage
        srcImage =new CreamTools().Bitmap2IplImage(bitmap);
        grayImage = opencv_core.IplImage.create(bitmap.getWidth(),bitmap.getHeight(),IPL_DEPTH_8U, 1);//分配图片参数进行的是黑白处理。
        cvCvtColor(srcImage,grayImage,CV_RGB2GRAY);//转换
        //  Bitmap mmm=showImage(grayImage);
        //  myImageView.setImageBitmap(mmm);
        dialog.cancel();//关闭弹窗
    }




    /*检测机身内存卡是否可用-------------------------------------------------------------------------------------*/
    public void CheckBuildinSDCard(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){  //SD卡可用
            //Toast.makeText(LoadImageActivity.this,"The SD can use!", Toast.LENGTH_LONG).show();
            //创建File对象用于存储拍照的图片       SD卡根目录
            String pathString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+"/Camera";
            pathFile = new File(pathString);
            // Make sure the pictures directory exists
            if(!pathFile.exists())
                pathFile.mkdirs();
        }
        else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            Toast.makeText(getActivity(),"The SD in only read model!", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            Toast.makeText(getActivity(),"Cannot access SD card!", Toast.LENGTH_LONG).show();
        }
    }

    /*GoNext监听函数-----------------------------------------------------------------------------------------------*/
    public class onClickGoLisntener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 启动图像处理的intent  并且将图像路径传进去
            if(myImageView.getDrawable() == null){
                Toast.makeText(getActivity(), "Image Load not sucessful !", Toast.LENGTH_LONG).show();
                return;
            }//end if
            //初始化数据
            Initialize init=new Initialize();
            init.InitPoint();
            init.Initdistance();//初始化改变的坐标；
            Intent imageHandle = new Intent(getActivity(),autoSelect.class);
            //imageHandle.putExtra("imagePathToOpencv", imagePathToOpencv);
            startActivity(imageHandle);

           // LoadImageActivity.this.finish();//结束这个intent
        }

    }
}
