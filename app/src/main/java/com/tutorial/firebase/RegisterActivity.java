package com.tutorial.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class RegisterActivity extends AppCompatActivity {

    //Defining view objects
    private EditText TxtEmail,TxtPassword,btnRegister;
    private Button btnRegistrar, btnLogin;
    private EditText TxtNombres,TxtApellidos,TxtFechaNacimiento,TxtCelular;

    //Declaramos el objeto Firebase
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Iniciamos el objeto firebase
        firebaseAuth = FirebaseAuth.getInstance();

        btnRegistrar = (Button) findViewById(R.id.bntRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
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

                    Toast.makeText(RegisterActivity.this,"Registro Exitoso ", Toast.LENGTH_LONG).show();
                }else{

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(RegisterActivity.this,"Usuario ya existe ",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this,"Ups Algo salio mal, no se registro ",Toast.LENGTH_LONG).show();
                    }

                }
                progressDialog.dismiss();
            }
        });
    }
}
