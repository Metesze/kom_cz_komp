package com.example.wms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button buttonAdd, buttonScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        buttonAdd = findViewById(R.id.button_add);
        buttonScan = findViewById(R.id.button_scan);

        dbHelper = new DatabaseHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();

        buttonAdd.setOnClickListener(v -> showAddProductDialog());
        buttonScan.setOnClickListener(v -> startBarcodeScanner());
    }

    private void loadProducts() {
        List<Product> productList = dbHelper.getAllProducts();
        adapter = new ProductAdapter(productList, dbHelper);
        recyclerView.setAdapter(adapter);
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        EditText nameEditText = view.findViewById(R.id.editTextName);
        EditText quantityEditText = view.findViewById(R.id.editTextQuantity);

        builder.setView(view)
                .setTitle("Dodaj Produkt")
                .setPositiveButton("Dodaj", (dialog, which) -> {
                    String name = nameEditText.getText().toString();
                    int quantity = Integer.parseInt(quantityEditText.getText().toString());
                    dbHelper.addProduct(new Product(0, name, quantity));
                    loadProducts();
                })
                .setNegativeButton("Anuluj", null)
                .create()
                .show();
    }

    private void startBarcodeScanner() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Skanowanie kodu kreskowego");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Obsługa zeskanowanego kodu kreskowego

            } else {
                // Anulowano skanowanie
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBarcodeScanner();
            } else {
                // Użytkownik odmówił dostępu do kamery
            }
        }
    }
}
