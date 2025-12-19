package jp.ac.meijo.android.mobilea;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoList);
        listView = findViewById(R.id.listTodo);
        listView.setAdapter(adapter);

        Button addButton = findViewById(R.id.btnAdd);
        addButton.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_todo, null);
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

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_morning_routine);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) { // Clock
                Intent intent = new Intent(this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_morning_routine) {
                return true;
            } else if (id == R.id.nav_post) {
                Intent intent = new Intent(this, PostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}