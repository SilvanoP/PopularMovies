package br.com.udacity.popularmovies.feature.shared;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {

    protected CompositeDisposable disposable;
    protected WeakReference<V> weakView;

    protected BasePresenterImpl() {
        disposable = new CompositeDisposable();
    }

    @Override
    public void setView(V view) {
        weakView = new WeakReference<>(view);
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
