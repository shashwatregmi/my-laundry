package com.example.mylaundry;

public class Photo {
    String uri, user;

    public Photo(String uri, String user){
        this.uri = uri;
        this.user = user;
    }

    public Photo(){

    }

    public String getUri() {
        return uri;
    }

    public String getUser() {
        return user;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setUser(String user) {
        this.user = user;
    }


}
