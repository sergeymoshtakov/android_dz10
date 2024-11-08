package com.example.dz_10.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.dz_10.R;
import com.example.dz_10.database.DBHelper;

public class CreateListFragment extends Fragment {
    private EditText listNameEditText;
    private EditText listDescriptionEditText;
    private DBHelper dbHelper;

    public CreateListFragment() {
        // Порожній конструктор за замовчуванням
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_list, container, false);

        listNameEditText = view.findViewById(R.id.list_name_edit_text);
        listDescriptionEditText = view.findViewById(R.id.list_description_edit_text);
        Button saveButton = view.findViewById(R.id.save_button);

        dbHelper = new DBHelper(getContext());

        // Обробник натискання кнопки збереження
        saveButton.setOnClickListener(v -> {
            String name = listNameEditText.getText().toString();
            String description = listDescriptionEditText.getText().toString();

            // Додавання нового списку до бази даних
            if (!name.isEmpty()) {
                dbHelper.addList(name, description);

                // Повернення до списку списків після збереження
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
