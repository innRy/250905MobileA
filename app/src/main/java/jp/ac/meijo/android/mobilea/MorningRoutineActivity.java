package jp.ac.meijo.android.mobilea;

import android.app.AlertDialog;
import android.content.Intent; // 画面遷移に必要
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

        // --- 1. ToDoリストの基本設定 ---
        todoList = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                todoList
        );

        listView = findViewById(R.id.listTodo);
        listView.setAdapter(adapter);

        // --- 2. 追加ボタン（ダイアログ表示）の設定 ---
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

        // --- 3. ボトムナビゲーションの設定 (追加部分) ---
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 現在の画面のアイテムを選択状態にする（Clockがこの画面の場合）
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_post) {
                // PostActivityへ画面遷移
                Intent intent = new Intent(this, PostActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_home) {
                // すでに現在のActivityにいるため、遷移は不要
                // (必要であれば一番上までスクロールさせる等の処理を書く)
                return true;
            }
            return false;
        });
    }
}
