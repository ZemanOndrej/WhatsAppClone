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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneInput, codeInput;
    private LinearLayout codePanel, phonePanel;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks authCallbacks;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        logIn();

        phoneInput = findViewById(R.id.phoneInput);
        codeInput = findViewById(R.id.codeInput);
        Button verifyPhoneButton = findViewById(R.id.phoneVerificationButton);
        Button verifyCodeButton = findViewById(R.id.codeVerificationButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        codePanel = findViewById(R.id.codePanel);
        phonePanel = findViewById(R.id.phonePanel);

        verifyCodeButton.setOnClickListener(v -> {
            if (verificationId != null && !codeInput.getText().toString().isEmpty()) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, codeInput.getText().toString());
                signInWithCredential(credential);
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.code_empty), Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(v -> {
            phonePanel.setVisibility(View.VISIBLE);
            codePanel.setVisibility(View.GONE);
        });

        verifyPhoneButton.setOnClickListener(v -> PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneInput.getText().toString(), 120,
                TimeUnit.SECONDS, LoginActivity.this, authCallbacks
        ));

        authCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(final FirebaseException e) {
                runOnUiThread(() -> {
                    Toast errorToast = Toast.makeText(LoginActivity.this, getString(R.string.firebase_error) + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                    errorToast.show();
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
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                logIn();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.wrong_code), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logIn() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
            userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("phone", user.getPhoneNumber());
                        userMap.put("name", user.getPhoneNumber());
                        userDB.updateChildren(userMap);
                    }
                    startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }

}
