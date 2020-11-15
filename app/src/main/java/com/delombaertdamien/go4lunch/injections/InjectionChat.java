package com.delombaertdamien.go4lunch.injections;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class InjectionChat {

    public static ChatViewModelFactory provideViewModelFactory (){
        return new ChatViewModelFactory();
    }
}
