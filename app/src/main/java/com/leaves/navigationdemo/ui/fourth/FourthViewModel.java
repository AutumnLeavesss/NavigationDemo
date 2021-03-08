package com.leaves.navigationdemo.ui.fourth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FourthViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FourthViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Fourth fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}