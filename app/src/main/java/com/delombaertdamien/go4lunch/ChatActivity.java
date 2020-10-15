package com.delombaertdamien.go4lunch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.delombaertdamien.go4lunch.injections.ChatViewModelFactory;
import com.delombaertdamien.go4lunch.injections.InjectionChat;
import com.delombaertdamien.go4lunch.ui.Discussion.DiscussionFragment;
import com.delombaertdamien.go4lunch.ui.Messages.MessagesFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatActivity extends AppCompatActivity {

    MessagesFragment messagesFragment;
    DiscussionFragment discussionFragment;
    private String currentUserID;
    private String speakerUserID;

    public final String EXTRA_ID_USER = "UserIDForChat";

    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.configureViewModel();

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            if(savedInstanceState == null){
                if(extras.getString(EXTRA_ID_USER) != null){
                    speakerUserID = extras.getString(EXTRA_ID_USER);
                    chatViewModel.setSpeakerUser(speakerUserID);
                    this.configureAndShowFragment(false);
                }else{
                    this.configureAndShowFragment(true);
                }
            }else{
                if (savedInstanceState.getSerializable(EXTRA_ID_USER) != null) {
                    speakerUserID = (String) savedInstanceState.getSerializable(EXTRA_ID_USER);
                    chatViewModel.setSpeakerUser(speakerUserID);
                    this.configureAndShowFragment(false);
                }else{
                    this.configureAndShowFragment(true);
                }
            }
        }else{
            this.configureAndShowFragment(true);
        }

    }

    private void configureViewModel() {
        ChatViewModelFactory chatViewModelFactory = InjectionChat.provideViewModelFactory(this);
        this.chatViewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);
    }

    private void configureAndShowFragment(Boolean isDiscussion) {
        // TODO WARNING FRAME LAYOUT
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        if (!isDiscussion) {
            messagesFragment = (MessagesFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_activity_chat);

            if (messagesFragment == null) {
                // B - Create new main fragment
                messagesFragment = new MessagesFragment();
                // C - Add it to FrameLayout container
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout_activity_chat, messagesFragment)
                        .commit();
            }
        } else {
            discussionFragment = (DiscussionFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_activity_chat);

            if (discussionFragment == null) {
                // B - Create new main fragment
                discussionFragment = new DiscussionFragment();
                // C - Add it to FrameLayout container
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout_activity_chat, discussionFragment)
                        .commit();
            }
        }
    }

    public ChatViewModel getViewModel() {
        return chatViewModel;
    }

}