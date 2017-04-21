package com.ssyijiu.criminalintent;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ssyijiu.common.util.ToastUtil;
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
        adapter = new CrimeAdapter(CrimeLab.get().getCrimeList());
        crimeRecycler.setAdapter(adapter);
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
            holder.bindCrime(crimeList.get(position));
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

        CrimeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindCrime(Crime crime) {
            this.crime = crime;
            tvTitle.setText(crime.title);
            cbSolved.setChecked(crime.solved);
            tvDate.setText(crime.date.toString());
        }

        @Override public void onClick(View v) {
            CrimeActivity.start(context,crime.id);
        }
    }
}
