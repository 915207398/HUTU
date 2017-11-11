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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.cai.hutu.R;
import com.example.cai.hutu.bottomnavigation.activity.Navigation;
import com.example.cai.hutu.tool.SysApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by dell on 2017/6/12.
 */

public class LoginActivity extends Activity{
    private EditText UserName = null;
    private EditText Password = null;
    Button btnLogIn = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        SysApplication.getInstance().addActivity(this);
        UserName = (EditText)findViewById(R.id.login_edit1);
        Password = (EditText)findViewById(R.id.login_edit2);
        btnLogIn = (Button)findViewById(R.id.login_bt1);
        btnLogIn.setOnClickListener(new btnLogInOnClicker());

    }
    public class btnLogInOnClicker implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //check the dat is right
//			String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-z0-9A-Z]+(?:[-.][a-z0-9A-Z]+)*\\.[a-zA-Z]+\\s*$";
//			Pattern p = Pattern.compile(strPattern);
//			java.util.regex.Matcher m = p.matcher(UserName.getText().toString());
//			if(!m.matches()){
//				Toast.makeText(LogInActivity.this, "Please input correct Email address or password!", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			if(!Password.getText().toString().equals("123456789")){
//				Toast.makeText(LogInActivity.this, "Please input correct Email address or password!", Toast.LENGTH_SHORT).show();
//				return;
//			}
            Intent intent = new Intent(LoginActivity.this,
                    Navigation.class);
            startActivity(intent);
        }


    }
}
