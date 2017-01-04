package com.albert.recyclerviewadapterdemo;

import java.util.List;

/**
 * Created by albert on 2016/6/21.
 */
public interface MainActivityContract {

    interface Presenter extends BasePresenter{

    }

    interface MainView extends BaseView<Presenter>{
        void showCategories(List<String> categories);
    }
}
