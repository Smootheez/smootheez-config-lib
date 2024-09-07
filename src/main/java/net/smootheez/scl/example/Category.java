package net.smootheez.scl.example;

public enum Category {
    CATEGORY_ONE ("example.category.one"),
    CATEGORY_TWO ("example.category.two"),
    CATEGORY_THREE ("example.category.three"),;

    private final String translation;
    Category(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }
}
