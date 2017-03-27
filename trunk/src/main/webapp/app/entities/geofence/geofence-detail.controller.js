(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('GeofenceDetailController', GeofenceDetailController);

    GeofenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Geofence', 'User'];

    function GeofenceDetailController($scope, $rootScope, $stateParams, previousState, entity, Geofence, User) {
        var vm = this;
        google.maps.Polygon.prototype.getBounds = function() {
            var bounds = new google.maps.LatLngBounds();
            var paths = this.getPaths();
            var path;        
            for (var i = 0; i < paths.getLength(); i++) {
                path = paths.getAt(i);
                for (var ii = 0; ii < path.getLength(); ii++) {
                    bounds.extend(path.getAt(ii));
                }
            }
            return bounds;
        }
        vm.geofence = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trakeyeApp:geofenceUpdate', function(event, result) {
            vm.geofence = result;
        });
        $scope.$on('$destroy', unsubscribe);
       
        drawmap(JSON.parse(entity.coordinates),function(latlng,geofence){
        	
        	var myOptions = {
                    zoom: 10,
                    center: geofence.getBounds().getCenter(),
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                $scope.map = new google.maps.Map(document.getElementById("map_canvas"), myOptions); 
               
                geofence.setMap($scope.map);
        });
        
        function drawmap(path,callback){
        	try{
        		
        		callback(new google.maps.LatLng(path[0].lat, path[0].lng),new google.maps.Polygon({
                    paths: path,
                    strokeColor: '#FF0000',
                    strokeOpacity: 0.8,
                    strokeWeight: 2,
                    fillColor: '#FF0000',
                    fillOpacity: 0.35
                  }));
        	}catch(err){
        		console.log('error')
        	}
        	 
        }
        
    }
})();
