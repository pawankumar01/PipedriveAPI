app.factory('utilService', function($cookies, $window, $mdDialog, $mdToast, $http) {
	var utilService = {};
	utilService.org = null;

	utilService.orgFields = [];

	//console.log("Simple Utility Service/Factory");
    getAllOrganizationFields();

	function getAllOrganizationFields(){
        var fullurl = "/api/v1/pd/organizationFields/";

        var options={
            "url": fullurl,
            "method": "GET",
            "Content-Type": "application/json",
        };

        $http(options)
        .then(function(response){
           // console.log("orgFields is", response);
            for(var i=0; i<response.data.data.length; i++){
                utilService.orgFields[response.data.data[i].key] = response.data.data[i];
            }

            },
        function(err){
            // console.log(err);
            if(err.status == 429){
                setTimeout(function(){
                    alert(err.data.desc);
                }, 3000);
            }
        });
    }

    utilService.getCurrentLocation = function(successCallback, errorCallback){
	   // console.log("navigator.geolocation", navigator.geolocation);
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var geolocation = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude
                };

                utilService.latitude = geolocation.lat;
                utilService.longitude = geolocation.lng;

               // console.log("lat", geolocation.lat);
              //  console.log("lng", geolocation.lng);

                successCallback(utilService.latitude, utilService.longitude);

            }, function (error){
                errorCallback(error);
            });
        }
    }

	utilService.setOrg = function(org){
        utilService.org = org;
    }

    utilService.getOrg = function () {
        return utilService.org;
    }

    utilService.showAlert = function(title, message){
        alert = $mdDialog.alert({
            title: title,
            textContent: message,
            ok: 'Close'
        });

        $mdDialog
            .show( alert )
            .finally(function() {
                alert = undefined;
            });
    }

    utilService.showConfirm = function(ev, successCallback, errorCallback) {
        var confirm = $mdDialog.confirm()
            .title('Delete Organization')
            .textContent('Are you sure you want to delete this Org?')
            .targetEvent(ev)
            .ok('Yes')
            .cancel('Cancel');

        $mdDialog.show(confirm).then(function() {
            successCallback();
        }, function() {
            errorCallback();
        });
    };

    utilService.showToast = function(message) {

        $mdToast.show(
            $mdToast.simple()
                .textContent(message)
                .position('right')
                .hideDelay(3000)
        );
    };



    return utilService;
});
