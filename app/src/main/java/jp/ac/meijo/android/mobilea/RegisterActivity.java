package jp.ac.meijo.android.mobilea;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        EditText editEmail = findViewById(R.id.edit_register_email);
        EditText editPassword = findViewById(R.id.edit_register_password);
        Button btnRegister = findViewById(R.id.button_register_next);
        Button btnBack = findViewById(R.id.button_register_back);

        btnRegister.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "全ての項目を入力してください", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "パスワードは6文字以上で入力してください", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "登録が完了しました！", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, PostActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "登録失敗: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        btnBack.setOnClickListener(v -> finish());
    }
}