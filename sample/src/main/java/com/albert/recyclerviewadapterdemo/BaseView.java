package com.albert.recyclerviewadapterdemo;

/**
 * Created by albert on 2016/6/21.
 */
public interface BaseView<T extends BasePresenter> {
    void setPresenter(T t);
}
