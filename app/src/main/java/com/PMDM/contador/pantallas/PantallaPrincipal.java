package com.PMDM.contador.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.PMDM.contador.R;

public class PantallaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pprincipal);
    }

    /**
     * Launches GameActivity
     * @param view the view being clicked
     */
    public void launchGameActivity(View view) {
        startActivity(new Intent(this, PantallaJuego.class));
    }

    /**
     * Launches OptionsActivity
     * @param view the view being clicked
     */
    public void launchOptionsActivity(View view) {
        startActivity(new Intent(this, PantallaOpciones.class));
    }

    /**
     * Launches InfoActivity
     * @param view the view being clicked
     */
    public void launchInfoActivity(View view) {
        startActivity(new Intent(this, PantallaInformacion.class));
    }

    /*
     * Cerrar aplicación.
     */
    public void closeApp(View view) {
        finishAffinity();
        finish();
        System.exit(0);
    }
}

