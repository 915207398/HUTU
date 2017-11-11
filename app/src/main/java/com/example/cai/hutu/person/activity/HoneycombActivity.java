package com.example.cai.hutu.person.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cai.hutu.R;
import com.example.cai.hutu.tool.HoneycombVView;

/**
 * Created by dell on 2017/6/16.
 */

public class HoneycombActivity extends Activity {
    HoneycombVView honeycombVView;
    String[] tittles = new String[]{"优惠活动", "技术文档", "供求信息", "新闻", "商品查询", "广告", "公司简介"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.honeycombactivity);

        honeycombVView = (HoneycombVView) findViewById(R.id.honeycombVView_main);
        honeycombVView.setOnActionListener(new HoneycombVView.ActionListener() {
            @Override
            public void ActionListener(int actionIndex) {
                String actionStr = actionIndex >= 0 ? tittles[actionIndex] : "空白区域";
                Toast.makeText(HoneycombActivity.this, "点击了 " + actionStr, Toast.LENGTH_SHORT).show();

                switch (actionIndex) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
