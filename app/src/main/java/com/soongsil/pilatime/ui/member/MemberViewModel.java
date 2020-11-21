package com.soongsil.pilatime.ui.member;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MemberViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MemberViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("회원관리 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}