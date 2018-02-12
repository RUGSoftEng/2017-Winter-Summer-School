[![Build Status](https://travis-ci.org/RUGSoftEng/2017-Winter-Summer-School-Android.svg?branch=test)](https://travis-ci.org/RUGSoftEng/2017-Winter-Summer-School-Android)
[![Coverage Status](https://coveralls.io/repos/github/RUGSoftEng/2017-Winter-Summer-School-Android/badge.svg?branch=master)](https://coveralls.io/github/RUGSoftEng/2017-Winter-Summer-School-Android?branch=master)
# UG Summer & Winter Schools Android Application
The​ ​Winter​ ​and​ ​Summer​ ​Schools​ ​Android​ ​Application​ ​is​ ​a​ ​project​ ​developed​ ​at​ ​the​ ​RUG​ ​which aims​ ​to​ ​help​ ​to​ ​communicate​ ​between​ ​the​ ​school​ ​organizers​ ​and​ ​the​ ​participants​ ​in​ ​order​ ​for them​ ​to​ ​provide​ ​and​ ​access​ ​general​ ​and​ ​specific​ ​information​ ​and​ ​to​ ​create​ ​community​ ​so​ ​that​ ​it acts​ ​a​ ​communication​ ​hub​ ​in​ ​general.​

Download Current Version on Google Play : [UG Summer & Winter Schools](https://play.google.com/store/apps/details?id=nl.rug.www.rugsummerschools)

## Getting Started

This instructions provide you with installation process to run the application on local machine using Android Studio.

### Prerequisites

* Android Studio 3.1 Beta+
* Android SDK 27+
* Gradle 4.5+
* Build tool version : 27.0.3
* Minimum SDK version 19

### Installation

Clone this project where you want to install. Run Android Studio and import project. Sync the project with Gradle files and build. You may have to install or download required files.

Build on command line :
```
./gradlew build
```

### Adding Android Studio SHA1 key to Firebase Console

This project authenticates users using Firebase via Facebook or Google account for forum section. In order to authenticate using Firebase, you have to add Android Studio SHA1 key to Firebase Console. Please contact to us to add your key.

### Setting up the sever

As default, the application is connected to the actual running server [Web application](http://turing13.housing.rug.nl:8800).
The application will be working normally. Also, you can run the application with local server (See : [2017-Winter-Summer-School-App](https://github.com/RUGSoftEng/2017-Winter-Summer-School-App)). In order to communicate with local server, you have to change the server URL to your local server address on `NetworkingService` class.

```
public class NetworkingService<T extends Content> {
    ...
    private static final String HTTP_URL = /* put your local server address here */;
    ...
```

## Author
* **Jeongkyun Oh** - j.k.oh@student.rug.nl
