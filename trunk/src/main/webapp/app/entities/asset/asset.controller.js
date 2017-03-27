(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('AssetController', AssetController);

    AssetController.$inject = ['$scope', '$state', 'Asset', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'AssetSearch', 'AssetSearchForMap'];

    function AssetController ($scope, $state, Asset, ParseLinks, AlertService, pagingParams, paginationConstants, AssetSearch, AssetSearchForMap) {
        var vm = this;
        
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.filterSearch=filterSearch;
        vm.drawassets=drawassets;
        vm.filterAssetsformap = filterAssetsformap;
        vm.assets = [];
        $scope.tab = 1;
        var latlng;
        var locations={};
        var userPaths={};
        
        $scope.setTab = function(newTab){
			  $scope.tab = newTab;
			  if( $scope.tab==2){
				  vm.drawassets();
			  }
			  
			};

			$scope.isSet = function(tabNum){
			  return $scope.tab === tabNum;
			};
			
			function filterAssetsformap(){
	           	if(vm.seachAssetformap!=null && !angular.isUndefined(vm.seachAssetformap)){
	           	AssetSearchForMap.searchassetsformap(vm.seachAssetformap,function(response){
	           		$scope.assetsById=response;
	           		drawLatestAssets($scope.assetsById);
	           		
	           	});
	           	}
	           };
        
        function filterSearch(){
        	if(vm.search!=null && vm.search!="" && !angular.isUndefined(vm.search)){
				    AssetSearch.query({searchvalue:vm.search},{
		                page: pagingParams.page - 1,
		                size: vm.itemsPerPage,
		                sort: sort()
		            }, onSuccess, onError);
					
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.assets = data;
                vm.page = pagingParams.page;
        
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
			}else{
				loadAll ();
			}
		}

        loadAll();

        function loadAll () {
            Asset.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.assets = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
        
        function drawassets(){
        	drawLatestAssets(vm.assets);
        }
function drawLatestAssets(response){
        	
        	if(response){
        		 initializeAssetMap(response,function(latlang){
        			 userPaths.latlng=latlang;
        			 locations.latlng = latlang;
        			 setupAssetMap(function(){
        				 $.each(response, function( index, value ) {
        					 if(value.assetType.layout == 'FIXED'){
        						 drawFixedAsset(value); 
        					 }if(value.assetType.layout == 'SPREAD'){
        					 drawPolyline(value);
        					 }
						});
					}); 
				});
			}
        	function setupAssetMap(callback){
	    		var myOptions = {
	                    zoom: 13,
	                    center: locations.latlng,
	                    mapTypeId: google.maps.MapTypeId.ROADMAP
	                };
	    		locations.map = new google.maps.Map(document.getElementById("map_canvas"), myOptions); 
	    		locations.overlay = new google.maps.OverlayView();
	    		locations.overlay.draw = function(){};
	    		locations.overlay.setMap($scope.map);
	    		locations.element = document.getElementById('map_canvas');
	                callback();
	    	}
        	
        	
    		
            function initializeAssetMap(latlangs, callback){
            	
        		if(latlangs && latlangs.length>0){
        			latlng = new google.maps.LatLng(latlangs[0].assetCoordinates[0].latitude,latlangs[0].assetCoordinates[0].longitude);
        			 callback(latlng);
        		 }else{
        			   if (navigator.geolocation) {
        	                navigator.geolocation.getCurrentPosition(function(position) {
        	                  
        	                	latlng=   new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        	                	 callback(latlng);
        	                }, function() {
        	                	
        	                  callback(new google.maps.LatLng("12.9716", "77.5946"));
        	                });
        	              } else {
        	            	  callback(new google.maps.LatLng("12.9716", "77.5946"));
        	              }
        		 }
        	}
            
            function drawFixedAsset(fixed){
            	$.each(fixed.assetCoordinates, function( index, value1 ) {
				
				var infoWindow = new google.maps.InfoWindow();
				var startMarker =new google.maps.Marker({
 		            map: locations.map,
 		            position: new google.maps.LatLng(value1.latitude, value1.longitude),
 		            title: fixed.name,
 		           icon: {
 		              size: new google.maps.Size(220, 220),
 		              scaledSize: new google.maps.Size(32, 32),
 		              origin: new google.maps.Point(0, 0),
 		              url: "data:image/png;base64,"+fixed.assetType.image,
 		              anchor: new google.maps.Point(16, 16)
 		            },
 		        });
 				var infoWindow2 = new google.maps.InfoWindow();
				startMarker.content = '<div class="infoWindowContent"> <b>Description :</b> ' +fixed.description+'</div>';
 				
				// on mouseout 
				google.maps.event.addListener(startMarker, 'mouseout', function() {
					infoWindow.close();	
				});
				
				var activeInfoWindow ;
				// add content to InfoWindow for click event 
				infoWindow2.setContent('<div class="infoWindowhead">' + startMarker.title + '</div>' + startMarker.content);
				
				// add listener on InfoWindow for click event
				google.maps.event.addListener(startMarker, 'click', function() {
					
					if(activeInfoWindow != null) activeInfoWindow.close();
					// Open InfoWindow - on click 
					infoWindow2.open(locations.map, startMarker);
					$(".userpath").click(function(){
						var ele= document.getElementById("userPathModal");
						$(ele).modal('show');
						 vm.drawUserPath($(this).attr('userid'));
					 });
					
					// Store new open InfoWindow in global variable
					activeInfoWindow = infoWindow2;
				}); 	
				//function resizeMap(){
        			setTimeout(function(){
        				google.maps.event.trigger(locations.map, "resize");
     		    	  locations.map.setCenter(userPaths.latlng);
     		       },100); 
        		//}
				//resizeAssetMap();
				
				 });
           	 
    			
            }
           function drawPolyline(spread){
            var spreadAssetCoordinates = [];
  	    	$.each(spread.assetCoordinates, function( index, value) {
  	    		
  	    		spreadAssetCoordinates.push({lat: value.latitude, lng: value.longitude});
  	    	});
  	    	var midLatLng= {lat: spread.assetCoordinates[0].latitude, lng: spread.assetCoordinates[0].longitude};
  	    	var infowindow = new google.maps.InfoWindow({
  	             content: '<div class="infoWindowhead">'+spread.name+ '<div class="infoWindowContent"> <b>Description :</b> ' +spread.description+'</div></div>'});
  	       infowindow.setPosition(midLatLng);
  	     //  infowindow.open(locations.map);
  	       var polylinepath = new google.maps.Polyline({
  	              path: spreadAssetCoordinates,
  	              geodesic: true,
  	              strokeColor:  spread.assetType.colorcode,
  	              strokeOpacity:  1.0,
  	              strokeWeight: 2.5,
  	              fillColor: spread.assetType.colorcode,
  	              fillOpacity: 0.3,
  	              editable: false
  	            });
  	    	polylinepath.setMap(locations.map);
  	    	setTimeout(function(){
				
		    	   google.maps.event.trigger(locations.map, "resize");
		    	  locations.map.setCenter(userPaths.latlng);
		       },100); 
  	    	 
  	      }
           
           
           
          	}
    }
})();
