package com.ssyijiu.criminalintent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CrimeFragment extends BaseFragment {

    @BindView(R.id.et_crime_title) EditText etCrimeTitle;

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
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
