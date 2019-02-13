## PipeDriveAPI
PDAPI is simple java wrapper over the Pipedrive Rest Services. 
It allows users to view, edit and delete Organizations in the Pipedrive.

### Responsive Design
The app is responsive and works well with desktop Web and Mobile devices.

### Directory Structure

```
src |
    |-java (backend)
    |        |-PipedriveResource (App Server Rest API)
    |        |-PipedriveAPI (Communicates with the Pipedrive Server)
    |-webapp (frontend)
    |        |- controller.js ( controller logic)
    |        |- utilService.js (utility js)   
    |-resources (contains pipedrive api key)
```
Make sure to replace api key in ```resources/pipedrive.properties``` before deploying.

### Technology Used
Backend uses ```Java, Jersey(Rest API), OkHTTP ```

Frontend uses ```HTML5, CSS, Javascript, Jquery, AngularJS```

Build Tools ```Gradle, Gretty(Tomcat)```


### Run
```bash
$ ./gradlew appRun
```

### Functionality
The app implements following feature:-
* Backend
    - View All Organizations
    - View Single Organization
    - Create a new Organization
    - Edit Organization
    - Delete Organization
    - Fetch nearest Organization to the user
    
* Frontend
    - Table View for list of Organization
    - Click to edit or delete Organization
    - Create new Organization
    - Nearest Organisation
    
### Nearest Organization
Nearest Organization automatically fetches the current location from browser. Kindly allow location popup.
It automatically shows the orgs near to current location.

Also, user can manually enter latitude and longitude and fetch org near to entered lat/lng.


### Hosted
https://pipedriveapi.herokuapp.com/

### Improvement & Things to do
Support for more objects