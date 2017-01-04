package com.albert.recyclerview.listener;

/**
 * Created by albert on 2016/11/5.
 */

public interface ItemCalculator {
    int findFirstVisibleItemPosition();
    int findLastVisibleItemPosition();
}
