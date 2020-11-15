package com.delombaertdamien.go4lunch.injections;

import android.content.Context;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class InjectionMain {

    public static MainViewModelFactory provideViewModelFactory (Context context){
        return new MainViewModelFactory(context);
    }
}
