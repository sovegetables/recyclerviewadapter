package com.albert.recyclerviewadapterdemo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.xogrp.recyclerviewadapterdemo.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest{

    private MainActivity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mActivity = mMainActivityTestRule.getActivity();
    }


    @Test
    public void testCategoriesRecyclerView(){
        mActivity.findViewById(R.id.rv);
    }



}