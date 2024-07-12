package com.example.project;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
class AdminHomePageViewModel extends ViewModel {

    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<String> hostelBlock = new MutableLiveData<>();

    public LiveData<String> getUsername(){

        return username;

    }

    public LiveData<String> getHostelBlock(){

        return hostelBlock;

    }

    public void setUsername(String name){

        username.setValue(name);

    }

    public void setHostelBlock(String block){

        hostelBlock.setValue(block);

    }

}
