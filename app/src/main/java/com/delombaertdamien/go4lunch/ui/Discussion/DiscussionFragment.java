package com.delombaertdamien.go4lunch.ui.Discussion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.ChatActivity;
import com.delombaertdamien.go4lunch.ChatViewModel;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Discussion;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.ui.adapter.AdaptorListViewDiscussions;
import com.delombaertdamien.go4lunch.utils.DiscussionsMaker;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class DiscussionFragment extends Fragment implements FirestoreCall.CallbackFirestoreDiscussion, ChatViewModel.ListenersStateRequestGetUser {

    //UI
    private RecyclerView recyclerViewDiscussion;
    private AdaptorListViewDiscussions adapter;
    //Toolbar
    private ImageView iconToolbar;
    private TextView nameToolbar;

    private Users currentUserObj;
    private String currentUserID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat_discussion, container, false);

        this.configureUI(root);
        this.getCurrentUser();
        return root;
    }

    private void configureUI(View root) {
        recyclerViewDiscussion = root.findViewById(R.id.fragment_chat_discussion_recycler_view);
        recyclerViewDiscussion.setLayoutManager(new LinearLayoutManager(getContext()));
        iconToolbar = root.findViewById(R.id.fragment_chat_discussion_toolbar_icon);
        nameToolbar = root.findViewById(R.id.fragment_chat_discussion_toolbar_name);
        adapter = new AdaptorListViewDiscussions();
        recyclerViewDiscussion.setAdapter(adapter);
    }

    private void getCurrentUser() {
        currentUserID = FirebaseAuth.getInstance().getUid();
        ChatActivity activity = (ChatActivity) getActivity();
        if(activity != null) {
            ChatViewModel model = activity.getViewModel();
            model.getObjectUser(this, currentUserID);
        }
    }

    private void getAllDiscussions() {
        FirestoreCall.getAllDiscussion(this);
    }

    @Override
    public void onSuccessGetAllDiscussions(List<Discussion> discussions) {
        adapter.updateData(discussions, currentUserID);
    }
    @Override
    public void onFailureGetAllDiscussions(Exception e) {
        Log.e("DiscussionFragment", e.getMessage());
    }

    // --- GET USER --- //
    @Override
    public void onSuccessGetUser(Users user) {
        currentUserObj = user;
        if(currentUserObj.getUrlPicture() != null){
            Glide.with(getContext())
                    .load(currentUserObj.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iconToolbar);
        }
        nameToolbar.setText(currentUserObj.getUsername());
        this.getAllDiscussions();
    }
    @Override
    public void onFailedGetUser(Exception e) {

    }

    // Never use
    @Override
    public void onSuccessGetUserSpeaker(Users user) {

    }


}
