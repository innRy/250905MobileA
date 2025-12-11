package jp.ac.meijo.android.mobilea.ui.auth.register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import jp.ac.meijo.android.mobilea.R;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel viewModel;
    private EditText emailInput, passwordInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ViewModel 取得
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);

        // ▼ UIイベント → ViewModel を呼ぶ
        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            viewModel.register(email, password);
        });

        // ▼ ViewModel の状態を監視 → UI反映
        viewModel.getLoading().observe(this, isLoading -> {
            if (isLoading != null) registerButton.setEnabled(!isLoading);
        });

        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null)
                Toast.makeText(this, "エラー: " + msg, Toast.LENGTH_SHORT).show();
        });

        viewModel.getSuccess().observe(this, ok -> {
            if (ok != null && ok) {
                Toast.makeText(this, "登録成功！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
