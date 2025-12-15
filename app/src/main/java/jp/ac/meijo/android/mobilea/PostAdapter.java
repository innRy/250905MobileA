package jp.ac.meijo.android.mobilea;
// ↑ パッケージ名 (jp.ac.meijo.android.mobilea) はプロジェクトに合わせてください

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Glide や Coil などの画像読み込みライブラリをプロジェクトに追加する必要があります
// import com.bumptech.glide.Glide; // Glide の場合
// import coil.Coil; // Coil の場合

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    // ----------------------------------------------------
    // 1. ViewHolder の作成 (レイアウトファイルのインフレート)
    // ----------------------------------------------------
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // grid_item_post.xml をインフレート（XMLをViewオブジェクトに変換）
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_post, parent, false);

        // 🔥 グリッドアイテムのサイズ調整（幅の1/3を高さにする）
        // post_screen.xml で spanCount="3" を設定しているため
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = parent.getMeasuredWidth() / 3;
        view.setLayoutParams(layoutParams);

        return new PostViewHolder(view);
    }

    // ----------------------------------------------------
    // 2. データのバインド (ViewHolder に Post オブジェクトのデータを渡す)
    // ----------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    // ----------------------------------------------------
    // 3. データ件数を返す
    // ----------------------------------------------------
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // ----------------------------------------------------
    // 【オプション】データの更新メソッド
    // ----------------------------------------------------
    public void updatePosts(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    // =======================================================
    // 🔥 PostViewHolder の定義 (Adapter の内部クラス)
    // =======================================================
    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public PostViewHolder(View itemView) {
            super(itemView);
            // grid_image_view の ID を使って ImageView を取得
            imageView = itemView.findViewById(R.id.grid_image_view);
        }

        public void bind(Post post) {
            // 1. 画像の読み込み
            // 【重要】post.getImageUrl() に含まれる URL を使い、ImageViewに画像を読み込みます。
            // ここに、Glide や Coil のコードを記述します。

            // 例: Glide を使う場合 (Glideをプロジェクトに追加後)
            /*
            Glide.with(itemView.getContext())
                .load(post.getImageUrl())
                .into(imageView);
            */

            // 2. クリックリスナーの設定
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 投稿がタップされたときの詳細画面への遷移処理などを実装
                }
            });
        }
    }
}
