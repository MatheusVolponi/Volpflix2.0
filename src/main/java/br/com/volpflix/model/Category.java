package br.com.volpflix.model;

public enum Category {
    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME ("Crime"),
    SUSPENSE("Suspense"),
    ANIMACAO("Animation"),
    AVENTURA("Adventure");

    private String categoryOmdb;

    Category(String categoryOmdb){
        this.categoryOmdb = categoryOmdb;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.categoryOmdb.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
