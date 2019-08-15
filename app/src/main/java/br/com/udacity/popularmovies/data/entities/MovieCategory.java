package br.com.udacity.popularmovies.data.entities;

public enum MovieCategory {
    POPULAR(1),
    TOP_RATED(2),
    UPCOMING(3),
    FAVORITE(4);

    private int value;

    public int getValue() {
        return value;
    }

    MovieCategory(int value) {
        this.value = value;
    }
}
