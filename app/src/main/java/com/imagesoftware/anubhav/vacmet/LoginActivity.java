package com.imagesoftware.anubhav.vacmet;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.imagesoftware.anubhav.vacmet.emailSending.GmailSender;
import com.imagesoftware.anubhav.vacmet.model.UserModel;
import com.imagesoftware.anubhav.vacmet.utils.FingerprintHandler;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, FingerprintHandler.FingerPrintCallback {
    private static final String USER_ROLE = "USER_ROLE";
    private static final String USER_SAP_LISTS = "USER_SAP_LISTS";

    /**
     * NOTE: Disabling the persistance of firebase, as sudden changes in console are not affected
     */
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
    }
    private static final int GOOGLE_SIGN_IN = 9090;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 9099;
    private static final int MY_PERMISSIONS_REQUEST_FINGERPRINT = 90192;
    private static final String LOG_IN_MODE_IS_EXISTING_USER = "LOG_IN_MODE_IS_EXISTING_USER";
    private static final String ADMIN_ACCESS = "ADMIN_ACCESS";
    private ImageView gifImageView, googleSignIn;
    private FrameLayout frameLayout;
    private LayoutInflater inflater;
    private Button btnSignIn;
    private Button btnSignUp;
    private FrameLayout btnLogin;
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout mainLayout;
    private Activity activity;
    private LinearLayout login_btns;
    private GoogleApiClient googleApiClient;
    private EditText etUserName_signIn, etPassword_signIn, etFirstName_signUp,
            etLastName_signUp, etEmail_signUp, etPassword_signUp, etReEnterPass_signUp, etContact_signUp;
    private Dialog dialog;
    private AnimationDrawable gmailAnimationDrawable;
    private Button btn_signUser;
    private Animation animation;
    private SmsBroadcast smsDeliverBroadcast;
    private String RECEIVE_ACTION = "RECEIVE";
    private IntentFilter intentFilter;
    private Random random;
    private String otpGeneratedValue;
    private EditText sendOtp;
    private Button btnOtpApprove;
    private AnimationDrawable otpReceiveAnimationDrawable;
    private LinearLayout llOtpLayout;
    private RelativeLayout rlLoaderLayout;
    private LinearLayout orRegisterVia;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static String url;
    private String strUrl;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userContact;
    private String behaviour = "";
    private String userSignIn;
    private String passSignIn;
    private ImageView loaderSignIn;
    private TextView textSignIn;
    private SharedPreferences sharedprefs;
    private final String LoginPrefs = "LoginPrefs";
    private final String SapId = "SapId";
    private final String OrderIdPrefs = "OrderIdPrefs";
    private final String ClientorServer = "ClientorServer";
    private final String LoggedInUser = "LoggedInUser";
    private final String LoggedInUserName = "LoggedInUserName";
    private final String LoggedInUserPassword = "LoggedInUserPassword";
    private EditText etSapId;
    private RadioButton radioClient, radioSales;
    private SharedPreferences orderIdPrefs;
    private String userSapId, userClientOrServer;
    private ProgressDialog progressDialog;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private TextView et_fingerPrintError;
    private KeyStore keyStore;
    private Cipher cipher;
    private static final String KEY_NAME = "PerfectSoftware";
    private TextView tv_switch_password_mode;
    private boolean adminAccess;
    private AlertDialog alertDialogBuilderForAuthFail;
    private TextView textViewForAlertBuilder;
    private AlertDialog.Builder alertBuilder;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        activity = this;
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.order_service_params);
        mFirebaseRemoteConfig.fetch(3600).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mFirebaseRemoteConfig.activateFetched();
                }
            }
        });
        initializeAlerts();
        initializeFirebaseAuth();
        init();
        random = new Random();
        random.setSeed(System.currentTimeMillis());
        otpGeneratedValue = String.valueOf(random.nextInt(5000));
        /**
         * As raw folder resides inside res folder, hence the id's will be automatically generated in the R.java
         * File, but this not happens in assets folder.
         * Below two lines helped in playing Gif's.
         */
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);
        Glide.with(this).load(R.raw.gif2).into(imageViewTarget);
        sharedprefs = getSharedPreferences(LoginPrefs, MODE_PRIVATE);
        orderIdPrefs = getSharedPreferences(OrderIdPrefs, MODE_PRIVATE);
        intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_ACTION);
        smsDeliverBroadcast = new SmsBroadcast();
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        mainLayout.setOnClickListener(this);
        gifImageView.setOnClickListener(this);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();


    }

    private void initializeAlerts() {
       textViewForAlertBuilder = new TextView(this);
       textViewForAlertBuilder.setTextColor(getResources().getColor(R.color.black));
       textViewForAlertBuilder.setCompoundDrawables(getResources().getDrawable(R.drawable.vac_small),null,null,null);
       textViewForAlertBuilder.setPadding((int)getResources().getDimension(R.dimen.d5),(int)getResources().getDimension(R.dimen.d5),(int)getResources().getDimension(R.dimen.d5),(int)getResources().getDimension(R.dimen.d5));
       textViewForAlertBuilder.setText(R.string.network_error_msg_alert);
       alertBuilder = new AlertDialog.Builder(LoginActivity.this).setCancelable(false)
                .setIcon(getResources().getDrawable(R.drawable.vac_small)).setView(textViewForAlertBuilder).
                        setPositiveButton(R.string.lets_try_again, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                frameLayout.removeAllViewsInLayout();
                                login_btns.setVisibility(View.VISIBLE);
                                btnSignIn.setVisibility(View.VISIBLE);
                                btnSignUp.setVisibility(View.VISIBLE);
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("",null);
        alertDialogBuilderForAuthFail = alertBuilder.create();
    }

    private void initializeFirebaseAuth() {
        if(connectionIsOnline()) {
            firebaseAuth = FirebaseAuth.getInstance();
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        //SignedIn
                    } else {
                        //SignedOut
                    }
                    updateUI(user, false);
                }
            };
        }
    }

    private void updateUI(FirebaseUser user, final boolean isAuthRequired) {
        boolean isAuthSuccess = (user != null);
        if (isAuthSuccess) {
            //Anonymous User SignedIn to Firebase., Now access Database...
            mDatabase = FirebaseDatabase.getInstance().getReference("users");
            if (behaviour.equalsIgnoreCase("SignUp")) {

//                String primaryKey = mDatabase.push().getKey();
                Date date = new Date();
                final UserModel userModel = new UserModel(userName, userEmail, userPassword, userContact, "false", new ArrayList<String>(), userSapId, userClientOrServer,"false","NA","NA",date.toString());

                mDatabase.child(userEmail.replace(".", getString(R.string.replacing_dot_in_firebase_db))).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(activity, "User already exists! Please try a different user or SignIn", Toast.LENGTH_SHORT).show();
                            bottomSheetBehavior.setHideable(true);
                            //Setting state to hideable without setting the above property could result in crash
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                            frameLayout.removeAllViews();
                            login_btns.setVisibility(View.VISIBLE);
                            btnSignIn.setVisibility(View.VISIBLE);
                            btnSignUp.setVisibility(View.VISIBLE);
                        } else {
                            mDatabase.child(userEmail.replace(".", getString(R.string.replacing_dot_in_firebase_db))).setValue(userModel);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                       alertDialogBuilderForAuthFail.setMessage(R.string.database_error_msg_for_alert);
                       textViewForAlertBuilder.setText(R.string.database_error_msg_for_alert);
                       alertDialogBuilderForAuthFail = alertBuilder.create();
                       alertDialogBuilderForAuthFail.show();
                    }
                });

