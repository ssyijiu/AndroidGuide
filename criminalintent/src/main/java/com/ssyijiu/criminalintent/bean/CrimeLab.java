package com.ssyijiu.criminalintent.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ssyijiu on 2017/4/21.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class CrimeLab {

    private List<Crime> crimeList;

    private static final CrimeLab INSTANCE = new CrimeLab();

    public  static CrimeLab get() {
        return INSTANCE;
    }


    private CrimeLab() {
        crimeList = new ArrayList<>();

        // 模拟数据
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.title = "Crime #" + i;
            crime.solved = i % 2 == 0; // Every other one
            crimeList.add(crime);
        }
    }

    public List<Crime> getCrimeList() {
        return crimeList;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : crimeList) {
            if(crime.id.equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
