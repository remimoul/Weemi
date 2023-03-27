package com.example.weemi;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class TakePhotoActivity extends AppCompatActivity {

    private ImageCapture imageCapture;
    private Preview preview;

    private TextureView textureView;
    private Button takePhotoButton;

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        textureView = findViewById(R.id.texture_view);
        takePhotoButton = findViewById(R.id.btn_take_photo);
        PreviewView viewFinder = findViewById(R.id.viewFinder);




        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());


                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                ImageCapture.Builder builder = new ImageCapture.Builder();
                imageCapture = builder
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                cameraProvider.unbindAll();
                Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                // No-op
            }
        }, ContextCompat.getMainExecutor(this));

        takePhotoButton.setOnClickListener(v -> {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            }
        });
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }



    private File getOutputDirectory() {
        // Stockage de la photo
        File mediaDir = new File(getExternalFilesDir(null), "CameraX");

        // Creer le dossier s'il n'existe pas
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
        }

        return mediaDir;
    }


    private void startCamera() {
        //Créer le fichier de sortie où la photo sera enregistrée
        File outputDirectory = getOutputDirectory();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        File outputFile = new File(outputDirectory, "photo_" + currentTimeStamp + ".jpg");

        long name = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }

        //Créer une instance de la classe ImageCapture.OutputFileOptions qui spécifie où la photo doit être enregistrée
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues()
                ).build();


        //Créer une instance de la classe ImageCapture.OutputFileOptions qui spécifie où la photo doit être enregistrée
        // ImageCapture.OutputFileOptions outputFileOptions =
        // new ImageCapture.OutputFileOptions.Builder(outputFile).build();
        // ImageCapture.OutputFileOptions outputFileOptions =
        //  new ImageCapture.OutputFileOptions.Builder(getContentResolver(),
        //   MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        //  new ContentValues()
        // ).build();

        //Déclencher la prise de photo
        imageCapture.takePicture(
                outputFileOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {


                        // Enregistrement de la photo dans la galerie
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(outputFile);
                        mediaScanIntent.setData(contentUri);
                        sendBroadcast(mediaScanIntent);


                        // Afficher un message à l'utilisateur
                        Toast.makeText(TakePhotoActivity.this, "Photo enregistrée dans " + outputFileResults.getSavedUri(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        //Afficher un message à l'utilisateur en cas d'erreur
                        Toast.makeText(TakePhotoActivity.this, "Erreur : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "La prise de photo a échoué: " + exception.getMessage(), exception);
                    }
                });





    }
}