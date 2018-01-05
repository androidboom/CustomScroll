package com.lb.customscroll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyScrollView scrollView;
    private EditText search_et;
    private TextView tv_left;
    private TextView tv_right;
    private TextView center;
    private ImageView iv_left;
    private ImageView iv_right;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        scrollView = (MyScrollView) findViewById(R.id.scrollview);
        search_et = (EditText)findViewById(R.id.search_et);
        tv_left = (TextView)findViewById(R.id.tv_left);
        tv_right = (TextView)findViewById(R.id.tv_right);
        center = (TextView)findViewById(R.id.center);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView)findViewById(R.id.iv_right);

        search_et.post(new Runnable() {
            @Override
            public void run() {
                scrollView.setHeaderView(tv_left,tv_right,search_et,iv_left,iv_right,center);
            }
        });
    }

}
