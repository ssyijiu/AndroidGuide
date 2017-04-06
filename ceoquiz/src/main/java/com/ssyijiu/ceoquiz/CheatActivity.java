package com.ssyijiu.ceoquiz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;

public class CheatActivity extends BaseActivity {

    @BindView(R.id.tv_tip) TextView tvTip;
    @BindView(R.id.btn_show_answer) Button btnShowAnswer;
    @BindView(R.id.tv_answer) TextView tvAnswer;


    @Override protected int getContentView() {
        return R.layout.activity_cheat;
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {

    }


    @OnClick(R.id.btn_show_answer) public void onViewClicked() {

    }


}
