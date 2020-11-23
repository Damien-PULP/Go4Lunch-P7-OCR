package com.delombaertdamien.go4lunch.injections;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.delombaertdamien.go4lunch.ui.activity.MainViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    MainViewModelFactory(Context context){
    this.context = context;
    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
