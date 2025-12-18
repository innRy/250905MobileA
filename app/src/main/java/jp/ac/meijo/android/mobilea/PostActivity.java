package jp.ac.meijo.android.mobilea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataOnResume();
    }

    private void loadDataOnResume() {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            Log.d(TAG, "Activity resumed. Reloading posts for user: " + user.getUid());
            loadPostsFromFirestore();
        } else {
            Log.d(TAG, "Activity resumed. User is null. Starting anonymous sign-in.");
            signInAnonymouslyAndLoadPosts();
        }
    }


    private void signInAnonymouslyAndLoadPosts() {

        auth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser newUser = auth.getCurrentUser();
                        Log.d(TAG, "Anonymous sign in successful. UID: " + newUser.getUid());
                        Toast.makeText(PostActivity.this, "匿名ログインしました。", Toast.LENGTH_SHORT).show();
                        loadPostsFromFirestore();
                    } else {
                        Log.w(TAG, "Anonymous sign in failed.", task.getException());
                        Toast.makeText(PostActivity.this, "認証エラー。投稿データの読み込みができません。", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadPostsFromFirestore() {
        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> newPosts = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());

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