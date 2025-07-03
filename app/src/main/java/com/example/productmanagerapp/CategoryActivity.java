package com.example.productmanagerapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    EditText edtCategoryName;
    Button btnAddCategory;
    ListView lvCategory;
    DBHelper dbHelper;
    ArrayList<String> categoryList;
    ArrayAdapter<String> adapter;
    ArrayList<Integer> categoryIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        lvCategory = findViewById(R.id.lvCategory);

        dbHelper = new DBHelper(this);
        categoryList = new ArrayList<>();
        categoryIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryList);
        lvCategory.setAdapter(adapter);

        btnAddCategory.setOnClickListener(v -> {
            String name = edtCategoryName.getText().toString();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO tblCategories(sName) VALUES(?)", new String[]{name});
            loadCategories();
        });

        lvCategory.setOnItemClickListener((parent, view, position, id) -> {
            int catId = categoryIds.get(position);
            Intent intent = new Intent(CategoryActivity.this, ProductActivity.class);
            intent.putExtra("CATEGORY_ID", catId);
            startActivity(intent);
        });

        lvCategory.setOnItemLongClickListener((parent, view, position, id) -> {
            int catId = categoryIds.get(position);
            new AlertDialog.Builder(CategoryActivity.this)
                .setTitle("Xoá danh mục")
                .setMessage("Bạn có chắc muốn xoá danh mục này?")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("DELETE FROM tblCategories WHERE PK_iCategoryID=?", new String[]{String.valueOf(catId)});
                    db.execSQL("DELETE FROM tblProducts WHERE FK_iCategoryID=?", new String[]{String.valueOf(catId)});
                    loadCategories();
                })
                .setNegativeButton("Huỷ", null)
                .show();
            return true;
        });

        loadCategories();
    }

    private void loadCategories() {
        categoryList.clear();
        categoryIds.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tblCategories", null);
        while (c.moveToNext()) {
            categoryList.add(c.getString(1));
            categoryIds.add(c.getInt(0));
        }
        adapter.notifyDataSetChanged();
    }
}