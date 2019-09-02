package com.tutorial.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    //Defining view objects
    private EditText TxtEmail,TxtPassword;
    private Button btnRegistrar, btnLogin;
    private ProgressDialog progressDialog;
    LoginButton loginButton;

    //Declaramos el objeto Firebase
    private FirebaseAuth firebaseAuth;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        //Iniciamos el objeto firebase
        firebaseAuth = FirebaseAuth.getInstance();

        callbackManager=CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //Referenciamos los views
        TxtEmail = (EditText) findViewById(R.id.txtEmail);
        TxtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrar = (Button) findViewById(R.id.btn_Register);

        progressDialog = new ProgressDialog(this);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loguearUsuario();
            }
        });

        //Para imprimir la llave para colocarlo en el Facebook Developers
        printKeyHash();
    }

    private void registrar() {
        Intent intencion = new Intent(getApplication(),RegisterActivity.class);
        startActivity(intencion);

    }

    private void signIn() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential  = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("ERROR_EDMT",""+e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Aqui es donde se obtiene el usuario o email, como tu quieras
                String email = authResult.getUser().getEmail();
                String user = authResult.getUser().getDisplayName();

                //Toast.makeText(MainActivity.this,"Te logeaste con el email: "+email, Toast.LENGTH_LONG).show();
                Intent intencion = new Intent(getApplication(),StartActivity.class);
                intencion.putExtra(StartActivity.user,user);
                startActivity(intencion);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.tutorial.firebase",PackageManager.GET_SIGNATURES);

            for (Signature signature:info.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }

        }catch (PackageManager.NameNotFoundException e){
           e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void loguearUsuario(){
        //Obtenemos el email y la contraseña desde la caja de texto
        final String email = TxtEmail.getText().toString().trim();
        String password = TxtPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacias
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Ingrese Email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Ingresar Contraseña ", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Ingresando...");
        progressDialog.show();

        //Loguear Usuario
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    int pos = email.indexOf("@");
                    String userFI = email.substring(0,pos);

                    Toast.makeText(MainActivity.this,"Logueo Exitoso ", Toast.LENGTH_LONG).show();
                    //Aqui mandas a la otra ventana
                    Intent intencion = new Intent(getApplication(),StartActivity.class);
                    //Aqui mandas el dato capturado a la variable de la vista la cual vas a cambiar
                    intencion.putExtra(StartActivity.user,userFI);
                    startActivity(intencion);

                }else{

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(MainActivity.this,"Se logueo mal ",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,"Ups! Algo salio mal, no se pudo ingresar",Toast.LENGTH_LONG).show();
                    }

                }
                progressDialog.dismiss();
            }
        });
    }
}
