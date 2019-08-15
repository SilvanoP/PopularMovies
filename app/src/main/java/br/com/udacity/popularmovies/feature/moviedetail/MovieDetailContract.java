package br.com.udacity.popularmovies.feature.moviedetail;

import java.util.List;

import br.com.udacity.popularmovies.data.entities.Review;
import br.com.udacity.popularmovies.data.entities.Video;
import br.com.udacity.popularmovies.feature.shared.BasePresenter;
import br.com.udacity.popularmovies.feature.shared.BaseView;

public interface MovieDetailContract {

    interface View extends BaseView {
        void showBackdropImage(String backdropUrl);
        void showPosterImage(String posterUrl);
        void showTitle(String title);
        void showOriginalName(String originalName);
        void showReleaseDate(String releaseDate);
        void showRating(float average);
        void showOverview(String overview);
        void showTrailers();
        void hideTrailers();
        void refreshTrailers(List<Video> videos);
        void showReviews();
        void hideReviews();
        void refreshReviews(List<Review> reviews);
        void changeFavoriteIcon(boolean isFavorited);
        void goToYoutube(String youtubeUrl);
    }

    interface Presenter extends BasePresenter<View> {
        void loadData();
        void onFavoritePressed();
        void onVideoPressed(int position);
    }
}
