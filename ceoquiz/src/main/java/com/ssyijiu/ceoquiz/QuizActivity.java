package com.ssyijiu.ceoquiz;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.ssyijiu.common.util.ToastUtil;

public class QuizActivity extends BaseActivity {

    @BindView(R.id.tv_question) TextView tvQuestion;
    @BindView(R.id.btn_true) Button btnTrue;
    @BindView(R.id.btn_false) Button btnFalse;
    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.btn_prev) Button btnPrev;

    private Question[] questions = new Question[] {
        new Question(R.string.question_1, true),
        new Question(R.string.question_2, false),
        new Question(R.string.question_3, true),
        new Question(R.string.question_4, false),
        new Question(R.string.question_5, true),
    };
    private int questionIndex = 0;

    @Override protected int getLayoutResId() {
        return R.layout.activity_quiz;
    }


    @Override protected void initViewAndData() {
        nextQuestion();
    }


    @OnClick({ R.id.btn_true, R.id.btn_false, R.id.btn_next, R.id.btn_prev,R.id.tv_question })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_question:
                nextQuestion();
                break;
            case R.id.btn_true:
                checkAnswer(true);
                break;
            case R.id.btn_false:
                checkAnswer(false);
                break;
            case R.id.btn_next:
                nextQuestion();
                break;
            case R.id.btn_prev:
                prevQuestion();
                break;
        }
    }


    public void checkAnswer(boolean answer) {
        if (answer == questions[questionIndex].answer) {
            ToastUtil.show(R.string.true_toast);
        } else {
            ToastUtil.show(R.string.false_toast);
        }
    }


    public void nextQuestion() {
        questionIndex = (questionIndex + 1) % questions.length;
        tvQuestion.setText(questions[questionIndex].text);
    }

    public void prevQuestion() {
        questionIndex = questionIndex - 1;
        if(questionIndex < 0) {
            questionIndex = 0;
        } else {
            tvQuestion.setText(questions[questionIndex].text);
        }
    }

}
