FunMapSF is an event browsing app that takes the curated events from sf.funcheap.com and displays them on a map, allowing users to easily find things to do in the bay area.
User Stories link : https://trello.com/b/0J15yyNi/funmapsf
## Required stories:

 * Users can view events from sf.funcheap.com on a Google Map view.
   - Events are shown as icons based on the event type
   * [x] Clustered events are consolidated into a numbered bubble.
 * [x] Users can select a "List View" to view a list of all events currently shown on the map.
   * [x] List view contains a background image and information such as title, location, price, and date/time, and a "bookmark" button.
 * [x] Users can click a cluster bubble and see a list of events from that cluster.
   * [x] Users can click an event icon or list item to view details in an "Event Detail" 
   screen
   * [x] Description, location, start/end dates and times, location, headline photo
   * [x] Bookmark button
   * [x] Share event button
   * [x] Add to calendar button
   * [x] Map view of the location along with a "Directions" button.
   * [x] Click a link to view an event webpage
 * Users can search and apply filters to the map
   * [x] Events matching a search parameter. Should be matched against event 
    * [x] Date Range
      - Today (Default)
      - This week
      - This weekend
      - Next two weeks
      - Next 30 days
    * [x] Price
      - Any (Default)
      - Free
    * [x] Categories
      - None (Default)
      - Any other categories Ex: Top Pick, Annual, Pets, etc...
 * [x] Users can save filters and view them in a "Saved filters" section
   - Users can elect to be notified when events are added to each filter.
 * [x] Users can view bookmark events on a list
 * [x] Events are stored and filtered using a local database periodically updated using Firebase, since no API exists for sf.funcheap.com.
   - When new events are added, check if it matches user subscriptions. If it does, 
     push a notification to that user. Clicking a notification will bring users to the 
     event detail page.

## Optional

 * Users can select a venue or general location and be shown a list of events 
   happening in that area, chronologically (i.e. a Calendar of events for that 
   area/venue)
 * Users can set an option to be notified of events "Near Me". This feature would be implemented using a location service that periodically checks for events happening that day within a specified range of the users current location.
   - Users should be able to set filter settings and distance thresholds for this notification using the same criteria as search filters.

