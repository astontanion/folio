# How to build the app

To avoid possible abuse of the Flickr Api Key, I have decided not to store it in version control. Instead, the api key must be added in the property `FLICKR_API_KEY="your_key"` and placed inside `app/.gradle.properties`.

To find a public api key, click [here](https://www.flickr.com/services/api/explore/flickr.photos.getRecent), then under output, select `do not sign call` and click `call method`. This should generate a url at the bottom of the page containing the api_key query param. Copy its value in the aforementioned property.

# Architecture

For this task we will use the MVVM pattern along with clean architecture. This will allow for a better separation of concern among components. The model will be standard Kotlin data classes and we will make use of Jetpack compose to construct our views. Furthermore, we will use viewmodels as the single source of truth for our UI state cross recomposition and configuration changes. The viewmodel will concern itself with updating the state and it will delegate business logic to use cases. Thus for unit tests, we will be primarily focussed on testing use cases and viewmodels.

# Main Library

1. Jetpack Compose: for view building
2. Retrofit: for api calls
3. Hilt: for dependency injection

# Additional Features

## Splash screen

Upon loading the app, you will be greeted by a splash screen.

## Observing network connection

If the network fails, we will display a message informing the user of this. Please test this by toggling airplane mode.

## Username and tags suggestion

When searching for tags or username, you will see the suggestion of tags/usernames derived from previous encountered tags/usernames. I have added this feature because it was not intuitive for the users to know what the existing tags and usernames are. Thus, this helps the users discover existing tags/usernames.

## Interactive tags
Clicking on a tag will display/search all images associated to the tag.


# Testing
As mentioned above, only viewmodels and use cases have been unit tested and this should cover the testing of repository as well. Due to lack of time, I was not able to test all composables but I have tested most of them.
