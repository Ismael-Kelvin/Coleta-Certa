package com.example.coletacerta.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.coletacerta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText edt_txt_email;
    private EditText edt_txt_senha;
    private Button btt_acessar;
    private CheckBox cb_mostrar_senha;
    private FirebaseAuth mAuth;
    private TextView txt_criar_conta;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        edt_txt_email= findViewById(R.id.edt_txt_email);
        edt_txt_senha= findViewById(R.id.edt_txt_senha);
        btt_acessar = findViewById(R.id.btt_acessar);
        cb_mostrar_senha = findViewById(R.id.cbx_mostrar_senha);
        txt_criar_conta = findViewById(R.id.txt_criar_conta);

        btt_acessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login_email = edt_txt_email.getText().toString();
                String login_senha = edt_txt_senha.getText().toString();

                if(!TextUtils.isEmpty(login_email) || !TextUtils.isEmpty(login_senha) ){
                    mAuth.signInWithEmailAndPassword(login_email,login_senha)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        abrir_tela_inicial();
                                    }
                                    else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Login.this, ""+ error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        txt_criar_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_indo_direita, R.anim.slide_saindo_esquerda);
                finish();

            }
        });
        cb_mostrar_senha.setButtonTintList(ContextCompat.getColorStateList(this, R.color.cbx_color_login));
        cb_mostrar_senha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                motrar_ocutar_senha(edt_txt_senha, isChecked);
            }
        });

    }

    private void abrir_tela_inicial() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void motrar_ocutar_senha(EditText editText, boolean mostrar){
        if (mostrar) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}