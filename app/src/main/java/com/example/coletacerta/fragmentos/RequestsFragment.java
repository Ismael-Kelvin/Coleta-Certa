package com.example.coletacerta.fragmentos;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coletacerta.Adapter.SolicitacaoAdapter;
import com.example.coletacerta.R;

import com.example.coletacerta.model.Solicitacao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SolicitacaoAdapter adapter;
    private final List<Solicitacao> listaSolicitacoes = new ArrayList<>();
    private DatabaseReference solicitacoesRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        recyclerView = view.findViewById(R.id.recycler_solicitacoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SolicitacaoAdapter(requireContext(), listaSolicitacoes);
        recyclerView.setAdapter(adapter);

        solicitacoesRef = FirebaseDatabase.getInstance().getReference("solicitacoes");

        recuperarSolicitacoes();

        return view;
    }

    private void recuperarSolicitacoes() {
        solicitacoesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaSolicitacoes.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Solicitacao s = ds.getValue(Solicitacao.class);
                    listaSolicitacoes.add(s);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // erro de leitura
            }
        });
    }
}
