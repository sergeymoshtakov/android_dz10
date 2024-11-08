package com.example.dz_10.adapters;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dz_10.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Cursor cursor;

    // Конструктор адаптера, принимающий курсор
    public ProductAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Инфлейтим layout для каждого элемента
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        if (cursor != null && !cursor.isClosed() && cursor.moveToPosition(position)) {
            String productName = cursor.getString(cursor.getColumnIndex("name"));
            double productCount = cursor.getDouble(cursor.getColumnIndex("count"));
            int checked = cursor.getInt(cursor.getColumnIndex("checked"));
            int countType = cursor.getInt(cursor.getColumnIndex("count_type"));

            holder.productName.setText(productName != null ? productName : "Unknown");
            holder.productCount.setText(String.valueOf(productCount));
            holder.productChecked.setText(checked == 1 ? "Checked" : "Not Checked");
            holder.productCountType.setText(String.valueOf(countType));

            Log.d("ProductAdapter", "Binding product: " + productName + ", count: " + productCount);
        } else {
            Log.e("ProductAdapter", "Cursor is null or empty at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        // Проверяем, что курсор не пустой и возвращаем количество элементов
        if (cursor != null && !cursor.isClosed()) {
            return cursor.getCount();
        }
        return 0;
    }

    // Метод для обновления курсора в адаптере
    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();  // Закрываем старый курсор
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();  // Уведомляем адаптер об изменениях
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        // Закрываем курсор, когда RecyclerView больше не используется
        if (cursor != null) {
            cursor.close();
        }
    }

    // Вьюхолдер для элементов RecyclerView
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productCount, productChecked, productCountType;

        public ProductViewHolder(View itemView) {
            super(itemView);
            // Инициализируем TextView для каждого элемента
            productName = itemView.findViewById(R.id.productName);
            productCount = itemView.findViewById(R.id.productCount);
            productChecked = itemView.findViewById(R.id.productChecked);
            productCountType = itemView.findViewById(R.id.productCountType);
        }
    }
}