package com.PMDM.contador.pantallas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.PMDM.contador.R;
import com.PMDM.contador.adapters.AdaptadorMejoras;
import com.PMDM.contador.modelos.Mejoras;
import com.PMDM.contador.utiles.CBigInteger;
import com.PMDM.contador.utiles.EmbeMejoras;
import com.PMDM.contador.utiles.RecyclerViewMejoras;
import com.google.android.material.button.MaterialButton;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

public class PantallaMejoras extends AppCompatActivity implements RecyclerViewMejoras.OnItemClickListener {

    private SharedPreferences.Editor editor;

    // Recycler view
    private ArrayList<Mejoras> upgradeList;
    private AdaptadorMejoras adapterUpgrade;
    private RecyclerView upgradesRecycler;

    // Base prices
    private final CBigInteger precioBase = new CBigInteger("100");
    private final CBigInteger precioAutobase = new CBigInteger("450");
    private final CBigInteger precioAutoBaseB = new CBigInteger("2670");

    // Actual prices
    private CBigInteger precioBasico = precioBase;
    private CBigInteger precioBaseB;
    private CBigInteger PrecioBasicoB = precioBaseB;
    private CBigInteger precioAutoBasico = precioAutobase;
    private CBigInteger precioAutoBasicoB = precioAutoBaseB;

    // Values
    private CBigInteger monedas;
    private CBigInteger valorClick;
    private CBigInteger valorAutoClick;

