package com.example.cai.hutu.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cai.hutu.R;
import com.example.cai.hutu.bottomnavigation.activity.Navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.R.string.no;

/**
 * Created by dell on 2017/6/16.
 */

public class languageActivity extends Activity {
    private Spinner spinner;
    private List<Map<String,Object>> datalist;
    private SimpleAdapter adapter;
    private Button button1;
    private Button button2;
    private String Text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language);
        button1=(Button) findViewById(R.id.language_bt1);
        button2=(Button) findViewById(R.id.language_bt2);
        button1.setOnClickListener(new onClickConfirm());
        button2.setOnClickListener(new onClickCancel());
        spinner=(Spinner) findViewById(R.id.language_spinner);
        //1.设置数据源
        datalist=new ArrayList<Map<String,Object>>();
        getData();
        //2.建立ArrayListAdapter(数组适配器)
        adapter=new SimpleAdapter(this,datalist,R.layout.lan_item,new String[]{"image","text"}, new int[]{R.id.language_image,R.id.language_text});
        //3.adpter设置下拉列表样式
        adapter.setDropDownViewResource(R.layout.lan_item);
        //4.spinner加载适配器
        spinner.setAdapter(adapter);
        //  spinner.setSelection();
        //5.spinner设置监听器
        spinner.setOnItemSelectedListener(new languageActivity.SpinnerListener());
    }
    //数据源
    private  void getData(){
        String text[]={"请选择语言","中文","English","Norge"};
        Object image[]={R.mipmap.icon_one,R.mipmap.zhongguo,R.mipmap.yingguo,R.mipmap.nuowei};
        for(int i=0;i<text.length;i++){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("image",image[i]);
        map.put("text",text[i]);
        datalist.add(map);
        }


    }

    //刷新页面
    private void restartActivity() {
        finish();
    }
    private void switchLanguage(String language) {
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Locale currentLocal = config.locale;
        if(language.equals("请选择语言")){

        }else {
        if (language.equals("English")) {
            config.locale = Locale.ENGLISH;
        } else if (language.equals("中文")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals("jp")) {
            config.locale = Locale.JAPAN;
            //config.locale = Locale.JAPANESE;
        } else if (language.equals("kr")) {
            config.locale = Locale.KOREA;
            //config.locale = Locale.KOREAN;
        }else if(language.equals("Norge")){
            config.locale = new Locale("nb");
        }
        resources.updateConfiguration(config, dm);

        //保存设置语言的类型
//        if(!currentLocal.equals(config.locale)){
//            Intent na= new Intent(languageActivity.this,LoginActivity.class);
//            startActivity(na);
//        }

        }
    }
  public class SpinnerListener implements AdapterView.OnItemSelectedListener{
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> lang=(HashMap<String,String>)parent.getItemAtPosition(position);
        Text=lang.get("text");
        Toast.makeText(languageActivity.this,Text,Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        String sInfo="什么也没选！";
        Toast.makeText(getApplicationContext(),sInfo, Toast.LENGTH_LONG).show();
    }
}
//Confirm
public class onClickConfirm implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        switchLanguage(Text);
        Intent na= new Intent(languageActivity.this,Navigation.class);
         startActivity(na);
        finish();
    }
}
//Cancel
    public class onClickCancel implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        Intent na= new Intent(languageActivity.this,Navigation.class);
        startActivity(na);
        finish();
    }
}
}
