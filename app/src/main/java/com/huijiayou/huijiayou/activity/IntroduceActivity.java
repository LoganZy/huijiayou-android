package com.huijiayou.huijiayou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

//介绍页
public class IntroduceActivity extends Activity {

    @Bind(R.id.viewPager_activityIntroduce_view)
    ViewPager viewPager_activityIntroduce_view;

    @Bind(R.id.ll_activityIntroduce_spots)
    LinearLayout ll_activityIntroduce_spots;

    @Bind(R.id.view_activityIntroduce_spot1)
    View view_activityIntroduce_spot1;

    @Bind(R.id.view_activityIntroduce_spot2)
    View view_activityIntroduce_spot2;

    @Bind(R.id.btn_activityIntroduce_open)
    Button btn_activityIntroduce_open;

    ArrayList<ImageView> imageViewArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        ButterKnife.bind(this);
        ((MyApplication) getApplication()).addActivity(this);

        initImageView();
        viewPager_activityIntroduce_view.setAdapter(new IntroduceViewPager());
        viewPager_activityIntroduce_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    switch (position){
                        case 0:
                            ll_activityIntroduce_spots.setVisibility(View.VISIBLE);
                            btn_activityIntroduce_open.setVisibility(View.GONE);
                            view_activityIntroduce_spot1.setBackground(getResources().getDrawable(R.drawable.shape_spot_h));
                            view_activityIntroduce_spot2.setBackground(getResources().getDrawable(R.drawable.shape_spot_n));
                            break;
                        case 1:
                            ll_activityIntroduce_spots.setVisibility(View.VISIBLE);
                            btn_activityIntroduce_open.setVisibility(View.GONE);
                            view_activityIntroduce_spot1.setBackground(getResources().getDrawable(R.drawable.shape_spot_n));
                            view_activityIntroduce_spot2.setBackground(getResources().getDrawable(R.drawable.shape_spot_h));
                            break;
                        case 2:
                            ll_activityIntroduce_spots.setVisibility(View.GONE);
                            btn_activityIntroduce_open.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void openMainActivity(View view){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void initImageView(){
        imageViewArrayList = new ArrayList<>();
        ImageView imageView1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_introduce_page1, null);
        ImageView imageView2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_introduce_page2, null);
        ImageView imageView3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_introduce_page3, null);
        imageViewArrayList.add(imageView1);
        imageViewArrayList.add(imageView2);
        imageViewArrayList.add(imageView3);
    }

    class IntroduceViewPager extends PagerAdapter{

        @Override
        public int getCount() {
            return imageViewArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewArrayList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViewArrayList.get(position));
            return imageViewArrayList.get(position);
        }
    }
}
