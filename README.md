# PopularMovies
Udacity Android Development Course

## The Movies Database API Key

Please before running put your key on gradle.properties, 'MyTheMovieDBApiToken' propertie
Also include the properties below on the same file:
android.enableJetifier=true
android.useAndroidX=true

## Libraries Used

- [Room](https://developer.android.com/topic/libraries/architecture/room): For SQLite database management
- [JavaRx](https://github.com/ReactiveX/RxJava): Mainly for management of background thread processes (with [RxAndroid](https://github.com/ReactiveX/RxAndroid) )
- [Picasso](https://square.github.io/picasso/): Management of images

## Layers

This app is build using MVP Architecture

- Model
This layer contains the entities, access to data sources (cloud, local database and local retrofit cache)
This layer can be found on the 'data' package.
data
  |- database
     |- MovieDAO.java (contains the calls to movies table on database)
     |- PopularMoviesDatabase.java (interface used by Room to create the database)
  |- entities
     |- remote (the classes below are used only for the Gson parser)
        |- MovieListResponse.java
        |- ReviewsListResponse.java
        |- VideosListResponse.java
     |- Movie.java
     |- MovieCategory (a more readable format of the categories)
     |- Review
     |- Video
  |- repository
     |- MoviesRepositoryImpl (contains the use cases and delegates the calls to the correct data sources)
  |- webservice
     |- CacheInterceptor (intercept retrofit calls to create and access the cache when needed)
     |- TheMovieDBService (contains the calls to The Movie Database through their api)

- Interactor
An interface to connect the model to Presenter
feature
  |- shared
     |- MoviesRepository.java

- Presenter
This layer serves as the middle ground between Model and View.
It retrieves data from Model and formats according to the View needs.
feature
  |- shared
     |- BasePresenterImpl.java (base implementation for the specific presenters)
  |- listmovies
     |- ListMoviePresenter.java
  |- moviedetail
     |- MovieDetailPresenter.java


- View
This layer contains the visual components of the app
feature
  |- listmovies
     |- MainActivity.java (the screen that shows the list of movies)
  |- moviedetail
     |- MovieDetailActivity.java (the screen that shows the detailed information of the selected movie)

- Other
util (contains helper classes and global constants)
di (configuration of Dagger2 for dependence injection)

## Other Information

This app tries to follow Single Responsibility Principle, that states that each class should have only on responsibility, i.e., should deal only with one functionality.

This app also tries to be as clean as possible, being able to be easily understood, tested and maintainable.