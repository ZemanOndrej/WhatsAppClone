package eu.ozeman.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText phoneInput, codeInput;
    private LinearLayout codePanel, phonePanel;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks authCallbacks;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        logIn();

        phoneInput = findViewById(R.id.phoneInput);
        codeInput = findViewById(R.id.codeInput);
        Button verifyPhoneButton = findViewById(R.id.phoneVerificationButton);
        Button verifyCodeButton = findViewById(R.id.codeVerificationButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        codePanel = findViewById(R.id.codePanel);
        phonePanel = findViewById(R.id.phonePanel);

        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, codeInput.getText().toString());
                signInWithCredential(credential);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonePanel.setVisibility(View.VISIBLE);
                codePanel.setVisibility(View.GONE);
            }
        });

        verifyPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneInput.getText().toString(), 120,
                        TimeUnit.SECONDS, MainActivity.this, authCallbacks
                );

            }
        });

        authCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(final FirebaseException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast errorToast = Toast.makeText(MainActivity.this, "Error, something failed with Firebase authentication. " + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                        errorToast.show();
                    }
                });
            }

            @Override
            public void onCodeSent(String idString, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(idString, forceResendingToken);

                verificationId = idString;
                phonePanel.setVisibility(View.GONE);
                codePanel.setVisibility(View.VISIBLE);
            }
        };
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    logIn();
                }
            }
        });
    }

    private void logIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            finish();
        }
    }

}
