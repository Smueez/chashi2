package com.example.chashi.ui.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TestDiseaseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TestDiseaseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is scan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
