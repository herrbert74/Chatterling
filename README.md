# Chatterling

Android Test challenge creating a chat interface. 

[Challenge description](CHALLENGE.md)

[Challenge feedback](FEEDBACK.md)

# Remarks from Zsolt Bertalan

## Tech

* I used commonly used libraries Coroutines, Dagger 2, AndroidX, and Compose.
* I also used less commonly used libraries, like MVIKotlin, Decompose and Essenty. See details in the structure section.
* I used my base library. This contains code that I could not find in other third party libraries, and what I use in 
  different projects regularly:
  * https://bitbucket.org/babestudios/babestudiosbase

## Structure

* I use a monorepo for such a tiny project, however I used a few techniques to show how I can build an app that 
  scales, even if they are an overkill as they are now.
* The three main sections (module groups in a larger project) are **data**, **domain**, and **ui**.
* **Domain** does not depend on anything and contains the api interfaces, and the model classes.
* **Data** implements the domain interfaces (repos) through the network, local and db packages or modules, and does not 
  depend on anything else, apart from platform and third party libraries.
* **Ui** uses the data implementations through dependency injection and the domain entities. **Root** package 
  provides the root implementations for Decompose Business Logic Components (BLoCs) and navigation.
* **Di** Dependency Injection through Dagger and Hilt

## Libraries used

* **MVIKotlin**, **Decompose** and **Essenty** are libraries from the same developer, who I know personally. Links to 
  the libraries:
    * https://github.com/arkivanov/MVIKotlin
      * An MVI library used on the screen or component level.
    * https://github.com/arkivanov/Decompose
      * A component based library built for Compose with Kotlin Multiplatform in mind, and provides the glue, what 
        normally the ViewModel and Navigation library does, but better. 
    * https://github.com/arkivanov/Essenty
      * Has some lifecycle and ViewModel wrappers and replacements.

## Justification for above libraries

* I do not think that starting an app with a scalable architecture is over engineering, but some people do think so. 
  I respectably disagree.
* I've used MVIKotlin professionally for years.
* I adopted MVIKotlin, then Decompose for private projects too, so I'm most proficient with these. I could have used 
  the standard MVVM and Jetpack Navigation, but it would take more time for me at this point.
* These libraries address issues, that the Google libraries failed to address. So no ViewModels, for example.
* Risks commonly associated with above libraries
  * Abandoned by author: No real risk, as I can copy to my project, others can pick up, or I can switch to similar.
  * Onboarding: For most people it will be new. That's a real problem, but the libraries are getting popular.
  * More boilerplate initially: they need some initial setup, but they scale well for more screens.

## Assumptions

* The message tails and timestamps are unstable variables, so I decided to recreate them in the presentation layer. 
  The alternative would have been to save them into the database and change them as needed. This might have been a 
  bad decision, but I went with it to come up with a Minimum Viable Product as quickly as possible.
* I left out the double confirmation ticks, as they meant that my message was read, but because there is no other 
  side of the conversation, they have no meaning yet. So they will need to be added when networking and real 
  conversation are introduced.
* Initially I assumed that my messages are on the left, because for me that feels more natural, and I use chat apps 
  very rarely. As a result it took me some additional time to adjust that.
* Being a static screen, I hardcoded the user name and the avatar.
* I added an up button despite there is nothing to go 'up' to. I assumed that there will be a sign in screen or 
  onboarding added later. For now it works as a finish button.
* I also added the More button on the top right. I assumed there will be functionality added later.
* I assumed the header design is for iOS originally, so I adapted it to Android.
* I assumed the send button needs to be disabled when the message TextField is empty.

## Problems dealt with

* UI according to the requirements.
* Received messages are randomly generated after the user sends a message from a predefined set of sentences.
* Messages are saved into the database and displayed again upon reopening the app.
* Business logic works as required, located mainly in the ChatExecutor class.

## Room for improvement

* Take time zones into consideration. Now we simply save and use the UTC timestamp, which coincides with London time in 
  the winter.
* More Unit tests. Only had time for a very basic one.
* UI is not pixel perfect, probably the paddings and other sizes are off, also colours apart from the primary colour.
* Repository is not main-safe (according to Google guidelines) yet. I'm ready for the switch to it, but my work is 
  template driven, so I could not get into this right now due to time constraints. This is not as big of an issue as 
  it sounds anyway, but it's a good recommendation.
* Some Compose elements might be simplified.
* I did not have the time for a nice launcher icon.
* Hard coded resources and constants should be replaced with proper resources and constant variables
* Scroll position and generally any state apart from components is not saved, but it's ready to be saved to 
  StateKeeper in MVIKotlin and Essenty, which is a similar implementation to AndroidX's SaveStateHandle.

## Time spent

* Setting up the project and project files: 1 hour (should be much more, but I have a detailed template)
* Create and adjust colour palette: 0.5 hour
* Create Compose UI elements and their previews: 2 hours
* Adjusting database, model and Dbo classes: 0.5 hours
* Making the basic app working: 1.5 hours
* Adding a test: 0.5 hours
* Bug fixing: 3 hours
* Writing readme: 1 hour
* Altogether: circa 10 hours
