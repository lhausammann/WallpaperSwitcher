WallpaperSwitcher
=================
Shows a wallaper from unsplash every x seconds.
Currently only on Mac OsX.

Settings
----
- activeDownloadName: flickr | unsplash
- interval: time in milliseconds to change the background image.

Unsplash works out of the box. FLickr needs additional settings (rename config/applicationSecret.yml to config/application.yml):
```yml
secret:
  flickr:
    user_id: <your user id>
    api_key: <your api key>
```   

Run (gradle wrapper)
----
cd <wallpaperSwitcherDirectory>
./gradlew bootRun



