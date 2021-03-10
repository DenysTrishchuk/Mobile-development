package ua.kpi.comsys.iv8127.android_prog.ui.lab1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Lab1ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Lab1ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}