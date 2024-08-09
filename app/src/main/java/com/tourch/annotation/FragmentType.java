package com.tourch.annotation;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({FragmentType.HOME_FRAGMENT, FragmentType.HISTORY_LIST_FRAGMENT, FragmentType.LOAD_BOARD_FRAGMENT,
        FragmentType.OFFER_FRAGMENT,FragmentType.PROFILE_FRAGMENT})

public @interface FragmentType {
    String HOME_FRAGMENT = "HomeFragment";
    String HISTORY_LIST_FRAGMENT = "HistoryListFragment";
    String OFFER_FRAGMENT = "OfferFragment";
    String LOAD_BOARD_FRAGMENT = "LoadBoardFragment";
    String PROFILE_FRAGMENT = "PROFILE_FRAGMENT";
}
// Declare the constants
