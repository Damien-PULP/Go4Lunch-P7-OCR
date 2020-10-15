package com.delombaertdamien.go4lunch.injections;

import android.content.Context;

public class InjectionChat {

    public static ChatViewModelFactory provideViewModelFactory (Context context){
        return new ChatViewModelFactory(context);
    }
}
