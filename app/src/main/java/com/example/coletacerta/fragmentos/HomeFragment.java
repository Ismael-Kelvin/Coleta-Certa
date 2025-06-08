package com.example.coletacerta.fragmentos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coletacerta.R;
import com.example.coletacerta.activity.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private TextView txt_comprimento;
    private FirebaseAuth mAuth;
    private Button logoutButton;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txt_comprimento = view.findViewById(R.id.txt_comprimento);


        mAuth = FirebaseAuth.getInstance();
        showGreeting();

        return view;
    }

    private void showGreeting() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nome = snapshot.child("nome").getValue(String.class);

                    String firstName = (nome != null && !nome.isEmpty()) ? nome.split(" ")[0] : "usuário";

                    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    String greeting = (hour >= 6 && hour < 12) ? "Bom dia" :
                            (hour >= 12 && hour < 18) ? "Boa tarde" : "Boa noite";

                    txt_comprimento.setText(greeting + ", " + firstName + "!");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    txt_comprimento.setText("Olá!");
                }
            });
        } else {
            txt_comprimento.setText("Olá!");
        }
    }

}

