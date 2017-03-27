(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('AssetDialogController', AssetDialogController)
   
        .directive("fileread", [function () {
        	'use strict';

            return {
                restrict: "A",

                link: function($scope, element){

                    element.change(function(event){
                        $scope.$apply(function(){
                          $scope.myFile =element[0].files[0];
                          $scope.asset = $scope.assetname;
                          
                        });
                    });
                }

            };
}]);

    AssetDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'Asset', 'User', 'AssetType', '$state', 'AssetUpload'];

    function AssetDialogController ($timeout, $scope, $stateParams, entity, Asset, User, AssetType, $state, AssetUpload) {
        var vm = this;

        vm.asset = entity;
        var entity_old = angular.copy( vm.asset);
        vm.clear = clear;
        vm.save = save;
        vm.assetType =entity.assetType;
        
        vm.upload= upload;
        
       
        vm.assettypes = AssetType.query();
        //vm.assettypeattributevalues = AssetTypeAttributeValue.query();
        //vm.assetcoordinates = AssetCoordinate.query();
        vm.selectattributes = selectedAssetTypeAttributes;
        vm.coOrdinates=[];
        vm.coOrdinate=null;
        var newFence=false;
        var latlng;
        var locations={};
        var centerlatlng=null;
       
        var formData;
        function upload(){
        	formData=new FormData();
        	
        	formData.append("assetname", $scope.assetname);
            formData.append('file',$scope.myFile);
        
        	AssetUpload.assetupload(formData,function(response,headers){
        		if(response.status == 200){
             		vm.error=null;
             		vm.success='OK';
             		$scope.spreadassetname='';
             		
            		$timeout(function(){
            			vm.success= '';
            		},4000);
            		
            		
             	} 
             	if(response.status == 400){
             		vm.success=null;
             		vm.error='error';
             			
             	} 
             });
        	
        }
        
        
      //  vm.spread=spread;
      //  vm.fixed=fixed;
	  /* import xlsx file start */
	   //Added for upload styles
        $(":file").filestyle({buttonBefore: true});      
        //$(":file").filestyle('placeholder', 'Choose File');
        $(":file").filestyle('buttonText', 'Browse File');
		
		$scope.importxlsx =/^(.*?)\.(xlsx)$/;
		
	  /*import end */
        vm.createNewFence = function(){
       	 newFence=true;
       	 vm.createfence();
       }
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.asset.id !== null) {
                Asset.update(vm.asset, onSaveSuccess, onSaveError);
            } else {
            	Asset.save(vm.asset, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trakeyeApp:assetUpdate', result);
            vm.isSaving = false;
            $state.go('asset');
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        
        function selectedAssetTypeAttributes(){
        	if(vm.assetType){
        		vm.asset.assetType=vm.assetType;
        		if(entity_old.assetType && vm.assetType.id===entity_old.assetType.id){
        			vm.asset.assetTypeAttributeValues = [];
        			vm.asset.assetTypeAttributeValues = entity_old.assetTypeAttributeValues;
        		}else{
        			vm.asset.assetTypeAttributeValues = [];
        			$.each(vm.assetType.assetTypeAttributes,function(key,value){
        				vm.asset.assetTypeAttributeValues.push({assetTypeAttribute:vm.assetType.assetTypeAttributes[key]});
            		});
        		}
        	}
        	
        }
        
        
        vm.createfence= function (){
        	var map = new google.maps.Map(document.getElementById('map_canvas'), {
	          center:  {lat:12.9716, lng:77.5946},
	          zoom: 13
	        });

	        if(vm.assetType.layout== 'SPREAD'){
	        	drawingManagerSpread();
	            drawingManagerValue('polylinecomplete',map);
	        }else if(vm.assetType.layout== 'FIXED'){
	        	drawingManagerFixed();
	        	drawingManagerValue('click',map);
	        }
	        	
	        if(entity.id && entity.id!=null && !newFence){
	        	if(vm.assetType.layout == 'SPREAD'){
	        	
	        	drawPolyline(function(polyline){
	        		polyline.setMap(map);
     	            google.maps.event.addListener(polyline.getPath(), 'insert_at', function () {
     	        		setGoefenceCoordinate(polyline);
     	            });
     	        	google.maps.event.addListener(polyline.getPath(), 'set_at', function (path) {
     	        		setGoefenceCoordinate(polyline);
     	            });
     	        	
     	        	vm.drawingManager.setOptions({
     	        	      drawingControl: false
     	        	    });
     	        	vm.drawingManager.setMap(null);
     	        	
	        	})
	        	}else if(vm.assetType.layout == 'FIXED'){
	        		var latitudelongitude= {latitude:vm.asset.assetCoordinates.latitude, longitude:vm.asset.assetCoordinates.longitude};
	        		
	        		setValues();
	        		drawingManagerFixed();
	        		drawMarker(map,vm.coOrdinate);
	        		//drawFixedAssetImages(vm.coOrdinate);
	        	}
	           
	        }
	        
	     function drawPolyline(callback){
	    	 
	    	  setValues();
	    	  JSON.parse(vm.coOrdinate).forEach(function(value,key){
	    		centerlatlng={lat:value.lat, lng:value.lng};
	    	 });
	    	 callback(new google.maps.Polyline({
	              path: JSON.parse(vm.coOrdinate),
	              geodesic: true,
	              strokeColor:  vm.asset.assetType.colorcode,
	              strokeOpacity:  0.8,
	              strokeWeight: 2,
	              fillColor: vm.asset.assetType.colorcode,
	              fillOpacity: 0.3,
	              editable: true
	            }));
	    	  map.setCenter(centerlatlng);
	      }
	      
      };
      
     
    function drawingManagerSpread(){
    	vm.drawingManager=new google.maps.drawing.DrawingManager({
            drawingMode: google.maps.drawing.OverlayType.POLYLINE,
            drawingControl: true,
            drawingControlOptions: {
              position: google.maps.ControlPosition.TOP_CENTER,
              drawingModes: ['polyline']
            },
            polylineOptions: {
              fillColor: '#ffff00',
              fillOpacity: 1,
              strokeWeight: 5,
              clickable: false,
              editable: true,
              zIndex: 1,
              geodesic:true
            }
          });
    }
    
    
    
    function drawingManagerFixed(){
    	vm.drawingManager=new google.maps.drawing.DrawingManager({
            drawingMode: google.maps.drawing.OverlayType.MARKER,
            drawingControl: true,
            drawingControlOptions: {
              position: google.maps.ControlPosition.TOP_CENTER,
              drawingModes: ['marker']
            },
            markerOptions: {icon: {
	              size: new google.maps.Size(220, 220),
		              scaledSize: new google.maps.Size(32, 32),
		              origin: new google.maps.Point(0, 0),
		              url: "data:image/png;base64,"+vm.asset.assetType.image,
		              anchor: new google.maps.Point(16, 16)
		            },}
            
          });
    }
    
    function drawingManagerValue(option,map){
    	
    	if(option == 'polylinecomplete'){
    	
    	google.maps.event.addListener(vm.drawingManager, option, function(event) {
        	google.maps.event.addListener(event.getPath(), 'insert_at', function () {
        		setGoefenceCoordinate(event);
            });
        	google.maps.event.addListener(event.getPath(), 'set_at', function (path) {
        		setGoefenceCoordinate(event);
            });
        	vm.drawingManager.setOptions({
        	      drawingControl: false
        	    });
        	vm.drawingManager.setMap(null);
        	  setGoefenceCoordinate(event);
        	});
    	vm.drawingManager.setMap(map);
    }else if(option == 'click'){
    	google.maps.event.addListener(map, 'click', function (e) {
    		placeMarker(e.latLng,map);
    		 vm.fixedArray=[];
    		 var coordinates={};
   		     coordinates.latitude=e.latLng.lat();
             coordinates.longitude=e.latLng.lng();
    		 vm.fixedArray.push(coordinates);
    		 vm.asset.assetCoordinates = vm.fixedArray;
    		 });
    }
    }
    var marker;
    function placeMarker(location,map) {
    	
      if ( marker ) {
        marker.setPosition(location);
      } else {
        marker = new google.maps.Marker({
          position: location,
          map: map,
          icon: {
              size: new google.maps.Size(220, 220),
              scaledSize: new google.maps.Size(32, 32),
              origin: new google.maps.Point(0, 0),
              url: "data:image/png;base64,"+vm.asset.assetType.image,
              anchor: new google.maps.Point(16, 16)
            },
        });
      }
    }
    $scope.mapEvents = {"click":function(event){
        
        }
    }
    
    function setGoefenceCoordinate(event){
  	  vm.assetCoordinateValues=[];
  	  var radius = event.getPath();
  	  for (var i =0; i < radius.getLength(); i++) {
  		    var coordinates={};
  		    coordinates.latitude=radius.getAt(i).lat();
            coordinates.longitude=radius.getAt(i).lng();
            
            vm.assetCoordinateValues.push(coordinates);
  		  if(i==radius.getLength()-1){
  			  $scope.$apply(function(){
  				vm.assetCoordinateValues.push(coordinates);
  			  })
  		  }	 
	        	
	     }
  	     if(vm.asset.assetCoordinates!=null && vm.asset.assetCoordinates.length!=0){
  	    	vm.assetCoordinateNewValues=[];
  	    	for (var i =0; i < vm.asset.assetCoordinates.length; i++) {
  	    		if(vm.asset.assetCoordinates[i].latitude!=vm.assetCoordinateValues[i].latitude 
  	    				&& vm.asset.assetCoordinates[i].longitude!=vm.assetCoordinateValues[i].longitude){
  	    			var coordinates={};
  	    		    coordinates.latitude=vm.assetCoordinateValues[i].latitude;
  	                coordinates.longitude=vm.assetCoordinateValues[i].longitude;
  	                vm.assetCoordinateNewValues.push(coordinates);
  	    		}
  	    	}
  	    	vm.asset.assetCoordinates=vm.assetCoordinateNewValues;
  	     }else{
  	    	 vm.asset.assetCoordinates=vm.assetCoordinateValues;
  	     }
  	  
    }
    
    function setValues(){
  	  vm.coOrdinates=[];
  	  for (var i =0; i < vm.asset.assetCoordinates.length; i++) {
  		  vm.coOrdinates.push({ lat:vm.asset.assetCoordinates[i].latitude,lng:vm.asset.assetCoordinates[i].longitude});
  		  if(i==vm.asset.assetCoordinates.length-1){
  		    vm.coOrdinate=JSON.stringify(vm.coOrdinates);
  		  }	 
  	  }
     }
    
      function drawMarker(map,response){
    	  $.each(JSON.parse(response), function( index, value ) {
    		 centerlatlng={lat:value.lat, lng:value.lng};
    	  
			var startMarker =new google.maps.Marker({
	            map: map,
	            position: new google.maps.LatLng(value.lat, value.lng),
	            
	            icon: {
	              size: new google.maps.Size(220, 220),
	              scaledSize: new google.maps.Size(32, 32),
	              origin: new google.maps.Point(0, 0),
	              url: "data:image/png;base64,"+vm.asset.assetType.image,
	              anchor: new google.maps.Point(16, 16)
	            },
	        });
    	  });
    	  map.setCenter(centerlatlng);
     }
    }
})();
