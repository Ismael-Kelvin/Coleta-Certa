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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.coletacerta.R;
import com.example.coletacerta.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    private EditText edt_txt_usuario_register;
    private EditText edt_txt_email_register;
    private EditText edt_txt_senha_register;
    private EditText edt_txt_senha_confirma_register;
    private Button btt_registrar;
    private CheckBox cb_mostrar_senha_register;
    private FirebaseAuth mAuth;
    private TextView txt_voltar_login;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        edt_txt_usuario_register = findViewById(R.id.edt_txt_usuario_register);
        edt_txt_email_register = findViewById(R.id.edt_txt_email_register);
        edt_txt_senha_register = findViewById(R.id.edt_txt_senha_register);
        edt_txt_senha_confirma_register = findViewById(R.id.edt_txt_senha_confirma_register);
        btt_registrar = findViewById(R.id.btt_registrar);
        cb_mostrar_senha_register = findViewById(R.id.cbx_mostrar_senha_register);
        txt_voltar_login = findViewById(R.id.txt_voltar_login);


        btt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = new UserModel();

                userModel.setEmail(edt_txt_email_register.getText().toString());
                userModel.setNome(edt_txt_usuario_register.getText().toString());
                String senha = edt_txt_senha_register.getText().toString();
                String confirma_senha = edt_txt_senha_confirma_register.getText().toString();

                if (!TextUtils.isEmpty(userModel.getNome()) && !TextUtils.isEmpty(userModel.getEmail()) && !TextUtils.isEmpty(senha) && !TextUtils.isEmpty(confirma_senha)) {
                    if (senha.equals(confirma_senha)) {
                        mAuth.createUserWithEmailAndPassword(userModel.getEmail(), senha)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            userModel.setId(mAuth.getUid());
                                            userModel.setPerfil("usuario");
                                            userModel.salvar();
                                            AbrirTelaHome();
                                        } else {
                                            String error;
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthWeakPasswordException e) {
                                                error = "A senha tem que ter no mínimo 6 caracteres";
                                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                                error = "O formato de e-mail é inválido";
                                            } catch (FirebaseAuthUserCollisionException e) {
                                                error = "E-mail já existe em nossa base de dados";
                                            } catch (FirebaseException e) {
                                                error = "Erro ao efeturar o cadastro, tente novamente";
                                                e.printStackTrace();
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(Register.this, "As senhas não correspondem", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cb_mostrar_senha_register.setButtonTintList(ContextCompat.getColorStateList(this, R.color.cbx_color_login));
        cb_mostrar_senha_register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mostrar_ocutar_senha(edt_txt_senha_register, isChecked);
                mostrar_ocutar_senha(edt_txt_senha_confirma_register, isChecked);
            }
        });

        txt_voltar_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_indo_esquerda, R.anim.slide_saindo_direita);
                finish();
            }
        });
    }

    private void AbrirTelaHome() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        overrideActivityTransition(R.anim.slide_indo_direita, R.anim.slide_saindo_esquerda);
        finish();
    }

    private void overrideActivityTransition(int slideIndoDireita, int slideSaindoEsquerda) {
        // Função necessária para overridePendingTransition
    }

    public void mostrar_ocutar_senha(EditText editText, boolean mostrar) {
        if (mostrar) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }


}
