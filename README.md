Project Nsme: TravelMate

Description: TravelMate is an app that helps users to find people traveling the same city or
country and get connected with each other. This app will utilize map and firebase to accomplish
its purpose. This app will prompt users to pinpoint the place they are traveling in the google
map and this will show the list of users who are or will be traveling that place. Additionally, if the
user wants to meet people from the same country or people who speak the same language, the
search result will show the result accordingly. The app will allow user to contact users shown in
the result page like a messenger.

Sample Login Accounts:

- Witch@gmail.com

- Ironman@gmail.com

- Hulk2@gmail.com

- Doctor2@gmail.com

- pw: 123123

Current Location: St. Louis (38.6531, -90.2953)

Progress:

Application is working as intended.  

- users can register, login

- in the menu activity, users can decide to either choose their place of interest using the Google Maps or check their current messages

- in the map activity, users can choose attractions or restaurants within 40km (avg width/length of a city) of their current location 

- markers appear when users click one of the two option buttons

- those nearby place options are shown as markers and markers can be pressed to start an activity showing the place's detail (pinpoint method we've talked about in the presentation)

- in the location detail activity, users can choose to find other users intending to go to the place by pressing a button

- pressing the button automatically sets users' destination to the location

- pressing the button also pulls up a list of users (pictures)

- users can then click any of those users and send messages

- once a message is sent/received, the conversation log appears in the current message activity

- users can access current messages anytime

- however, once users are no longer in the same place, they are prevented from exchange messages (they can still view their old messages)


Future Plan (Features that were not in our plan/presentation but would be good if featured):

- We thought about implementing search for the map activity in addition to the pinpoint method we decided to use.

- but filtering search to show only if the place is within the certain distance from the current location was difficult than it seemed.

- If I get a chance to look into Google Maps API again, I'll try to implement search as well.

- Show a list of places nearby. 

- Currently, users can choose places from markers on the map and showing the list of places as well will be more user friendly. 

- In this project, we still decided to implement the version with markers because our intention was to explore Google Maps Api.

- Using camera to select profile pic

- Another feature I would like to add to our app is notifying users if a user they've met or exchanged messages is in the same locatin as users once again.


Work Division:

Won Young Kang (444516):

 - Messaging portion
 
 - Map Get Nearby places
 
 - Nearby places detail
 
 - Firebase portion
 
 - testing
 
 
Zahra Lambe (443019):

 - Map Current Location
 
 - Place marker at my current location
 
 - testing

Dependencies:

- used com.hbb20.CountryCodePicker in the xml for choosing a country

- used com.squareup.retrofit2 for grabbing json data of google place api




