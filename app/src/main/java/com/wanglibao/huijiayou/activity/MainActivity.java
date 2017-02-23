package com.wanglibao.huijiayou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.wanglibao.huijiayou.R;

import java.util.ArrayList;
import java.util.List;

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

        List<String> datas = new ArrayList<>();
        datas.add("1");
        datas.add("2");
        datas.add("3");
        datas.add("4");

        List<String> bendi = new ArrayList<>();
        bendi.add("2");
        bendi.add("4");

        List<String> xinde = new ArrayList<>();

        for (String string:datas){
            for (int i = 0; i < bendi.size(); i++){
                if (string.equals(bendi.get(i))){
                    datas.remove(string);
                    break;
                }
                if (i == bendi.size()){
                    xinde.add(string);
                }
            }
        }

        String value = "";
        for (String string:xinde){
            value += string;
        }
        Toast.makeText(this,value,Toast.LENGTH_LONG);
    }
}
