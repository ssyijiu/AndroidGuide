package com.ssyijiu.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ssyijiu.criminalintent.app.BaseFragment;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.bean.CrimeLab;
import java.util.List;

/**
 * Created by ssyijiu on 2017/4/21.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class CrimeListFragment extends BaseFragment {

    private static final int REQUEST_CODE_CRIME = 0;
    @BindView(R.id.rv_crime) RecyclerView crimeRecycler;
    private CrimeAdapter adapter;


    @Override protected int getFragLayoutId() {
        return R.layout.fragment_crime_list;
    }


    @Override protected void initViewAndData(View rootView, Bundle savedInstanceState) {
        crimeRecycler.setLayoutManager(new LinearLayoutManager(context));
        updateUI();
    }


    private void updateUI() {
        if (adapter == null) {
            adapter = new CrimeAdapter(CrimeLab.get().getCrimeList());
            crimeRecycler.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    private void updateUiItem(int position) {
        if(adapter == null) {
            adapter = new CrimeAdapter(CrimeLab.get().getCrimeList());
            crimeRecycler.setAdapter(adapter);
        } else {
            adapter.notifyItemRangeChanged(position - 1,3);
        }
    }


    private class CrimeAdapter extends RecyclerView.Adapter<CrimeViewHolder> {
        List<Crime> crimeList;


        CrimeAdapter(List<Crime> crimeList) {
            this.crimeList = crimeList;
        }


        @Override
        public CrimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater
                .inflate(R.layout.item_crime, parent, false);

            return new CrimeViewHolder(view);
        }


        @Override public void onBindViewHolder(CrimeViewHolder holder, int position) {
            holder.bindCrime(crimeList.get(position), position);
        }


        @Override public int getItemCount() {
            return crimeList.size();
        }
    }


    class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_crime_tv_title) TextView tvTitle;
        @BindView(R.id.item_crime_cb_solved) CheckBox cbSolved;
        @BindView(R.id.item_crime_tv_date) TextView tvDate;

        private Crime crime;
        private int position;


        CrimeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        void bindCrime(Crime crime, int position) {
            this.crime = crime;
            this.position = position;
            tvTitle.setText(crime.title);
            cbSolved.setChecked(crime.solved);
            tvDate.setText(crime.getDate());
        }


        @Override public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(context, crime.id, position);
            startActivityForResult(intent,REQUEST_CODE_CRIME);
        }
    }


    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CRIME
            && resultCode == CrimeFragment.RESULT_CODE_CRIME_POSITION) {
            int position = CrimeFragment.resultPosition(data);
            updateUiItem(position);

        }
    }
}
