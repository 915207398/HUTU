package com.example.cai.hutu.bottomnavigation.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cai.hutu.R;
import com.example.cai.hutu.collect.activity.cream;
import com.example.cai.hutu.login.activity.languageActivity;
import com.example.cai.hutu.tool.HoneycombVView;
import com.example.cai.hutu.plot.activity.PlotActivity;

/**
 * Created by dell on 2017/6/7.
 */

public class FragmentThree extends Fragment {
    HoneycombVView honeycombVView;
//    String[] tittles = new String[]{"优惠活动", "技术文档", "供求信息", "新闻", "商品查询", "广告", "公司简介"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.honeycombactivity,container,false);
        honeycombVView = (HoneycombVView)view.findViewById(R.id.honeycombVView_main);
        honeycombVView.setOnActionListener(new HoneycombVView.ActionListener() {
            @Override
            public void ActionListener(int actionIndex) {
//                String actionStr = actionIndex >= 0 ? tittles[actionIndex] : "空白区域";
//                Toast.makeText(getActivity(), "点击了 " + actionStr, Toast.LENGTH_SHORT).show();

                switch (actionIndex) {
                    case 0:
                        Intent PLOT = new Intent(getActivity(),PlotActivity.class);
                        startActivity(PLOT);
                        break;
                    case 1:
                        Intent Cream= new Intent(getActivity(),cream.class);
                        startActivity(Cream);
                        break;
                    case 2:
                        Intent language= new Intent(getActivity(),languageActivity.class);
                        startActivity(language);
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
        return view;
    }


}
