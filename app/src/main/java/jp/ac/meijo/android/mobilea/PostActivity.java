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
        // (2)で作成するレイアウトファイル
        setContentView(R.layout.activity_post);

        // 1. データの準備（ダミーデータ）
        posts = createDummyPosts();

        // 2. RecyclerViewの初期化
        recyclerView = findViewById(R.id.recycler_view_posts);

        // 3. レイアウトマネージャの設定 (グリッド表示)
        // PostAdapterのonCreateViewHolderで width / 3 に高さを設定しているため、
        // ここで spanCount=3 にするのが適切です。
        int spanCount = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        // 4. Adapterの設定
        postAdapter = new PostAdapter(posts);
        recyclerView.setAdapter(postAdapter);
    }

    /**
     * テスト用のダミーPostデータを作成します。
     */
    private List<Post> createDummyPosts() {
        List<Post> dummyList = new ArrayList<>();
        // 実際にはFirebaseやAPIからデータを取得します。
        // ここでは、ダミーURL（プレースホルダー画像）を使っていくつかPostを作成
        for (int i = 1; i <= 30; i++) {
            // 注意: 実際のアプリでは有効な画像URLを使用する必要があります
            String dummyUrl = "https://picsum.photos/seed/" + i + "/400/500";
            String userId = "user_" + (i % 5 + 1);

            Post post = new Post(dummyUrl, userId);
            dummyList.add(post);
        }
        return dummyList;
    }
}