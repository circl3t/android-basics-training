# Globule App

## Summary
With Globule you can:
* Browse through "all" the countries and select a country
* Get a representation of the country territory on Google maps
* Get markers of the country attractions
* Get population density, weather average, etc some facts about the country as well as the latest article (fun fact) about the country or people from the country
* Famous people from the country
* Challenge your knowledge about countries
* Get an insight of how "native" you are to which countries
* Add different markers on the Map (has visited, want to visit) on the attractions and circle the icon green for visited and red for targeted etc.
* Add images or short texts to a location or a country
* Create and Edit Your profile (country tags on your profile for example Color.Green.copy(alpha = 0.8) if you are "80%" native to Switzerland etc)
* User location can be accessed to display the country they are in, in order to show that country when user uses first time

## Architecture 

## DataBase
* Will need lots of data and raw jsons will be slow? firestore can it be faster? QQQ
* Need to cache stuff? QQQ
* local storage? QQQ
* difference between res folder in the project? QQQ

## Data 
* Need country data type (has polygons, attractions, latest news, answers for questions, quiz, centerlocation, zoomtofitnicely, useraddedImages, userAddedTexts, famouspeople)
* Need attraction data type (location, picture, description, beenvisited or targeted or neither, userAddedImages, userAddedTexts)
* Need user data type (loggedIn, native percentage, location, visitedlist, wishlist, profilepic , friends, badges etc)
* Need quiz data type (list of questions with corresponding answers)
* SQL for this? QQQ
* Relational Database? QQQ
* What if FireStore? QQQ
* What if some data is static? constants? then it needs to be in the app resource right? or locally storing ? which one get the data the fastest? QQQ

## Screens and Navigation
* Need a top level destination with google maps and a country picker top bar probably MainScreen
* right bottom navigation icon navigates to profile screen (or maybe no bottom navigation bar will be nicer and you navigate to stuff by clicking on the map or something)

## UI
* includes animation
* loading indicators
* 

## Performance
* use coroutines to avoid blocking the main
* long running operations in the background

## Best Practices
* DI with hilt
* Espresso for Testing
* 
