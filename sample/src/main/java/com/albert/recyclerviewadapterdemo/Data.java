package com.albert.recyclerviewadapterdemo;

/**
 * Created by albert on 2016/7/23.
 */

public class Data {
    public static final String[] ITEMS = new String[47];

    static {
        for (int index = 0; index < ITEMS.length; index ++) {
            ITEMS[index] = "item" + index;
        }
    }
}
