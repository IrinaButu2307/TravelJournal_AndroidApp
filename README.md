# TravelJournal_AndroidApp
Main purpose - keep track of the user's trips ~ editable list of trips

Key Activities
<img src = "https://user-images.githubusercontent.com/32790344/53452246-6f7c5480-3a29-11e9-8137-a08dcad9d7ac.jpg" width="250" height="450">
<img src = "https://user-images.githubusercontent.com/32790344/53452250-72774500-3a29-11e9-876f-5385a2b54047.jpg" width="250" height="450">
<img src = "https://user-images.githubusercontent.com/32790344/53452263-75723580-3a29-11e9-9ed7-2ed9f658bfe1.jpg" width="250" height="450">

The app opens with a login screen directed by Firebase Google Authentication. 
The main screen is a Navigation Drawer and its main fragment is a Trip List (RecyclerView) which is displayed when the Home item is clicked. 
The user has the option to: 
1. add a new trip by clicking the FAB (give permission to camera/gallery and choose a date-fragment Date Picker); 
2. see the full description of a trip by long pressing on it; 
3. mark as favourite one trip; its info is saved in Firebase and added in the list contained in the Favourite menu option of the Navigation Drawer. 
All the data from the app is saved in Firebase Cloud Firestore, and in Room.