    // Views
    TextView textoMonedas;
    private TextView textoValorClick;
    private TextView textoValorAutoClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pmejoras);

        // Shared preferences
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        editor = sharedPref.edit();

        // Getting values from bundle
        Bundle bundle = getIntent().getExtras();

        monedas = new CBigInteger(Objects.requireNonNull(bundle).getString(getString(R.string.PMonedas), "0"));
        valorClick = new CBigInteger(Objects.requireNonNull(bundle).getString(getString(R.string.PValorClick), "1"));
        valorAutoClick = new CBigInteger(Objects.requireNonNull(bundle).getString(getString(R.string.PValorAutoClick), "0"));

        // View Assignment
        textoMonedas = findViewById(R.id.text_shop_coins);
        textoMonedas.setText(monedas.withSuffix("$"));
        textoValorClick = findViewById(R.id.text_click_value);
        textoValorClick.setText(valorClick.withSuffix("$/click"));
        textoValorAutoClick = findViewById(R.id.text_auto_click);
        textoValorAutoClick.setText(valorAutoClick.withSuffix("$/s"));

        // Restoring prices
        precioBasico = new CBigInteger(sharedPref.getString(getString(R.string.Precio_Basico), precioBase.toString()));
        PrecioBasicoB = new CBigInteger(sharedPref.getString(getString(R.string.Precio_BBasico), precioBaseB.toString()));
        precioAutoBasico = new CBigInteger(sharedPref.getString(getString(R.string.Precio_Auto), precioAutobase.toString()));
        precioAutoBasicoB = new CBigInteger(sharedPref.getString(getString(R.string.Precio_AutoB), precioAutoBaseB.toString()));

        // Setting recycler view
        upgradesRecycler = findViewById(R.id.recycler_upgrades);
        upgradesRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        upgradesRecycler.addItemDecoration(new EmbeMejoras(8, 6, 0, 0));
        upgradesRecycler.addOnItemTouchListener(new RecyclerViewMejoras(this, upgradesRecycler, this));

        upgradeList = new ArrayList<>();

        // Generating upgrade options
        generateUpgradeButton("basic", getString(R.string.mejoraclick), "+1", precioBasico);
        generateUpgradeButton("auto", getString(R.string.mejoraautoclick), "+1", precioAutoBasico);
        generateUpgradeButton("mega", getString(R.string.mejoraclickb), "+0.35%", PrecioBasicoB);
        generateUpgradeButton("mega_auto", getString(R.string.mejoraautoclickb), "+0.35%",  precioAutoBasicoB);

        updateUI();
        gameLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePrefs();
    }

    /**
     * Checks which button was being clicked and executes an action based on that.
     * Also updates the price of the button that was being clicked.
     *
     * @param view     the view being clicked
     * @param position the position on the list of the view being clicked
     */
    @Override
    public void onItemClick(View view, int position) {
        Button button = upgradeList.get(position).getButton();
        CBigInteger[] values;
        CBigInteger newPrice = new CBigInteger("0");

        switch (button.getTag().toString()) {
            case "basic":
                values = onPurchaseAction(button, textoValorClick, "$/click", valorClick, precioBasico, precioBase, new BigDecimal("1.03"), new BigInteger("1"));
                valorClick = values[0];
                precioBasico = values[1];
                newPrice = precioBasico;
                break;
            case "mega":
                values = onPurchaseAction(button, textoValorClick, "$/click", valorClick,PrecioBasicoB, precioBaseB, new BigDecimal("1.07"), new BigDecimal("1.35"));
                valorClick = values[0];
                PrecioBasicoB = values[1];
                newPrice = PrecioBasicoB;
                break;
            case "auto":
                values = onPurchaseAction(button, textoValorAutoClick, "$/s", valorAutoClick, precioAutoBasico, precioAutobase, new BigDecimal("1.05"), new BigInteger("1"));
                valorAutoClick = values[0];
                precioAutoBasico = values[1];
                newPrice = precioAutoBasico;
                break;
            case "mega_auto":
                values = onPurchaseAction(button, textoValorAutoClick, "$/s", valorAutoClick, precioAutoBasicoB, precioAutoBaseB, new BigDecimal("1.08"), new BigDecimal("1.35"));
                valorAutoClick = values[0];
                precioAutoBasicoB = values[1];
                newPrice = precioAutoBasicoB;
                break;
        }

        if (newPrice.compareTo(BigInteger.valueOf(0)) >= 0) {
            upgradeList.get(position).setPrice(newPrice);
        }
    }

    @Override
    public void onLongItemClick(View view, int position) {
        // TODO: on long click perform multiple clicks until user desist touching the screen
        view.performClick();
    }

    /**
     * If user click on the return icon it will save the current state and start GameActivity again.
     *
     * @param view the view being clicked
     */
    public void returnOnClick(View view) {
        Intent intent = new Intent(this, PantallaJuego.class);
        intent.putExtra(getString(R.string.PMonedas), monedas.toString());
        intent.putExtra(getString(R.string.PValorClick), valorClick.toString());
        intent.putExtra(getString(R.string.PValorAutoClick), valorAutoClick.toString());
        intent.putExtra(getString(R.string.Precio_Basico), precioBasico.toString());
        startActivity(intent);
        savePrefs();
        finish();
    }

    /**
     * Saves all the prices and click values to the sharedPrefs
     */
    public void savePrefs() {
        editor.putString(getString(R.string.PMonedas), monedas.toString());
        editor.putString(getString(R.string.PValorClick), valorClick.toString());
        editor.putString(getString(R.string.PValorAutoClick), valorAutoClick.toString());
        editor.putString(getString(R.string.Precio_Basico), precioBasico.toString());
        editor.putString(getString(R.string.Precio_BBasico), PrecioBasicoB.toString());
        editor.putString(getString(R.string.Precio_Auto), precioAutoBasico.toString());
        editor.putString(getString(R.string.Precio_AutoB), precioAutoBasicoB.toString());
        editor.apply();
    }

    /**
     * Each second adds the auto click value to the coins.
     */
    @SuppressWarnings("BusyWait")
    private void gameLoop() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (valorAutoClick.compareTo(BigInteger.valueOf(0)) > 0) {
                        monedas = monedas.add(valorAutoClick);
                        runOnUiThread(() -> textoMonedas.setText(monedas.withSuffix("$")));
                        new Thread(this::updateDisabledButtons).start();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Updates the UI
     */
    private void updateUI() {
        adapterUpgrade = new AdaptadorMejoras(upgradeList);
        upgradesRecycler.setAdapter(adapterUpgrade);
        new Thread(this::updateDisabledButtons).start();
    }

    /**
     * Generates a new upgrade button, sets it's tag (based on the prefs keys of its value)
     * and adds it to the list.
     *
     * @param buttonTag   the tag of the button
     * @param name        the name of the upgrade
     * @param description the description of the upgrade
     * @param price       the price of the upgrade
     */
    private void generateUpgradeButton(String buttonTag, String name, String description, CBigInteger price) {
        MaterialButton button = new MaterialButton(this);
        button.setTag(buttonTag);
        upgradeList.add(new Mejoras(this, name, description, price, button, false));
    }

    private CBigInteger[] onPurchaseAction(Button button, TextView infoTextView, String msg, CBigInteger actualClickValue, CBigInteger price, CBigInteger basePrice, BigDecimal priceFactor, BigDecimal valueFactor) {
        if (monedas.compareTo(price) >= 0) {
            monedas = monedas.subtract(price);

            price = basePrice.add(new BigDecimal(price).multiply(priceFactor).toBigInteger());
            actualClickValue = CBigInteger.toCustomBigInteger(new BigDecimal(actualClickValue).multiply(valueFactor));

            textoMonedas.setText(monedas.withSuffix("$"));
            button.setText(price.withSuffix("$"));
            infoTextView.setText(actualClickValue.withSuffix(msg));

            ScaleAnimation fade_in = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            fade_in.setDuration(100);
            button.startAnimation(fade_in);
            new Thread(this::updateDisabledButtons).start();
        }
        return new CBigInteger[]{actualClickValue, price};
    }

    // -1 <; 0 ==; 1 >;
    private CBigInteger[] onPurchaseAction(Button button, TextView infoTextView, String msg, CBigInteger actualClickValue, CBigInteger price, CBigInteger basePrice, BigDecimal priceFactor, BigInteger toAddValue) {
        if (monedas.compareTo(price) >= 0) {
            monedas = monedas.subtract(price);

            price = basePrice.add(new BigDecimal(price).divide(priceFactor, 0, RoundingMode.CEILING).toBigInteger());
            actualClickValue = actualClickValue.add(toAddValue);

            textoMonedas.setText(monedas.withSuffix("$"));
            button.setText(price.withSuffix("$"));
            infoTextView.setText(actualClickValue.withSuffix(msg));

            ScaleAnimation fade_in = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            fade_in.setDuration(100);
            button.startAnimation(fade_in);
            new Thread(this::updateDisabledButtons).start();
        }
        return new CBigInteger[]{actualClickValue, price};
    }

    private void updateDisabledButtons() {
        for (int i = 0; i < upgradeList.size(); i++) {
            Mejoras upgrade = upgradeList.get(i);
            if (monedas.compareTo(upgrade.getPrice()) >= 0) {
                final int position = i;
                runOnUiThread(() -> {
                    upgrade.setEnabled(true);
                    adapterUpgrade.notifyItemChanged(position, upgrade);
                });
            } else {
                final int position = i;
                runOnUiThread(() -> {
                    upgrade.setEnabled(false);
                    adapterUpgrade.notifyItemChanged(position, upgrade);
                });
            }
        }

    }

}
