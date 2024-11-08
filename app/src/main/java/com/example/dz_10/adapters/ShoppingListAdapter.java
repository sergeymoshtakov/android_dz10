package com.example.dz_10.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dz_10.R;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private Cursor cursor;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int listId);
    }

    public ShoppingListAdapter(Cursor cursor, OnItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (one row in the table)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Move the cursor to the correct position
        if (cursor != null && cursor.moveToPosition(position)) {
            int idIndex = cursor.getColumnIndex("_id");
            int nameIndex = cursor.getColumnIndex("name");
            int dateIndex = cursor.getColumnIndex("date");
            int descriptionIndex = cursor.getColumnIndex("description");

            // Extract data from the cursor
            int listId = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String date = cursor.getString(dateIndex); // Format this as needed
            String description = cursor.getString(descriptionIndex);

            // Set the data to the TextViews
            holder.nameTextView.setText(name);
            holder.dateTextView.setText(date);
            holder.descriptionTextView.setText(description);

            // Set click listener for the row (each item in the list)
            holder.itemView.setOnClickListener(v -> listener.onItemClick(listId));
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    // ViewHolder for the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView dateTextView;
        TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}