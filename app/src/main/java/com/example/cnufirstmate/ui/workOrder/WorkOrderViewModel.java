package com.example.cnufirstmate.ui.workOrder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkOrderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WorkOrderViewModel() {
    }

    public LiveData<String> getText() {
        return mText;
    }
}