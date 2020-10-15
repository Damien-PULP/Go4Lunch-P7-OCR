package com.delombaertdamien.go4lunch.injections;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.delombaertdamien.go4lunch.ChatViewModel;
import com.delombaertdamien.go4lunch.MainViewModel;

public class ChatViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    ChatViewModelFactory (Context context){
        this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ChatViewModel.class)){
            return (T) new ChatViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
