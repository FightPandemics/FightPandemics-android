# FightPandemics Android


[![License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat)](https://github.com/FightPandemics/FightPandemics-android/blob/development/LICENSE)
[![Platform](https://img.shields.io/badge/platform-android-green.svg?style=flat&logo=android)](#)
[![API](https://img.shields.io/badge/API-21%2B-blue.svg?style=flat)](#)
[![Build Status](https://travis-ci.org/FightPandemics/FightPandemics-android.svg?branch=development)](https://travis-ci.com/FightPandemics/FightPandemics-android)


Android app for the [FightPandemics](https://fightpandemics.com/)

### Getting Started
* The app is written entirely in Kotlin and uses the Gradle build system
* To build the app, use the gradlew build command or use `Import Project` in Android Studio.
* A Stable Version of 4.0 or newer is required and can be downloaded [Here](https://developer.android.com/studio)
* Open `FightPandemics-android` in Android Studio 
* Happy coding!

## Contributing

See the [CONTRIBUTING](https://github.com/FightPandemics/FightPandemics-android/blob/development/CONTRIBUTING.md) file for guidelines on contributing.

## Introduction 
The application uses Clean Architecture based on MVVM and Repository patterns. Implemented Architecture principles follow Google recommended [Guide to app architecture](https://developer.android.com/jetpack/guide)
![architecture](https://user-images.githubusercontent.com/24237865/77502018-f7d36000-6e9c-11ea-92b0-1097240c8689.png)

We kept logic away from Activities and Fragments and moved it to [ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel). We observed data using [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) and used the Data Binding Library to bind UI components 
in layouts to the app's data sources.

[Android Jetpack](https://developer.android.com/topic/libraries/architecture/) is used as an Architecture but not limited to ViewModel, LiveData, Lifecycles, Navigation, Room and Data Binding. See a complete list in "Libraries used" section.

The application does network HTTP requests via [Retrofit, OkHttp](https://github.com/square/retrofit) and [GSON](https://github.com/google/gson). Loaded data is saved to SQL based database Room, which serves as single source of truth and support offline mode. Paging library is used for data pagination online and offline.

Kotlin Coroutines manage background threads with simplified code and reducing needs for callbacks. Combination of Coroutines and Kotlin build in functions (transformation, collections) are preferred over RxJava 2.

Dagger 2 is used for dependency injection.

Glide is used for image loading and Timber for logging.




Libraries Used
--------------

Android Jetpack is a set of components, tools and guidance to make great Android apps. They bring
together the existing Support Library and Architecture Components and arranges them into four
categories:

![Android Jetpack](https://androidexample365.com/content/images/2018/06/jetpack_donut.png "Android Jetpack Components")

* [Foundation][0] - Components for core system capabilities, Kotlin extensions and support for
  multidex and automated testing.
  * [AppCompat][1] - Degrade gracefully on older versions of Android.
  * [Android KTX][2] - Write more concise, idiomatic Kotlin code.
  * [Test][4] - An Android testing framework for unit and runtime UI tests.
* [Architecture][10] - A collection of libraries that help you design robust, testable, and
  maintainable apps. Start with classes for managing your UI component lifecycle and handling data
  persistence.
  * [Data Binding][11] - Declaratively bind observable data to UI elements.
  * [Lifecycles][12] - Create a UI that automatically responds to lifecycle events.
  * [LiveData][13] - Build data objects that notify views when the underlying database changes.
  * [Navigation][14] - Handle everything needed for in-app navigation.
  * [Room][16] - SQLite database with in-app objects and compile-time checks.
  * [ViewModel][17] - Store UI-related data that isn't destroyed on app rotations. Easily schedule
     asynchronous tasks for optimal execution.
  * [WorkManager][18] - Manage your Android background jobs.
  * [Paging][19] - Load and display small chunks of data at a time.
* [UI][30] - Details on why and how to use UI Components in your apps - together or separate.
  * [Animations & Transitions][31] - Move widgets and transition between screens.
  * [Fragment][34] - A basic unit of composable UI.
  * [Layout][35] - Lay out widgets using different algorithms.
  * [Material][36] - Material Components.
* Third party
  * [Kotlin Coroutines][91] for managing background threads with simplified code
     and reducing needs for callbacks.
  * [Dagger 2][92] A fast dependency injector.
  * [Retrofit 2][93] A configurable REST client.
  * [OkHttp 3][94] A type-safe HTTP client.
  * [GSON][95] A Json - Object converter using reflection.
  * [Glide][90] Image loading.
  * [Timber][96] A logger.


[0]: https://developer.android.com/jetpack/components
[1]: https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat
[2]: https://developer.android.com/kotlin/ktx
[4]: https://developer.android.com/training/testing/
[10]: https://developer.android.com/jetpack/arch/
[11]: https://developer.android.com/topic/libraries/data-binding/
[12]: https://developer.android.com/topic/libraries/architecture/lifecycle
[13]: https://developer.android.com/topic/libraries/architecture/livedata
[14]: https://developer.android.com/topic/libraries/architecture/navigation/
[16]: https://developer.android.com/topic/libraries/architecture/room
[17]: https://developer.android.com/topic/libraries/architecture/viewmodel
[18]: https://developer.android.com/topic/libraries/architecture/workmanager
[19]: https://developer.android.com/topic/libraries/architecture/paging
[30]: https://developer.android.com/guide/topics/ui
[31]: https://developer.android.com/training/animation/
[34]: https://developer.android.com/guide/components/fragments
[35]: https://developer.android.com/guide/topics/ui/declaring-layout
[36]: https://material.io/develop/android/docs/getting-started/
[90]: https://bumptech.github.io/glide/
[91]: https://kotlinlang.org/docs/reference/coroutines-overview.html
[92]: https://dagger.dev/users-guide
[93]: https://square.github.io/retrofit/
[94]: https://square.github.io/okhttp/
[95]: https://github.com/google/gson
[96]: https://github.com/JakeWharton/timber
[97]: http://facebook.github.io/stetho/




## Reference to get you started 
1. https://proandroiddev.com/gradle-dependency-management-with-kotlin-94eed4df9a28
2. https://proandroiddev.com/intro-to-app-modularization-42411e4c421e
3. https://www.droidcon.com/media-detail?video=380845032
4. https://proandroiddev.com/kotlin-clean-architecture-1ad42fcd97fa
5. https://www.raywenderlich.com/3595916-clean-architecture-tutorial-for-android-getting-started
6. http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
7. https://developer.android.com/guide/navigation/navigation-dynamic
8. https://proandroiddev.com/navigation-with-dynamic-feature-modules-48ee7645488
9. https://medium.com/androiddevelopers/patterns-for-accessing-code-from-dynamic-feature-modules-7e5dca6f9123
10. https://hackernoon.com/android-components-architecture-in-a-modular-word-d0k32i6


## Credits

Special thanks to all [contributors](https://github.com/FightPandemics/FightPandemics-android/contributors) :purple_heart:

## License

FightPandemics is available under the MIT license. See the [LICENSE](https://github.com/FightPandemics/FightPandemics-android/blob/development/LICENSE) file for more info.
