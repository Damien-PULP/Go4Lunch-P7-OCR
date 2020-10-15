package com.delombaertdamien.go4lunch.injections;

import android.content.Context;

public class InjectionMain {

    public static MainViewModelFactory provideViewModelFactory (Context context){
        return new MainViewModelFactory(context);
    }
}
