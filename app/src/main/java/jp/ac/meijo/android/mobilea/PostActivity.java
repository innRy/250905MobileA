package jp.ac.meijo.android.mobilea;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import jp.ac.meijo.android.mobilea.adapter.PostAdapter;

public class PostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        posts = createDummyPosts();

        recyclerView = findViewById(R.id.recycler_view_posts);

        int spanCount = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);
    }

    private List<Post> createDummyPosts() {
        List<Post> dummyList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            String dummyUrl = "https://picsum.photos/seed/" + i + "/400/500";
            String userId = "user_" + (i % 5 + 1);

            Post post = new Post(dummyUrl, userId);
            dummyList.add(post);
        }
        return dummyList;
    }
}