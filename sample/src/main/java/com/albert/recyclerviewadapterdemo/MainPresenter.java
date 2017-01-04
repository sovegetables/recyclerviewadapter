package com.albert.recyclerviewadapterdemo;

import java.util.List;

/**
 * Created by albert on 2016/6/21.
 */
public class MainPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.MainView mView;
    private List<String> mCategories;

    public static void injectPresenter(MainActivityContract.MainView view, List<String> categories){
        new MainPresenter(view, categories);
    }

    private MainPresenter(MainActivityContract.MainView view, List<String> categories) {
        view.setPresenter(this);
        mView = view;
        mCategories = categories;
    }

    @Override
    public void start() {
        mView.showCategories(mCategories);
    }
}
