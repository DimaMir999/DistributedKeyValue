package org.dimamir999.model;

public enum CommandType {
    CREATE("POST"), READ("GET"), UPDATE("PUT"), DELETE("DEL");

    private final String keyWord;

    CommandType(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyWord(){
        return keyWord;
    }
}
