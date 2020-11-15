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
import com.delombaertdamien.go4lunch.ui.activity.ChatActivity;
import com.delombaertdamien.go4lunch.ui.activity.ChatViewModel;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Discussion;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.ui.adapter.AdaptorListViewDiscussions;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class DiscussionFragment extends Fragment implements FirestoreCall.CallbackFirestoreDiscussion, ChatViewModel.ListenersStateRequestGetUser {

    // TOOLBAR
    private ImageView iconToolbar;
    private TextView nameToolbar;
    // ADAPTER
    private AdaptorListViewDiscussions adapter;

    private String currentUserID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat_discussion, container, false);

        configureUI(root);
        getCurrentUser();
        return root;
    }

    // init UI
    private void configureUI(View root) {
        RecyclerView recyclerViewDiscussion = root.findViewById(R.id.fragment_chat_discussion_recycler_view);
        recyclerViewDiscussion.setLayoutManager(new LinearLayoutManager(getContext()));
        iconToolbar = root.findViewById(R.id.fragment_chat_discussion_toolbar_icon);
        nameToolbar = root.findViewById(R.id.fragment_chat_discussion_toolbar_name);
        adapter = new AdaptorListViewDiscussions();
        recyclerViewDiscussion.setAdapter(adapter);
    }
    // Get current user - callback to get user is this
    private void getCurrentUser() {
        currentUserID = FirebaseAuth.getInstance().getUid();
        ChatActivity activity = (ChatActivity) getActivity();
        if(activity != null) {
            ChatViewModel model = activity.getViewModel();
            model.getObjectUser(this, currentUserID);
        }
    }

    // Get all discussion of this user
    private void getAllDiscussions() {
        FirestoreCall.getAllDiscussion(this);
    }

    // Response of get current object user && call - 'getAllDiscussions'
    @Override
    public void onSuccessGetUser(Users user) {
        if(user.getUrlPicture() != null){
            Glide.with(getContext())
                    .load(user.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iconToolbar);
        }
        nameToolbar.setText(user.getUsername());
        this.getAllDiscussions();
    }
    @Override
    public void onFailedGetUser() {

    }

    // Response of Method 'getAllDiscussions' && update adapter with List discussions
    @Override
    public void onSuccessGetAllDiscussions(List<Discussion> discussions) {
        adapter.updateData(discussions, currentUserID);
    }
    @Override
    public void onFailureGetAllDiscussions(Exception e) {
        Log.e("DiscussionFragment", e.getMessage());
    }

}
