package com.example.cnufirstmate.ui.cnu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Web_ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Web_ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is cnu fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}