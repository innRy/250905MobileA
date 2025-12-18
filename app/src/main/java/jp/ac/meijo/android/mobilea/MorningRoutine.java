package jp.ac.meijo.android.mobilea;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MorningRoutine extends AppCompatActivity {

    List<String> todoList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.morningroutine);

        Button addButton = findViewById(R.id.btnAdd);

        addButton.setOnClickListener(v -> {

            // ダイアログ用レイアウトを読み込む
            View dialogView = getLayoutInflater()
                    .inflate(R.layout.dialog_add_todo, null);

            EditText editDialog =
                    dialogView.findViewById(R.id.editTodoDialog);

            new AlertDialog.Builder(this)
                    .setTitle("Todoを追加")
                    .setView(dialogView)
                    .setPositiveButton("追加", (dialog, which) -> {
                        String text = editDialog.getText().toString();

                        if (!text.isEmpty()) {
                            todoList.add(text);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("キャンセル", null)
                    .show();
        });

    }
}
