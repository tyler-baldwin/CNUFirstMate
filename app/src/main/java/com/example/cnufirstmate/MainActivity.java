package com.example.cnufirstmate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cnufirstmate.ui.workOrder.AdminActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private int RC_SIGN_IN = 1001;
    private int ADMIN_INT = 1002;

    SignInButton signInButton;

    private AppBarConfiguration mAppBarConfiguration;
    private GoogleSignInClient mGoogleSignInClient;
    private String name;
    public static String email;
    private FirebaseFirestore db;
    GoogleSignInAccount account;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirestore();
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            setContentView(R.layout.sign_in);
            findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.sign_in_button:
                            doSignIn();
                            break;
                    }
                }
            });
        } else {
            setupWorkOrder(account);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        account = GoogleSignIn.getLastSignedInAccount(this);


    }

    public void setUpLogIn() {
        setContentView(R.layout.sign_in);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        doSignIn();
                        break;
                }
            }
        });
    }


    public void setupWorkOrder(GoogleSignInAccount acc) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            Button fab = findViewById(R.id.wosubmit);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Submitted Work Order", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    submitOrder();
                }
            });
        } catch (Exception e) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                navController.getGraph())
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


//        account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(acc);
    }


    private void updateUI(GoogleSignInAccount acc) {
        //sometimes it is slow to inflate so this can catch those instances
        try {
            if (acc != null) {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                TextView loginName = headerView.findViewById(R.id.loginName);
                TextView loginEmail = (TextView) headerView.findViewById(R.id.loginEmail);
                ImageView loginURL = headerView.findViewById(R.id.loginURL);
                String personName = acc.getDisplayName();
                String personEmail = acc.getEmail();
                name = personName;
                email = personEmail;
                Uri personURL = acc.getPhotoUrl();
                loginName.setText(personName);
                loginEmail.setText(personEmail);
                loginURL.setImageURI(null);
                loginURL.setImageURI(personURL);
            } else {
                Toast.makeText(getApplicationContext(), "No Account Somehow", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "bruh moment inflator slow", Toast.LENGTH_LONG).show();
        }
    }

    private void saveUser(String uid) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("uid", uid);
        orderMap.put("name", name);
        orderMap.put("email", email);

    }

    //this method submits the order to the nosql database for review by admins
    private void submitOrder() {
        Map<String, Object> orderMap = new HashMap<>();
        Spinner buildingSpinner = (Spinner) findViewById(R.id.reshall_spinner);
        String buildingText = (String) buildingSpinner.getSelectedItem();
        TextInputLayout room = findViewById(R.id.room);
        String roomText = room.getEditText().getText().toString();
        TextInputLayout issue = findViewById(R.id.issue);
        String issueText = issue.getEditText().getText().toString();
        Date currentTime = Calendar.getInstance().getTime();
        orderMap.put("name", name);
        orderMap.put("email", email);
        orderMap.put("date", currentTime);
        orderMap.put("building", buildingText);
        orderMap.put("room", roomText);
        orderMap.put("issue", issueText);
        // Add a new document with a generated ID
        db.collection("orders")
                .add(orderMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    //this is important to get an instance of the database
    private void initFirestore() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, need listener
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
//                saveUser(account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("sif", "Google sign in failed", e);
                // ...
            }
            handleSignInResult(task);
//            setupWorkOrder(account);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount accTask = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            setupWorkOrder(accTask);
//            updateUI(accTask);
        } catch (ApiException e) {
            Log.w("Signing in", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
//        GoogleSignInAccount startAcc = GoogleSignIn.getLastSignedInAccount(this);
//        if (startAcc == null) {
//            setContentView(R.layout.sign_in);
//            findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    switch (view.getId()) {
//                        case R.id.sign_in_button:
//                            doSignIn();
//                            break;
//                    }
//                }
//            });
//        } else {
//            setupWorkOrder(startAcc);
//        }
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TA", "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                logOut();
                break;
            case R.id.admin:
                adminStart();
                break;
            case android.R.id.home:
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                        || super.onSupportNavigateUp();

        }
        return true;
    }
//starts sign in activity
    public void doSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void adminStart(){
        Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
        startActivityForResult(adminIntent, ADMIN_INT );
    }

    //logs out of current gmail acc
    private void logOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        account = null;
                        name = null;
                        email = null;
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }


}
