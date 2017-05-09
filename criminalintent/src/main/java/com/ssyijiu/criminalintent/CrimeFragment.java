package com.ssyijiu.criminalintent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.ssyijiu.common.util.BitmapUtil;
import com.ssyijiu.common.util.DateUtil;
import com.ssyijiu.common.util.IOUtil;
import com.ssyijiu.common.util.IntentUtil;
import com.ssyijiu.common.util.PhoneUtil;
import com.ssyijiu.criminalintent.app.BaseFragment;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.db.CrimeDao;
import com.ssyijiu.criminalintent.util.AfterTextWatcher;
import com.ssyijiu.criminalintent.util.RealmUtil;
import io.realm.Realm;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CrimeFragment extends BaseFragment {

    private static final String ARG_CRIME_ID = "arg_crime_id";
    private static final String ARG_CRIME_POSITION = "arg_crime_position";

    private static final int REQUEST_CRIME_DATE = 2001;
    private static final int REQUEST_CONTACT = 2002;
    private static final int REQUEST_PHOTO = 2003;

    @BindView(R2.id.et_crime_title) EditText etCrimeTitle;
    @BindView(R2.id.btn_crime_date) Button btnCrimeDate;
    @BindView(R2.id.cb_crime_solved) CheckBox cbCrimeSolved;
    @BindView(R2.id.btn_choose_suspect) Button btnChooseSuspect;
    @BindView(R2.id.btn_crime_report) Button btnCrimeReport;
    @BindView(R2.id.btn_call_suspect) Button btnCallSuspect;
    @BindView(R2.id.img_crime_photo) ImageView imgCrimePhoto;
    @BindView(R2.id.ib_crime_photo) ImageButton ibCrimePhoto;

    private Crime crime;
    private File crimePhoto;
    private Intent photoIntent;


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override protected void parseArguments(Bundle arguments) {
        String crimeId = arguments.getString(ARG_CRIME_ID);
        crime = CrimeDao.instance().getCrime(crimeId);
        crimePhoto = CrimeDao.instance().getPhotoFile(crime);
    }


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_crime;
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {

        // init view
        etCrimeTitle.setText(crime.title);
        cbCrimeSolved.setChecked(crime.solved);

        if (crime.hasSuspect()) {
            btnChooseSuspect.setText(crime.suspect);
        }

        if (crime.hasSuspectPhoneNum()) {
            btnCallSuspect.setText(crime.suspectPhoneNum);
        } else {
            btnCallSuspect.setEnabled(false);
        }

        updateDate();
        updatePhotoView();
        initPhotoIntent();

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


    private void initPhotoIntent() {
        photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 设备没有相机时拍照按钮不可用
        boolean canTakePhoto = crimePhoto != null &&
            IntentUtil.checkIntentAvailable(photoIntent);
        imgCrimePhoto.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri;
            // 适配 Android 7.0
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                uri = Uri.fromFile(crimePhoto);
            } else {
                uri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".crime_images", crimePhoto);
            }
            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        }
    }


    @Override public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        // 选择日期
        if (requestCode == REQUEST_CRIME_DATE
            && resultCode == DatePickerFragment.REQUEST_CRIME_DATE) {
            RealmUtil.transaction(new Realm.Transaction() {
                @Override public void execute(Realm realm) {
                    crime.date = DatePickerFragment.resultDate(data);
                }
            });
            updateDate();

        } else if (requestCode == REQUEST_CONTACT) {
            // 选择联系人
            String[] userInfo = parseContactInfo(data);
            if (!TextUtils.isEmpty(userInfo[0])) {
                final String suspect = userInfo[0];

                RealmUtil.transaction(new Realm.Transaction() {
                    @Override public void execute(Realm realm) {
                        crime.suspect = suspect;
                    }
                });
                btnChooseSuspect.setText(suspect);
            }

            if (!TextUtils.isEmpty(userInfo[1])) {
                final String phoneNum = userInfo[1];
                RealmUtil.transaction(new Realm.Transaction() {
                    @Override public void execute(Realm realm) {
                        crime.suspectPhoneNum = phoneNum;
                    }
                });
                btnCallSuspect.setText(phoneNum);
                btnCallSuspect.setEnabled(true);
            }

        } else if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            // 拍照
            updatePhotoView();
        }
    }


    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_delete, menu);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_del_crime:
                CrimeDao.instance().deleteCrime(crime.id);
                context.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateDate() {
        btnCrimeDate.setText(crime.getDate());
    }


    private void updatePhotoView() {
        if (crimePhoto != null && crimePhoto.exists()) {
            Bitmap bitmap = BitmapUtil.getScaledBitmap(
                crimePhoto.getPath(), getActivity());
            imgCrimePhoto.setImageBitmap(bitmap);
        } else {
            imgCrimePhoto.setImageBitmap(null);
        }
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
        if (crime.hasSuspect()) {
            suspect = getString(R.string.crime_report_suspect, crime.suspect);
        } else {
            suspect = getString(R.string.crime_report_no_suspect);
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


    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void chooseSuspect() {
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
            ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pickContact, REQUEST_CONTACT);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CrimeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
            grantResults);
    }


    private String[] parseContactInfo(Intent data) {

        String[] userInfo = new String[2];

        if (data == null) {
            return userInfo;
        }

        Uri contactUri = data.getData();

        // 1. 查询联系人 id 和 name
        String[] queryFields = new String[] {
            ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME
        };

        // 2. 获取查询结果
        Cursor cursor = context.getContentResolver()
            .query(contactUri, queryFields, null, null, null);

        Cursor phoneCursor = null;

        try {
            // Double-check that you actually got results
            if (cursor == null || cursor.getCount() == 0) {
                return userInfo;
            }

            cursor.moveToFirst();  // 游标移动到第一行
            // 获取第0列数据，对应前面 queryFields#ContactsContract.Contacts._ID
            String contactId = cursor.getString(0);
            String name = cursor.getString(1);
            userInfo[0] = name;

            String phoneNumber;

            // 3. 使用 id 查询电话号码
            phoneCursor = context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // uri
                    // 查询的列
                    new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                    // 查询条件
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                    // 条件参数
                    new String[] { contactId },
                    // 是否排序
                    null);

            if (phoneCursor == null || phoneCursor.getCount() == 0) {
                return userInfo;
            }

            phoneCursor.moveToFirst();
            phoneNumber = phoneCursor.getString(0);
            userInfo[1] = phoneNumber;

            return userInfo;

        } finally {
            IOUtil.close(cursor);
            IOUtil.close(phoneCursor);
        }
    }


    @Nullable private String parseContactName(Intent data) {

        if (data == null) {
            return null;
        }

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
                return null;
            }

            // Pull out the first column of the first row of data -
            // that is your suspect's name.
            cursor.moveToFirst();
            return cursor.getString(0);
        } finally {
            IOUtil.close(cursor);
        }
    }


    @OnClick({ R.id.btn_crime_date, R.id.btn_choose_suspect, R.id.btn_crime_report,
                 R.id.btn_call_suspect, R.id.img_crime_photo,
                 R.id.ib_crime_photo })
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_crime_date:
                showDateDialog();
                break;
            case R.id.btn_choose_suspect:
                CrimeFragmentPermissionsDispatcher.chooseSuspectWithCheck(this);
                break;
            case R.id.btn_call_suspect:
                callSuspect();
                break;
            case R.id.btn_crime_report:
                report();
                break;
            case R.id.ib_crime_photo:
                takePhoto();
                break;
            case R.id.img_crime_photo:
                showPhoto();
                break;
            default:
        }
    }


    private void showPhoto() {}


    private void showDateDialog() {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(crime.date);
        // TimePickerFragment dialog = TimePickerFragment.newInstance(crime.date);
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_CRIME_DATE);
        dialog.show(manager, dialog.getClass().getSimpleName());

    }


    private void callSuspect() {
        PhoneUtil.toDial(context, btnCallSuspect.getText().toString().trim());
    }


    private void report() {
        IntentUtil.shareText(context, getCrimeReport(), getString(R.string.crime_report_subject),
            getString(R.string.send_report));
    }


    private void takePhoto() {
        startActivityForResult(photoIntent, REQUEST_PHOTO);
    }

}
