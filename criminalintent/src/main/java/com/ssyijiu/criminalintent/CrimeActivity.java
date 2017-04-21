package com.ssyijiu.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.ssyijiu.criminalintent.app.SimpleFragmentActivity;
import java.util.UUID;

public class CrimeActivity extends SimpleFragmentActivity {

    public static final String EXTRA_CRIME_ID = "extra_crime_id";


    @Override protected void initViewAndData(Bundle savedInstanceState) {

    }


    @Override protected Fragment createFragment() {
        return new CrimeFragment();
    }

    public static void start(Context context, UUID crimeId) {
        Intent intent = new Intent(context,CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        context.startActivity(intent);
    }
}
