> PGR208 Android Programming exam
<h1 align="center">
Reversed Image Search 📱🔍✍(◔◡◔) EXAM
</h1>


### Egne notater (slettes før levering):
* [x] Få på plass "Results" (omgjøre navn til "Saved" imo), dette blir trolig en custom
  view der vi viser hva som er lagret i DB, søke-bildet og resultater fra bilde søkt på
* [x] Få navigasjonsbaren til å bli med uansett hvilket fragment/activity man er på
* [x] Implementere SQLite med Room (husk hvorfor dette er så jævlig bra for vi må justifye bruk av Room)
* [x] Få UI logisk, riktig og til å se pen ut, uten tekst oppå hverandre. Kun ha nødvendig tekst som er forståelig for brukeren
* [x] Gi brukeren feedback når alle 3 endepunktene gir [] (f.eks. ansiktsbilde rune har) - RDJ
* [x] Enlarge imageDatabaseModel med AnimatorSet eller AnimationSet med en onclick, også potensielt ha knapp som lagrer bilde etter det. Først på ResultActivity også kanskje implementere det samme på SavedActivity. - RDJ
* [x] Handle exceptions, ikke nødvendigvis exception thrown på tomt array [] - RDJ
* [x] Nytt design - Stian
* [x] Bildene som bruker velger å save til DB, bør også legges til i download folder på telefonen (det står i eksamensteksten)
* [x] Hvis det ikke er en bildefil som velges, bør det times ut eller med Toast til bruker
* [ ] På savedactivity: Få fullscreen der også, med bilde som er søkt på i toppen i fullscreen mode i tillegg til en basket ikon-knapp for sletting av bildet
* [ ] Adde timeout på upload (60sec ish)
* [ ] Evt. andre ting dere kommer på
* [ ] Evt. andre ting dere kommer på
* [ ] Evt. andre ting dere kommer på

### Application Requirements

* [x] At least one list must be handled by a RecyclerView
* [x] Use intents to pass data
* [x] Use non-UI-blocking requests to the server
* [x] User SQLite database to store data
* [x] Create at least one reasonably complex custom view (MainActivity: Argumenter for det i rapport. Vi har ikke tatt hensyn til å lage egne custom view components, da det ikke har vært i pensum(NOTE: notér mer på rapport om dette punket))
* [x] Use Kotlin as a main programming language (some Java is acceptable when re-using
  the code found online)
* [x] Make use of all the end-points on the server
* [x] Exception handling (e.g. no network connection)
* [x] Make sure to handle all the Android lifecycle states (the app will be paused, resumed,
  stopped, etc. during testing) (det gjør vi med LiveData(NOTE: skriver vi om dette i rapporten?))
* [x] Make use of res/drawable, res/layout, and res/values
* [ ] Both in code and in the report students are required to put comments and/or explicitly state which of the sub-requirements from Table 3 they are targeting.

### Sub requirements (disse skal vi henvise til i koden, hvor det er brukt, filnavn og linjenr.)
* [x] **Hard** (8p): [ApiServices @ 221] [FullscreenActivity @ 80, 118] Show that you understand when it is appropriate to use the elvis
  operator by pointing to parts of code where it was used and discussing what edge cases it is meant to handle.
* [x] **Hard** (8p): [MainActivity @ 74, 96] [ResultActivity @ 73] [SavedActivity @ 59] [ApiService @ 47, 88] Make use of lambdas and higher order functions when processing data
  that is similar, but not exactly the same. Discuss the code in the report as well as pros, cons, and alternative code structure.
* [x] **Hard** (4p): [ImageViewModel @ 25, 31] [ApiServices @ 221] Make use of threads or coroutines to make asynchronous operations. Your UI should never freeze until some operation is being executed. Discuss in the report your async code and try to time it by using Android Studio tools or timer in the code for each iteration of your operation. What is the best/worst case scenario of the operation you decided to make asynchronous?
* [x] **Hard** (8p): [Image @ 12] Store and retrieve images as blobs in/from the SQLite database.
  Describe and discuss the database structure in detail. What are the pros / cons / alternatives considering a commercial version of your application.
* [x] **Hard** (3p):  [MainActivity @ 73] Create callbacks across the application to let the parent class / view
  know when something is changing. Write about all your callbacks in the report and why they are useful for the information flow in your app.
* [x] Soft (8p): [Helpers @ 15] Make (or extend existing) classes with methods and companion classes to
  process images. Make use of constructors, overloading, properties, overriding, and interfaces where it is appropriate. Discuss pros, cons, and alternative code structure in the report by referencing your code.
* [x] Soft (8p): Implement your own RecyclerView adapter and discuss the view you used for
  each row in detail in the report.
* [x] Soft (3p): Have more than one Activity / Fragment (up to you which one you use). Explain
  why you decided to use Activities and/or Fragments.
* [x] Hard (4p): Pass the data as Parcelable between Activities / fragments
* [x] Soft (8p): Extend a 3rd-party complex view or make your own. Document what parts of
  the code you have written yourself if the view is downloaded from somewhere.
* [ ] **REPORT (35p)**: (More info in the exam text)
* [x] (3p) How well you followed the rules / guidelines / suggestions / how attentive you were when reading this task description





### Additional Tasks

* [x] (3p) Use third-party libraries
* [x] (3p) Extra effort in processing images will be rewarded
* [x] (3p) Comprehensive design will be rewarded
* [ ] (3p) Use of Android OS services will be rewarded (e.g. battery level, microphone, GPS, etc.)
* [x] (3p) Compress images to save memory
* [ ] (3p) Use bound service for downloading the data periodically
* [x] (6p) Make your own drawables (Hand/Adobe painted or Drawable-programmed in Android
  Studio) 
