package com.example.coletacerta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.coletacerta.R;
import com.example.coletacerta.model.Solicitacao;

import java.util.List;

public class SolicitacaoAdapter extends RecyclerView.Adapter<SolicitacaoAdapter.MyViewHolder> {

    private final List<Solicitacao> listaSolicitacoes;
    private final Context context;

    public SolicitacaoAdapter(Context context, List<Solicitacao> lista) {
        this.context = context;
        this.listaSolicitacoes = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_solicitacao, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Solicitacao s = listaSolicitacoes.get(position);
        holder.txtEndereco.setText(s.getLogradouro() + ", " + s.getNumero() + " - " + s.getBairro());
        holder.txtMateriais.setText("Materiais: " + s.getMateriais());

        Glide.with(context)
                .load(s.getImagemUrl())
                //.placeholder(R.drawable.ic_placeholder) // opcional
                .into(holder.imgSolicitacao);
    }

    @Override
    public int getItemCount() {
        return listaSolicitacoes.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSolicitacao;
        TextView txtEndereco, txtMateriais;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgSolicitacao = itemView.findViewById(R.id.img_solicitacao);
            txtEndereco = itemView.findViewById(R.id.txt_endereco);
            txtMateriais = itemView.findViewById(R.id.txt_materiais);
        }
    }
}
