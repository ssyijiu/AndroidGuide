package com.ssyijiu.ceoquiz;

import java.io.Serializable;

/**
 * Created by ssyijiu on 2017/4/3.
 * Github : ssyijiu
 * Email  : lxmyijiu@163.com
 */

public class Question implements Serializable{
    public int text;
    public boolean answer;
    public boolean isCheat;


    public Question() {
    }


    public Question(int text, boolean answer) {
        this.text = text;
        this.answer = answer;
        this.isCheat = false;
    }
}
