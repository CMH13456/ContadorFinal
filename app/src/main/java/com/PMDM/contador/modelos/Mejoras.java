package com.PMDM.contador.modelos;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.PMDM.contador.utiles.CBigInteger;
import com.google.android.material.button.MaterialButton;

public class Mejoras {

    private String name;
    private String description;
    private CBigInteger price;
    private MaterialButton button;
    private boolean isEnabled;
    private final Activity context;

    public Mejoras(Activity context, String name, String description, CBigInteger price, MaterialButton button, boolean isEnabled) {
        this.context = context;
        this.name = name;
        this.description = description;
        this.price = price;
        this.button = button;
        this.isEnabled = isEnabled;
    }

    public Activity getContext() {
        return context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CBigInteger getPrice() {
        return price;
    }

    public void setPrice(CBigInteger price) {
        this.price = price;
    }

    public MaterialButton getButton() {
        return button;
    }

    public void setButton(MaterialButton button) {
        this.button = button;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getFormattedPrice() {
        return price.withSuffix("$");
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        button.setEnabled(isEnabled);
    }

    @NonNull
    @Override
    public String toString() {
        return "Upgrade{" +
                "name='" + name + '\'' +
                ", value='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}

