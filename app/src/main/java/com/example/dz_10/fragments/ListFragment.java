package com.example.dz_10.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dz_10.MainActivity;
import com.example.dz_10.R;
import com.example.dz_10.adapters.ShoppingListAdapter;
import com.example.dz_10.database.DBHelper;

public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private DBHelper dbHelper;

    public ListFragment() {
        // Empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getContext());

        // Load all lists from the database
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM Lists", null);
        adapter = new ShoppingListAdapter(cursor, listId -> {
            // Open products fragment for the selected list
            ((MainActivity) getActivity()).openProductsFragment(listId);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}