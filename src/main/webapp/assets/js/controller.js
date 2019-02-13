app.controller('pdCtrl', ["$scope", "$http", "utilService", "$cookies", "$window", function($scope, $http, $utilService, $cookies, $window){
   // console.log('In pipedrive controller');

    $scope.query = {
        order: 'name',
        limit: 10,
        page: 1
    };

    /**
     *  Fetch all Users
     */
    fetchAllOrgs();
    function fetchAllOrgs(){
        var fullurl = "/api/v1/pd/organizations";

        var options={
            "url": fullurl,
            "method": "GET",
            "Content-Type": "application/json",
        };
        $http(options)
        .then(function(response){
         // console.log("response is", response);
            if(!response.data.success){
                $utilService.showAlert("Sorry", response.data.error);
                return;
            }

            if(response.data.data){
                $scope.orgs = response.data.data;
                $scope.orgs.count = response.data.data.length;
            }
            $(".tableprogress").hide();
        },
        function(err){
           // console.log(err);
            $(".tableprogress").hide();
            if(err){
                $utilService.showAlert("Error", err.data.desc);
            }
        });
    }

    $scope.edit = function(org){
        $utilService.setOrg(org);
        $window.location.href = "/#!/edit/"+org.id;
    }

}]);


app.controller('editCtrl', ["$scope", "$http", "utilService", "$routeParams", "$window", function($scope, $http, $utilService, $routeParams, $window){
  //  console.log('In edit controller');
    var org = $utilService.getOrg();
  //  console.log("org is ", org);
    $scope.orgFields = $utilService.orgFields;

    $scope.autocomplete = new google.maps.places.Autocomplete((document.getElementById('autocomplete')),
        {types: ['geocode']});

    // When the user selects an address from the dropdown, called an function
    google.maps.event.addListener($scope.autocomplete, 'place_changed', function() {
        $scope.place = $scope.autocomplete.getPlace();
     //   console.log("places selected is" , $scope.place);
        var latitude =  $scope.place.geometry.location.lat().toFixed(6).replace( /[\.]?0+$/, '' );
        var longitude = $scope.place.geometry.location.lng().toFixed(6).replace( /[\.]?0+$/, '' );
        $scope.org.address = latitude + "," + longitude;
        $scope.org.address_geocoded = $scope.place;
    });




    $scope.orgId = $routeParams.orgId;

    //if(org == null){
        fetchOrg();
    // }else{
    //     $scope.org = org;
    // }


    function fetchOrg(){
        var fullurl = "/api/v1/pd/organizations/" + $scope.orgId;

        var options={
            "url": fullurl,
            "method": "GET",
            "Content-Type": "application/json",
        };

        $http(options)
        .then(function(response){
           // console.log("response is", response);

            if(!response.data.success){
                $utilService.showAlert("Sorry", response.data.error);
                return;
            }


            $scope.org = response.data.data;
            if($scope.org.active_flag == false){
                $utilService.showAlert("Sorry", "This Organization is not active");
                $window.location.href = "/#!/";
            }
        },
        function(err){
            // console.log(err);
            if(err){
                $utilService.showAlert("Error", err.data.desc);
            }
        });
    }


    $scope.saveOrg = function(){
        var fullurl = "/api/v1/pd/organizations/" + $scope.orgId;

        var options={
            "url": fullurl,
            "method": "PUT",
            "Content-Type": "application/json",
            "data" : $scope.org
        };

        $http(options)
        .then(function(response){
           // console.log("response is", response);
            if(!response.data.success){
                $utilService.showAlert("Sorry", response.data.error);
                return;
            }

            $utilService.showToast("Org has been saved successfully!");
            $scope.org = response.data.data;
        },
        function(err){
            // console.log(err);
            if(err){
                $utilService.showAlert("Error", err.data.desc);
            }
        });
    }

    $scope.showDeleteConfirmation = function(ev){
        $utilService.showConfirm(ev, function(){
            $scope.deleteOrg()
        }, function(){
            return;
        })

    }

    $scope.deleteOrg = function(){
        var fullurl = "/api/v1/pd/organizations/" + $scope.orgId;

        var options={
            "url": fullurl,
            "method": "DELETE",
            "Content-Type": "application/json",
        };

        $http(options)
        .then(function(response){
            if(!response.data.success){
                $utilService.showAlert("Sorry", response.data.error);
                return;
            }

           // console.log("response is", response);
            $scope.org = response.data.data;
            $utilService.showAlert("Success", "Organization has been deleted");
            $window.location.href = "/#!/";
        },
        function(err){
            // console.log(err);
            if(err){
                $utilService.showAlert("Error", err.data.desc);
            }
        });
    }

}]);



