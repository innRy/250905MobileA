package jp.ac.meijo.android.mobilea;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            moveToPostActivity();
            return;
        }

        EditText editEmail = findViewById(R.id.edit_login_email);
        EditText editPassword = findViewById(R.id.edit_login_password);
        Button btnLogin = findViewById(R.id.button_login_submit);
        TextView textRegister = findViewById(R.id.text_go_to_registration);

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "ログイン成功", Toast.LENGTH_SHORT).show();
                                moveToPostActivity();
                            } else {
                                Toast.makeText(LoginActivity.this, "エラー: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show();
            }
        });

        textRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    private void moveToPostActivity() {
        Intent intent = new Intent(LoginActivity.this, PostActivity.class);
        startActivity(intent);
        finish();
    }
}
