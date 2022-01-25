# News App

A demo Android project that aggregates news from [newsapi.org](https://newsapi.org)

## Project Specifications
The app includes the following features: 
- Fetch news using [newsapi.org](https://newsapi.org) and display in the app.
- News can be bookmarked to be read later.
- News are fetched periodically (even when the app is not running), saved locally and are available 
  when the app is opened.
- Articles become stale when a maximum amount is reached. The stale articles are replaced with new 
  ones.
- Full articles are presented in a web view.

## App Architecture
The app is multi-module and uses the MVVM architecture (also employs clean architecture to an extent).

- The UI uses both XML views and Compose. Some screens are purely Android XML views 
  while a few other ones are a combination of both.
- View Models: The app uses the [Android architecture ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel)
- Database: [Room Database library](https://developer.android.com/training/data-storage/room)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- Dependency Injection with [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Custom tabs](https://developer.chrome.com/docs/android/custom-tabs/)

## Requirements/Getting Started

- The project is built in Android Studio with Arctic Fox | 2020.3.1 Patch 4 version
- No additional steps are required. Open the project in Android Studio, sync project with Gradle files, and build project in AS, and run on a connected device/emulator

#### Test

- This project contains unit tests and can be run in Android Studio.
  
