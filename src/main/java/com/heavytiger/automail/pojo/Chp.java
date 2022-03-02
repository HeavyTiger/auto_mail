package com.heavytiger.automail.pojo;

/**
 * @author heavytiger
 * @version 1.0
 * @description chp文案
 * @date 2022/3/1 15:04
 */
public class Chp {
    private Integer id;

    private Boolean isUsed;

    private String sentence;

    public Chp() {
    }

    public Chp(Integer id, Boolean isUsed, String sentence) {
        this.id = id;
        this.isUsed = isUsed;
        this.sentence = sentence;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
