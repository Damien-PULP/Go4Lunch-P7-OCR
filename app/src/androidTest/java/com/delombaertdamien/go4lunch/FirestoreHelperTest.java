package com.delombaertdamien.go4lunch;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.delombaertdamien.go4lunch.models.Favorite;
import com.delombaertdamien.go4lunch.models.Message;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.service.FavoriteHelper;
import com.delombaertdamien.go4lunch.service.MessageHelper;
import com.delombaertdamien.go4lunch.service.UserHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class FirestoreHelperTest {

    /**
     * This is test CRUD on collection Users
     */
    //CREATE
    @Test
    public void createAUser (){

        UserHelper.getAllUsers().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                // GET USERS BEFORE CREATE THE USER
                final List<Users> userListBeforeCreate = new ArrayList<>();

                for(QueryDocumentSnapshot snap : querySnapshot){
                    Users users = snap.toObject(Users.class);
                    userListBeforeCreate.add(users);
                }
                Log.d("FirebaseInstrumentedTest", "size : " + userListBeforeCreate.size());

                //CREATE A USER
                final String uid = "user_test_id" + userListBeforeCreate.size();
                final String username = "username_test" + userListBeforeCreate.size();
                final String urlPicture = "https://www.flaticon.com/svg/static/icons/svg/145/145804.svg";


                UserHelper.createUser(uid, username, urlPicture, null);

                // GET USERS AFTER CREATE THE USER
                UserHelper.getAllUsers().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        List<Users> userListAfterCreate = new ArrayList<>();

                        for(QueryDocumentSnapshot snap : querySnapshot){
                            Users users = snap.toObject(Users.class);
                            userListAfterCreate.add(users);
                        }
                        Log.d("FirebaseInstrumentedTest", "size : " + userListAfterCreate.size());

                        assertEquals(userListBeforeCreate.size() + 1, userListAfterCreate.size());

                        Users user = userListAfterCreate.get(userListAfterCreate.size() - 1);

                        assertEquals(user.getUserId(), uid);
                        assertEquals(user.getUsername(), username);
                        assertEquals(user.getUrlPicture(), urlPicture);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseInstrumentedTest", e.getMessage());
                        fail();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseInstrumentedTest", e.getMessage());
                fail();
            }
        });

    }
    //READ
    @Test
    public void getAllUsers() {

        UserHelper.getAllUsers().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                List<Users> userList = new ArrayList<>();
                for(QueryDocumentSnapshot snap : querySnapshot){
                    Users users = snap.toObject(Users.class);
                    userList.add(users);
                }
                Log.d("FirebaseInstrumentedTest", "size : " + userList.size());
                assertTrue(userList.size() > 0);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseInstrumentedTest", e.getMessage());
                fail();
            }
        });
    }
    //UPDATE
    @Test
    public void updateAUser (){

        UserHelper.getAllUsers().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                //CREATE A USER
                final String uid = "user_test_id";
                final String username = "username_test";
                final String urlPicture = "https://www.flaticon.com/svg/static/icons/svg/145/145804.svg";

                UserHelper.createUser(uid, username, urlPicture, null);

                // GET USERS BEFORE UPDATE THE USER
                final List<Users> userListBeforeUpdate = new ArrayList<>();

                for(QueryDocumentSnapshot snap : querySnapshot){
                    Users users = snap.toObject(Users.class);
                    userListBeforeUpdate.add(users);
                }

                // GET USERS AFTER CREATE THE USER
                UserHelper.getAllUsers().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        final String placeId = "placeID";
                        final String lunchPlaceTime = "10/10/20";

                        UserHelper.updateLunchPlace(uid, placeId, lunchPlaceTime);

                        List<Users> userListAfterUpdate = new ArrayList<>();

                        for(QueryDocumentSnapshot snap : querySnapshot){
                            Users users = snap.toObject(Users.class);
                            userListAfterUpdate.add(users);
                        }

                        Users user = userListAfterUpdate.get(userListAfterUpdate.size() - 1);

                        assertEquals(user.getUserId(), uid);
                        assertEquals(user.getLunchPlaceID(), placeId);
                        assertNotEquals(user.getDateLunchPlace(), null);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseInstrumentedTest", e.getMessage());
                        fail();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseInstrumentedTest", e.getMessage());
                fail();
            }
        });

    }
    //DELETE
    @Test
    public void deleteAUser (){

        UserHelper.getAllUsers().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                final String uid = "user_test_id_to_delete";
                //CREATE A USER
                UserHelper.createUser(uid ,"username_test_to_delete" , "https://www.flaticon.com/svg/static/icons/svg/145/145804.svg", null);
                // GET USERS BEFORE DELETE THE USER
                final List<Users> userListBeforeDelete = new ArrayList<>();

                for(QueryDocumentSnapshot snap : querySnapshot){
                    Users users = snap.toObject(Users.class);
                    userListBeforeDelete.add(users);
                }
                //DELETE THE USER
                UserHelper.deleteUser(uid);
                // GET USERS AFTER DELETE THE USER
                UserHelper.getAllUsers().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        List<Users> userListAfterDelete = new ArrayList<>();

                        for(QueryDocumentSnapshot snap : querySnapshot){
                            Users users = snap.toObject(Users.class);
                            userListAfterDelete.add(users);
                        }
                        assertEquals(userListBeforeDelete.size(), userListAfterDelete.size()  + 1);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseInstrumentedTest", e.getMessage());
                        fail();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirebaseInstrumentedTest", e.getMessage());
                fail();
            }
        });
    }

    /**
     * This is test to add en suppress favorite at a user
     */
    // CREATE
    @Test
    public void addAFavorite (){

        //CREATE A USER
        final String uid = "user_test_id";
        final String username = "username_test";
        final String urlPicture = "https://www.flaticon.com/svg/static/icons/svg/145/145804.svg";

        UserHelper.createUser(uid, username, urlPicture, null);

        final String placeId = "placeID";
        final String placeName = "placeName";
        final String placeUrl = "placeUrl";

        FavoriteHelper.addFavoritePlace(uid, placeId, placeName, placeUrl);

        FavoriteHelper.getAllPlaceIDFavorite(uid).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                List<Favorite> favorites = new ArrayList<>();
                for(QueryDocumentSnapshot snap : querySnapshot){
                    Favorite favorite = snap.toObject(Favorite.class);
                    favorites.add(favorite);
                }

                Favorite favoriteAdded = favorites.get(favorites.size() - 1);

                assertEquals(favoriteAdded.getIdPlace(), placeId);
                assertEquals(favoriteAdded.getNamePlace(), placeName);
                assertEquals(favoriteAdded.getUrlPlace(), placeUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fail();
            }
        });

    }
    @Test
    public void removeAFavorite (){

        //CREATE A USER
        final String uid = "user_test_id";
        final String username = "username_test";
        final String urlPicture = "https://www.flaticon.com/svg/static/icons/svg/145/145804.svg";

        UserHelper.createUser(uid, username, urlPicture, null);

        final String placeId = "placeID";
        final String placeName = "placeName";
        final String placeUrl = "placeUrl";

        FavoriteHelper.addFavoritePlace(uid, placeId, placeName, placeUrl);

        FavoriteHelper.getAllPlaceIDFavorite(uid).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                for(QueryDocumentSnapshot snap : querySnapshot){
                    Favorite favorite = snap.toObject(Favorite.class);
                    FavoriteHelper.deleteFavoritePlace(uid, favorite.getIdPlace());
                }

                FavoriteHelper.getAllPlaceIDFavorite(uid).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        assertEquals(querySnapshot.size(), 0);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fail();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fail();
            }
        });
    }

    /**
     * This is test to add message in discussion
     */
    //CREATE
    @Test
    public void addMessageInDiscussion (){
       // String textMessage, Date dateCreate, String chat, String userSenderID, String userOneName, String userOneID, String userOneUrlIcon, String userTwoName, String userTwoID, String userTwoUrlIcon

        //Message info
        final String textMessage = "My content text message";
        final Date date = new Date();
        final String chatId = "chat_id";
        final String userSenderId = "user1ID";

        //User 1
        final String user1ID = "user1ID";
        final String user1Name = "user1Name";
        final String user1Url = "user1Picture";
        //User 2
        final String user2ID = "user2ID";
        final String user2Name = "user2Name";
        final String user2Url = "user2Picture";

        MessageHelper.createMessageForChat(textMessage, date, chatId, userSenderId, user1Name, user1ID, user1Url, user2Name, user2ID, user2Url);

        MessageHelper.getAllMessagesForChat(chatId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                List<Message> messages = new ArrayList<>();
                for (QueryDocumentSnapshot snap : querySnapshot){
                    messages.add(snap.toObject(Message.class));
                }
                assertTrue(messages.size() > 0);
                assertEquals(messages.get(messages.size() - 1).getMessage(), textMessage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            fail();
            }
        });
    }


}
