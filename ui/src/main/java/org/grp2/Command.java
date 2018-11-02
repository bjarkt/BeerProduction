package org.grp2;

public class Command {

    private String keyword;
    private String[] args;

    public Command(String keyword, String[] args) {
        this.keyword = keyword;
        this.args = args;
    }

    public String getKeyword() {
        return keyword;
    }

    public String[] getArgs() {
        return args;
    }
}
