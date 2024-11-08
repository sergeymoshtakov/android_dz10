package com.example.dz_10.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dz_10.R;
import com.example.dz_10.database.DBHelper;
import com.example.dz_10.adapters.TypeAdapter; // Если создадите адаптер для типов
import com.example.dz_10.adapters.ListAdapter;

public class CreateProductFragment extends Fragment {

    private DBHelper dbHelper;
    private EditText productNameEditText, productCountEditText;
    private Spinner listSpinner, countTypeSpinner;
    private CheckBox checkedCheckBox;
    private Button saveButton;

    public CreateProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_product, container, false);

        dbHelper = new DBHelper(getContext());

        productNameEditText = view.findViewById(R.id.product_name);
        productCountEditText = view.findViewById(R.id.product_count);
        listSpinner = view.findViewById(R.id.spinner_list); // Spinner для выбора списка
        countTypeSpinner = view.findViewById(R.id.spinner_count_type); // Spinner для выбора типа
        checkedCheckBox = view.findViewById(R.id.checkbox_checked); // CheckBox для "checked"
        saveButton = view.findViewById(R.id.button_save_product);

        // Заполняем спиннеры
        loadLists();
        loadCountTypes();

        saveButton.setOnClickListener(v -> saveProduct());

        return view;
    }

    private void loadLists() {
        // Загружаем все существующие списки
        Cursor cursor = dbHelper.getLists();
        // Используем адаптер для отображения в Spinner
        // Можно создать адаптер, который будет показывать название списка
        listSpinner.setAdapter(new ListAdapter(getContext(), cursor));
    }

    private void loadCountTypes() {
        // Загружаем все существующие типы
        Cursor cursor = dbHelper.getTypes();
        // Создаём адаптер для отображения типов
        countTypeSpinner.setAdapter(new TypeAdapter(getContext(), cursor));
    }

    private void saveProduct() {
        String name = productNameEditText.getText().toString();
        String countStr = productCountEditText.getText().toString();

        if (name.isEmpty() || countStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double count = Double.parseDouble(countStr);
        int checked = checkedCheckBox.isChecked() ? 1 : 0; // Получаем значение из чекбокса
        int countType = getCountTypeId(); // Получаем выбранный тип
        int listId = getListId(); // Получаем выбранный список

        if (listId != -1) {
            dbHelper.addProduct(name, count, listId, checked, countType);
            Toast.makeText(getContext(), "Product saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please select a list", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCountTypeId() {
        // Получаем выбранный тип из Spinner
        Cursor cursor = (Cursor) countTypeSpinner.getSelectedItem();
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("_id"));
        }
        return 1; // По умолчанию "шт"
    }

    private int getListId() {
        // Получаем выбранный список из Spinner
        Cursor cursor = (Cursor) listSpinner.getSelectedItem();
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("_id"));
        }
        return -1; // Если не выбран список
    }
}