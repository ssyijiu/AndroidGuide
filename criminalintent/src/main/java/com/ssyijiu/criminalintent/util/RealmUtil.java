package com.ssyijiu.criminalintent.util;

import io.realm.Realm;

/**
 * Created by ssyijiu on 2017/4/27.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class RealmUtil {

    private static Realm realm = Realm.getDefaultInstance();

    public static Realm getRealm() {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public static void transaction(Realm.Transaction transaction) {

        if(transaction != null) {
            getRealm().executeTransaction(transaction);
        }
    }

}
