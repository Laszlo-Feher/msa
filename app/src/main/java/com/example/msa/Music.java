package com.example.msa;

public class Music {
    private String id;
    private String title;
    private String url;

    public Music(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public Music() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
