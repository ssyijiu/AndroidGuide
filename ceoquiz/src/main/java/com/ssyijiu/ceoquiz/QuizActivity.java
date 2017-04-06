package com.ssyijiu.ceoquiz;

import android.os.Bundle;
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

    private final String QUESTION_INDEX = "currentQuestionIndex";

    private Question[] questions = new Question[] {
        new Question(R.string.question_1, true),
        new Question(R.string.question_2, false),
        new Question(R.string.question_3, true),
        new Question(R.string.question_4, false),
        new Question(R.string.question_5, true),
    };

    /** 当前问题索引 */
    private int currentQuestionIndex = 0;

    @Override protected int getContentView() {
        return R.layout.activity_quiz;
    }


    @Override protected void initViewAndData(Bundle savedInstanceState) {

        // 恢复问题的索引
        if(null != savedInstanceState) {
            currentQuestionIndex = savedInstanceState.getInt(QUESTION_INDEX);
        }
        tvQuestion.setText(questions[currentQuestionIndex].text);

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


    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存问题索引
        outState.putInt(QUESTION_INDEX, currentQuestionIndex);
    }

    /** 检查答案是否正确 */
    public void checkAnswer(boolean answer) {
        if (answer == questions[currentQuestionIndex].answer) {
            ToastUtil.show(R.string.true_toast);
        } else {
            ToastUtil.show(R.string.false_toast);
        }
    }

    /** 显示下一个问题 */
    public void nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.length;
        tvQuestion.setText(questions[currentQuestionIndex].text);
    }

    /** 显示上一个问题 */
    public void prevQuestion() {
        currentQuestionIndex = currentQuestionIndex - 1;
        if(currentQuestionIndex < 0) {
            currentQuestionIndex = 0;
        } else {
            tvQuestion.setText(questions[currentQuestionIndex].text);
        }
    }

}
