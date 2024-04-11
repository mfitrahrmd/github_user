BEFORE RUN :
- set config field Github API Key with your Github Key in build.gradle(app)

### UI

The UI implements single activity architecture

1. `MainActivity` - Activity, Host the navigation component fragments.
2. `Fragments` - Contains fragments:
   a) `SearchUsersFragment` - Show popular users (currently in Indonesia) & search for users.

   b) `DetailUserFragment` - Show more information about a user.

   c) `UserFavoriteFragment` - Show saved users.

   d) `SettingsFragment` - For app settings

### ViewModel

Each fragment has it's own View Model for fetching popular users, searching users and add user to favorite. Also send out the status of the process call like Loading, Success, Error using `sealed` class.

### Repository

The Repository is responsible to fetch data needed by UI, without UI knowing how it was fetched

- `Cache` - local data storage to store data fetched from network
- `Data Source` - different implementations of where/how to get the data
- `Remote Mediator` - handle pagination and store data from network into cache

### Entity

Different types of entity

1. `User` - Domain level entity
2. `Remote` - Network level entity
3. `DB` - Local cache level entity

### Dependency Injection

This app using manual Dependency Injection where all ViewModels are registered in ui.main.AppViewModelProvider and to be used by BaseFragment.
The rest of dependency are injected from application class GithubUserApplication.

## Tech Stack
1.  [Android appcompat](https://developer.android.com/jetpack/androidx/releases/appcompat), [KTX](https://developer.android.com/kotlin/ktx), [Constraint layout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout), [Material Support](https://material.io/develop/android/docs/getting-started).
2.  [Android View Binding](https://developer.android.com/topic/libraries/view-binding)
3. [Retrofit](https://square.github.io/retrofit/) for REST API communication
4. [Coroutine](https://developer.android.com/kotlin/coroutines) for Network call
5. [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
6. [Room](https://developer.android.com/jetpack/androidx/releases/room) for local database.
7. [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) for supporting navigation through the app.
8. [Glide](https://github.com/bumptech/glide) for image loading.
9. [Facebook Shimmer](https://github.com/facebookarchive/shimmer-android) for skeleton loading.
10. [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for paging
11. [Data Store](https://developer.android.com/topic/libraries/architecture/datastore) for store preference
