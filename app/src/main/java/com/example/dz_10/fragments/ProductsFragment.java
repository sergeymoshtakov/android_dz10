package com.example.dz_10.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dz_10.R;
import com.example.dz_10.adapters.ProductAdapter;
import com.example.dz_10.database.DBHelper;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getContext());

        // Получаем все продукты без фильтрации по списку
        Cursor cursor = dbHelper.getProducts();
        if (cursor != null && cursor.moveToFirst()) {
            productAdapter = new ProductAdapter(cursor);
            recyclerView.setAdapter(productAdapter);
        } else {
            Toast.makeText(getContext(), "No products found", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}