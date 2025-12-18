package jp.ac.meijo.android.mobilea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import jp.ac.meijo.android.mobilea.adapter.PostAdapter;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> posts;
    private FloatingActionButton fabNewPost;
    private ImageButton buttonLogout;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        posts = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_posts);
        fabNewPost = findViewById(R.id.fab_new_post);
        buttonLogout = findViewById(R.id.button_logout);

        int spanCount = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);

        fabNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, TweetActivity.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();

                Toast.makeText(PostActivity.this, "ログアウトしました", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PostActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserAuthAndLoadData();
    }

    private void checkUserAuthAndLoadData() {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            Log.d(TAG, "User is authenticated: " + user.getUid());
            loadPostsFromFirestore();
        } else {
            Log.d(TAG, "No authenticated user. Redirecting to LoginActivity.");
            Intent intent = new Intent(PostActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void loadPostsFromFirestore() {
        db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> newPosts = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imageUrl = document.getString("imageUrl");
                            String userId = document.getString("userId");

                            if (imageUrl != null && userId != null) {
                                Post post = new Post(imageUrl, userId);
                                newPosts.add(post);
                            }
                        }
                        postAdapter.updatePosts(newPosts);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        Toast.makeText(PostActivity.this, "投稿データの取得に失敗しました。", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}