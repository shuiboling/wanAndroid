package com.example.wanandroid.network.response;

import java.io.Serializable;
import java.util.List;

public class SearchResp implements Serializable {
    /*
    * "apkLink":"",
    * "audit":1,
    * "author":"",
    * "canEdit":false,
    * "chapterId":494,
    * "chapterName":"广场",
    * "collect":false,
    * "courseId":13,
    * "desc":"",
    * "descMd":"",
    * "envelopePic":"",
    * "fresh":true,
    * "id":12016,
    * "link":"https://juejin.im/entry/5e4c966ce51d45270f52b742",
    * "niceDate":"5小时前",
    * "niceShareDate":"5小时前",
    * "origin":"",
    * "prefix":"",
    * "projectLink":"",
    * "publishTime":1582170577000,
    * "selfVisible":0,
    * "shareDate":1582170577000,
    * "shareUser":"goweii",
    * "superChapterId":494,
    * "superChapterName":"广场Tab",
    * "tags":[],
    * "title":"<em class='highlight'>Android</em>性能优化：我总结了关于内存泄漏的所有知识",
    * "type":0,
    * "userId":20382,
    * "visible":0,
    * "zan":0*/
    private String title;
    private String chapterName;
    private String shareUser;
//    private List<Tags> tags;
    private String niceDate;
    private String link;
    private String author;
    private boolean collect;

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public boolean getCollect() {
        return collect;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getChapterName() {
        return chapterName;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public String getShareUser() {
        return shareUser;
    }

    public String getTitle() {
        return title;
    }

//    public List<Tags> getTags() {
//        return tags;
//    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
    }

//    public void setTags(List<Tags> tags) {
//        this.tags = tags;
//    }

    public void setTitle(String title) {
        this.title = title;
    }
}
