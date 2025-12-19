package jp.ac.meijo.android.mobilea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);

        View constraintLayout = findViewById(R.id.constraintLayout);
        if (constraintLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(constraintLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
                return insets;
            });
        }

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        posts = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_posts);
        fabNewPost = findViewById(R.id.fab_new_post);
        buttonLogout = findViewById(R.id.button_logout);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);

        fabNewPost.setOnClickListener(v -> {
            startActivity(new Intent(PostActivity.this, TweetActivity.class));
        });

        buttonLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(PostActivity.this, "ログアウトしました", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PostActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_post);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_morning_routine) {
                Intent intent = new Intent(this, MorningRoutineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_post) {
                return true;
            }
            return false;
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
            loadPostsFromFirestore();
        } else {
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
                                newPosts.add(new Post(imageUrl, userId));
                            }
                        }
                        postAdapter.updatePosts(newPosts);
                    }
                });
    }
}