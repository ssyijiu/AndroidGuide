package com.ssyijiu.ceoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.Locale;

public class CheatActivity extends BaseActivity {

    @BindView(R.id.tv_tip) TextView tvTip;
    @BindView(R.id.btn_show_answer) Button btnShowAnswer;
    @BindView(R.id.tv_answer) TextView tvAnswer;
    @BindView(R.id.tv_build_version) TextView tvBuildVersion;

    private static final String EXTRA_ANSWER_IS_TRUE = "extra_answer_is_true";
    private static final String EXTRA_ANSWER_SHOW = "extra_answer_show";
    private static final String IS_CHEAT = "is_cheat";

    private boolean answer;
    private boolean isCheat;


    @Override protected int getContentView() {
        return R.layout.activity_cheat;
    }


    @Override protected void parseIntent(Intent intent) {
        answer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            tvAnswer.setText(String.valueOf(answer));
            isCheat = savedInstanceState.getBoolean(IS_CHEAT);
            setCheatResult(isCheat);
        }

        // 手机型号、Android 版本、API 级别
        tvBuildVersion.setText(
            String.format(Locale.getDefault(), "%s: Android %s API %d", Build.MODEL,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT));
    }


    @OnClick(R.id.btn_show_answer) public void onClick() {
        tvAnswer.setText(String.valueOf(answer));
        isCheat = true;
        setCheatResult(true);
    }


    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_CHEAT, isCheat);
    }


    private void setCheatResult(boolean isCheat) {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_ANSWER_SHOW, isCheat));
    }


    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOW, false);
    }


    public static void startForResult(Activity context, int requestCode, boolean answer) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answer);
        context.startActivityForResult(intent, requestCode);
    }


    public static Intent newIntent(Context context, boolean answer) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answer);
        return intent;
    }

}
