package com.delombaertdamien.go4lunch.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.injections.ChatViewModelFactory;
import com.delombaertdamien.go4lunch.injections.InjectionChat;
import com.delombaertdamien.go4lunch.ui.Discussion.DiscussionFragment;
import com.delombaertdamien.go4lunch.ui.Messages.MessagesFragment;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class ChatActivity extends AppCompatActivity {

    // FRAGMENT
    MessagesFragment messagesFragment;
    DiscussionFragment discussionFragment;
    // VIEW MODEL
    private ChatViewModel chatViewModel;
    //EXTRA
    public final String EXTRA_ID_USER = "UserIDForChat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        configureViewModel();

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            String speakerUserID;
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

    // Configuring the view model to using
    private void configureViewModel() {
        ChatViewModelFactory chatViewModelFactory = InjectionChat.provideViewModelFactory();
        this.chatViewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);
    }
    // Configure and show fragment Discussion && Message - Params isDiscussion
    private void configureAndShowFragment(Boolean isDiscussion) {
        if (!isDiscussion) {
            messagesFragment = (MessagesFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_activity_chat);
            if (messagesFragment == null) {
                messagesFragment = new MessagesFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout_activity_chat, messagesFragment)
                        .commit();
            }
        } else {
            discussionFragment = (DiscussionFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_activity_chat);
            if (discussionFragment == null) {
                discussionFragment = new DiscussionFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout_activity_chat, discussionFragment)
                        .commit();
            }
        }
    }

    // Return current view model
    public ChatViewModel getViewModel() {
        return chatViewModel;
    }

}