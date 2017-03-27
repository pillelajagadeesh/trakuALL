(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('TrCaseController', TrCaseController);

    TrCaseController.$inject = ['$filter','$scope','$http', '$state', 'TrCase', '$compile','ParseLinks', 'AlertService','$stateParams', 'pagingParams', 'paginationConstants','TrCaseUser','TrCaseSearch', 'TrCasePriority','TrCasePrioritySearch','TrCaseEdit','$timeout'];

    function TrCaseController ($filter,$scope,$http, $state, TrCase,$compile, ParseLinks, AlertService, $stateParams, pagingParams, paginationConstants,TrCaseUser,TrCaseSearch,TrCasePriority, TrCasePrioritySearch,TrCaseEdit,$timeout) {
        var vm = this;
        
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.trCases=[];
        var latlng;
        var userPaths={};
    	var locations={};
    	vm.loadAll=loadAll;
    	vm.filterSearch=filterSearch;
    	vm.assigncasetouser = assigncasetouser;
    	vm.editcase = editcase;
    	
    	vm.filterUsers = filterUsers;
        vm.drawcases=drawCases;
        vm.drawCasesWithDetail=drawCasesWithDetail;
        vm.filterTrcases=filterTrcases;
        vm.filterTrcasesId=filterTrcasesId;
		$scope.tab = 1;

			$scope.setTab = function(newTab){
			  $scope.tab = newTab;
			  if( $scope.tab==2){
				  vm.drawcases();
			  }
			  if($scope.tab==3){
				  vm.drawCasesWithDetail();
			  }
			};

			$scope.isSet = function(tabNum){
			  return $scope.tab === tabNum;
			};
			
			function filterSearch(){
				
				if($scope.search!=null && $scope.search!="" && !angular.isUndefined($scope.search)){
					if($stateParams.priority==undefined){
						TrCaseSearch.query({userId:$scope.search,
			                page: pagingParams.page - 1,
			                size: vm.itemsPerPage,
			                sort: sort()
			            }, onSuccess, onError);
					}else{
						TrCasePrioritySearch.query({priority:$stateParams.priority,searchtext:$scope.search,
			                page: pagingParams.page - 1,
			                size: vm.itemsPerPage,
			                sort: sort()
			            }, onSuccess, onError);
					}	
	            function sort() {
	                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
	                if (vm.predicate !== 'id') {
	                  //  result.push('id');
	                }
	                return result;
	            }
	            function onSuccess(data, headers) {
	                vm.links = ParseLinks.parse(headers('link'));
	                vm.totalItems = headers('X-Total-Count');
	                vm.queryCount = vm.totalItems;
	                vm.trCases = data;
	                vm.page = pagingParams.page;
	        
	            }
	            function onError(error) {
	                AlertService.error(error.data.message);
	            }
				}else{
					loadAll ();
				}
			}
			
			loadAll ();
        function loadAll () {
           if($stateParams.priority==undefined){
        	
        	TrCase.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
           }else{
        	   TrCasePriority.query({priority:$stateParams.priority,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        }
            
            
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                  //  result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.trCases = data;
                vm.trCasesbackup= data;
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
        
      	$scope.openInfoWindow = function(e, selectedMarker){
            e.preventDefault();
            google.maps.event.trigger(selectedMarker, 'click');
        }
        
      	TrCaseUser.gettrcasesuser(function(response){
        	$scope.trcaseuser=response;
        });
      	
        function filterTrcases(){
        	
        	if($scope.seachCase!=null && $scope.seachCase!=""  && !angular.isUndefined($scope.seachCase)){
        		TrCaseUser.searchtrcasesuserbyvalue($scope.seachCase,function(response){
            		$scope.searchtrcase=response;
            		drawLatestCasesWithDetail($scope.searchtrcase);
            	});
             }else{
            	 drawLatestCasesWithDetail(vm.trCasesbackup);
             }
        }
        
        function filterTrcasesId(){
        	if(vm.seachCaseId!=null && vm.seachCaseId!="" && !angular.isUndefined(vm.seachCaseId)){
            	TrCaseUser.searchtrcasesuserbyvalue(vm.seachCaseId,function(response){
            		vm.caseIds = response;
            	});
             }
        }
        
        function filterUsers(){
        	if($scope.seachCase1!=null && !angular.isUndefined($scope.seachCase1)){
        	TrCaseUser.gettrcasesuserbyid($scope.seachCase1,function(response){
        		$scope.trcaseuser=response;
        		drawLatestCases($scope.trcaseuser);
        		//drawLatestCases($filter('filter')($filter('filter')($scope.trcaseuser, true ), { 'id': vm.seachCase} ));
        	});
        	}
        }
        
        function filterUsers(){
        	
        	if($scope.seachCase1!=null && !angular.isUndefined($scope.seachCase1)){
        	TrCaseUser.gettrcasesuserbyid($scope.seachCase1,function(response){
        		$scope.trcaseuser=response;
        		drawLatestCases($scope.trcaseuser);
        		//drawLatestCases($filter('filter')($filter('filter')($scope.trcaseuser, true ), { 'id': vm.seachCase} ));
        	});
        	}
        }
        
        
        function drawCases(){
        	drawLatestCases($scope.trcaseuser);
        }
        function drawLatestCases(response){
        	
        	if(response){
        		 initializeMap(response,function(latlang){
        			 locations.latlng = latlang;
        			 setupMap(function(){
        				 $.each(response, function( index, value ) {
        					var infoWindow = new google.maps.InfoWindow();
              				var startMarker =new google.maps.Marker({
              		            map: locations.map,
              		            position: new google.maps.LatLng(value.pinLat, value.pinLong),
              		            title: value.login,
              		          
              		        });
							
							var infoWindow2 = new google.maps.InfoWindow();
							if(value.userCase=='C'){
              				if(value.priority == 'LOW'){
              					startMarker.setIcon('content/images/Pointers-L4.png');
              				} else if(value.priority == 'MEDIUM'){
              					startMarker.setIcon('content/images/Pointers-M4.png');
              				} else if(value.priority == 'HIGH'){
              					startMarker.setIcon('content/images/Pointers-H4.png');
              				}else if(value.priority == 'CRITICAL'){
              					startMarker.setIcon('content/images/Pointer-C4.png');
              				}
              				var span='<a userId="'+value.geofenceName+'" class="userpath" >Load travelled path</a>';
              				startMarker.content = '<div class="infoWindowContent"><b>Case Id : </b>'+value.id+'<br> <b>Reported By :</b> ' +value.reportedByUser+'<br> <b> Assigned To :</b> '+value.assignedToUser+'<br> <b>Address :</b> '+value.address+'</div>';
              				
							}else{
						    if(value.status == 'ACTIVE'){
	               				 startMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
	               			} else if(value.status == 'INACTIVE'){
	               				 startMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/red-dot.png');
	               			} else if(value.status == 'IDLE'){
	               				  startMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/yellow-dot.png');
	               			}
						        var span='<a userId="'+value.userid+'"  class="userpath" >Load travelled path</a>';
	               				var assigncasespan='<a userId="'+value.userid+'" userLogin="'+value.login+'" class="assigncase" >Assign Case</a>';
	               				startMarker.content = '<div class="infoWindowContent">' +value.address+'<br>'+assigncasespan+'</div>';	
							}
							
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
								$(".assigncase").click(function(){
									$('#assigncaseModal').modal('show');
									vm.assigncasetouser($(this).attr('userId'), $(this).attr('userLogin'));
									vm.seachCaseId=null;
						        	 vm.caseIds=null;
						        	 vm.error=null;
						        	 vm.success=null;
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
    			 latlng = new google.maps.LatLng(latlangs[0].pinLat,latlangs[0].pinLong);
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
        
        function drawCasesWithDetail(){
        	drawLatestCasesWithDetail(vm.trCases);
        }
        function drawLatestCasesWithDetail(response){
        	
        	if(response){
        		 initializeTrcaseMap(response,function(latlang){
        			 locations.latlng = latlang;
        			 setupTrcaseMap(function(){
        				 $.each(response, function( index, value ) {
        					var infoWindow = new google.maps.InfoWindow();
              				var startMarker =new google.maps.Marker({
              		            map: locations.map,
              		            position: new google.maps.LatLng(value.pinLat, value.pinLong),
              		            title: value.description,
              		          
              		        });
							
							var infoWindow2 = new google.maps.InfoWindow();
							//if(value.userCase=='C'){
              				if(value.priority == 'LOW'){
              					startMarker.setIcon('content/images/Pointers-L4.png');
              				} else if(value.priority == 'MEDIUM'){
              					startMarker.setIcon('content/images/Pointers-M4.png');
              				} else if(value.priority == 'HIGH'){
              					startMarker.setIcon('content/images/Pointers-H4.png');
              				}else if(value.priority == 'CRITICAL'){
              					startMarker.setIcon('content/images/Pointer-C4.png');
              				}
              				var span='<a userId="'+value.geofenceName+'" class="userpath" >Load travelled path</a>';
              				startMarker.content = '<div class="infoWindowContent"> <b>Reported By :</b> ' +value.reportedByUser+'<br> <b> Assigned To :</b> '+value.assignedToUser+'<br> <b>Address :</b> '+value.address+'</div>';
              				
							//}
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
							resizeTrCaseMap();
						});
					}); 
				});
			}
        	function setupTrcaseMap(callback){
	    		var myOptions = {
	                    zoom: 10,
	                    center: locations.latlng,
	                    mapTypeId: google.maps.MapTypeId.ROADMAP
	                };
	    		locations.map = new google.maps.Map(document.getElementById("map_canvas1"), myOptions); 
	    		locations.overlay = new google.maps.OverlayView();
	    		locations.overlay.draw = function(){};
	    		locations.overlay.setMap($scope.map);
	    		locations.element = document.getElementById('map_canvas1');
	                callback();
	    	}
        	}
        
        function resizeTrCaseMap(){
			setTimeout(function(){
		    	   google.maps.event.trigger(locations.map, "resize");
		    	   locations.map.setCenter(locations.latlng);
		       },1000); 
		}
        function initializeTrcaseMap(latlangs, callback){
    		if(latlangs && latlangs.length>0){
    			 latlng = new google.maps.LatLng(latlangs[0].pinLat,latlangs[0].pinLong);
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
        $('.select2').select2();
        
        
        function assigncasetouser(userId, userLogin){
        	vm.editUserId=userId;
        	$scope.edituserLogin = userLogin;
        	TrCaseEdit.getalltrcases(function(response){
            	$scope.trallcases=response;
            	
            });
        
       }
        vm.success = null;
        vm.error=null;
        function editcase(){
        	 var data = {userId:vm.editUserId,caseId:vm.seachCaseId}
        	
        	 /*TrCaseEdit.edittrcase(data, function(response){
            	$scope.edittrcaseresponse=response;
            	vm.success = 'OK';
            
            });*/
        	 TrCaseEdit.edittrcase(data,function(response,headers){
        		
         	    if(response.status == 200){
             		vm.error=null;
             		vm.success='OK';
             		
             	} 
             	if(response.status == 400){
             		vm.success=null;
             		vm.error='No case found with this case id';
             		
             	} 
             });
        	 $timeout(function (){
        	 $('#assigncaseModal').modal('hide');
        	 vm.success = null;
        	 vm.error=null;
        	 vm.seachCaseId=null;
        	 vm.caseIds=null;
        	 },2000);
       }
    }
})();
