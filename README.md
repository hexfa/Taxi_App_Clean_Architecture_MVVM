# Clean_Architecture_MVVM
Clean Code Map & List by Clean Architecture & MVVM with Test code
<br>
<h3>An application which has 3 major Duty:</h3>
1.Receive the data from Remote(End Point) which it is some useful vehicle- information provided by Json
<br>2.Showing the list of our vehicles which is done in this project in
“MainActivity.kt” using RecyclerView
<br>3.Showing all vehicle on the map using marker

<br><h3>Additional capabilities which this project has:</h3>
1.Bottom sheet to ask user whether he/she wants to send request to that vehicle or not(fake functionality by showing Toast).
<br>2.Swipe Refresh Layout in “MainActivity.kt” which with pulling down the page we can manual refresh the data from Remote(End Point).
<br>3.Click Events on markers in “MapActivity.kt” which will set center of camera on marker and zoom in on that specific area and opens the bottom sheet which is described on the first item in this list.
<br>4.Click Events on Request buttons in “MapActivity.kt” that will show a
“Snackbar” which shows Request for that specific car has been sent.
<br>5.Click on item in the list of vehicles that will bring the user to “MapActivity.kt” and find and zoom on specific which user has been clicked on and opens the bottom sheet which is described on the first item in this list.

<br><h3>Technologies:</h3>
1.Clean Architecture
<br>2.MVVM
<br>3.View Binding
<br>4.Google Map
<br>5.Retrofit
<br>6.Coroutines
<br>7.Dagger Hilt
<br>8.Swipe Refresh Layout
<br>9.Different Screen Size
<br>10.Unit Test

<br><h3>Some noticeable points:</h3>
1.Using “DiffUtil” technology for keep our list updated : “DiffUtil” is the best technology to updating list that does not need to reload the data
<br>2.Testing the “UseCase” with Simulating data transfer between “Repository” and “UseCase” which is provided by Creating Repository Interface that contains RepositoryImpl ‘s behavior and Creating fake repository in test which inherit from Repository Interface and creating fake data to send to “UseCaseTest” Class and we can use the fake repository which is simulating the behavior of real repository to Create ”UseCase” Object to test it.
<br>3.Using lambda expression for “onClick” events of items in list of vehicles