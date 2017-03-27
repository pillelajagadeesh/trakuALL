(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrServiceController', TrServiceController);

    TrServiceController.$inject = ['$scope', '$state','$compile', 'TrService', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','TrCaseUser' , 'TrServiceSearch','TrServiceSearchForMap'];

    function TrServiceController ($scope, $state,$compile, TrService, ParseLinks, AlertService, pagingParams, paginationConstants,TrCaseUser, TrServiceSearch,TrServiceSearchForMap) {
        var vm = this;
        
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.drawservice=drawservice;
       // vm.filterUsers=filterUsers;
        vm.filterSearch=filterSearch;
        vm.filterTrSevices=filterTrSevices;
        
        var latlng;
        var userPaths={};
    	var locations={};
    	
        loadAll();
        
        $scope.tab = 1;

		$scope.setTab = function(newTab){
		  $scope.tab = newTab;
		  if( $scope.tab==2){
			  vm.drawservice();
		  }
		};

		$scope.isSet = function(tabNum){
		  return $scope.tab === tabNum;
		};

        function loadAll () {
            TrService.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                   // result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.trServices = data;
                vm.trServicesbackup = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        
        function filterSearch(){
        	
			if($scope.search!=null && $scope.search!="" && !angular.isUndefined($scope.search)){
				
			TrServiceSearch.query({userId:$scope.search,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                   // result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                
                vm.trServices = data;
                vm.page = pagingParams.page;
        
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
			}else{
				loadAll ();
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
        
        /*function filterUsers(){
        	if(vm.seachCase!=null && !angular.isUndefined(vm.seachCase)){
        	TrCaseUser.gettrcasesuserbyid(vm.seachCase,function(response){
        		$scope.trcaseuser=response;
        		drawLatestCases($scope.trcaseuser);
        	});
        	}
        }*/
        
        function filterTrSevices(){
        	
        	if($scope.seachService!=null && $scope.seachService!="" && !angular.isUndefined($scope.seachService)){
        		TrServiceSearchForMap.searchserviceinmap($scope.seachService,function(response){
            		$scope.searchtrservice=response;
            		drawLatestlocations($scope.searchtrservice);
            	});
             }else{
            	 drawLatestlocations(vm.trServicesbackup);
             }
        }
        
        function drawservice(){
        	drawLatestlocations(vm.trServices);
        }
        
        function drawLatestlocations(response){
        	if(response){
        		 initializeMap(response,function(latlang){
        			 locations.latlng = latlang;
        			 setupMap(function(){
        				 $.each(response, function( index, value ) {
        					var infoWindow = new google.maps.InfoWindow();
              				var startMarker =new google.maps.Marker({
              		            map: locations.map,
              		            position: new google.maps.LatLng(value.trCase.pinLat, value.trCase.pinLong),
              		            title: value.description,
              		          
              		        });
							
							var infoWindow2 = new google.maps.InfoWindow();
							
							if(value.trCase.priority == 'LOW'){
              					startMarker.setIcon('content/images/Pointers-L4.png');
              				} else if(value.trCase.priority == 'MEDIUM'){
              					startMarker.setIcon('content/images/Pointers-M4.png');
              				} else if(value.trCase.priority == 'HIGH'){
              					startMarker.setIcon('content/images/Pointers-H4.png');
              				}else if(value.trCase.priority == 'CRITICAL'){
              					startMarker.setIcon('content/images/Pointer-C4.png');
              				}
              				var span='<a userId="'+value.trCase.geofenceName+'" class="userpath" >Load travelled path</a>';
              				startMarker.content = '<div class="infoWindowContent">' +value.trCase.address+'<br></div>';
              				$compile(startMarker.content)($scope);
              				
						    google.maps.event.addListener(startMarker, 'mouseover', function(){
							infoWindow.setContent('<div class="infoWindowhead">' + startMarker.title + '</div>' + startMarker.content);
							infoWindow.open(locations.map, startMarker);
								
              		        });
		
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
			
							resizeMap();
						});
        				 
					}); 
				});
			}
        	function setupMap(callback){
	    		var myOptions = {
	                    zoom: 10,
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
        	}
        
        function resizeMap(){
			setTimeout(function(){
		    	   google.maps.event.trigger(locations.map, "resize");
		    	   locations.map.setCenter(locations.latlng);
		       },1000); 
		}
        
        function initializeMap(latlangs, callback){
    		if(latlangs && latlangs.length>0){
    			 latlng = new google.maps.LatLng(latlangs[0].trCase.pinLat,latlangs[0].trCase.pinLong);
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
    }
})();
