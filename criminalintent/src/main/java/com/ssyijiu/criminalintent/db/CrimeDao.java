package com.ssyijiu.criminalintent.db;

import com.ssyijiu.common.util.FileUtil;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.util.RealmUtil;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ssyijiu on 2017/4/21.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class CrimeDao {

    private static final CrimeDao INSTANCE = new CrimeDao();

    private Realm realm;


    public static CrimeDao instance() {
        return INSTANCE;
    }


    private CrimeDao() {
        realm = RealmUtil.getRealm();
    }


    /**
     * 查询所有
     */
    public RealmResults<Crime> queryAllCrimes() {
        return realm.where(Crime.class).findAll();
    }

    /**
     * 根据 id 查询 Item
     */
    public Crime getCrime(String id) {
        return realm.where(Crime.class).equalTo("id", id).findFirst();
    }


    /**
     * 插入 or 更新
     */
    public void insertOrUpdateCrime(final Crime crime) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(crime);
            }
        });
    }


    /**
     * 删除 crime
     */
    public void deleteCrime(final String id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                RealmResults<Crime> crimes = queryAllCrimes();
                Crime crime = getCrime(id);
                FileUtil.deleteFile(crime.getPhotoFile());
                int indexOf = crimes.indexOf(crime);
                crimes.deleteFromRealm(indexOf);
            }
        });

    }


    public void gc() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                RealmResults<Crime> allCrimes = queryAllCrimes();
                for (Crime crime : allCrimes.createSnapshot()) {
                    if (crime.couldDelete()) {
                        FileUtil.deleteFile(crime.getPhotoFile());
                        getCrime(crime.id).deleteFromRealm();
                    }
                }
            }
        });
    }


    public int size() {
        return queryAllCrimes().size();
    }

}
