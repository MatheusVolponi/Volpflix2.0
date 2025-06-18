package br.com.volpflix.model;

public enum Category {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME ("Crime", "Crime"),
    SUSPENSE("Suspense", "Suspense"),
    ANIMACAO("Animation", "Animação"),
    AVENTURA("Adventure", "Aventura");

    private String categoryOmdb;
    private String portugeseCategory;

    Category(String categoryOmdb, String portugeseCategory){
        this.categoryOmdb = categoryOmdb;
        this.portugeseCategory = portugeseCategory;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.categoryOmdb.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Category fromPortuguese(String text) {
        for (Category category : Category.values()) {
            if (category.portugeseCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
