package com.ssyijiu.criminalintent.db;

import android.os.Environment;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.ToastUtil;
import com.ssyijiu.criminalintent.app.App;
import com.ssyijiu.criminalintent.bean.Crime;
import com.ssyijiu.criminalintent.util.RealmUtil;
import io.realm.Realm;
import io.realm.RealmResults;
import java.io.File;

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
        Crime crime = new Crime();
        crime.title = "crime";
        insertOrUpdateCrime(crime);
    }


    /**
     * 查询所有
     */
    public RealmResults<Crime> queryAllCrimes() {
        return realm.where(Crime.class).findAll();
    }


    /**
     * 异步查询所有
     */
    public RealmResults<Crime> queryAllCrimesAsync() {
        return realm.where(Crime.class).findAllAsync();
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
     * 异步事物 插入 or 更新
     */
    public void insertOrUpdateCrimeAsync(final Crime crime, Realm.Transaction.OnSuccess onSuccess) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(crime);
            }
        }, onSuccess);
    }


    /**
     * 删除 crime
     */
    public void deleteCrime(final String id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                MLog.i(Thread.currentThread().getName());
                RealmResults<Crime> crimes = queryAllCrimes();
                int indexOf = crimes.indexOf(getCrime(id));
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
                        getCrime(crime.id).deleteFromRealm();
                    }
                }
            }
        });
    }


    public int size() {
        return queryAllCrimes().size();
    }


    private class RealmAsyncSuccess implements Realm.Transaction.OnSuccess {
        String message;


        RealmAsyncSuccess(String message) {
            this.message = message;
        }


        @Override public void onSuccess() {
            ToastUtil.show(this.message);
        }
    }


    private class RealmAsyncError implements Realm.Transaction.OnError {
        @Override public void onError(Throwable error) {
            MLog.e("delete error!", error);
            ToastUtil.show(error.getMessage());
        }
    }

    public File getPhotoFile(Crime crime) {
        File externalFilesDir = App.getContext()
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }
}
