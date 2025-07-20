package dev.blaauwendraad.recipe_book.data.entity;

public enum RoleName {
    admin("admin"),
    user("user");

    RoleName(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
