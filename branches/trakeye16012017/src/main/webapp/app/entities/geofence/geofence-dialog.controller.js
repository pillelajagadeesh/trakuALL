(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('GeofenceDialogController', GeofenceDialogController);

    GeofenceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$state', 'entity', 'Geofence', 'User'];

    function GeofenceDialogController ($timeout, $scope, $stateParams, $state, entity, Geofence, User) {
        var vm = this;
        var newFence=false;
        vm.geofence = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.createNewFence = function(){
        	 newFence=true;
        	 vm.createfence();
        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            //$uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.geofence.id !== null) {
                Geofence.update(vm.geofence, onSaveSuccess, onSaveError);
            } else {
                Geofence.save(vm.geofence, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:geofenceUpdate', result);
           // $uibModalInstance.close(result);
            vm.isSaving = false;
			$state.go('geofence');
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.modifiedDate = false;
        

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        
        
      
        

          vm.createfence =function(){
        	  
        	  
        	
        	        var map = new google.maps.Map(document.getElementById('map_canvas'), {
        	          center:  {lat:12.9716, lng:77.5946},
        	          zoom: 8
        	        });

        	        var drawingManager = new google.maps.drawing.DrawingManager({
        	          drawingMode: google.maps.drawing.OverlayType.POLYGON,
        	          drawingControl: true,
        	          drawingControlOptions: {
        	            position: google.maps.ControlPosition.TOP_CENTER,
        	            drawingModes: ['polygon']
        	          },
        	          polygonOptions: {
        	            fillColor: '#ffff00',
        	            fillOpacity: 1,
        	            strokeWeight: 5,
        	            clickable: false,
        	            editable: true,
        	            zIndex: 1,
        	            geodesic:true
        	          }
        	        })
        	        google.maps.event.addListener(drawingManager, 'polygoncomplete', function(event) {
        	        	google.maps.event.addListener(event.getPath(), 'insert_at', function () {
        	        		setGoefenceCoordinate(event);
        	            });
        	        	google.maps.event.addListener(event.getPath(), 'set_at', function (path) {
        	        		setGoefenceCoordinate(event);
        	            });
        	        	
        	        	drawingManager.setOptions({
        	        	      drawingControl: false
        	        	    });
        	        	  drawingManager.setMap(null);
        	        	  setGoefenceCoordinate(event);
        	        	   
        	        	});
        	        drawingManager.setMap(map);
        	        if(entity.id && entity.id!=null && !newFence){
        	        	
        	        	drawPolygon(function(polygon){
        	        		 polygon.setMap(map);
             	            google.maps.event.addListener(polygon.getPath(), 'insert_at', function () {
             	        		setGoefenceCoordinate(polygon);
             	            });
             	        	google.maps.event.addListener(polygon.getPath(), 'set_at', function (path) {
             	        		setGoefenceCoordinate(polygon);
             	            });
             	        	
             	        	drawingManager.setOptions({
             	        	      drawingControl: false
             	        	    });
             	        	  drawingManager.setMap(null);
        	        	})
        	           
        	        }
        	        
        	      function setGoefenceCoordinate(event){
        	    	  var coOrdinates=[];
        	    	  var radius = event.getPath();
        	    	  for (var i =0; i < radius.getLength(); i++) {
        	    		  coOrdinates.push({ lat:radius.getAt(i).lat(),lng:radius.getAt(i).lng()});
        	    		  if(i==radius.getLength()-1){
        	    			  $scope.$apply(function(){
        	    				  vm.geofence.coordinates=JSON.stringify(coOrdinates);
        	    			  })
        	    		  }	 
        	  	        	
  	        	     }
        	    	  
  	        	  
        	      }
        	      function drawPolygon(callback){
        	    	  callback(new google.maps.Polygon({
        	                path:JSON.parse(entity.coordinates),

        	                strokeColor: "#ff0000",
        	                strokeOpacity: 0.8,
        	                strokeWeight: 2,
        	                fillColor: "#ff0000",
        	                fillOpacity: 0.3,
        	                editable: true
        	            }));
        	      }
        	
         
        };
        
    }
    
    
})();

