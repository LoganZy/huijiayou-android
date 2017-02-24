package com.wanglibao.huijiayou.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.fragment.GasFragment;
import com.wanglibao.huijiayou.fragment.HomeFragment;
import com.wanglibao.huijiayou.fragment.UserFragment;
import com.wanglibao.huijiayou.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.fl_mainActivity_fragmentShell)
    FrameLayout fl_mainActivity_fragmentShell;

    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar bottom_navigation_bar;

    HomeFragment homeFragment;
    UserFragment userFragment;
    GasFragment gasFragment;

    FragmentManager fragmentManager;

    private static final int requestCode_login = 0;

    private long exitTime = 0; // 按返回键的间隔
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
        gasFragment = new GasFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_mainActivity_fragmentShell,homeFragment)
                .add(R.id.fl_mainActivity_fragmentShell,userFragment)
                .add(R.id.fl_mainActivity_fragmentShell,gasFragment)
                .commit();

        bottom_navigation_bar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"会加油"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"账户"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"更多")).initialise();


        bottom_navigation_bar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        fragmentManager.beginTransaction().attach(homeFragment).detach(userFragment).detach(gasFragment).commit();
                        break;
                    case 1:
                        fragmentManager.beginTransaction().attach(userFragment).detach(homeFragment).detach(gasFragment).commit();
                        break;
                    case 2:
                        fragmentManager.beginTransaction().attach(gasFragment).detach(userFragment).detach(homeFragment).commit();
                        break;
                }
            }
            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
        bottom_navigation_bar.selectTab(0);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.createNormalToast(this, "再按一次退出应用");
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                myApplication.exit();
            }

        }
        return super.onKeyDown(keyCode, event);
    }


}
