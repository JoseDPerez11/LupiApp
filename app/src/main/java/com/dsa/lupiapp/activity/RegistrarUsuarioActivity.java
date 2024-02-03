package com.dsa.lupiapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.databinding.ActivityRegistrarUsuarioBinding;
import com.dsa.lupiapp.entity.service.Cliente;
import com.dsa.lupiapp.entity.service.DocumentoAlmacenado;
import com.dsa.lupiapp.entity.service.Usuario;
import com.dsa.lupiapp.viewmodel.ClienteViewModel;
import com.dsa.lupiapp.viewmodel.DocumentoAlmacenadoViewModel;
import com.dsa.lupiapp.viewmodel.UsuarioViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDateTime;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private File file;
    private ClienteViewModel clienteViewModel;
    private UsuarioViewModel usuarioViewModel;
    private DocumentoAlmacenadoViewModel documentoAlmacenadoViewModel;
    private ActivityRegistrarUsuarioBinding binding;
    private static final int LOCATION_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrarUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.init();
        this.initViewModel();
        this.spinners();
    }

    private void spinners() {

        // Lista de tipos de documentos
        String[] tipoDoc = getResources().getStringArray(R.array.tipoDoc);
        ArrayAdapter arrayTipoDoc = new ArrayAdapter(this, R.layout.dropdown_item, tipoDoc);
        binding.dropdownTipoDoc.setAdapter(arrayTipoDoc);

        // Lista de departamentos
        String[] departamentos = getResources().getStringArray(R.array.departamentos);
        ArrayAdapter arrayDepartamentos = new ArrayAdapter(this, R.layout.dropdown_item, departamentos);
        binding.dropdownDepartamento.setAdapter(arrayDepartamentos);

        // Lista de provincias
        String[] provincias = getResources().getStringArray(R.array.provincias);
        ArrayAdapter arrayProvincias = new ArrayAdapter(this, R.layout.dropdown_item, provincias);
        binding.dropdownProvincia.setAdapter(arrayProvincias);

        // Lista de distritos
        String[] distritos = getResources().getStringArray(R.array.distritos);
        ArrayAdapter arrayDistritos = new ArrayAdapter(this, R.layout.dropdown_item, distritos);
        binding.dropdownDistrito.setAdapter(arrayDistritos);

    }

    // Inicializa los ViewModels utilizados en esta actividad
    private void initViewModel() {
        // Crea una instancia de ViewModelProvider para obtener los ViewModels
        final ViewModelProvider viewModelProvider = new ViewModelProvider(this);

        // Obtiene una instancia de los viewmodels a través del ViewModelProvider
        this.clienteViewModel = viewModelProvider.get(ClienteViewModel.class);
        this.usuarioViewModel = viewModelProvider.get(UsuarioViewModel.class);
        this.documentoAlmacenadoViewModel = viewModelProvider.get(DocumentoAlmacenadoViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verifica si la aplicación tiene el permiso de lectura de almacenamiento externo
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) !=
                PackageManager.PERMISSION_GRANTED) {
            // Si no tiene el permiso, solicita permisos al usuario
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_MEDIA_IMAGES },
                    LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Comprueba el código de solicitud de permisos
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                // Verifica si el usuario ha concedido el permiso
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Si el usuario concede el permiso, muestra un mensaje de agradecimiento
                    Toast.makeText(this, "permiso para leer datos concedidos" , Toast.LENGTH_SHORT).show();
                } else {
                    // Si el usuario niega el permiso, muestra un mensaje indicando la necesidad de los permisos
                    Toast.makeText(this, "Sin permiso para leer el almacenamiento.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void init() {
        binding.btnSubirImagen.setOnClickListener(v -> {
            this.cargarImagen();
        });

        binding.btnGuardarDatos.setOnClickListener(v -> {
            this.guardarDatos();
        });

        this.textChangedListener();
    }

    private void guardarDatos() {

        Cliente cliente;
        if (validate()) {
            cliente = new Cliente();
            try {
                // Asigna los valores del formulario al objeto Cliente
                cliente.setNombres(binding.edtNameUser.getText().toString());
                cliente.setApellidoPaterno(binding.edtApellidoPaternoU.getText().toString());
                cliente.setApellidoMaterno(binding.edtApellidoMaternoU.getText().toString());
                cliente.setTipoDoc(binding.dropdownTipoDoc.getText().toString());
                cliente.setNumDoc(binding.edtNumDocU.getText().toString());
                cliente.setDepartamento(binding.dropdownDepartamento.getText().toString());
                cliente.setProvincia(binding.dropdownProvincia.getText().toString());
                cliente.setDistrito(binding.dropdownDistrito.getText().toString());
                cliente.setTelefono(binding.edtTelefonoU.getText().toString());
                cliente.setDireccionEnvio(binding.edtDireccionU.getText().toString());
                cliente.setId(0);

                // Obtiene la fecha y hora actual para generar un nombre único para el archivo
                LocalDateTime localDateTime = LocalDateTime.now();

                // Crea una solicitud para enviar el archivo (imagen) al servidor
                RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data")), somedata;

                // Crea una solicitud de datos (nombre del archivo)
                String fileName = "" + localDateTime.getDayOfMonth()
                        + (localDateTime.getMonthValue() + 1)
                        + localDateTime.getYear()
                        + localDateTime.getHour()
                        + localDateTime.getMinute()
                        + localDateTime.getSecond();

                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                somedata = RequestBody.create("profilePh" + fileName, MediaType.parse("text/plain"));

                // Utiliza el ViewModel de DocumentoAlmacenado para guardar el archivo en el servidor
                this.documentoAlmacenadoViewModel.save(part, somedata).observe(this, response -> {
                    if (response.getRpta() == 1) {
                        // Configura la foto del cliente con el documento almacenado
                        cliente.setFoto(new DocumentoAlmacenado());
                        cliente.getFoto().setId(response.getBody().getId());

                        // Utiliza el ViewModel de Cliente para guardar el cliente en el servidor
                        this.clienteViewModel.save(cliente).observe(this, clienteResponse -> {
                            if (clienteResponse.getRpta() == 1) {
                                // Obtiene el id del cliente
                                int idcliente = clienteResponse.getBody().getId();

                                // Crea un objeto Usuario
                                Usuario usuario = new Usuario();
                                usuario.setEmail(binding.edtEmailUser.getText().toString());
                                usuario.setClave(binding.edtPasswordUser.getText().toString());
                                usuario.setVigencia(true);
                                usuario.setCliente(new Cliente(idcliente));

                                // Utiliza el ViewModel de Usuario para guardar el usuario en el servidor
                                this.usuarioViewModel.save(usuario).observe(this, usuarioResponse -> {
                                    if (usuarioResponse.getRpta() == 1) {
                                        succesMessage("Estupendo! Su información ha sido guardada con éxito en el sistema.");
                                    } else {
                                        errorMessage("No se ha podido guardar los datos, intentelo de nuevo");
                                    }
                                });

                            } else {
                                errorMessage("No se ha podido guardar los datos, intentelo de nuevo");
                            }
                        });

                    } else {
                        errorMessage("No se ha podido guardar los datos, intentelo de nuevo");
                    }
                });

            } catch (Exception e) {
                warningMessage("Se ha producido un error: " + e.getMessage());
            }
        } else {
            errorMessage("Por favor, complete todos los campos del formulario");
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
            retorno = false;
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