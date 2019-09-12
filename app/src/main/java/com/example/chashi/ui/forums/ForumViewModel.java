package com.example.chashi.ui.forums;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForumViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ForumViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is forum fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}