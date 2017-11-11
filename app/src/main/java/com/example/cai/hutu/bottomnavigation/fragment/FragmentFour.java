package com.example.cai.hutu.bottomnavigation.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cai.hutu.R;
import com.example.cai.hutu.autoselect.autoSelect;
import com.example.cai.hutu.bottomnavigation.tools.Constants;
import com.example.cai.hutu.tool.SysApplication;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by dell on 2017/6/7.
 */

public class FragmentFour extends Fragment {
    private Button  exit;
    private TextView  textview;
    private Dialog dialog;//弹窗
    private Button dialog_button12;//弹窗button
    private  Button dialog_cancol;//弹窗取消

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person,container,false);
        SysApplication.getInstance().addActivity(getActivity());

        exit=(Button)view.findViewById(R.id.person_bt5);

        dialog = new Dialog(getActivity(),R.style.Dialog);
        dialog.setContentView(R.layout.dialog_instructions);
        dialog.setTitle(null);
        textview=(TextView) dialog.findViewById(R.id.dialog_text001);
        textview.setText(R.string.remind);
        exit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                dialog.show();
            }});
        dialog_button12= (Button) dialog.findViewById(R.id.dialog_button12);
        dialog_button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysApplication.getInstance().exit();
            }
        });
        dialog_cancol=(Button)dialog.findViewById(R.id.dialog_cancol);
        dialog_cancol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();//取消窗口
            }
        });
        return view;
    }
}
