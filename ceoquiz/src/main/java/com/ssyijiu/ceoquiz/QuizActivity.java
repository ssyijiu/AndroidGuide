package com.ssyijiu.ceoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.ssyijiu.common.util.ToastUtil;

public class QuizActivity extends BaseActivity {

    private static final int REQUEST_CODE_CHEAT = 0x01;
    private static final String QUESTION_INDEX = "currentQuestionIndex";
    private static final String IS_CHEAT = "ischeat";

    @BindView(R.id.tv_question) TextView tvQuestion;
    @BindView(R.id.btn_true) Button btnTrue;
    @BindView(R.id.btn_false) Button btnFalse;
    @BindView(R.id.btn_next) Button btnNext;

    @BindView(R.id.btn_prev) Button btnPrev;
    @BindView(R.id.btn_cheat) Button btnCheat;

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

        if (null != savedInstanceState) {
            // 恢复问题索引
            currentQuestionIndex = savedInstanceState.getInt(QUESTION_INDEX);
            // 恢复是否作弊
            questions = (Question[]) savedInstanceState.getSerializable(IS_CHEAT);
        }
        if(questions != null) {
            tvQuestion.setText(questions[currentQuestionIndex].text);
        }
    }


    @OnClick({ R.id.btn_true, R.id.btn_false, R.id.btn_next, R.id.btn_prev, R.id.tv_question,
                 R.id.btn_cheat })
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
            case R.id.btn_cheat:
                goCheat();
                break;
        }
    }


    /** 作弊 */
    private void goCheat() {
        boolean answer = questions[currentQuestionIndex].answer;
        // Intent intent = CheatActivity.newIntent(context, answer);
        // startActivityForResult(intent, REQUEST_CODE_CHEAT);

        CheatActivity.startForResult(this, REQUEST_CODE_CHEAT, answer);
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data != null) {
                questions[currentQuestionIndex].isCheat = CheatActivity.wasAnswerShown(data);
            }
        }

    }


    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存问题索引
        outState.putInt(QUESTION_INDEX, currentQuestionIndex);
        // 保存是否作弊
        outState.putSerializable(IS_CHEAT, questions);
    }


    /** 检查答案是否正确 */
    public void checkAnswer(boolean answer) {

        int messageResId;
        if (questions[currentQuestionIndex].isCheat) {
            messageResId = R.string.cheat_toast;
        } else if (answer == questions[currentQuestionIndex].answer) {
            messageResId = R.string.true_toast;
        } else {
            messageResId = R.string.false_toast;
        }

        ToastUtil.show(messageResId);
    }


    /** 显示下一个问题 */
    public void nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.length;
        tvQuestion.setText(questions[currentQuestionIndex].text);
    }


    /** 显示上一个问题 */
    public void prevQuestion() {
        currentQuestionIndex = currentQuestionIndex - 1;
        if (currentQuestionIndex < 0) {
            currentQuestionIndex = 0;
        } else {
            tvQuestion.setText(questions[currentQuestionIndex].text);
        }
    }

}
