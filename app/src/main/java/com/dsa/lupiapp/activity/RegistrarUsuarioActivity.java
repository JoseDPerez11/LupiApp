package com.dsa.lupiapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.databinding.ActivityRegistrarUsuarioBinding;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private File file;
    private ActivityRegistrarUsuarioBinding binding;
    private final static int LOCATION_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrarUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.init();
    }

    private void init() {
        binding.btnSubirImagen.setOnClickListener(v -> {
            cargarImagen();
        });

        this.textChangedListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verifica si la aplicación tiene el permiso de lectura de almacenamiento externo
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
        PackageManager.PERMISSION_GRANTED) {
            // Si no tiene el permiso, solicita permisos al usuario
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    LOCATION_REQUEST_CODE);
        }
    }

    private void textChangedListener() {
        binding.edtNameUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtInputNameUser.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        binding.edtApellidoPaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtInputApellidoPaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        binding.edtApellidoMaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtInputApellidoMaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Comprueba el código de solicitud de permisos
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                // Verifica si el usuario ha concedido el permiso
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Si el usuario concede el permiso, muestra un mensaje de agradecimiento
                    Toast.makeText(this, "Gracias por conceder los permisos para " +
                            "leer el almacenamiento, estos permisos son necesarios para poder " +
                            "escoger tu foto de perfil", Toast.LENGTH_SHORT).show();
                } else {
                    // Si el usuario niega el permiso, muestra un mensaje indicando la necesidad de los permisos
                    Toast.makeText(this, "No podemos realizar el registro si no nos concedes los permisos para leer el almacenamiento.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent, "Seleccione la aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Verifica si el resultado de la actividad es RESULT_OK (operación exitosa)
        if (resultCode == RESULT_OK) {
            // Obtiene el URI de los datos devueltos por la actividad
            Uri uri = data.getData();

            // Obtiene la ruta real del archivo utilizando el URI
            final String realPath = getRealPathFromURI(uri);
            // Crea un objeto File con la ruta real del archivo
            this.file = new File(realPath);
            // Establece la imagen del usuario en un ImageView utilizando el URI
            this.binding.imageUser.setImageURI(uri);
        }
    }

    // Función para obtener la ruta real de un URI dado
    private String getRealPathFromURI(Uri contentUri) {
        String result;

        // Consulta el proveedor de contenido para obtener información sobre el URI
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);

        // Verifica si la consulta devuelve un cursor válido
        if (cursor == null) {
            // Si el cursor es nulo, utiliza la ruta del URI directamente
            result = contentUri.getPath();
        } else {
            // Si hay un cursor, mueve el cursor al primer resultado
            cursor.moveToFirst();
            // Obtiene el índice de la columna que contiene la ruta del archivo en el proveedor de contenido
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            // Obtiene la ruta del archivo desde el cursor usando el índice
            result = cursor.getString(idx);
            // Cierra el cursor después de obtener la información necesaria
            cursor.close();
        }

        // Devuelve la ruta real del archivo
        return result;
    }

    private boolean validate() {
        boolean retorno = true;
        String nombres, apellidoPaterno, apellidoMaterno, numDoc, telefono, direccion, correo, clave,
                dropTipoDoc, dropDepartamento, dropProvincia, dropDistrito;

        nombres = binding.edtNameUser.getText().toString();
        apellidoPaterno = binding.edtApellidoPaternoU.getText().toString();
        apellidoMaterno = binding.edtApellidoMaternoU.getText().toString();
        numDoc = binding.edtNumDocU.getText().toString();
        telefono = binding.edtTelefonoU.getText().toString();
        direccion = binding.edtDireccionU.getText().toString();
        correo = binding.edtEmailUser.getText().toString();
        clave = binding.edtPasswordUser.getText().toString();
        dropTipoDoc = binding.dropdownTipoDoc.getText().toString();
        dropDepartamento = binding.dropdownDepartamento.getText().toString();
        dropProvincia = binding.dropdownProvincia.getText().toString();
        dropDistrito = binding.dropdownDistrito.getText().toString();

        if (this.file == null) {
            errorMessage("Debe de seleccionar una foto de perfil");
            retorno = true;
        }
        if (nombres.isEmpty()) {
            binding.txtInputNameUser.setError("Ingresar nombres");
            retorno = false;
        } else {
            binding.txtInputNameUser.setErrorEnabled(false);
        }
        if (apellidoPaterno.isEmpty()) {
            binding.txtInputApellidoPaternoU.setError("Ingresar apellido paterno");
            retorno = false;
        } else {
            binding.txtInputApellidoPaternoU.setErrorEnabled(false);
        }
        if (apellidoMaterno.isEmpty()) {
            binding.txtInputApellidoMaternoU.setError("Ingresar apellido materno");
            retorno = false;
        } else {
            binding.txtInputApellidoMaternoU.setErrorEnabled(false);
        }
        if (numDoc.isEmpty()) {
            binding.txtInputNumeroDocU.setError("Ingresar número documento");
            retorno = false;
        } else {
            binding.txtInputNumeroDocU.setErrorEnabled(false);
        }
        if (telefono.isEmpty()) {
            binding.txtInputTelefonoU.setError("Ingresar número telefónico");
            retorno = false;
        } else {
            binding.txtInputTelefonoU.setErrorEnabled(false);
        }
        if (direccion.isEmpty()) {
            binding.txtInputDireccionU.setError("Ingresar dirección de su casa");
            retorno = false;
        } else {
            binding.txtInputDireccionU.setErrorEnabled(false);
        }
        if (correo.isEmpty()) {
            binding.txtInputEmailUser.setError("Ingresar correo electrónico");
            retorno = false;
        } else {
            binding.txtInputEmailUser.setErrorEnabled(false);
        }
        if (clave.isEmpty()) {
            binding.txtInputPasswordUser.setError("Ingresar clave para el inicio de sesión");
            retorno = false;
        } else {
            binding.txtInputPasswordUser.setErrorEnabled(false);
        }
        if (dropTipoDoc.isEmpty()) {
            binding.txtInputTipoDoc.setError("Seleccionar Tipo Doc");
            retorno = false;
        } else {
            binding.txtInputTipoDoc.setErrorEnabled(false);
        }
        if (dropDepartamento.isEmpty()) {
            binding.txtInputDepartamento.setError("Seleccionar Departamento");
            retorno = false;
        } else {
            binding.txtInputDepartamento.setErrorEnabled(false);
        }
        if (dropProvincia.isEmpty()) {
            binding.txtInputProvincia.setError("Seleccionar Provincia");
            retorno = false;
        } else {
            binding.txtInputProvincia.setErrorEnabled(false);
        }
        if (dropDistrito.isEmpty()) {
            binding.txtInputDistrito.setError("Seleccionar Distrito");
            retorno = false;
        } else {
            binding.txtInputDistrito.setErrorEnabled(false);
        }

        return retorno;

    }

    public void succesMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen trabajo")
                .setContentText(message).show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Ops...")
                .setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setContentText("Notificación")
                .setContentText(message).show();
    }

}