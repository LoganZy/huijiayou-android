package com.wanglibao.huijiayou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.wanglibao.huijiayou.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fl_mainActivity_fragmentShell)
    FrameLayout fl_mainActivity_fragmentShell;

    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar bottom_navigation_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        bottom_navigation_bar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"会加油"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"账户"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"更多")).initialise();



    }
}