app.controller('createCtrl', ["$scope", "$http", "utilService", "$routeParams", function($scope, $http, $utilService, $routeParams){
   // console.log('In create controller');
    $scope.org = {};
    $scope.orgFields = $utilService.orgFields;

    $scope.autocomplete = new google.maps.places.Autocomplete((document.getElementById('autocomplete')),
        {types: ['geocode']});

    // When the user selects an address from the dropdown, called an function
    google.maps.event.addListener($scope.autocomplete, 'place_changed', function() {
        $scope.place = $scope.autocomplete.getPlace();
     //   console.log("places selected is" , $scope.place);
        var latitude =  $scope.place.geometry.location.lat().toFixed(6).replace( /[\.]?0+$/, '' );
        var longitude = $scope.place.geometry.location.lng().toFixed(6).replace( /[\.]?0+$/, '' );
        $scope.org.address = latitude + "," + longitude;
        $scope.org.address_geocoded = $scope.place;
    });


    $scope.createOrg = function(){
        var fullurl = "/api/v1/pd/organizations/";

        var options={
            "url": fullurl,
            "method": "POST",
            "Content-Type": "application/json",
            "data" : $scope.org
        };

        $http(options)
        .then(function(response){
          //  console.log("response is", response);
            if(!response.data.success){
                $utilService.showAlert("Sorry", response.data.error);
                return;
            }

            $utilService.showAlert("Organization Created", "Org has been create successfully!");
            $scope.org = response.data.data;
        },
        function(err){
            // console.log(err);
            if(err){
                $utilService.showAlert("Error", err.data.desc);
            }
        });
    }



}]);




app.controller('nearestCtrl', ["$scope", "$http", "utilService", "$routeParams", function($scope, $http, $utilService, $routeParams){
   // console.log('In nearest controller');
    $scope.orgs = [];
    $scope.latitude = null;
    $scope.longitude = null;


    $scope.fetchNearestOrg = function(lat, lng){
        $(".tableprogress").show();
        var fullurl = "/api/v1/pd/nearestorg?lat=" + lat + "&lng=" + lng ;

        var options={
            "url": fullurl,
            "method": "GET",
            "Content-Type": "application/json",
        };

        $http(options)
        .then(function(response){
          //  console.log("response is", response);

            if(response.data == null  || response.data.length == 0)
                $utilService.showAlert("No Nearest Organization", "No nearest organization found");

            $scope.orgs = response.data;
            $scope.initMap(lat, lng);
            $(".tableprogress").hide();
        },
        function(err){
            // console.log(err);
            $(".tableprogress").hide();
            if(err){
                $utilService.showAlert("Error", err.data.desc);
            }
        });
    }

    var icon = {
        url: "/assets/images/blue.png", // url
        scaledSize: new google.maps.Size(20, 20), // scaled size
        origin: new google.maps.Point(0,0), // origin
        anchor: new google.maps.Point(0, 0) // anchor
    };


    $scope.initMap=function(lat, lng){
        // $scope.definePopupClass();
        map = new google.maps.Map(document.getElementById('map'), {
            zoom: 3,
            center: new google.maps.LatLng(lat, lng),
        });





        new google.maps.Marker({
            position: new google.maps.LatLng(lat, lng),
            map: map,
            animation: google.maps.Animation.DROP,
            icon: icon,
            visible: true
        });

        //geocoder = new google.maps.Geocoder();


        var markers = [];
        angular.forEach($scope.orgs, function(org){
            var geo = org.address.split(",");
            var value = {
                lat: parseFloat(geo[0]),
                lng: parseFloat(geo[1])
            }
            markers.push(createMarker(value, map, org.name));
        });
       // console.log(markers[0]);



    }


    var createMarker = function(value, map, title) {
      //  console.log("lat is", value.lat, " and lang is ", value.lng)
        if (!value.lat && !value.lng) {
          //  console.log("invalide lat and lang--returning");
            return;
        }

        var marker = new google.maps.Marker({
            position: new google.maps.LatLng(value.lat, value.lng),
            map: map,
            title: title,
            visible: true
        });

        marker.addListener('click', function() {
            infowindow.setContent(title);
            infowindow.open(map, marker);
        });

        return marker;

    }

    var infowindow = new google.maps.InfoWindow();

    $utilService.getCurrentLocation($scope.fetchNearestOrg, function(error){
        if(error.PERMISSION_DENIED) $utilService.showAlert("Location Denied", "Please allow location access from browser");
        $(".tableprogress").hide();
    });

    $scope.fetchclosestOrg = function(){
        if($scope.longitude && $scope.latitude) {
             if($scope.latitude < -90 || $scope.latitude > 90){
                 $utilService.showAlert("Not Valid", "Please enter valid latitude between -90 to 90");
                 return;
             }

             if($scope.longitude  < -180 || $scope.longitude  > 180){
                 $utilService.showAlert("Not Valid", "Please enter valid longitude between -180 to 180");
                 return;
             }


            $scope.fetchNearestOrg($scope.latitude, $scope.longitude);
        }else{
            $utilService.showAlert("Not Valid", "Please enter valid latitude/longitude");
        }
    }

}]);