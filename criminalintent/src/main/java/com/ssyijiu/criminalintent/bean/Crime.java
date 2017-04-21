package com.ssyijiu.criminalintent.bean;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ssyijiu on 2017/4/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class Crime {
    public UUID id;
    public String title;
    public Date date;
    public boolean solved;

    public Crime() {
        id = UUID.randomUUID();
        date = new Date();
    }
}
