package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.adapter.OilCardAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的油卡
 */
public class OilCardActivity extends BaseActivity {

    @Bind(R.id.btn_activityOilCard_addOilCard)
    Button btn_activityOilCard_addOilCard;

    @Bind(R.id.recyclerView_activityOilCard_list)
    RecyclerView recyclerView_activityOilCard_list;

    private List<OilCardAdapter.OilCardEntity> oilCardEntityList = new ArrayList<>();
    private OilCardAdapter oilCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_card);
        ButterKnife.bind(this);
        initTitle();
        tvTitle.setText("我的油卡");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, GridLayoutManager.VERTICAL,false);

    }

    private void getOilCards() {

    }
}
