package jp.ac.meijo.android.mobilea.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import jp.ac.meijo.android.mobilea.Home;
import jp.ac.meijo.android.mobilea.R;
import jp.ac.meijo.android.mobilea.ui.auth.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit, passEdit;
    private Button loginBtn;
    private TextView registerLink;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ViewModel の取得
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        emailEdit = findViewById(R.id.editEmail);
        passEdit  = findViewById(R.id.editPassword);
        loginBtn  = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.txtRegisterLink);

        // ● ボタン押下 → ViewModel を呼び出す
        loginBtn.setOnClickListener(v -> {
            String email = emailEdit.getText().toString();
            String password = passEdit.getText().toString();
            viewModel.login(email, password);
        });

        // ● 新規登録リンク押下 → RegisterActivity に遷移
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // -------- ViewModel の状態を監視 --------

        viewModel.getLoading().observe(this, isLoading -> {
            loginBtn.setEnabled(!isLoading);
        });

        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, "ログイン失敗: " + msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getSuccess().observe(this, ok -> {
            if (ok != null && ok) {
                Toast.makeText(this, "ログイン成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Home.class));
                finish();
            }
        });
    }
}