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
