package ru.otus.domain;

import org.apache.commons.lang3.RandomUtils;

public class Soul {
    private static final int START_INCLUSIVE = 0;
    private static final int END_INCLUSIVE = 1000;

    private String name;
    private int badDeeds;
    private int goodDeeds;

    public Soul(String name) {
        this.name = name;
        this.badDeeds = RandomUtils.nextInt(START_INCLUSIVE, END_INCLUSIVE);
        this.goodDeeds = RandomUtils.nextInt(START_INCLUSIVE, END_INCLUSIVE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBadDeeds() {
        return badDeeds;
    }

    public void setBadDeeds(int badDeeds) {
        this.badDeeds = badDeeds;
    }

    public int getGoodDeeds() {
        return goodDeeds;
    }

    public void setGoodDeeds(int goodDeeds) {
        this.goodDeeds = goodDeeds;
    }

    public boolean isGood() {
        return goodDeeds > badDeeds;
    }
}
