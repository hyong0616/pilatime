package com.soongsil.pilatime.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalendarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CalendarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("일정관리 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}