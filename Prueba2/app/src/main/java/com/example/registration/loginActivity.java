package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class loginActivity extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;


    final String TAG = "LoginActivity";
    int RC_SIGN_IN =1;
    String TAG1 = "GoogleSignIn";

    //TextView lblCrearCuenta;
    //EditText txtInputEmail, txtInputPassword;
    //Button btnLogin;
    Button btnGoogle;

    private ProgressDialog mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_login);
//        txtInputEmail = findViewById(R.id.inputEmail);
//        txtInputPassword = findViewById(R.id.inputPassword);
        //btnLogin = findViewById(R.id.btnlogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        //lblCrearCuenta = findViewById(R.id.txtNotieneCuenta);

//        lblCrearCuenta.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(loginActivity.this,RegisterActivity.class));
//            }
//        });

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                verificarCredenciales();
//            }
//        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresar();
            }
        });

        mProgressBar = new ProgressDialog(loginActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mfirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Resultado devuelto al iniciar el Intent de GoogleSignInApi.getSignInIntent (...);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG1, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In fallido, actualizar GUI
                    Log.w(TAG1, "Google sign in failed", e);
                }
            } else {
                Log.d(TAG1, "Error, login no exitoso:" + task.getException().toString());
                Toast.makeText(this, "Ocurrio un error. " + task.getException().toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mfirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    //FirebaseUser user = mAuth.getCurrentUser();
                    //Iniciar DASHBOARD u otra actividad luego del SigIn Exitoso
                    startActivity(new Intent(loginActivity.this, MainActivity.class));
                    loginActivity.this.finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    public void ingresar(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart(){
        FirebaseUser user = mfirebaseAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(loginActivity.this, MainActivity.class));
        }
        super.onStart();
    }


//    public void verificarCredenciales(){
//        //String email = txtInputEmail.getText().toString();
//        //String password = txtInputPassword.getText().toString();
//        //if(email.isEmpty() || !email.contains("@")){
////            showError(txtInputEmail, "Email no valido");
////        }else if(password.isEmpty()|| password.length()<7){
////            showError(txtInputPassword, "Password invalida");
////        }else{
//            //Mostrar ProgressBar
//            mProgressBar.setTitle("Login");
//            mProgressBar.setMessage("Iniciando sesión, espere un momento..");
//            mProgressBar.setCanceledOnTouchOutside(false);
//            mProgressBar.show();
//            //Registrar usuario
//            //Exitoso -> Mostrar toast
//            //redireccionar - intent a login
//            Intent intent = new Intent(loginActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            //ocultar progressBar
//            mProgressBar.dismiss();
//        }
//    }

    private void showError(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

}