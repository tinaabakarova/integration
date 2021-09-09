package ru.otus.domain;

public class Human {
     private String body;
     private Soul soul;

    public Human(String body, Soul soul) {
        this.body = body;
        this.soul = soul;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Soul getSoul() {
        return soul;
    }

    public void setSoul(Soul soul) {
        this.soul = soul;
    }
}
