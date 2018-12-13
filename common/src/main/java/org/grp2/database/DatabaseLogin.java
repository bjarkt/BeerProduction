package org.grp2.database;

public enum DatabaseLogin {
    LIVE("jdbc:postgresql://tek-mmmi-db0a.tek.c.sdu.dk:5432/si3_2018_group_22_db",
            "si3_2018_group_22",
            "snipe0[seism"),

    /**
     * Remember to start a docker instance when using this! `docker-compose up testdb`
     */
    TEST("jdbc:postgresql://localhost:5431/testdb", "postgres", "password");

    private String url;
    private String username;
    private String password;

    DatabaseLogin(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
