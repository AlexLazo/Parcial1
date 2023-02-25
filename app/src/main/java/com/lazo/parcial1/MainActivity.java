package com.lazo.parcial1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_ASK_PERMISSION = 1;
    Button btnCompartir;
    ImageView imgFoto;

    Uri uriImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        btnCompartir = findViewById(R.id.btnCompartir);
        imgFoto = findViewById(R.id.imgInfo);

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentW = new Intent(Intent.ACTION_SEND);
                intentW.setType("image/*");
                intentW.setPackage("com.whatsapp");
                //Para enviar por correo
                //intentW.setPackage("com.google.android.gm");

                if (uriImg != null){

                    intentW.putExtra(Intent.EXTRA_STREAM, uriImg);

                    try {
                        startActivity(intentW);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Error al enviar\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(MainActivity.this, "No se selecciono una imagen", Toast.LENGTH_LONG).show();

                }
            }
        });

        imgFoto .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                try {
                    startActivityForResult(intentGaleria, 1);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error al abrir galeria\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            uriImg = data.getData();
            imgFoto.setImageURI(uriImg);
        }
    }
}