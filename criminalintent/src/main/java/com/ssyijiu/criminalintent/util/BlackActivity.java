package com.ssyijiu.criminalintent.util;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.ssyijiu.common.util.ToastUtil;
import com.ssyijiu.criminalintent.R;

public class BlackActivity extends AppCompatActivity
    implements BlankFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);

        BlankFragment fragment = BlankFragment.newInstance("param1", "param2");
        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container, fragment).commit();

    }


    @Override public void onFragmentInteraction(Uri uri) {
        ToastUtil.show("onFragmentInteraction");
    }
}
