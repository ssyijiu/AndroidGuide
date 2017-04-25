package com.ssyijiu.criminalintent.recycleradapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ssyijiu.criminalintent.CrimePagerActivity;
import com.ssyijiu.criminalintent.R;
import com.ssyijiu.criminalintent.bean.Crime;
import java.util.List;

import static com.ssyijiu.criminalintent.CrimeListFragment.REQUEST_CODE_CRIME;

/**
 * Created by ssyijiu on 2017/4/25.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.CrimeViewHolder> {
    private List<Crime> crimeList;
    private Fragment fragment;
    private Context context;


    public CrimeAdapter(List<Crime> crimeList, Fragment fragment) {
        this.crimeList = crimeList;
        this.fragment = fragment;
        context = fragment.getContext();
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

    class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
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
            cbSolved.setOnCheckedChangeListener(this);
            tvDate.setText(crime.getDate());
        }


        @Override public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(context, crime.id, position);
            fragment.startActivityForResult(intent,REQUEST_CODE_CRIME);
        }


        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            crime.solved = isChecked;
        }
    }
}
