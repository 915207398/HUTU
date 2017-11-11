package com.example.cai.hutu.bottomnavigation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.cai.hutu.R;
import com.example.cai.hutu.bottomnavigation.fragment.FragmentFour;
import com.example.cai.hutu.bottomnavigation.fragment.FragmentOne;
import com.example.cai.hutu.bottomnavigation.fragment.FragmentThree;
import com.example.cai.hutu.bottomnavigation.fragment.FragmentTwo;
import com.example.cai.hutu.bottomnavigation.tools.Constants;
import com.example.cai.hutu.tool.SysApplication;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import static com.example.cai.hutu.bottomnavigation.tools.Constants.mBottomNavigationBar;

/**
 * Created by dell on 2017/6/7.
 */

public class Navigation  extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener  {
   // private BottomNavigationBar mBottomNavigationBar;
    private FragmentOne mFragmentOne;
    private FragmentTwo mFragmentTwo;
    private FragmentThree mFragmentThree;
    private FragmentFour mFragmentFour;

    public Navigation() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naigation);
        SysApplication.getInstance().addActivity(this);
        mBottomNavigationBar=(BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);

        /*** the setting for BadgeItem ***/

     /*   BadgeItem badgeItem = new BadgeItem();
        badgeItem.setHideOnSelect(false)
                .setText("10")
                .setBackgroundColorResource(R.color.orange)
                .setBorderWidth(0);
*/
        /*** the setting for BottomNavigationBar ***/
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
//        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
//        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
//        mBottomNavigationBar.setBarBackgroundColor(R.color.blue);//set background color for navigation bar
//        mBottomNavigationBar.setInActiveColor(R.color.white);//unSelected icon color
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.icon_one, R.string.tab_one).setActiveColorResource(R.color.green))
                .addItem(new BottomNavigationItem(R.mipmap.icon_two, R.string.tab_two).setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.icon_three, R.string.tab_three).setActiveColorResource(R.color.lime))
                .addItem(new BottomNavigationItem(R.mipmap.icon_four, R.string.tab_four))
                .setFirstSelectedPosition(0)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }

    /**
     * set the default fragment
     */
    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mFragmentOne = FragmentOne.newInstance("welcome");
        transaction.replace(R.id.ll_content, mFragmentOne).commit();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (mFragmentOne == null) {
                    mFragmentOne = FragmentOne.newInstance("welcome");
                }
                transaction.replace(R.id.ll_content, mFragmentOne);
                break;
            case 1:
                if (mFragmentTwo == null) {

                }
                mFragmentTwo=new FragmentTwo();
                transaction
                        .addToBackStack(null).replace(R.id.ll_content, mFragmentTwo);
                break;
            case 2:
                if (mFragmentThree == null) {
                }

                mFragmentThree=new FragmentThree();
                transaction.replace(R.id.ll_content, mFragmentThree);
                break;
            case 3:

                mFragmentFour=new FragmentFour();
                transaction.replace(R.id.ll_content, mFragmentFour);
                break;
            default:
                if (mFragmentOne == null) {
                    mFragmentOne = FragmentOne.newInstance("First Fragment");
                }
                transaction.replace(R.id.ll_content, mFragmentOne);
                break;
        }
        transaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
