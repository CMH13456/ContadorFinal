package com.PMDM.contador.pantallas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.PMDM.contador.R;
import com.PMDM.contador.adapters.AdaptadorOpciones;
import com.PMDM.contador.modelos.Opciones;

import java.util.ArrayList;
import java.util.List;

public class PantallaOpciones extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private List<Opciones> optionList;
    boolean checkedDefaultValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popciones);

        sharedPref = getPreferences(MODE_PRIVATE);
        editor = sharedPref.edit();

        ListView optionListView = findViewById(R.id.listview_options);

        optionList = new ArrayList<>();

        addOption(getResources().getString(R.string.nombreTema), getResources().getString(R.string.descripcionTema), getResources().getString(R.string.PModoOscuro));
        addOption("Sample name", "Sample desc", "sampleSwitch1");
        addOption("Sample name", "Sample desc", "sampleSwitch2");

        optionListView.setAdapter(new AdaptadorOpciones(this, R.layout.mopciones, R.id.text_blank, optionList));

    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < optionList.size(); i++) {
            Opciones option = optionList.get(i);
            editor.putBoolean(option.getTag(), option.isChecked());
        }
        editor.apply();
    }

    /**
     * Checks which switch was activated (or deactivated) and perform an action based on that
     * @param compoundButton the switch that is being clicked
     * @param isChecked whether the switch is checked or not
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getTag().toString()) {
            case "toggleTheme":
                toggleTheme(isChecked);
                toggleChecked(compoundButton, isChecked);
                break;
        }
        toggleChecked(compoundButton, isChecked);
    }

    /**
     * Checks if there is a previously saved state of the switch (based in tag) and adds the option
     * to the list
     *
     * @param name name of the option
     * @param desc description of the option
     * @param tag tag of the option
     */
    private void addOption(String name, String desc, String tag) {
        boolean isChecked = sharedPref.getBoolean(tag, checkedDefaultValue);

        //TODO: Make sure that each option does what it says if "isChecked" is true

        optionList.add(new Opciones(name, desc, tag, isChecked));
    }

    /**
     * This method finds the switch's Option object and sets isChecked accordingly to its current state.
     *
     * @param compoundButton the button who changed the state
     * @param isChecked      whether the switch it's be checked or not
     */
    private void toggleChecked(CompoundButton compoundButton, boolean isChecked) {
        for (Opciones option : optionList) {
            if (option.getTag().equals(compoundButton.getTag())) {
                option.setChecked(isChecked);
            }
        }
    }

    /**
     * If isChecked is true sets the dark theme for the application and if not then sets the light
     * mode
     * @param isChecked whether the switch is checked or not
     */
    private void toggleTheme(boolean isChecked) {
        if (isChecked) { // b => isChecked, if the switch if checked then b = true
            // Setting theme to night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // Setting theme to light mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}

