package com.example.coletacerta.model;

import android.speech.SpeechRecognizer;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserModel {

    private String id;
    private String nome;
    private String email;
    private String perfil;


    public UserModel(){

    }

    public UserModel(String id, String nome, String email, String perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public void salvar(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Usuarios").child(getId()).setValue(this);
    }

}
