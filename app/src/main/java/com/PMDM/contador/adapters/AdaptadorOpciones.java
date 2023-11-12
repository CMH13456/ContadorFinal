package com.PMDM.contador.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.PMDM.contador.R;
import com.PMDM.contador.modelos.Opciones;
import com.PMDM.contador.pantallas.PantallaOpciones;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;
import java.util.Objects;

public class AdaptadorOpciones  extends ArrayAdapter<Opciones> {

    public AdaptadorOpciones(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Opciones> options) {
        super(context, resource, textViewResourceId, options);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @NonNull
        Opciones option = Objects.requireNonNull(getItem(position));

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.mopciones, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.text_option_name)).setText(option.getName());
        ((TextView)convertView.findViewById(R.id.text_option_description)).setText(option.getDescription());
        convertView.findViewById(R.id.text_option_description).setTag(option.getTag());

        convertView.findViewById(R.id.switch_option).setTag(option.getTag());
        ((SwitchMaterial)convertView.findViewById(R.id.switch_option)).setOnCheckedChangeListener(((PantallaOpciones)getContext()));
        ((SwitchMaterial)convertView.findViewById(R.id.switch_option)).setChecked(option.isChecked());

        return super.getView(position, convertView, parent);
    }
}
