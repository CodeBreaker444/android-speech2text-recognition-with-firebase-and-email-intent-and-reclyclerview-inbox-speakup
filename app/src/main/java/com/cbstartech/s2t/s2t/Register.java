package com.cbstartech.s2t.s2t;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cbstartech.bmail.bmail.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

/**
Created by CodeBreaker
 */

public class Register extends AppCompatActivity {

    private EditText inputEmail, inputPassword,inputName,inputDob;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    TextToSpeech tts;
    String l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseApp.initializeApp(this);
        checkPermission();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputName=(EditText) findViewById(R.id.name);
        inputDob=(EditText) findViewById(R.id.dob);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

       /** btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(Register.this, ResetPasswordActivity.class));
            }
        });
        **/
        inputEmail.setInputType(InputType.TYPE_NULL);
        inputDob.setInputType(InputType.TYPE_NULL);
        inputName.setInputType(InputType.TYPE_NULL);
        inputPassword.setInputType(InputType.TYPE_NULL);


        tts=new TextToSpeech(Register.this, new TextToSpeech.OnInitListener() {

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
                        tts.speak("You are at Register Screen.Please Enter Your Name", TextToSpeech.QUEUE_ADD, null);

                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inputName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    btnSignUp.setText("REGISTER");

                }else{
                    //Toast.makeText(MainActivity.this, "Get Focus", Toast.LENGTH_SHORT).show();
                    btnSignUp.setText("NEXT");
                    l="1";
                    tts.speak("Enter Your Name", TextToSpeech.QUEUE_ADD, null);

                    // editText1.requestFocus();
                }

            }
        });
        inputDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // Toast.makeText(MainActivity.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                    btnSignUp.setText("REGISTER");

                }else{
                    //Toast.makeText(MainActivity.this, "Get Focus", Toast.LENGTH_SHORT).show();
                    btnSignUp.setText("NEXT");
                    l="2";
                    tts.speak("Enter Your Date Of Birth", TextToSpeech.QUEUE_ADD, null);


                    // editText1.requestFocus();
                }

            }
        });
        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // Toast.makeText(MainActivity.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                    btnSignUp.setText("REGISTER");


                }else{
                    // Toast.makeText(MainActivity.this, "Get Focus", Toast.LENGTH_SHORT).show();
                    btnSignUp.setText("NEXT");
                    l="3";
                    tts.speak("Enter Your EMail", TextToSpeech.QUEUE_ADD, null);

                    // editText1.requestFocus();
                }

            }
        });
        inputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // Toast.makeText(MainActivity.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                    btnSignUp.setText("REGISTER");


                }else{
                    // Toast.makeText(MainActivity.this, "Get Focus", Toast.LENGTH_SHORT).show();
                    btnSignUp.setText("REGISTER");
                    l="4";
                    tts.speak("Enter Your Password", TextToSpeech.QUEUE_ADD, null);

                    // editText1.requestFocus();
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
                        inputName.setText(matches.get(0).replace(" ",""));
                    }
                    else if(l=="2")
                        inputDob.setText(matches.get(0).replace(" ",""));
                    else if(l=="3")
                        inputEmail.setText(matches.get(0).replace(" ","").toLowerCase());
                    else if(l=="4")
                        inputPassword.setText(matches.get(0).replace(" ","").toLowerCase());

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }

        });

        findViewById(R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        if(l=="1")
                            inputName.setHint("Name");
                        else if(l=="2")
                            inputDob.setHint("Date-Of-Birth");
                        else if(l=="3")
                            inputEmail.setHint("Email");
                        else if(l=="4")
                            inputPassword.setHint("Password");


                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        if(l=="1") {
                            inputName.setText("");
                            inputName.setHint("Listening...");
                        }else if(l=="2") {
                            inputDob.setText("");
                            inputDob.setHint("Listening...");
                        }else if(l=="3"){
                            inputEmail.setText("");
                            inputEmail.setHint("Listening...");
                        }else if(l=="4"){
                            inputPassword.setText("");
                            inputPassword.setHint("Listening...");

                        }
                        break;
                }
                return false;
            }
        });



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String name = inputName.getText().toString().trim();
                String dob = inputDob.getText().toString().trim();
                if(l=="1")
                {
                    inputDob.requestFocus();
                }else if(l=="2"){
                    inputEmail.requestFocus();}
                else if(l=="3")
                {
                    inputPassword.requestFocus();
                }else if(l=="4") {

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(dob)) {
                        Toast.makeText(getApplicationContext(), "Enter Date Of Birth!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(getApplicationContext(), "Enter Your Name!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    //create user
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                        tts.speak("Authentication Failed",TextToSpeech.QUEUE_ADD,null);
                                    } else {
                                        tts.speak("Successfully Registered.Please Login",TextToSpeech.QUEUE_ADD,null);

                                        auth.signOut();
                                        startActivity(new Intent(Register.this, Login.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
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
    }
}