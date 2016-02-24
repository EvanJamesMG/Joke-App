package com.example.evanjames.mogjoke.activity;

/**
 * Created by EvanJames on 2015/9/17.
 */
public class ListViewItem_girl {
    String content;
    String imageTitle;
    String imageURL;

    public ListViewItem_girl(String content, String imageTitle,String imageURL) {
        super();
        this.content = content;
        this.imageTitle = imageTitle;
        this.imageURL = imageURL;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String  getImageTitle(){
        return imageTitle;
    }
    public void setImageTitle(String imageTitle){
        this.imageTitle = imageTitle;
    }

}
