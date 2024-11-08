package com.example.dz_10;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dz_10.fragments.CreateListFragment;
import com.example.dz_10.fragments.CreateProductFragment;
import com.example.dz_10.fragments.ListFragment;
import com.example.dz_10.fragments.ProductsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteDatabase("shopping_list.db");

        // Кнопки
        Button createListButton = findViewById(R.id.button_create_list);
        Button createProductButton = findViewById(R.id.button_create_product);
        Button listsButton = findViewById(R.id.button_lists);
        Button productsButton = findViewById(R.id.button_products);

        // Set up onClick listeners for each button
        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateListFragment();
            }
        });

        createProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateProductFragment();
            }
        });

        listsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListFragment();
            }
        });

        productsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductsFragment();
            }
        });
    }

    // Open the fragment for creating a list
    public void openCreateListFragment() {
        Fragment fragment = new CreateListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Open the fragment for creating a product
    public void openCreateProductFragment() {
        Fragment fragment = new CreateProductFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Open the fragment to display all lists
    public void openListFragment() {
        Fragment fragment = new ListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Open the fragment to display products, pass listId as an argument
    public void openProductsFragment(int listId) {
        Fragment fragment = new ProductsFragment();

        // Create a bundle to pass the listId
        Bundle bundle = new Bundle();
        bundle.putInt("listId", listId); // Pass listId to the next fragment
        fragment.setArguments(bundle); // Set the arguments for the fragment

        // Start the fragment transaction
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Default openProductsFragment without arguments for fallback case
    public void openProductsFragment() {
        Fragment fragment = new ProductsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
