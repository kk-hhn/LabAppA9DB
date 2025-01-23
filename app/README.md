# TODO APP
## Installation Guide
0. Clone the repository into your android studio application (using the version control button at the top left of your screen)
1. Locate the .apk file in app/src/main/java | ALTERNATIVELY if you are on mobile, download only the apk from github
2. execute it using an application like jadx | alternatively just click on it on your phone

## Funktionsbeschreibung
The App has a dashboard which is selected upon opening the app, with 2 options as buttons:
- Unfinished Todos
- Finished Todos

In the Unfinished Todos Screen you can edit, add and delete todos with their respective names, priorities, deadlines and statuses (not stati)
In the Finished Todos Screen you can delete and un-finish each todo

This App utilizes a SQLite database generated from DBeaver inside the apk which just provides the table structure at first and later saves the todos as well
It is coded fully in Kotlin, using Jetpack Compose Material3 elements

Known Problems:
duplicate todos can and will be created if generated using code and not the in-app buttons