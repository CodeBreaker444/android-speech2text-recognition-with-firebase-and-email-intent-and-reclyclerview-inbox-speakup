package com.cbstartech.s2t.s2t;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button inbox;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Button send;
    TextToSpeech tts;
    String l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cbstartech.bmail.bmail.R.layout.activity_main);
        inbox = (Button) findViewById(com.cbstartech.bmail.bmail.R.id.inbox);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Inbox.class);
                startActivity(intent);
               /* signOut();
                Intent intent= new Intent();
                startActivity(new Intent(MainActivity.this, Login.class));*/
            }
        });
        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
            }
        };tts=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

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
                        tts.speak("Enter TO address", TextToSpeech.QUEUE_ADD, null);

                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });

        checkPermission();
        final EditText editText0 = (EditText) findViewById(com.cbstartech.bmail.bmail.R.id.editText0);
        final EditText editText = (EditText) findViewById(com.cbstartech.bmail.bmail.R.id.editText);
        final EditText editText1 = (EditText) findViewById(com.cbstartech.bmail.bmail.R.id.editText1);
        final Button send=(Button) findViewById(com.cbstartech.bmail.bmail.R.id.send);

        editText0.setInputType(InputType.TYPE_NULL);
        editText.setInputType(InputType.TYPE_NULL);
        editText1.setInputType(InputType.TYPE_NULL);
              editText0.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    send.setText("SEND");

                }else{
                    //Toast.makeText(MainActivity.this, "Get Focus", Toast.LENGTH_SHORT).show();
                    send.setText("NEXT");
                    l="1";
                    tts.speak("Enter TO address", TextToSpeech.QUEUE_ADD, null);

                    // editText1.requestFocus();
                }

            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                   // Toast.makeText(MainActivity.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                    send.setText("SEND");

                }else{
                    //Toast.makeText(MainActivity.this, "Get Focus", Toast.LENGTH_SHORT).show();
                    send.setText("NEXT");
                    l="2";
                    tts.speak("Enter Subject", TextToSpeech.QUEUE_ADD, null);


                    // editText1.requestFocus();
                }

            }
        });
        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                   // Toast.makeText(MainActivity.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                    send.setText("SEND");


                }else{
                   // Toast.makeText(MainActivity.this, "Get Focus", Toast.LENGTH_SHORT).show();
                    send.setText("SEND");
                    l="3";
                    tts.speak("Enter Message", TextToSpeech.QUEUE_ADD, null);

                    // editText1.requestFocus();
                }

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(l=="1")
                {
                    editText.requestFocus();
                }else if(l=="2"){
                    editText1.requestFocus();}
                    else if(l=="3")
                {
                    Toast.makeText(MainActivity.this, "Sending..", Toast.LENGTH_SHORT).show();
                    String to = editText0.getText().toString();
                    String subject = editText.getText().toString();
                    String message = editText1.getText().toString();
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_TEXT, message);
                    email.putExtra(Intent.EXTRA_EMAIL,new String[]{to});
                    email.putExtra(Intent.EXTRA_SUBJECT,subject);

                    //need this to prompts email client only
                    email.setType("message/rfc822");

                    try {
                        startActivity(Intent.createChooser(email, "Choose an Email client :"+to));
                    } catch (ActivityNotFoundException exception) {
                        Toast.makeText(MainActivity.this, "No email clients installed on device!", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }


            @Override

            public void onResults(Bundle bundle) {
                //getting all the matches

                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    if(l=="1") {
                        String k = matches.get(0).replace(" ", "");
                        editText0.setText(matches.get(0).replace(" ",""));
                    }
                        else if(l=="2")
                            editText.setText(matches.get(0));
                            else
                                editText1.setText(matches.get(0));

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }

        });

        findViewById(com.cbstartech.bmail.bmail.R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                            if(l=="1")
                                editText0.setHint("You will see input here");
                            else if(l=="2")
                                editText.setHint("You will see input here");
                                    else
                                    editText1.setHint("You will see input here");


                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        if(l=="1") {
                            editText0.setText("");
                            editText0.setHint("Listening...");
                        }else if(l=="2") {
                            editText.setText("");
                            editText.setHint("Listening...");
                        }else {
                            editText1.setText("");
                            editText1.setHint("Listening...");
                        }
                        break;
                }
                return false;
            }
        });
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }      public void signOut() {
        auth.signOut();
    }

}



