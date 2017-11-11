package com.example.cai.hutu.collect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.cai.hutu.R;
import com.example.cai.hutu.bottomnavigation.activity.Navigation;
import com.example.cai.hutu.collect.ItemPO.ItemPO;
import com.example.cai.hutu.collect.MyAdapter.MySimpleAdapter;
import com.example.cai.hutu.collect.tools.Initialize;
import com.example.cai.hutu.tool.SysApplication;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/6/6.
 */

public class report extends Activity{
    public TextView reportTitleView = null;
    public TextView reportContentView = null;
    public List<HashMap<String,Object>> data = null;//data used to store data for report show
    private ListView report_ListView = null;
    private List<Map<String,Object>> listView_data;
    public TextView edittext;
    public Button come_back_home;
    private ScrollView scrollView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        SysApplication.getInstance().addActivity(report.this);
        Bundle bundle = new Bundle();
        bundle  = this.getIntent().getExtras();
        String filePath = bundle.getString("filePath");
        data = new ArrayList<HashMap<String,Object>>();
        data = GetReportDataFromFile(filePath);
        MySimpleAdapter reportAdapter = new MySimpleAdapter(this, data, R.layout.item, new String[]{"Marker","Result"},
                new int[]{R.id.Marker,R.id.result});
        ((ListView)findViewById(R.id.ListView)).setAdapter(reportAdapter);
//        edittext=(TextView)findViewById(R.id.add_content);
//        edittext.setText(ItemPO.showString);
        scrollView = (ScrollView) findViewById(R.id.report_scrollview);
        scrollView.smoothScrollTo(0, 0);
        come_back_home=(Button)findViewById(R.id.come_back_home);
        come_back_home.setOnClickListener(new report.onClickCancel());
    }


    private List<HashMap<String,Object>> GetReportDataFromFile(String filePath){
        List<HashMap<String,Object>> mydata = new ArrayList<HashMap<String,Object>>();
        if(filePath == null) return null;
        String rData;
        //open the file,read data from it and show on TextView
        try{
            BufferedReader rBuffer = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            int i = 0;
            for(i = 0,rData = rBuffer.readLine();rData != null;rData = rBuffer.readLine(),i++){
                if(i > 5){
                    //split the string
                    String[] strArray = rData.split("[|]");
                    HashMap<String,Object> item = new HashMap<String,Object>();
                    item.put("Marker", strArray[1]);
                    item.put("Result", strArray[2]);
                    mydata.add(item);
                    continue;
                }
                if(i == 1) ((TextView)findViewById(R.id.text_days)).setText(rData);
            }
            rBuffer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return mydata;
    }
    //Cancel
    public class onClickCancel implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            //初始化数据
            Initialize init=new Initialize();
            init.InitPoint();

            Intent na= new Intent(report.this,Navigation.class);
            startActivity(na);
           // SysApplication.getInstance().exit();

            finish();
//         AppManager off=new AppManager();
        }
    }


}
