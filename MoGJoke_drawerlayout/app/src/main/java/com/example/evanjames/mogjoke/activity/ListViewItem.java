package com.example.evanjames.mogjoke.activity;

/**
 * Created by EvanJames on 2015/9/15.
 */
public class ListViewItem {
    String content;
    String imageTitle;
    String imageTime;
    String imageURL;

    public ListViewItem(String content, String imageTitle,String imageTime,String imageURL) {
        super();
        this.content = content;
        this.imageTitle = imageTitle;
        this.imageTime = imageTime;
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

    public String getImageTime(){
        return imageTime;
    }

    public void setImageTime(){
        this.imageTime = imageTime;
    }



}