package com.ssyijiu.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.ssyijiu.criminalintent.util.AfterTextWatcher;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class CrimeFragment extends BaseFragment {

    private static final String ARG_CRIME_ID = "arg_crime_id";
    private static final String ARG_CRIME_POSITION = "arg_crime_position";
    private static final String RESULT_CRIME_POSITION = "result_crime_position";

    public static final int RESULT_CODE_CRIME_POSITION = 1001;

    @BindView(R.id.et_crime_title) EditText etCrimeTitle;
    @BindView(R.id.btn_crime_date) Button btnCrimeDate;
    @BindView(R.id.cb_crime_solved) CheckBox cbCrimeSolved;

    private Crime crime;
    public UUID id;


    @Override protected void parseArguments(Bundle arguments) {
        UUID crimeId = (UUID) arguments.getSerializable(ARG_CRIME_ID);
        int crimePosition = arguments.getInt(ARG_CRIME_POSITION);

        crime = CrimeLab.get().getCrime(crimeId);
        context.setResult(RESULT_CODE_CRIME_POSITION,
            new Intent().putExtra(RESULT_CRIME_POSITION, crimePosition));
    }


    public static Fragment newInstance(UUID id, int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, id);
        args.putSerializable(ARG_CRIME_POSITION, position);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static int resultPosition(Intent intent) {
        if (intent != null) {
            return intent.getIntExtra(RESULT_CRIME_POSITION, 0);
        }
        return 0;
    }


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_crime;
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {

        // init view
        etCrimeTitle.setText(crime.title);
        btnCrimeDate.setText(DateUtil.date2String(crime.date,
            new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.getDefault())));
        btnCrimeDate.setEnabled(false);
        cbCrimeSolved.setChecked(crime.solved);

        // update view
        etCrimeTitle.addTextChangedListener(new AfterTextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                crime.title = s.toString();
            }
        });

        cbCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.solved = isChecked;
            }
        });

    }

}
