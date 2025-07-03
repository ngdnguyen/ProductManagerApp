package com.example.productmanagerapp;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    EditText edtProductName, edtProductPrice;
    Button btnAddProduct;
    ListView lvProduct;
    DBHelper dbHelper;
    ArrayList<String> productList;
    ArrayList<Integer> productIds;
    ArrayAdapter<String> adapter;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        edtProductName = findViewById(R.id.edtProductName);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        lvProduct = findViewById(R.id.lvProduct);

        dbHelper = new DBHelper(this);
        productList = new ArrayList<>();
        productIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        lvProduct.setAdapter(adapter);

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);

        btnAddProduct.setOnClickListener(v -> {
            String name = edtProductName.getText().toString();
            String price = edtProductPrice.getText().toString();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO tblProducts(sName, Price, FK_iCategoryID) VALUES(?,?,?)",
                new String[]{name, price, String.valueOf(categoryId)});
            loadProducts();
        });

        lvProduct.setOnItemLongClickListener((parent, view, position, id) -> {
            int prodId = productIds.get(position);
            new AlertDialog.Builder(ProductActivity.this)
                .setTitle("Xoá sản phẩm")
                .setMessage("Bạn có chắc muốn xoá sản phẩm này?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("DELETE FROM tblProducts WHERE PK_iProductID=?", new String[]{String.valueOf(prodId)});
                    loadProducts();
                })
                .setNegativeButton("Huỷ", null)
                .show();
            return true;
        });

        loadProducts();
    }

    private void loadProducts() {
        productList.clear();
        productIds.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT PK_iProductID, sName, Price FROM tblProducts WHERE FK_iCategoryID=?",
                new String[]{String.valueOf(categoryId)});
        while (c.moveToNext()) {
            productIds.add(c.getInt(0));
            String name = c.getString(1);
            double price = c.getDouble(2);
            String priceFormatted = String.format("%,.0f", price).replace(",", ".");
            productList.add(name + " - " + priceFormatted + " đ");
        }
        adapter.notifyDataSetChanged();
    }

}