package com.ssyijiu.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import butterknife.BindView;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.criminalintent.app.BaseFragment;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.bean.CrimeLab;
import com.ssyijiu.criminalintent.util.AfterTextWatcher;
import com.ssyijiu.criminalintent.util.RealmUtil;
import io.realm.Realm;
import java.util.UUID;

import static android.R.attr.id;

public class CrimeFragment extends BaseFragment {

    private static final String ARG_CRIME_ID = "arg_crime_id";
    private static final String ARG_CRIME_POSITION = "arg_crime_position";
    private static final String RESULT_CRIME_POSITION = "result_crime_position";

    public static final int RESULT_CODE_CRIME_POSITION = 1001;
    private static final int REQUEST_CRIME_DATE = 2001;

    @BindView(R.id.et_crime_title) EditText etCrimeTitle;
    @BindView(R.id.btn_crime_date) Button btnCrimeDate;
    @BindView(R.id.cb_crime_solved) CheckBox cbCrimeSolved;

    private Crime crime;


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override protected void parseArguments(Bundle arguments) {
        String crimeId = arguments.getString(ARG_CRIME_ID);
        int crimePosition = arguments.getInt(ARG_CRIME_POSITION);

        crime = CrimeLab.instance().getCrime(crimeId);
        context.setResult(RESULT_CODE_CRIME_POSITION,
            new Intent().putExtra(RESULT_CRIME_POSITION, crimePosition));
    }


    public static Fragment newInstance(String id, int position) {
        Bundle args = new Bundle();
        args.putString(ARG_CRIME_ID, id);
        args.putInt(ARG_CRIME_POSITION, position);
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
        cbCrimeSolved.setChecked(crime.solved);
        btnCrimeDate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(crime.date);
                // TimePickerFragment dialog = TimePickerFragment.newInstance(crime.date);
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_CRIME_DATE);
                dialog.show(manager, dialog.getClass().getSimpleName());
            }
        });
        updateDate();

        // update view
        etCrimeTitle.addTextChangedListener(new AfterTextWatcher() {
            @Override public void afterTextChanged(final Editable s) {
                RealmUtil.transaction(new Realm.Transaction() {
                    @Override public void execute(Realm realm) {
                        crime.title = s.toString();
                    }
                });
            }
        });

        cbCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                RealmUtil.transaction(new Realm.Transaction() {
                    @Override public void execute(Realm realm) {
                        crime.solved = isChecked;
                    }
                });
            }
        });

    }


    @Override public void onPause() {
        super.onPause();
        if (TextUtils.isEmpty(crime.title)) {
            CrimeLab.instance().deleteCrime(crime);
        }
    }


    @Override public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQUEST_CRIME_DATE
            && resultCode == DatePickerFragment.REQUEST_CRIME_DATE) {
            RealmUtil.transaction(new Realm.Transaction() {
                @Override public void execute(Realm realm) {
                    crime.date = DatePickerFragment.resultDate(data);
                }
            });
            updateDate();
        }
    }


    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_delete, menu);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_del_crime:
                RealmUtil.transaction(new Realm.Transaction() {
                    @Override public void execute(Realm realm) {
                        crime.title = null;
                        // if title is null , crime will be delete
                    }
                });
                context.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateDate() {
        btnCrimeDate.setText(crime.getDate());
    }

}
