package com.delombaertdamien.go4lunch.injections;

import android.content.Context;

public class Injection {

    public static ViewModelFactory provideViewModelFactory (Context context){
        return new ViewModelFactory();
    }
}
