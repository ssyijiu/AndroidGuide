package com.ssyijiu.criminalintent;

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
import com.ssyijiu.common.util.DateUtil;
import com.ssyijiu.common.util.ToastUtil;
import com.ssyijiu.criminalintent.app.BaseFragment;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.bean.CrimeLab;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class CrimeFragment extends BaseFragment {

    @BindView(R.id.et_crime_title) EditText etCrimeTitle;
    @BindView(R.id.btn_crime_date) Button btnCrimeDate;
    @BindView(R.id.cb_crime_solved) CheckBox cbCrimeSolved;

    private Crime crime;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 强耦合，CrimeFragment 只能用于 CrimeActivity，失去了 Fragment 的复用性。
        UUID id = (UUID) context.getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        crime = CrimeLab.get().getCrime(id);
    }


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_crime;
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {

        etCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override public void afterTextChanged(Editable s) {}
        });

        etCrimeTitle.setText(crime.title);

        btnCrimeDate.setText(DateUtil.date2String(crime.date,
            new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.getDefault())));
        btnCrimeDate.setEnabled(false);

        cbCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.solved = isChecked;
                ToastUtil.show(String.valueOf(isChecked));
            }
        });
        cbCrimeSolved.setChecked(crime.solved);

    }

}
