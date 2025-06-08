package com.example.coletacerta.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coletacerta.R;
import com.example.coletacerta.model.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<UserModel> listaUsuarios;
    private Context context;

    public UsuarioAdapter(List<UserModel> listaUsuarios, Context context) {
        this.listaUsuarios = listaUsuarios;
        this.context = context;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        UserModel user = listaUsuarios.get(position);
        holder.txtNome.setText(user.getNome());
        holder.txtEmail.setText(user.getEmail());

        holder.btnAlterarPerfil.setOnClickListener(v -> {
            final String[] perfis = {"Usuário", "Coletor", "Empresa"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Escolher novo perfil")
                    .setItems(perfis, (dialog, which) -> {
                        String novoPerfil = perfis[which];
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                .child("Usuarios").child(user.getId()).child("perfil");
                        ref.setValue(novoPerfil).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Perfil atualizado!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Erro ao atualizar perfil", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtEmail;
        Button btnAlterarPerfil;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeUsuario);
            txtEmail = itemView.findViewById(R.id.txtEmailUsuario);
            btnAlterarPerfil = itemView.findViewById(R.id.btnAlterarPerfil);
        }
    }
}

