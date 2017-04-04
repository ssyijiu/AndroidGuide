package com.ssyijiu.ceoquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ssyijiu.common.util.ToastUtil;

public class QuizActivity extends AppCompatActivity {

    @BindView(R.id.tv_question) TextView tvQuestion;
    @BindView(R.id.btn_true) Button btnTrue;
    @BindView(R.id.btn_false) Button btnFalse;
    @BindView(R.id.btn_next) Button btnNext;

    private Question[] questions = new Question[] {
        new Question(R.string.question_1, true),
        new Question(R.string.question_2, false),
        new Question(R.string.question_3, true),
        new Question(R.string.question_4, false),
        new Question(R.string.question_5, true),
    };
    private int questionIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        setTitle(R.string.ssyijiu);

        tvQuestion.setText(questions[questionIndex].text);
    }

    @OnClick({ R.id.btn_true, R.id.btn_false,R.id.btn_next })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_true:
                ToastUtil.show(R.string.true_toast);
                break;
            case R.id.btn_false:
                ToastUtil.show(R.string.false_toast);
                break;
            case R.id.btn_next:
                ToastUtil.show(R.string.false_toast);
                break;
        }
    }
}
