package br.com.udacity.popularmovies.feature.shared;

public interface BasePresenter<V extends BaseView> {

    void setView(V view);
    void onDestroy();
}
