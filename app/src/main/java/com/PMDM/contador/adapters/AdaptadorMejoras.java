package com.PMDM.contador.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.PMDM.contador.R;
import com.PMDM.contador.modelos.Mejoras;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AdaptadorMejoras extends RecyclerView.Adapter<AdaptadorMejoras.ViewHolderUpgrade> {

    ArrayList<Mejoras> upgradeList;

    public AdaptadorMejoras(ArrayList<Mejoras> upgradeList) {
        this.upgradeList = upgradeList;
    }

    @NonNull
    @Override
    public ViewHolderUpgrade onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mmejoras, parent, false);
        return new ViewHolderUpgrade(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUpgrade holder, int position) {
        holder.assignItem(upgradeList.get(position));
    }

    @Override
    public int getItemCount() {
        return upgradeList.size();
    }

    public static class ViewHolderUpgrade extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        MaterialButton button;

        public ViewHolderUpgrade(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_upgrade_name);
            description = itemView.findViewById(R.id.text_upgrade_description);
            button = itemView.findViewById(R.id.upgradeButton);
        }

        public void assignItem(@NonNull Mejoras upgrade) {
            name.setText(upgrade.getName());
            description.setText(upgrade.getDescription());
            button.setText(upgrade.getFormattedPrice());
            button.setEnabled(upgrade.isEnabled());

            if(!button.isEnabled()) {
                button.setBackgroundColor(upgrade.getContext().getColor(R.color.plum));
                button.setShadowLayer(1.6f, 1.5f, 1.3f, Color.WHITE);
            } else {
                button.setBackgroundColor(upgrade.getContext().getColor(R.color.rebecca_purple));
                button.setShadowLayer(0,0,0, Color.BLACK);
            }
        }
    }
}