//                mDatabase.child(userEmail).setValue(userModel);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            if (isAuthRequired) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(activity, "Please wait for Admins to Approve...", Toast.LENGTH_SHORT).show();
                                bottomSheetBehavior.setHideable(true);
                                //Setting state to hideable without setting the above property could result in crash
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                            frameLayout.removeAllViews();
                                login_btns.setVisibility(View.VISIBLE);
                                btnSignIn.setVisibility(View.VISIBLE);
                                btnSignUp.setVisibility(View.VISIBLE);
                            } else {
                                passUser();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        alertDialogBuilderForAuthFail.setMessage(R.string.database_error_msg_for_alert);
                        textViewForAlertBuilder.setText(R.string.database_error_msg_for_alert);
                        alertDialogBuilderForAuthFail = alertBuilder.create();
                        alertDialogBuilderForAuthFail.show();

                    }
                });
            } else if (behaviour.equalsIgnoreCase("Login")) {
                //Search Database, to match with fields
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectData((Map<String, Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        alertDialogBuilderForAuthFail.setMessage(R.string.database_error_msg_for_alert);
                        textViewForAlertBuilder.setText(R.string.database_error_msg_for_alert);
                        alertDialogBuilderForAuthFail = alertBuilder.create();
                        alertDialogBuilderForAuthFail.show();
                    }
                });

            }

        } else {
            //Not SignedIn
            Toast.makeText(activity, "User not connected to server...Login Again!!", Toast.LENGTH_SHORT).show();
        }
        if (isAuthRequired) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(activity, "Please wait for Admins to Approve...", Toast.LENGTH_SHORT).show();
            bottomSheetBehavior.setHideable(true);
            //Setting state to hideable without setting the above property could result in crash
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                            frameLayout.removeAllViews();
            login_btns.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.VISIBLE);
        }
    }

    private void passUser() {
        SharedPreferences.Editor edit = sharedprefs.edit();
        edit.putString(LoggedInUser, userEmail);
        edit.putString(LoggedInUserName, userName);
        edit.putString(LoggedInUserPassword, userPassword);
        edit.putBoolean(LOG_IN_MODE_IS_EXISTING_USER, false);
        edit.putString(USER_ROLE, "NA");
        edit.putString(USER_SAP_LISTS, "NA");
        edit.apply();
        SharedPreferences.Editor orderIdPrefsEdit = orderIdPrefs.edit();
        orderIdPrefsEdit.putString(SapId, userSapId);
        orderIdPrefsEdit.putString(ClientorServer, userClientOrServer);
        orderIdPrefsEdit.apply();
        url = strUrl;
        frameLayout.removeAllViewsInLayout();
        View view_otp = inflater.inflate(R.layout.activity_otp, null, false);
        view_otp.findViewById(R.id.approved_user_layout).setVisibility(View.GONE);
        view_otp.findViewById(R.id.otp_layout).setVisibility(View.GONE);
        view_otp.findViewById(R.id.loader_layout).setVisibility(View.VISIBLE);
        frameLayout.addView(view_otp);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(null);
        bottomSheetBehavior.setSkipCollapsed(false);
        bottomSheetBehavior.setPeekHeight(500);
        ((ImageView) findViewById(R.id.loader)).setBackgroundResource(R.drawable.frames_1);
        final AnimationDrawable animationDrawable = (AnimationDrawable) ((ImageView) findViewById(R.id.loader)).getBackground();
        animationDrawable.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationDrawable.stop();
                findViewById(R.id.loader_layout).setVisibility(View.GONE);
                findViewById(R.id.approved_user_layout).setVisibility(View.VISIBLE);
            }
        }, 2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, OrderStatus.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    private void collectData(Map<String, Object> value) {
        boolean foundUser = false;
        String name = "";
        String userRole = null;
        String sapLists = null;
        boolean isAuthorized = false;
        for (Map.Entry<String, Object> entrySet : value.entrySet()) {
            Map<String, Object> user = (Map<String, Object>) entrySet.getValue();
            if (((String) user.get("userEmail")).equalsIgnoreCase(etUserName_signIn.getText().toString().trim()) && ((String) user.get("userPass")).equalsIgnoreCase(etPassword_signIn.getText().toString().trim())) {
                //Successfully Matched Records....
                foundUser = true;
                if ((user.get("approved")!=null)) {
                    String valueFromServer = (String) user.get("approved");
                    if(valueFromServer.equalsIgnoreCase("true")) {
                        isAuthorized = true;
                    }
                }
                if(user.get("isAdmin")!=null) {
                    String isAdmin = ((String) user.get("isAdmin"));
                    if("true".equalsIgnoreCase(isAdmin)){
                        adminAccess = true;
                    }
                }else if(user.get("admin")!=null){
                    String isAdmin = ((String) user.get("admin"));
                    if("true".equalsIgnoreCase(isAdmin)){
                        adminAccess = true;
                    }
                }
                name = ((String) user.get("userName"));
                userSapId = ((String) user.get("sapId"));
                userClientOrServer = ((String) user.get("clientOrServer"));
                if(user.get("userRole")!=null){
                    userRole = (String) user.get("userRole");
                }
                if(user.get("sapIdList")!=null){
                    sapLists = (String) user.get("sapIdList");
                }
            }
        }
        if (foundUser && isAuthorized) {
            //User Found
            boolean isExistingUser = false;
            if(etUserName_signIn.getText().toString().trim().equalsIgnoreCase(sharedprefs.getString(LoggedInUser, null))){
                //User exists in record.
                isExistingUser = true;
            }
            SharedPreferences.Editor edit = sharedprefs.edit();
            edit.putString(LoggedInUser, etUserName_signIn.getText().toString().trim());
            edit.putString(LoggedInUserName, name);
            edit.putString(LoggedInUserPassword, etPassword_signIn.getText().toString().trim());
            edit.putBoolean(LOG_IN_MODE_IS_EXISTING_USER, isExistingUser);
            edit.putBoolean(ADMIN_ACCESS,adminAccess);
            edit.putString(USER_ROLE,userRole);
            edit.putString(USER_SAP_LISTS,sapLists);
            edit.apply();

            SharedPreferences.Editor orderIdPrefsEdit = orderIdPrefs.edit();
            orderIdPrefsEdit.putString(SapId, userSapId);
            orderIdPrefsEdit.putString(ClientorServer, userClientOrServer);
            orderIdPrefsEdit.apply();
            Intent intent = new Intent(LoginActivity.this, OrderStatus.class);
            startActivity(intent);
            finish();
        } else {
            //User Not Found....
            if (foundUser && !isAuthorized) {
                //Not Authorized
                loaderSignIn.setVisibility(View.GONE);
                btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sign_in));
                textSignIn.setVisibility(View.VISIBLE);
                Toast.makeText(activity, "Oops, User has not been authorized yet, contact Admin, or Try again later!", Toast.LENGTH_SHORT).show();
            } else {
                //User not found
                etUserName_signIn.requestFocus();
                etUserName_signIn.setError(getResources().getString(R.string.username_not_correct), getResources().getDrawable(R.drawable.error_24dp));
                loaderSignIn.setVisibility(View.GONE);
                btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sign_in));
                textSignIn.setVisibility(View.VISIBLE);
                Toast.makeText(activity, "Oops, we could'nt find you in our records...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        gifImageView = (ImageView) findViewById(R.id.gifLogin);
        frameLayout = (FrameLayout) findViewById(R.id.bottomSheet);
        btnSignIn = (Button) findViewById(R.id.btn_signIn);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
        mainLayout = (RelativeLayout) findViewById(R.id.activity_login);
        login_btns = (LinearLayout) findViewById(R.id.login_btn_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (sharedprefs.getString(LoggedInUser, null) != null && sharedprefs.getString(LoggedInUserPassword, null) != null) {
            /**
             * Some user is logged in...and we have sharedPrefs Data
             * 1.)If have fingerprint, then on successful scan, follow the same code as below, i.e
             *    paste the saved userCreds, and hit firebase
             * 2.)Else, just paste user id in field and wait for user to enter password, after which
             *    hit firebase .
             */

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.USE_FINGERPRINT},
                                MY_PERMISSIONS_REQUEST_FINGERPRINT);
                    } else {
                        checkForFingerPrintOnResume();
                    }
                } else {
                    checkForFingerPrintOnResume();
                }
            }else{
                inflatePasswordAutomaticSignIn(false);
            }

        }
        registerReceiver(smsDeliverBroadcast, intentFilter);

    }

    private void checkForFingerPrintOnResume() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                if (fingerprintManager.isHardwareDetected()) {
                    //FingerPrintCode
                    frameLayout.removeAllViewsInLayout();
                    View signIn_View = inflater.inflate(R.layout.activity_fingerprint, null, false);
                    tv_switch_password_mode = (TextView) signIn_View.findViewById(R.id.switchViewFingerPrint);
                    tv_switch_password_mode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inflatePasswordAutomaticSignIn(false);
                        }
                    });
                    et_fingerPrintError = (TextView) signIn_View.findViewById(R.id.errorText);
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        et_fingerPrintError.setText("Register at least one fingerprint in Settings");
                    }else{
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            et_fingerPrintError.setText("Lock screen security not enabled in Settings");
                        }else{
                            generateKey();


                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                FingerprintHandler helper = new FingerprintHandler(this,this);
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                    int width = View.MeasureSpec.makeMeasureSpec(signIn_View.getWidth(), View.MeasureSpec.UNSPECIFIED);
                    signIn_View.measure(width, View.MeasureSpec.UNSPECIFIED);
                    int height = signIn_View.getMeasuredHeight();
                    frameLayout.addView(signIn_View);
                    bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                    bottomSheetBehavior.setPeekHeight(height);
                    bottomSheetBehavior.setHideable(false);
                    bottomSheetBehavior.setBottomSheetCallback(null);
                    bottomSheetBehavior.setSkipCollapsed(false);

                }else {
                    inflatePasswordAutomaticSignIn(false);
                }
              }
            } else {
            inflatePasswordAutomaticSignIn(false);
            }
        }

    private void inflatePasswordAutomaticSignIn(boolean isFullSignInRequired) {
        frameLayout.removeAllViewsInLayout();
        View signIn_View = inflater.inflate(R.layout.activity_sign_in, null, false);
        final TextView tvChangeUser = (TextView) signIn_View.findViewById(R.id.tv_editUser);
        tvChangeUser.setVisibility(View.VISIBLE);
        etUserName_signIn = (EditText) signIn_View.findViewById(R.id.et_username);
        etPassword_signIn = (EditText) signIn_View.findViewById(R.id.et_password);
        btnLogin = (FrameLayout) signIn_View.findViewById(R.id.frameSignIn);
        loaderSignIn = (ImageView) signIn_View.findViewById(R.id.loaderSignIn);
        textSignIn = (TextView) signIn_View.findViewById(R.id.tvSignIn);
        etUserName_signIn.setText(sharedprefs.getString(LoggedInUser, null));
        etUserName_signIn.setEnabled(false);
        etUserName_signIn.setFocusable(false);
        btnLogin.setOnClickListener(this);
        tvChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUserName_signIn.setEnabled(true);
                etUserName_signIn.setFocusable(true);
                etUserName_signIn.setText("");
                etUserName_signIn.setFocusable(true);
                etUserName_signIn.setFocusableInTouchMode(true);
                SharedPreferences.Editor editor = sharedprefs.edit();
                editor.putString(LoggedInUser, null);
                editor.putString(LoggedInUserPassword, null);
                editor.apply();
                tvChangeUser.setVisibility(View.GONE);
            }
        });
        if(isFullSignInRequired) {

                etPassword_signIn.setText(sharedprefs.getString(LoggedInUserPassword,null));
                etPassword_signIn.setEnabled(false);
                etPassword_signIn.setFocusable(false);

                behaviour = "Login";
                userSignIn = etUserName_signIn.getText().toString().trim();
                passSignIn = etPassword_signIn.getText().toString().trim();
                if(userSignIn.equalsIgnoreCase("")){
                    etUserName_signIn.requestFocus();
                    etUserName_signIn.setError(getResources().getString(R.string.username_cannot_be_left_blank),getResources().getDrawable(R.drawable.error_24dp));
                    return;
                }else if(passSignIn.equalsIgnoreCase("")){
                    etPassword_signIn.requestFocus();
                    etPassword_signIn.setError(getResources().getString(R.string.password_cannot_be_left_blank),getResources().getDrawable(R.drawable.error_24dp));
                    return;
                }else{

                    if(connectionIsOnline()) {
                        btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sign_in_approved));
                        textSignIn.setVisibility(View.GONE);
                        loaderSignIn.setVisibility(View.VISIBLE);
                        GlideDrawableImageViewTarget glideDrawableImageViewTarget = new GlideDrawableImageViewTarget(loaderSignIn);
                        Glide.with(this).load(R.raw.rolling).into(glideDrawableImageViewTarget);
                        signAnonymousFirebaseUser(false);
                    }else{
                        if (sharedprefs.getString(LoggedInUser, null) != null && sharedprefs.getString(LoggedInUserPassword, null) != null) {
                            if(sharedprefs.getString(LoggedInUser, null).equalsIgnoreCase(etUserName_signIn.getText().toString().trim()) &&
                                    sharedprefs.getString(LoggedInUserPassword, null).equalsIgnoreCase(etPassword_signIn.getText().toString().trim())){
                                btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sign_in_approved));
                                textSignIn.setVisibility(View.GONE);
                                loaderSignIn.setVisibility(View.VISIBLE);
                                GlideDrawableImageViewTarget glideDrawableImageViewTarget = new GlideDrawableImageViewTarget(loaderSignIn);
                                Glide.with(this).load(R.raw.rolling).into(glideDrawableImageViewTarget);
                                signAnonymousFirebaseUser(false);
                            }else{
                                etUserName_signIn.setError(getString(R.string.user_offline_match));

                            }
                        }
                    }
                }
                btnLogin.performClick();
        }
        int width = View.MeasureSpec.makeMeasureSpec(signIn_View.getWidth(), View.MeasureSpec.UNSPECIFIED);
        signIn_View.measure(width, View.MeasureSpec.UNSPECIFIED);
        int height = signIn_View.getMeasuredHeight();
        frameLayout.addView(signIn_View);
        bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setPeekHeight(height);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(null);
        bottomSheetBehavior.setSkipCollapsed(false);

    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public void onClick(View view) {
      switch(view.getId()){
          case R.id.et_username:
              etUserName_signIn.requestFocus();
              break;
          case R.id.et_password:
              etPassword_signIn.requestFocus();
              break;
          case R.id.et_firstName:
              etFirstName_signUp.requestFocus();
              break;
          case R.id.et_lastName:
              etLastName_signUp.requestFocus();
              break;
          case R.id.et_Email:
              etEmail_signUp.requestFocus();
              break;
          case R.id.et_password_signUp:
              etPassword_signUp.requestFocus();
              break;
          case R.id.et_rePassword:
              etReEnterPass_signUp.requestFocus();
              break;
          case R.id.et_Contact:
              etContact_signUp.requestFocus();
              break;
          case R.id.btn_signIn:
              frameLayout.removeAllViewsInLayout();
              View signIn_View = inflater.inflate(R.layout.activity_sign_in,null,false);
              etUserName_signIn = (EditText) signIn_View.findViewById(R.id.et_username);
              etUserName_signIn.setOnClickListener(this);
              etPassword_signIn = (EditText) signIn_View.findViewById(R.id.et_password);
              etPassword_signIn.setOnClickListener(this);
              loaderSignIn = (ImageView) signIn_View.findViewById(R.id.loaderSignIn);
              textSignIn = (TextView) signIn_View.findViewById(R.id.tvSignIn);
              btnLogin = (FrameLayout) signIn_View.findViewById(R.id.frameSignIn);
              btnLogin.setOnClickListener(this);
              frameLayout.addView(signIn_View);
              bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
              addBottomSheetCallbacks(bottomSheetBehavior,"Login");
              bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
              break;
          case R.id.btn_signUp:
              frameLayout.removeAllViewsInLayout();
              View signUp_View = inflater.inflate(R.layout.activity_sign_up,null,false);
              orRegisterVia = (LinearLayout) signUp_View.findViewById(R.id.ll_orRegisterVia);
              googleSignIn = (ImageView) signUp_View.findViewById(R.id.google_signIn);
              googleSignIn.setOnClickListener(this);
              frameLayout.addView(signUp_View);
              etFirstName_signUp = (EditText) findViewById(R.id.et_firstName);
              etFirstName_signUp.setOnClickListener(this);
              animation = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.blink);
              btn_signUser = (Button) findViewById(R.id.btn_signUser);
              ((Button) findViewById(R.id.btn_signUser)).setAnimation(animation);
              ((Button) findViewById(R.id.btn_signUser)).setOnClickListener(this);
              animation.start();
              etLastName_signUp = (EditText) findViewById(R.id.et_lastName);
              etLastName_signUp.setOnClickListener(this);
              etEmail_signUp = (EditText) findViewById(R.id.et_Email);
              etEmail_signUp.setOnClickListener(this);
              etPassword_signUp = (EditText) findViewById(R.id.et_password_signUp);
              etPassword_signUp.setOnClickListener(this);
              etReEnterPass_signUp = (EditText) findViewById(R.id.et_rePassword);
              etReEnterPass_signUp.setOnClickListener(this);
              etSapId = (EditText) findViewById(R.id.et_SapID);
              radioClient = (RadioButton) findViewById(R.id.radio_signup_client);
              radioSales = (RadioButton) findViewById(R.id.radio_signup_sales_executive);
              etContact_signUp = (EditText) findViewById(R.id.et_Contact);
              etContact_signUp.setOnClickListener(this);
              bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
              addBottomSheetCallbacks(bottomSheetBehavior,"Login");
              bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
              break;
          case R.id.btn_signUser:
              if(!etFirstName_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                      !etLastName_signUp.getText().toString().trim().equalsIgnoreCase("") &&
                      !etEmail_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                      !etPassword_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                      !etReEnterPass_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                      !etContact_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                      !etSapId.getText().toString().trim().equalsIgnoreCase("")&&
                      etPassword_signUp.getText().toString().trim().equals(etReEnterPass_signUp.getText().toString().trim()) && connectionIsOnline()){
                  animation.cancel();
                  frameLayout.removeAllViewsInLayout();
                  userName = etFirstName_signUp.getText().toString().trim()+" "+etLastName_signUp.getText().toString().trim();
                  userEmail = etEmail_signUp.getText().toString().trim();
                  userPassword = etPassword_signUp.getText().toString().trim();
                  userContact = etContact_signUp.getText().toString().trim();
                  userSapId = etSapId.getText().toString().trim();
                  if(radioClient.isChecked()){
                      userClientOrServer = "c";
                  }else if(radioSales.isChecked()){
                      userClientOrServer = "s";
                  }
                  View view_otp = inflater.inflate(R.layout.activity_otp,null,false);
                  view_otp.findViewById(R.id.approved_user_layout).setVisibility(View.GONE);
                  frameLayout.addView(view_otp);
                  llOtpLayout = (LinearLayout) findViewById(R.id.otp_layout);
                  rlLoaderLayout = (RelativeLayout) findViewById(R.id.loader_layout);
                  llOtpLayout.setVisibility(View.GONE);
                  rlLoaderLayout.setVisibility(View.VISIBLE);
                  sendOtp = (EditText) findViewById(R.id.et_otp);
                  btnOtpApprove = (Button) findViewById(R.id.btn_otp);
                  btnOtpApprove.setOnClickListener(this);
                  bottomSheetBehavior.setHideable(false);
                  bottomSheetBehavior.setBottomSheetCallback(null);
                  bottomSheetBehavior.setSkipCollapsed(false);
                  bottomSheetBehavior.setPeekHeight(500);
                  ((ImageView)findViewById(R.id.loader)).setBackgroundResource(R.drawable.frames_1);
                  otpReceiveAnimationDrawable = (AnimationDrawable) ((ImageView)findViewById(R.id.loader)).getBackground();
                  otpReceiveAnimationDrawable.start();
                  TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                  if(telephonyManager!=null && !telephonyManager.getNetworkOperatorName().equalsIgnoreCase("") && !telephonyManager.getNetworkOperator().equalsIgnoreCase("")){
                      //Has a network...
                      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                          sendSms();
                      }else{
                          SmsManager smsManager = SmsManager.getDefault();
                          //Todo: Problem when sim is at Always Ask
                          PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginActivity.this, 1, new Intent(RECEIVE_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
                          smsManager.sendTextMessage(etContact_signUp.getText().toString().trim(), null , otpGeneratedValue, null, pendingIntent);

                      }

                  }else{
                      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                          sendEmail();
                      }else {
                          ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                          NetworkInfo wifi =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                          NetworkInfo mobileNet =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                          if(wifi.isConnected() || mobileNet.isConnected()) {
                              GmailSender gmailSender = new GmailSender(mFirebaseRemoteConfig.getString("master_email"), mFirebaseRemoteConfig.getString("master_pass"),LoginActivity.this);
                              try {
                                  gmailSender.sendMail("VACMET-Otp", otpGeneratedValue, mFirebaseRemoteConfig.getString("master_email"), etEmail_signUp.getText().toString().trim());
                                  /**
                                   * HANDLE FROM HERE NOW>>>>>>>>>>>>>>>>>Getting Exception
                                   */
                                  otpReceiveAnimationDrawable.stop();
                                  rlLoaderLayout.setVisibility(View.GONE);
                                  llOtpLayout.setVisibility(View.VISIBLE);
                              } catch (Exception e) {
                                  e.printStackTrace();
                                  rlLoaderLayout.setVisibility(View.GONE);
                                  Toast.makeText(activity, "Error Sending Mail,try again...", Toast.LENGTH_SHORT).show();

                              }
                          }else{
                              Toast.makeText(activity, "Please check your mobile-network/wifi, and try again...", Toast.LENGTH_SHORT).show();
                              textViewForAlertBuilder.setText(R.string.network_error_msg_alert);
                              alertDialogBuilderForAuthFail = alertBuilder.create();
                              alertDialogBuilderForAuthFail.show();

                          }
                      }
                  }
              }if(!connectionIsOnline()){
              textViewForAlertBuilder.setText(R.string.network_error_msg_alert);
              alertDialogBuilderForAuthFail = alertBuilder.create();
                alertDialogBuilderForAuthFail.show();
              }if(etFirstName_signUp.getText().toString().trim().equalsIgnoreCase("")){
                  etFirstName_signUp.setHintTextColor(Color.RED);
              }if(etLastName_signUp.getText().toString().trim().equalsIgnoreCase("")){
                  etLastName_signUp.setHintTextColor(Color.RED);
              }if(etEmail_signUp.getText().toString().trim().equalsIgnoreCase("")){
                  etEmail_signUp.setHintTextColor(Color.RED);
              }if(etPassword_signUp.getText().toString().trim().equalsIgnoreCase("")){
                  etPassword_signUp.setHintTextColor(Color.RED);
              }if(etReEnterPass_signUp.getText().toString().trim().equalsIgnoreCase("")){
                  etReEnterPass_signUp.setHintTextColor(Color.RED);
              }if(etContact_signUp.getText().toString().trim().equalsIgnoreCase("")){
                  etContact_signUp.setHintTextColor(Color.RED);
              } if(!etFirstName_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                  !etLastName_signUp.getText().toString().trim().equalsIgnoreCase("") &&
                  !etEmail_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                  !etPassword_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                  !etReEnterPass_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                  !etContact_signUp.getText().toString().trim().equalsIgnoreCase("")&&
                  !etPassword_signUp.getText().toString().trim().equals(etReEnterPass_signUp.getText().toString().trim())){
                    etReEnterPass_signUp.setError(getResources().getString(R.string.password_not_match),getResources().getDrawable(R.drawable.error_24dp));
                    etReEnterPass_signUp.requestFocus();
              }


              break;
          case R.id.btn_otp:
              if(sendOtp.getText().toString().trim().equalsIgnoreCase(otpGeneratedValue)){
                  progressDialog = new ProgressDialog(LoginActivity.this);
                  progressDialog.setMessage("Sending your Info securely to Admin! Please Wait...");
                  progressDialog.setCanceledOnTouchOutside(false);
                  progressDialog.setCancelable(false);
                  progressDialog.show();
                  TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                  if(telephonyManager!=null && !telephonyManager.getNetworkOperatorName().equalsIgnoreCase("") && !telephonyManager.getNetworkOperator().equalsIgnoreCase("")){
                      //Has a network...
                      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                          sendSmsToAdmin();
                      }else{
                          SmsManager smsManager = SmsManager.getDefault();
//                          PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginActivity.this, 1, new Intent(RECEIVE_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
                          smsManager.sendTextMessage(getString(R.string.AdminPhoneNo), null , userName+"\n"+userClientOrServer+" "+"with Sap #:"+userSapId+"\n"+getString(R.string.requestingAuth), null, null);

                      }

                  }else{
                      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                          sendEmailToAdmin();
                      }else {
                          ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                          NetworkInfo wifi =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                          NetworkInfo mobileNet =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                          if(wifi.isConnected() || mobileNet.isConnected()) {
                              GmailSender gmailSender = new GmailSender(mFirebaseRemoteConfig.getString("master_email"), mFirebaseRemoteConfig.getString("master_pass"),LoginActivity.this);
                              try {
                                  gmailSender.sendMail("Requesting Authorization For Vacmet Application", userName+" ( "+userEmail+" )"+"\n"+userClientOrServer+" "+"with Sap #:"+userSapId+"\n"+getString(R.string.requestingAuth), mFirebaseRemoteConfig.getString("master_email"), getString(R.string.adminEmails));

                              } catch (Exception e) {
                                  e.printStackTrace();
                                  Toast.makeText(activity, "Error Sending Mail,try again...", Toast.LENGTH_SHORT).show();

                              }
                          }else{
                              Toast.makeText(activity, "Please check your mobile-network/wifi, and try again...", Toast.LENGTH_SHORT).show();

                          }
                      }
                  }


                  behaviour = "SignUp";
                  signAnonymousFirebaseUser(true);

              }else{
                  sendOtp.setError(getResources().getString(R.string.otp_not_matched),getResources().getDrawable(R.drawable.error_24dp));
                  sendOtp.requestFocus();
              }
              break;
          case R.id.frameSignIn:
              /**
               * Using Dummy Data for Login Here..could be from service later on...
               */
              behaviour = "Login";
              userSignIn = etUserName_signIn.getText().toString().trim();
              passSignIn = etPassword_signIn.getText().toString().trim();
              if(userSignIn.equalsIgnoreCase("")){
                  etUserName_signIn.requestFocus();
                  etUserName_signIn.setError(getResources().getString(R.string.username_cannot_be_left_blank),getResources().getDrawable(R.drawable.error_24dp));
                  return;
              }else if(passSignIn.equalsIgnoreCase("")){
                  etPassword_signIn.requestFocus();
                  etPassword_signIn.setError(getResources().getString(R.string.password_cannot_be_left_blank),getResources().getDrawable(R.drawable.error_24dp));
                  return;
              }else{

                  if(connectionIsOnline()) {
                      btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sign_in_approved));
                      textSignIn.setVisibility(View.GONE);
                      loaderSignIn.setVisibility(View.VISIBLE);
                      GlideDrawableImageViewTarget glideDrawableImageViewTarget = new GlideDrawableImageViewTarget(loaderSignIn);
                      Glide.with(this).load(R.raw.rolling).into(glideDrawableImageViewTarget);
                      signAnonymousFirebaseUser(false);
                  }else{
                      if (sharedprefs.getString(LoggedInUser, null) != null && sharedprefs.getString(LoggedInUserPassword, null) != null) {
                          if(sharedprefs.getString(LoggedInUser, null).equalsIgnoreCase(etUserName_signIn.getText().toString().trim()) &&
                                  sharedprefs.getString(LoggedInUserPassword, null).equalsIgnoreCase(etPassword_signIn.getText().toString().trim())){
                              btnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_sign_in_approved));
                              textSignIn.setVisibility(View.GONE);
                              loaderSignIn.setVisibility(View.VISIBLE);
                              GlideDrawableImageViewTarget glideDrawableImageViewTarget = new GlideDrawableImageViewTarget(loaderSignIn);
                              Glide.with(this).load(R.raw.rolling).into(glideDrawableImageViewTarget);
                              signAnonymousFirebaseUser(false);
                          }else{
                              etUserName_signIn.setError(getString(R.string.user_offline_match));
                          }
                      }else{
                          textViewForAlertBuilder.setText(R.string.first_time_offline_logine_msg);
                          alertDialogBuilderForAuthFail = alertBuilder.create();
                          alertDialogBuilderForAuthFail.show();
                      }
                  }
              }

              break;
          case R.id.activity_login:
              onKeyboardDown(mainLayout);
              break;
          case R.id.google_signIn:
              dialog = new Dialog(LoginActivity.this);
              dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
              dialog.setContentView(R.layout.loader);
              dialog.setCancelable(false);
              dialog.findViewById(R.id.iv_loader).setBackgroundResource(R.drawable.frames_1);
              gmailAnimationDrawable = (AnimationDrawable) dialog.findViewById(R.id.iv_loader).getBackground();
              gmailAnimationDrawable.start();
              dialog.show();
              Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
              startActivityForResult(intent,GOOGLE_SIGN_IN);

              break;
          case R.id.gifLogin:
              onKeyboardDown(gifImageView);
              break;
      }
    }

    private void signAnonymousFirebaseUser(final boolean isAuthRequired) {

        if(connectionIsOnline()) {
            if(firebaseAuth == null){
                initializeFirebaseAuth();
            }
            firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Log.d("LoginActivity", "signInAnonymously", task.getException());
                        textViewForAlertBuilder.setText(R.string.network_error_msg_alert);
                        alertDialogBuilderForAuthFail = alertBuilder.create();
                        alertDialogBuilderForAuthFail.show();

                    } else if (task.isSuccessful()) {
                        if (isAuthRequired) {
                            updateUI(task.getResult().getUser(), true);
                        } else {
                            updateUI(task.getResult().getUser(), false);
                        }

                    }
                }
            });
        }else{

            /**
             *
             *Just Sign In, as sharedPrefs would be saved already
             */
            SharedPreferences.Editor edit = sharedprefs.edit();
            edit.putBoolean(LOG_IN_MODE_IS_EXISTING_USER, true);
            edit.apply();
            Intent intent = new Intent(LoginActivity.this, OrderStatus.class);
            startActivity(intent);
            finish();
        }
    }

    @TargetApi(23)
    private void sendEmail() {
        if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NETWORK_STATE)) {
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);
            }
        }else{
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifi =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileNet =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(wifi.isConnected() || mobileNet.isConnected()) {
                GmailSender gmailSender = new GmailSender(mFirebaseRemoteConfig.getString("master_email"), mFirebaseRemoteConfig.getString("master_pass"),LoginActivity.this);
                try {
                    gmailSender.sendMail("VACMET-Otp", otpGeneratedValue, mFirebaseRemoteConfig.getString("master_email"), etEmail_signUp.getText().toString().trim());
                    /**
                     * HANDLE FROM HERE NOW>>>>>>>>>>>>>>>>>Getting Exception
                     */
                    otpReceiveAnimationDrawable.stop();
                    rlLoaderLayout.setVisibility(View.GONE);
                    llOtpLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    rlLoaderLayout.setVisibility(View.GONE);
                    Toast.makeText(activity, "Error Sending Mail,try again...", Toast.LENGTH_SHORT).show();

                }
            }else{
                Toast.makeText(activity, "Please check your mobile-network/wifi, and try again...", Toast.LENGTH_SHORT).show();

            }

        }

    }

    @TargetApi(23)
    private void sendEmailToAdmin() {
        if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NETWORK_STATE)) {
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);
            }
        }else{
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifi =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileNet =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(wifi.isConnected() || mobileNet.isConnected()) {
                GmailSender gmailSender = new GmailSender(mFirebaseRemoteConfig.getString("master_email"), mFirebaseRemoteConfig.getString("master_pass"),LoginActivity.this);
                try {
                    gmailSender.sendMail("Requesting Authorization For Vacmet Application", userName+" ( "+userEmail+" )"+"\n"+userClientOrServer+" "+"with Sap #:"+userSapId+"\n"+getString(R.string.requestingAuth)+"\n\n\n\n\n"+getResources().getString(R.string.visit_app_in_mail), mFirebaseRemoteConfig.getString("master_email"), mFirebaseRemoteConfig.getString("master_receiver_emails"));

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Error Sending Mail,try again...", Toast.LENGTH_SHORT).show();

                }
            }else{
                Toast.makeText(activity, "Please check your mobile-network/wifi, and try again...", Toast.LENGTH_SHORT).show();

            }

        }

    }

    @TargetApi(23)
    private void sendSms() {
        if (checkSelfPermission(android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS)) {
            } else {
                requestPermissions(new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }else if(checkSelfPermission(android.Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED){
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginActivity.this, 1, new Intent(RECEIVE_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
            smsManager.sendTextMessage(etContact_signUp.getText().toString().trim(), null , otpGeneratedValue, null, pendingIntent);

        }else{
            requestPermissions(new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
    }

    @TargetApi(23)
    private void sendSmsToAdmin() {
        if (checkSelfPermission(android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS)) {
            } else {
                requestPermissions(new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }else if(checkSelfPermission(android.Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mFirebaseRemoteConfig.getString("master_admin_cell_no"), null , userName+"\n"+userClientOrServer+" "+"with Sap #:"+userSapId+"\n"+getString(R.string.requestingAuth)+"\n\n\n\n\n"+getResources().getString(R.string.visit_app_in_mail), null, null);

        }else{
            requestPermissions(new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginActivity.this, 1, new Intent(RECEIVE_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
                    smsManager.sendTextMessage(etContact_signUp.getText().toString().trim(), null , otpGeneratedValue, null, pendingIntent);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_FINGERPRINT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkForFingerPrintOnResume();
                } else {
                    requestPermissions(new String[]{Manifest.permission.USE_FINGERPRINT},
                            MY_PERMISSIONS_REQUEST_FINGERPRINT);
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo wifi =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo mobileNet =  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if(wifi.isConnected() || mobileNet.isConnected()) {
                        GmailSender gmailSender = new GmailSender(mFirebaseRemoteConfig.getString("master_email"), mFirebaseRemoteConfig.getString("master_pass"), LoginActivity.this);
                        try {
                            gmailSender.sendMail("VACMET-Otp", otpGeneratedValue, mFirebaseRemoteConfig.getString("master_email"), etEmail_signUp.getText().toString().trim());
                            /**
                             * HANDLE FROM HERE NOW>>>>>>>>>>>>>>>>>Getting Exception
                             */
                            otpReceiveAnimationDrawable.stop();
                            rlLoaderLayout.setVisibility(View.GONE);
                            llOtpLayout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            rlLoaderLayout.setVisibility(View.GONE);
                            Toast.makeText(activity, "Error Sending Mail,try again...", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        Toast.makeText(activity, "Please check your mobile-network/wifi, and try again...", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                            MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);
                }
            }
            break;
        }
    }

    private void addBottomSheetCallbacks(final BottomSheetBehavior bottomSheetBehavior, final String state) {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(!state.equalsIgnoreCase("Approved")) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        btnSignIn.setVisibility(View.INVISIBLE);
                        btnSignUp.setVisibility(View.INVISIBLE);
                    } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        btnSignIn.setVisibility(View.VISIBLE);
                        btnSignUp.setVisibility(View.VISIBLE);
                    }
                    bottomSheetBehavior.setSkipCollapsed(true);
                }else{
                    bottomSheetBehavior.setSkipCollapsed(false);
                    bottomSheetBehavior.setPeekHeight(150);
                    login_btns.setVisibility(View.GONE);
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {

                    }
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(connectionIsOnline()) {
            if(firebaseAuth!=null) {
                firebaseAuth.addAuthStateListener(authStateListener);
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsDeliverBroadcast);
        if(connectionIsOnline()) {
            if(firebaseAuth!=null) {
                firebaseAuth.removeAuthStateListener(authStateListener);
            }
        }

    }
    public void onKeyboardDown(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            //Keyboard is There. Hence Hide.
          /*  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            inputMethodManager.hideSoftInputFromInputMethod(v.getWindowToken(),0);*/
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

//to hide it, call the method again
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            inputMethodManager.hideSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(),0);
        }
        else if(bottomSheetBehavior!=null &&(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)){
            if(frameLayout!=null && frameLayout.findViewById(R.id.ll_root_otp_layout) == null) {
                frameLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        //Todo: Crashing here
                        bottomSheetBehavior.setSkipCollapsed(true);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                });
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        textViewForAlertBuilder.setText(R.string.network_error_msg_alert);
        alertDialogBuilderForAuthFail = alertBuilder.create();
        alertDialogBuilderForAuthFail.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GOOGLE_SIGN_IN:
                GoogleSignInResult googleSignInResult =  Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(googleSignInResult.isSuccess()){
                    if(googleSignInResult.getSignInAccount().getDisplayName()!= null) {
                        String name = googleSignInResult.getSignInAccount().getDisplayName();
                        String[] nameparts = name.split(" ");
                        etFirstName_signUp.setText(nameparts[0]);
                        etLastName_signUp.setText(nameparts[nameparts.length-1]);
                    }
                    if(googleSignInResult.getSignInAccount().getPhotoUrl()!=null){
                        strUrl = googleSignInResult.getSignInAccount().getPhotoUrl().toString();

                    }
                    if(googleSignInResult.getSignInAccount().getEmail()!=null){
                        etEmail_signUp.setText(googleSignInResult.getSignInAccount().getEmail());
                    }
                     googleSignIn.setVisibility(View.GONE);
                     orRegisterVia.setVisibility(View.GONE);
                     gmailAnimationDrawable.stop();
                     dialog.dismiss();
                }else{
                    Toast.makeText(activity, "There seems a problem connecting with your Google Account...", Toast.LENGTH_SHORT).show();
                    gmailAnimationDrawable.stop();
                    dialog.dismiss();
                }
                break;
        }
    }

    @Override
    public void onFingerPrintSuccess() {
        inflatePasswordAutomaticSignIn(true);
    }

    private boolean connectionIsOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }

    }

    private class SmsBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * you could check request codes via getRequestCode(), but we are using for Deliver Intent- in this case
             * we do not have much request codes...could be used for Send Intents...
             */
            otpReceiveAnimationDrawable.stop();
            rlLoaderLayout.setVisibility(View.GONE);
            llOtpLayout.setVisibility(View.VISIBLE);

        }
    }
}
