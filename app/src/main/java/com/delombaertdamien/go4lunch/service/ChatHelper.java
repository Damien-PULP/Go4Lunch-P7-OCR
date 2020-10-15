package com.delombaertdamien.go4lunch.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatHelper {

    public static final String NAME_COLLECTION = "chats";

    public static CollectionReference getChatCollection (){
        return FirebaseFirestore.getInstance().collection(NAME_COLLECTION);
    }

    public static Task<QuerySnapshot> getAllDiscussions (){ return getChatCollection().get(); }
}
