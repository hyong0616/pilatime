package com.soongsil.pilatime.ui.goods;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GoodsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GoodsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("상품관리 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}