ClickClick
==========

Required Dependencies
---------------------
+ Eclipse Luna
+ ADT 24.0+
+ Latest Android sdk with Google Play Services Library

Setting up the app in eclipse
-----------------------------
+ Clone the git repo
+ Import the project to your eclipse workspace
+ Import the appcompat project to the workspace from <sdk-directory>/extras/android/support/v7
+ Import the google-play-service_lib project from <sdk-directory>/extras/google/google_play_services
+ Check the properties of both the above projects by right clicking and selecting properties. In the Android tab, make sure that Android 5.0+ is checked and Is Library is checked.
+ Open the properties of clickclick app and in the Android tab remove any references which are present with the red cross.
+ Add the above to libraries by clicking the add button, you chould see green ticks along side them once added.
+ The project should now be ready for testing.

WorkFlow of the App
-------------------
+ All the options are available in the action bar. 
+ You can either click a picture which is then stored in your SD card under the folder Pictures/ClickClick.
+ You can start tracking your location by pressing the track button, whenever there is a change in your location, you get a toast and the latlong will be stored in the SQLite DB.
+ You can also stop the tracking. Also, the tracking is stopped whenever you leave the app.
+ At a time only 5 photos are loaded, if you want to load more photos, press the load more button.
+ The listview is not optimised presently, so it takes time before the correct image comes in the correct place, but there will be no lag.
