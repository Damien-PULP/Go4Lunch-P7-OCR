package com.delombaertdamien.go4lunch.injections;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.delombaertdamien.go4lunch.MainViewModel;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private Context context;
    MainViewModelFactory(Context context){
    this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
