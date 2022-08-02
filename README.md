Bottle Rocket Android Maps Guidelines
=====================================

## Steps on obtaining an API key to run the Android Maps Demo.
1. Get the API Key at https://mapsplatform.google.com and click on get started. (You might need to setup billing).
2. In the console click on the left hamburger menu on the top-most left hand side of the site and choose `API & Services` > `credentials`.
3. Click on Create Credentials found on the top-middle of the website and choose API Key, copy this API key which you will be using for this demo.
4. On the dialog box click on edit API key. You can choose `None` for the application restrictions if you're only testing out the demo. Otherwise choose Android apps and follow the directions.
5. Choose library from the side menu and click on Maps SDK for Android. On the next screen, choose Enable.
   1. If you need the Places API you will also need to locate it from the list of library options and enable this as well.
6. Back in the project locate the `secrets` block from project level build.gradle and follow the directions in adding your key to the app.
   1. Note: Here is the documentation on what the Secrets Gradle plugin is for https://developers.google.com/maps/documentation/android-sdk/secrets-gradle-plugin

## Change from AllProjects to DependencyResolutionManagement
From Gradle version 6.8 and up, `allProjects` block is not required to list repositories. Instead list them in the project-level `settings.gradle.kts`. If you attempt to add an allProjects block in your gradle file, Gradle will throw an error if dependencyResolutionManagement is present
in the project-level `settings.gradle.kts`. 

