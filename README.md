> PGR208 Android Programming exam
<h1 align="center">
Reversed Image Search 🖼🔍📱✍(◔◡◔) EXAM
</h1>



### Application Requirements

* [x] At least one list must be handled by a RecyclerView
* [x] Use intents to pass data
* [x] Use non-UI-blocking requests to the server
* [x] User SQLite database to store data
* [x] Create at least one reasonably complex custom view
* [x] Use Kotlin as a main programming language (some Java is acceptable when re-using
  the code found online)
* [x] Make use of all the end-points on the server
* [x] Exception handling (e.g. no network connection)
* [x] Make sure to handle all the Android lifecycle states (the app will be paused, resumed,
  stopped, etc. during testing)
* [x] Make use of res/drawable, res/layout, and res/values
* [x] Both in code and in the report students are required to put comments and/or explicitly state which of the sub-requirements from Table 3 they are targeting.


### Sub requirements 
* [x] **Hard** (8p): [ApiServices @ 219] [FullscreenActivity @ 83] [Utils @ 95] [ResultsAdapter @ 35] Show that you understand when it is appropriate to use the elvis
  operator by pointing to parts of code where it was used and discussing what edge cases it is meant to handle.
* [x] **Hard** (8p): [MainActivity @ 112, 128] [ResultActivity @ 79] [SavedActivity @ 57] [ApiService @ 51, 115] Make use of lambdas and higher order functions when processing data
  that is similar, but not exactly the same. Discuss the code in the report as well as pros, cons, and alternative code structure.
* [x] **Hard** (4p): [ImageViewModel @ 23, 29] [ApiServices @ 216] Make use of threads or coroutines to make asynchronous operations. Your UI should never freeze until some operation is being executed. Discuss in the report your async code and try to time it by using Android Studio tools or timer in the code for each iteration of your operation. What is the best/worst case scenario of the operation you decided to make asynchronous?
* [x] **Hard** (8p): [ImageDatabaseModel @ 15] Store and retrieve images as blobs in/from the SQLite database.
  Describe and discuss the database structure in detail. What are the pros / cons / alternatives considering a commercial version of your application.
* [x] **Hard** (3p): [MainActivity @ 82, 92] Create callbacks across the application to let the parent class / view
  know when something is changing. Write about all your callbacks in the report and why they are useful for the information flow in your app.
* [x] Soft (8p): Make (or extend existing) classes with methods and companion classes to
  process images. Make use of constructors, overloading, properties, overriding, and interfaces where it is appropriate. Discuss pros, cons, and alternative code structure in the report by referencing your code.
* [x] Soft (8p): [SavedAdapter] [ResultsAdapter] Implement your own RecyclerView adapter and discuss the view you used for
  each row in detail in the report.
* [x] Soft (3p): Have more than one Activity / Fragment (up to you which one you use). Explain
  why you decided to use Activities and/or Fragments.
* [x] Hard (4p): [ImageDatabaseModel @ 10] [ImageResultItemModel @ 6] Pass the data as Parcelable between Activities / fragments
* [x] Soft (8p): Extend a 3rd-party complex view or make your own. Document what parts of
  the code you have written yourself if the view is downloaded from somewhere.
* [x] **REPORT (35p)**: (More info in the exam text)
* [x] (3p) How well you followed the rules / guidelines / suggestions / how attentive you were when reading this task description


### Additional Tasks

* [x] (3p) Use third-party libraries
* [x] (3p) Extra effort in processing images will be rewarded
* [x] (3p) Comprehensive design will be rewarded
* [ ] (3p) Use of Android OS services will be rewarded (e.g. battery level, microphone, GPS, etc.)
* [x] (3p) Compress images to save memory
* [ ] (3p) Use bound service for downloading the data periodically
* [x] (6p) [Bottom_Border] Make your own drawables (Hand/Adobe painted or Drawable-programmed in Android
  Studio) 
