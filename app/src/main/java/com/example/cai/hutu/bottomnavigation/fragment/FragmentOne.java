package com.example.cai.hutu.bottomnavigation.fragment;

import android.os.Bundle;

import com.example.cai.hutu.bottomnavigation.tools.Constants;

/**
 * Created by dell on 2017/6/7.
 */

public class FragmentOne extends BaseFragment{
    public static FragmentOne newInstance(String s){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ARGS,s);
        FragmentOne fragment = new FragmentOne();
        fragment.setArguments(bundle);
        return fragment;
    }
}
