package com.tutorial.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity {

    //Defining view objects
    private EditText TxtEmail;
    private EditText TxtPassword;
    private EditText TxtNombres;
    private EditText TxtApellidos;
    private EditText TxtFechaNacimiento;
    private EditText TxtCelular;
    private Button btnRegistrar, btnLogin;
    private ProgressDialog progressDialog;

    //Declaramos el objeto Firebase
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Iniciamos el objeto firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //Referenciamos los views
        TxtEmail = (EditText) findViewById(R.id.txtEmail);
        TxtPassword = (EditText) findViewById(R.id.txtPassword);
        btnRegistrar = (Button) findViewById(R.id.bntRegistrar);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(this);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loguearUsuario();
            }
        });
    }
    private void registrarUsuario(){
        //Obtenemos el email y la contraseña desde la caja de texto
        String email = TxtEmail.getText().toString().trim();
        String password = TxtPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacias
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Ingrese porfavor Email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Ingresar Porfavor Contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Realizando Registro en linea");
        progressDialog.show();

        //Registramos un nuevo Usuario
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){

                 Toast.makeText(MainActivity.this,"Registro Exitoso ", Toast.LENGTH_LONG).show();
             }else{

                 if(task.getException() instanceof FirebaseAuthUserCollisionException){
                     Toast.makeText(MainActivity.this,"Usuario ya existe ",Toast.LENGTH_SHORT).show();
                 }else{
                     Toast.makeText(MainActivity.this,"Ups Algo salio mal, no se registro ",Toast.LENGTH_LONG).show();
                 }

             }
             progressDialog.dismiss();
            }
        });
    }

    private void loguearUsuario(){
        //Obtenemos el email y la contraseña desde la caja de texto
        final String email = TxtEmail.getText().toString().trim();
        String password = TxtPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacias
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Ingrese porfavor Email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Ingresar Porfavor Contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Ingresando");
        progressDialog.show();

        //Loguear Usuario
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    int pos = email.indexOf("@");
                    String user = email.substring(0,pos);

                    Toast.makeText(MainActivity.this,"Logueo Exitoso ", Toast.LENGTH_LONG).show();
                    Intent intencion = new Intent(getApplication(),ActivityWelcome.class);
                    intencion.putExtra(ActivityWelcome.user,user);
                    startActivity(intencion);


                }else{

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(MainActivity.this,"Logueo Mal ",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,"Ups Algo salio mal, no se registro",Toast.LENGTH_LONG).show();
                    }

                }
                progressDialog.dismiss();
            }
        });
    }

}
