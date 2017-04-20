package com.ssyijiu.criminalintent;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.ssyijiu.common.util.ToastUtil;

public class CrimeFragment extends BaseFragment {

    @BindView(R.id.et_crime_title) EditText etCrimeTitle;
    @BindView(R.id.btn_crime_date) Button btnCrimeDate;
    @BindView(R.id.cb_crime_solved) CheckBox cbCrimeSolved;

    private Crime crime;
    private Unbinder unbinder;


    public CrimeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        crime = new Crime();
    }

    @Override protected int getFragLayoutId() {
        return R.layout.fragment_crime;
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, rootView);
        etCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnCrimeDate.setText(crime.date.toString());
        btnCrimeDate.setEnabled(false);

        cbCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.solved = isChecked;
                ToastUtil.show(String.valueOf(isChecked));
            }
        });
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
