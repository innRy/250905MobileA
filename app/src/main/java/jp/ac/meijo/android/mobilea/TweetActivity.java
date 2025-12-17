package jp.ac.meijo.android.mobilea;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class TweetActivity extends AppCompatActivity {

    private ImageView imageViewSelectedPhoto;
    private TextView textViewSelectHint;
    private Button button;

    private Uri selectedImageUri = null;

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private FirebaseFirestore db;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        displaySelectedImage(selectedImageUri);
                        button.setEnabled(true);
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(this, "画像を選択するにはストレージの権限が必要です。", Toast.LENGTH_LONG).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        imageViewSelectedPhoto = findViewById(R.id.image_view_selected_photo);
        textViewSelectHint = findViewById(R.id.text_view_select_hint);
        button = findViewById(R.id.button);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "認証セッションがありません。ホーム画面に戻ってください。", Toast.LENGTH_LONG).show();
            button.setEnabled(false);
        } else {
            button.setEnabled(false);
        }

        imageViewSelectedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndOpenGallery();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    uploadImageToFirebase(selectedImageUri);
                } else {
                    Toast.makeText(TweetActivity.this, "画像を選択してください", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkPermissionAndOpenGallery() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissionLauncher.launch(permission);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void displaySelectedImage(Uri imageUri) {
        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(imageViewSelectedPhoto);

        textViewSelectHint.setVisibility(View.GONE);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (storage == null || auth.getCurrentUser() == null) {
            Toast.makeText(this, "認証エラーまたはサービス未初期化", Toast.LENGTH_LONG).show();
            return;
        }

        button.setEnabled(false);

        final String fileName = "posts/" + auth.getCurrentUser().getUid() + "/" + UUID.randomUUID().toString();
        StorageReference imageRef = storage.getReference().child(fileName);

        imageRef.putFile(imageUri)
                .addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    button.setText("アップロード中 (" + (int) progress + "%)");
                })
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        savePostToFirestore(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    button.setEnabled(true);
                    button.setText("投稿");
                    Toast.makeText(this, "アップロード失敗: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void savePostToFirestore(String imageUrl) {
        if (db == null || auth.getCurrentUser() == null) return;

        Map<String, Object> postData = new HashMap<>();
        postData.put("imageUrl", imageUrl);
        postData.put("userId", auth.getCurrentUser().getUid());
        postData.put("timestamp", FieldValue.serverTimestamp());

        db.collection("posts")
                .add(postData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "投稿が完了しました！", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    button.setEnabled(true);
                    button.setText("投稿");
                    Toast.makeText(this, "データ保存失敗: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}