package com.zhuandian.qxe.entity;

/**
 * 新闻资讯的业务类
 * Created by 谢栋 on 2016/9/3.
 */
public class NewsEntity {

    private String title;
    private String contentUrl;
    private String image1URL;
    private String image2URL;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage2URL() {
        return image2URL;
    }

    public void setImage2URL(String image2URL) {
        this.image2URL = image2URL;
    }

    public String getImage1URL() {
        return image1URL;
    }

    public void setImage1URL(String image1URL) {
        this.image1URL = image1URL;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    private String itemContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}



