package com.delombaertdamien.go4lunch.utils;

import com.delombaertdamien.go4lunch.models.Discussion;
import com.delombaertdamien.go4lunch.models.Users;

import java.util.ArrayList;
import java.util.List;

public class DiscussionsMaker implements FirestoreCall.CallbackFirestore {

    private List<Discussion> discussions = new ArrayList<>();
    private List<String> discussionsSpeakerID = new ArrayList<>();
    private List<String> discussionsID = new ArrayList<>();
    private CallbackDiscussionsMaker callback;


    public interface CallbackDiscussionsMaker {
        void onSuccessGetAllDiscussionsMaker (List<Discussion> discussions);
        void onFailureGetAllDiscussionsMaker (Exception e);
    }

    public DiscussionsMaker(CallbackDiscussionsMaker callback) {
        this.callback = callback;
    }

    public void createAndGetDiscussions(String userID, List<String> discussionsID){

        this.discussionsID = discussionsID;
        for(String discussion : discussionsID){
            String[] splitDiscussion = discussion.split("&");
            for(String part : splitDiscussion){
                if(!part.equals(userID)){
                    discussionsSpeakerID.add(part);
                }
            }

        }
        FirestoreCall.getAllUsers(this);
    }

    @Override
    public void onSuccessGetUsers(List<Users> users) {
        for(int i = 0; i < discussionsID.size(); i++){
            for(Users user : users){
                if(discussionsID.get(i).equals(user.getUserId())){
                  // Discussion discu = new Discussion(discussionsID.get(i), user.getUsername(), user.getUserId(), user.getUrlPicture());
                  // discussions.add(discu);
                }
            }
        }
        callback.onSuccessGetAllDiscussionsMaker(discussions);
    }
    @Override
    public void onFailureGetUsers(Exception e) {
        callback.onFailureGetAllDiscussionsMaker(e);
    }


}
