package com.ssyijiu.criminalintent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import butterknife.BindView;
import com.ssyijiu.common.util.DateUtil;
import com.ssyijiu.common.util.IOUtil;
import com.ssyijiu.common.util.IntentUtil;
import com.ssyijiu.common.util.ToastUtil;
import com.ssyijiu.criminalintent.app.BaseFragment;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.bean.CrimeLab;
import com.ssyijiu.criminalintent.util.AfterTextWatcher;
import com.ssyijiu.criminalintent.util.RealmUtil;
import io.realm.Realm;
import java.text.SimpleDateFormat;
import java.util.Locale;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CrimeFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_CRIME_ID = "arg_crime_id";
    private static final String ARG_CRIME_POSITION = "arg_crime_position";

    private static final int REQUEST_CRIME_DATE = 2001;
    private static final int REQUEST_CONTACT = 2002;

    @BindView(R2.id.et_crime_title) EditText etCrimeTitle;
    @BindView(R2.id.btn_crime_date) Button btnCrimeDate;
    @BindView(R2.id.cb_crime_solved) CheckBox cbCrimeSolved;
    @BindView(R2.id.btn_choose_suspect) Button btnChooseSuspect;
    @BindView(R2.id.btn_crime_report) Button btnCrimeReport;

    private Crime crime;


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override protected void parseArguments(Bundle arguments) {
        String crimeId = arguments.getString(ARG_CRIME_ID);
        crime = CrimeLab.instance().getCrime(crimeId);
    }


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_crime;
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {

        // init view
        etCrimeTitle.setText(crime.title);
        cbCrimeSolved.setChecked(crime.solved);

        btnCrimeDate.setOnClickListener(this);
        btnChooseSuspect.setOnClickListener(this);
        btnCrimeReport.setOnClickListener(this);

        if (crime.isHasSuspect()) {
            btnChooseSuspect.setText(crime.suspect);
        }

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


    @Override public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        // 去选择日期
        if (requestCode == REQUEST_CRIME_DATE
            && resultCode == DatePickerFragment.REQUEST_CRIME_DATE) {
            RealmUtil.transaction(new Realm.Transaction() {
                @Override public void execute(Realm realm) {
                    crime.date = DatePickerFragment.resultDate(data);
                }
            });
            updateDate();

        } else if (requestCode == REQUEST_CONTACT && data != null) { // 选择联系人
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for.
            String[] queryFields = new String[] {
                ContactsContract.Contacts.DISPLAY_NAME
            };
            // Perform your query - the contactUri is like a "where"
            // clause here
            Cursor cursor = context.getContentResolver()
                .query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that you actually got results
                if (cursor == null || cursor.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                cursor.moveToFirst();
                final String suspect = cursor.getString(0);

                RealmUtil.transaction(new Realm.Transaction() {
                    @Override public void execute(Realm realm) {
                        crime.suspect = suspect;
                    }
                });
                btnChooseSuspect.setText(suspect);
            } finally {
                IOUtil.close(cursor);
            }
        }
    }


    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_delete, menu);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_del_crime:
                CrimeLab.instance().deleteCrime(crime.id);
                context.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateDate() {
        btnCrimeDate.setText(crime.getDate());
    }


    private String getCrimeReport() {
        String solvedString;
        if (crime.solved) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);

        }

        String date = DateUtil.date2String(crime.date, new SimpleDateFormat("EEE, MMM dd",
            Locale.getDefault()));

        String suspect;
        if (crime.isHasSuspect()) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, crime.suspect);
        }

        return getString(R.string.crime_report_detailed,
            crime.title, date, solvedString, suspect);
    }


    public static Fragment newInstance(String id, int position) {
        Bundle args = new Bundle();
        args.putString(ARG_CRIME_ID, id);
        args.putInt(ARG_CRIME_POSITION, position);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_crime_date:
                showDateDialog();
                break;
            case R.id.btn_choose_suspect:
                CrimeFragmentPermissionsDispatcher.chooseSuspectWithCheck(this);
                break;
            case R.id.btn_crime_report:
                report();
                break;
            default:
        }
    }


    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void chooseSuspect() {
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
            ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pickContact, REQUEST_CONTACT);
    }


    private void report() {
        IntentUtil.shareText(context, getCrimeReport(), getString(R.string.crime_report_subject),
            getString(R.string.send_report));
    }


    private void showDateDialog() {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(crime.date);
        // TimePickerFragment dialog = TimePickerFragment.newInstance(crime.date);
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_CRIME_DATE);
        dialog.show(manager, dialog.getClass().getSimpleName());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CrimeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
            grantResults);
    }
}
