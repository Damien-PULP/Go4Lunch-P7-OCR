package com.delombaertdamien.go4lunch.service;

import com.delombaertdamien.go4lunch.models.Discussion;
import com.delombaertdamien.go4lunch.models.Message;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.Date;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class MessageHelper {

    public static final String NAME_COLLECTION = "message";

    public static Query getAllMessagesForChat (String chat) {
        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(NAME_COLLECTION)
                .orderBy("dateCreate")
                .limit(50);
    }
    public static Task<DocumentReference> createMessageForChat(String textMessage, Date dateCreate, String chat, String userSenderID, String userOneName, String userOneID, String userOneUrlIcon, String userTwoName, String userTwoID, String userTwoUrlIcon){
        // 1 - Create the Message object
        Message message = new Message(textMessage, dateCreate, userSenderID);

        Discussion discussion= new Discussion(chat, userOneName, userOneID, userOneUrlIcon, userTwoName, userTwoID, userTwoUrlIcon, textMessage);
        ChatHelper.getChatCollection().document(chat).set(discussion);
        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(NAME_COLLECTION)
                .add(message);
    }

}
