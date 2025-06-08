package com.example.coletacerta.fragmentos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.coletacerta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterFragment extends Fragment {

        private EditText edtLogradouro, edtNumero, edtBairro, edtCidade, edtCep, edtObservacoes;
        private TextView txtMateriais, txt_foto;
        private Button btnEnviar;
        private ImageView imgPreview;
        private Uri imageUri;
        private Uri cameraImageUri;


        private static final int REQUEST_IMAGE_GALLERY = 1;
        private static final int REQUEST_IMAGE_CAMERA = 2;

        private boolean[] selectedItems;
        private String[] materiais = {"Madeira", "Concreto", "Metais", "Plástico", "Vidro"};
        private ArrayList<String> materiaisSelecionados = new ArrayList<>();

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_register, container, false);

                // Inicializa os componentes
                edtLogradouro = view.findViewById(R.id.edt_logradouro_coleta);
                edtNumero = view.findViewById(R.id.edt_numero_coleta);
                edtBairro = view.findViewById(R.id.edt_bairro_coleta);
                edtCidade = view.findViewById(R.id.edt_cidade_coleta);
                edtCep = view.findViewById(R.id.edt_cep_coleta);
                edtObservacoes = view.findViewById(R.id.edt_observacoes);
                txtMateriais = view.findViewById(R.id.cbs_materiais);
                txt_foto = view.findViewById(R.id.txt_foto);
                imgPreview = view.findViewById(R.id.img_preview);
                btnEnviar = view.findViewById(R.id.btt_enviar_solicitacao);

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }


                selectedItems = new boolean[materiais.length];

                // Clique para seleção de materiais
                txtMateriais.setOnClickListener(v -> showMateriaisDialog());

                // Clique para anexar imagem
                txt_foto.setOnClickListener(v -> showImagePickerDialog());

                // Clique no botão de envio
                btnEnviar.setOnClickListener(v -> enviarSolicitacao());

                return view;
        }

        private void showMateriaisDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Selecione os materiais");

                builder.setMultiChoiceItems(materiais, selectedItems, (dialog, which, isChecked) -> {
                        if (isChecked && !materiaisSelecionados.contains(materiais[which])) {
                                materiaisSelecionados.add(materiais[which]);
                        } else {
                                materiaisSelecionados.remove(materiais[which]);
                        }
                });

                builder.setPositiveButton("OK", (dialog, which) -> {
                        StringBuilder sb = new StringBuilder();
                        for (String item : materiaisSelecionados) {
                                sb.append(item).append(", ");
                        }
                        if (!materiaisSelecionados.isEmpty()) {
                                sb.setLength(sb.length() - 2); // remove última vírgula e espaço
                                txtMateriais.setText(sb.toString());
                        } else {
                                txtMateriais.setText("Selecionar materiais");
                        }
                });

                builder.setNegativeButton("Cancelar", null);

                builder.show();
        }

        private void showImagePickerDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Anexar imagem")
                        .setItems(new String[]{"Galeria", "Câmera"}, (dialog, which) -> {
                                if (which == 0) {
                                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                                        galleryIntent.setType("image/*");
                                        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
                                } else {
                                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                                                try {
                                                        File photoFile = createImageFile();
                                                        cameraImageUri = FileProvider.getUriForFile(
                                                                requireContext(),
                                                                requireContext().getPackageName() + ".fileprovider",
                                                                photoFile
                                                        );
                                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                                                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(getContext(), "Erro ao criar arquivo da imagem", Toast.LENGTH_SHORT).show();
                                                }
                                        }

                                }
                        });
                builder.show();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (resultCode == Activity.RESULT_OK) {
                        if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                                imageUri = data.getData();
                                imgPreview.setImageURI(imageUri);
                                imgPreview.setVisibility(View.VISIBLE);
                        } else if (requestCode == REQUEST_IMAGE_CAMERA) {
                                if (cameraImageUri != null) {
                                        imageUri = cameraImageUri;
                                        imgPreview.setImageURI(imageUri);
                                        imgPreview.setVisibility(View.VISIBLE);
                                } else {
                                        Toast.makeText(getContext(), "Erro ao capturar imagem", Toast.LENGTH_SHORT).show();
                                }
                        }
                }
        }



        private void enviarSolicitacao() {
                String logradouro = edtLogradouro.getText().toString().trim();
                String numero = edtNumero.getText().toString().trim();
                String bairro = edtBairro.getText().toString().trim();
                String cidade = edtCidade.getText().toString().trim();
                String cep = edtCep.getText().toString().trim();
                String observacoes = edtObservacoes.getText().toString().trim();
                String materiais = txtMateriais.getText().toString();

                if (logradouro.isEmpty() || numero.isEmpty() || bairro.isEmpty() ||
                        cidade.isEmpty() || cep.isEmpty() || materiais.equals("Selecionar materiais")) {
                        Toast.makeText(getContext(), "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                        return;
                }

                if (imageUri == null) {
                        Toast.makeText(getContext(), "Anexe uma imagem", Toast.LENGTH_SHORT).show();
                        return;
                }

                // Enviar imagem para o Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                String file_image = "imagens/user_" + FirebaseAuth.getInstance().getUid() + "/" + System.currentTimeMillis() + ".jpg";
                StorageReference imgRef = storageRef.child(file_image);

                UploadTask uploadTask = imgRef.putFile(imageUri);

                uploadTask.addOnSuccessListener(taskSnapshot -> {
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Salvar dados no Firebase Database
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("solicitacoes");
                                String id = dbRef.push().getKey();

                                if (id != null) {
                                        HashMap<String, Object> dados = new HashMap<>();
                                        dados.put("logradouro", logradouro);
                                        dados.put("numero", numero);
                                        dados.put("bairro", bairro);
                                        dados.put("cidade", cidade);
                                        dados.put("cep", cep);
                                        dados.put("observacoes", observacoes);
                                        dados.put("materiais", materiais);
                                        dados.put("imagemUrl", uri.toString());

                                        dbRef.child(id).setValue(dados)
                                                .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(getContext(), "Solicitação enviada com sucesso!", Toast.LENGTH_SHORT).show();
                                                        limparFormulario();
                                                })
                                                .addOnFailureListener(e -> {
                                                        Toast.makeText(getContext(), "Erro ao salvar dados", Toast.LENGTH_SHORT).show();
                                                });
                                }

                        });
                }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Erro ao enviar imagem", Toast.LENGTH_SHORT).show();
                });
        }

        private void limparFormulario() {
                edtLogradouro.setText("");
                edtNumero.setText("");
                edtBairro.setText("");
                edtCidade.setText("");
                edtCep.setText("");
                edtObservacoes.setText("");
                txtMateriais.setText("Selecionar materiais");
                materiaisSelecionados.clear();
                for (int i = 0; i < selectedItems.length; i++) {
                        selectedItems[i] = false;
                }
                imgPreview.setImageBitmap(null);
                imgPreview.setVisibility(View.GONE);
                imageUri = null;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if (requestCode == 101) {
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(getContext(), "Permissões concedidas", Toast.LENGTH_SHORT).show();
                        } else {
                                Toast.makeText(getContext(), "Permissões negadas", Toast.LENGTH_SHORT).show();
                        }
                }
        }

        private File createImageFile() throws IOException {
                String fileName = "IMG_" + System.currentTimeMillis();
                File storageDir = requireContext().getCacheDir();
                File image = File.createTempFile(fileName, ".jpg", storageDir);
                return image;
        }

}
