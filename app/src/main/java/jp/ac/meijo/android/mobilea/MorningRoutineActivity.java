package jp.ac.meijo.android.mobilea;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView; // 追加
import java.util.ArrayList;
import java.util.List;

public class MorningRoutineActivity extends AppCompatActivity {

    List<String> todoList;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morningroutine);

        todoList = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                todoList
        );

        listView = findViewById(R.id.listTodo);
        listView.setAdapter(adapter);

        Button addButton = findViewById(R.id.btnAdd);
        addButton.setOnClickListener(v -> {
            View dialogView = getLayoutInflater()
                    .inflate(R.layout.dialog_add_todo, null);

            EditText editDialog = dialogView.findViewById(R.id.editTodoDialog);

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_post) {
                // PostActivityへ画面遷移
                Intent intent = new Intent(this, PostActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_home) {
                return true;
            }
            return false;
        });
    }
}
