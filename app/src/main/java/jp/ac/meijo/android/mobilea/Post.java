package jp.ac.meijo.android.mobilea;

public class Post {
    private String imageUrl;
    private String userId;

    public Post(){

    }

    public Post(String imageUrl, String userId) {
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setUserId(String userId){
        this.userId=userId;
    }


    public void setImageUrl(String imageUrl){
        this.imageUrl=imageUrl;
    }
}
