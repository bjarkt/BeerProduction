package org.grp2.shared;

public class Recipe {
    private int id;
    private String name;
    private int minSpeed;
    private int maxSpeed;

    public Recipe(int id, String name, int minSpeed, int maxSpeed) {
        this.id = id;
        this.name = name;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
