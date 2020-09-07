package com.delombaertdamien.go4lunch.injections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.delombaertdamien.go4lunch.MainViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    ViewModelFactory(){

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
