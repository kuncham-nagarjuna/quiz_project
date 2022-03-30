package com.quiz.quiz;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText phone;
    private FirebaseAuth mAuth;
    private CountryCodePicker country_code;
    private String get_country_code, get_phone;

    private SessionManager sessionManager;
    private Dialog progressDialog;
    TextView dialog_message;
    private LinearLayout otp_layout, login_layout;
    private String phoneNumberInPrefs = null;
    private TextView retryTimer, myOtpexpiresTXT;
    private boolean authInProgress;
    private CountDownTimer countDownTimer;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    Date current_date, added_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        TextView back = findViewById(R.id.back);
        otp_layout = findViewById(R.id.otp_layout);
        country_code = findViewById(R.id.country_code_picker);
        phone = findViewById(R.id.phone);
        retryTimer = findViewById(R.id.resend);
        myOtpexpiresTXT = findViewById(R.id.otp_expires_txt);
        Button login_btn = findViewById(R.id.loginButton);
        Button getotp_btn = findViewById(R.id.getotp);
        login_layout = findViewById(R.id.login_layout);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);

        login_layout.setVisibility(View.VISIBLE);
        otp_layout.setVisibility(View.GONE);

        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.loading_dialog_layout);
        dialog_message = progressDialog.findViewById(R.id.message);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCanceledOnTouchOutside(false);
        sessionManager = new SessionManager(getApplicationContext());

        country_code.registerCarrierNumberEditText(phone);

        back.setOnClickListener(view -> {
            otp_layout.setVisibility(View.GONE);
            login_layout.setVisibility(View.VISIBLE);
        });

        login_btn.setOnClickListener(view -> {
            if (TextUtils.isEmpty(phoneNumberInPrefs)) {
                Log.e("check-sub", "submit");
            } else {
                if (TextUtils.isEmpty(otp1.getText().toString()) || TextUtils.isEmpty(otp2.getText().toString()) || TextUtils.isEmpty(otp3.getText().toString()) || TextUtils.isEmpty(otp4.getText().toString()) || TextUtils.isEmpty(otp5.getText().toString()) || TextUtils.isEmpty(otp6.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    verifyCode(otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString());
                }
            }
        });

        getotp_btn.setOnClickListener(view -> {
            country_code.registerCarrierNumberEditText(phone);
            verify_phone();
        });

        EditText[] edit = {otp1, otp2, otp3, otp4, otp5, otp6};
        otp1.addTextChangedListener(new GenericTextWatcher(otp1, edit));
        otp2.addTextChangedListener(new GenericTextWatcher(otp2, edit));
        otp3.addTextChangedListener(new GenericTextWatcher(otp3, edit));
        otp4.addTextChangedListener(new GenericTextWatcher(otp4, edit));
        otp5.addTextChangedListener(new GenericTextWatcher(otp5, edit));
        otp6.addTextChangedListener(new GenericTextWatcher(otp6, edit));

    }

    private void login() {
        dialog_message.setText("Please Wait...");
        progressDialog.show();

        sessionManager.createLoginSession(get_country_code, get_phone);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String da = sdf.format(date);
        sessionManager.set_login_time(da);
        Log.e("login_time", sessionManager.get_login_time());

        Intent login_home = new Intent(getApplicationContext(), MainActivity.class);
        progressDialog.dismiss();
        startActivity(login_home);
        finish();
    }

    public void verify_phone() {
        if (validate()) {
            phoneNumberInPrefs = "+" + get_country_code + get_phone;
            Log.e("phoneNumberInPrefs--", phoneNumberInPrefs);
            otp_layout.setVisibility(View.VISIBLE);
            login_layout.setVisibility(View.GONE);
            sendVerificationCode(phoneNumberInPrefs);
        }
    }

    private void showProgress(int i) {
        String title = (i == 1) ? "Sending otp" : "Verifying otp";
        String message = (i == 1) ? ("One time password is being sent to:\n" + phoneNumberInPrefs) : "Verifying otp...";
        progressDialog.setTitle(title);
        dialog_message.setText(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void sendVerificationCode(String phone) {
        Log.e("Login--", "sendVerificationCode start");
        showProgress(1);
        Log.e("Login--", "after progress");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(LoginActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressDialog.dismiss();

                                final String code = phoneAuthCredential.getSmsCode();

                                if (code != null) {
                                    Log.e("OTP---verify-", code);

                                    otp1.setText(String.valueOf(code.charAt(0)));
                                    otp2.setText(String.valueOf(code.charAt(1)));
                                    otp3.setText(String.valueOf(code.charAt(2)));
                                    otp4.setText(String.valueOf(code.charAt(3)));
                                    otp5.setText(String.valueOf(code.charAt(4)));
                                    otp6.setText(String.valueOf(code.charAt(5)));
                                }
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                authInProgress = false;
                                progressDialog.dismiss();
                                countDownTimer.cancel();
                                Log.e("ERR_MESSAGE", "Something went wrong" + ((e.getMessage() != null) ? ("\n" + e.getMessage()) : ""));
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    String inv_temp = "Invalid phone number.";
                                    Toast.makeText(getApplicationContext(), inv_temp, Toast.LENGTH_SHORT).show();
//                                    Snackbar.make(getView().findViewById(android.R.id.content), inv_temp, Snackbar.LENGTH_SHORT).show();
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    Toast.makeText(getApplicationContext(), "Quota exceeded.", Toast.LENGTH_SHORT).show();
//                                    Snackbar.make(getView().findViewById(android.R.id.content), "Quota exceeded.", Snackbar.LENGTH_SHORT).show();
                                }
                                retryTimer.setVisibility(View.VISIBLE);
                                retryTimer.setText(R.string.resend_code);
                                myOtpexpiresTXT.setText(R.string.didt_recive_code);
                                retryTimer.setOnClickListener(view -> sendVerificationCode(phoneNumberInPrefs));
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                authInProgress = true;
                                mVerificationId = verificationId;
                                mResendToken = forceResendingToken;
                                myOtpexpiresTXT.setText(R.string.you_can_resend_the_otp_in);
                                progressDialog.dismiss();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        startCountdown();
    }

    private void startCountdown() {
        retryTimer.setOnClickListener(null);
        myOtpexpiresTXT.setText(R.string.resend_otp_in);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                if (retryTimer != null) {
                    String timer = (l / 1000) + " seconds";
                    retryTimer.setText(timer);
                }
            }

            @Override
            public void onFinish() {
                if (retryTimer != null) {
                    retryTimer.setText(R.string.resend);
                    myOtpexpiresTXT.setText(R.string.didt_recive_code);
                    retryTimer.setOnClickListener(view -> sendVerificationCode(phoneNumberInPrefs));
                }
            }
        }.start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        showProgress(2);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                dialog_message.setText("Logging you in!");
                login();
            }

        }).addOnFailureListener(e -> {

            if (e.getMessage() != null && e.getMessage().contains("invalid")) {
                Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), e.getMessage() != null ? "\n" + e.getMessage() : "", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
            authInProgress = false;
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private boolean validate() {
        boolean valid = true;

        get_country_code = country_code.getSelectedCountryCode();
        get_phone = phone.getText().toString().trim();

        Log.e("data--", get_country_code + "--" + get_phone);

        if (get_phone.length() != 10) {
            phone.setError("Enter Valid 10 digit Mobile Number");
            valid = false;
        } else {
            phone.setError(null);
        }

        return valid;
    }

}
