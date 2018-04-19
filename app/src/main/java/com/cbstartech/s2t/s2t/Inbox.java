package com.cbstartech.s2t.s2t;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cbstartech.bmail.bmail.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;


public class Inbox
        extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "Inbox";
    ActionBar actionBar;
    private FirebaseAuth.AuthStateListener authListener;

    private FirebaseAuth auth;

    TextToSpeech tts;

    Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_recycler_view);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InboxRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#\">" + "INBOX" + "</font>"));
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF1744")));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayUseLogoEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(true);        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();
        tts=new TextToSpeech(Inbox.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        tts.speak("You are at Inbox Screen. There are 11 Emails", TextToSpeech.QUEUE_ADD, null);

                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(Inbox.this, Login.class));
                    finish();
                }
            }
        };
        signOut = (Button) findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Inbox.this, Inbox.class);
                //startActivity(intent);
               signOut();
                Intent intent= new Intent();
                startActivity(new Intent(Inbox.this, Login.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((InboxRecyclerViewAdapter) mAdapter).setOnItemClickListener(new InboxRecyclerViewAdapter.MyClickListener() {
                                                                              @Override
                                                                              public void onItemClick(int position, View v) {
                                                                                  Log.i(LOG_TAG, " Clicked on Item " + position);
                                                                              }
                                                                          });
    }

    private ArrayList<InboxViewHoder> getDataSet() {
        ArrayList results = new ArrayList<InboxViewHoder>();
        for (int index = 0; index < 10; index++) {
            InboxViewHoder obj = new InboxViewHoder("Sender " + index+1,"Top Bikes " + index+1);
            results.add(index, obj);

        } return results;
    } public void signOut() {
        auth.signOut();
    }
}
