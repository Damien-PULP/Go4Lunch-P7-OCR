package com.delombaertdamien.go4lunch.injections;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.delombaertdamien.go4lunch.ui.activity.ChatViewModel;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class ChatViewModelFactory implements ViewModelProvider.Factory {

    ChatViewModelFactory (){
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ChatViewModel.class)){
            return (T) new ChatViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
