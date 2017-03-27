(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('LocationLogController', LocationLogController);

    LocationLogController.$inject = ['$filter','$scope', '$compile', '$state', 'LocationLog', 'ParseLinks', 'AlertService', 'pagingParams', '$stateParams', 'paginationConstants','LocationLogMap','LocationLogMapPath','$interval'];

    function LocationLogController ($filter,$scope,$compile, $state, LocationLog, ParseLinks, AlertService, pagingParams, $stateParams, paginationConstants,LocationLogMap,LocationLogMapPath,$interval) {
        var vm = this;
        
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.drawUserPath = drawUserPath;
        vm.pathuserid = null;
        vm.logsbystatus=[];
        vm.all=all;
        vm.active=active;
        vm.inactive=inactive;
        vm.idle=idle;
       vm.filterUsers = filterUsers;
        vm.fromDate = null;
        vm.toDate = null;
        vm.today = today;
        vm.today();
        vm.onChangeDate = onChangeDate;
        var latlng;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        var dateFormat = 'yyyy-MM-dd';
        vm.selectedLogs="ALL";
       // loadLogsTable();
        setTimeout(function(){
        	vm.updateUsers();
		}, 1000);
        vm.updateUsers=function(){
        	vm.locateUsers(function(){
        		if(vm.selectedLogs==="ALL"){
            		vm.all();
            	}else if (vm.selectedLogs==="ACTIVE"){
            		vm.active();
            	}else if (vm.selectedLogs==="INACTIVE"){
            		vm.inactive();
            	}else if (vm.selectedLogs==="IDLE"){
            		vm.idle();
            	} 
        	})
 	   }
     var updateLogs= $interval(function(){
    	   
    	   vm.updateUsers();
        	
        },60000);
    	var userPaths={};
    	var locations={};
    	
		
	    function onChangeDate(){
            vm.drawUserPath(vm.pathuserid);
        }
        
        function today () {
            var today = new Date();
            vm.toDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());
            vm.fromDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        }
      

        function loadLogsTable () {
	        	
	            var fromDate = $filter('date')(vm.fromDate, dateFormat);
	            var toDate = $filter('date')(vm.toDate, dateFormat);
                var data = {fromDate:vm.fromDate.getTime(),toDate:vm.toDate.getTime()}
                
                LocationLogMap.getlogsbydate(data,function(response,headers){
                	vm.links = ParseLinks.parse(headers('link'));
                    vm.totalItems = headers('X-Total-Count');
                    vm.queryCount = vm.totalItems;
                    vm.locationLogs = response;
                    vm.page = pagingParams.page;
                    
                });
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
           
        $scope.$watch('login',function(searchkey,oldval){
        	if(searchkey || oldval)
        		drawLatestlocations($filter('filter')($scope.locations, {userName: searchkey}));
      	})
    	$scope.openInfoWindow = function(e, selectedMarker){
            e.preventDefault();
            google.maps.event.trigger(selectedMarker, 'click');
        }
        function drawUserPath(userId){
        	 var data = {fromDate:vm.fromDate.getTime(),toDate:vm.toDate.getTime(),userId:userId}
        	LocationLogMapPath.getmaplogs(data,function(response){
        		vm.pathuserid= response.id;
        		drawlines(response);
        	});
        }
      
         vm.locateUsers = function(callback){
        	 LocationLogMapPath.locateUsers(function(response){
        		 $scope.locations=response;
        		 callback();
        	 });
        }
        
         function filterUsers(){
        	 if(vm.selectedLogs==="ALL"){
        		 drawLatestlocations($filter('filter')($scope.locations, { 'login': vm.searchUser  } ));        		 
        	 }else{
        		 drawLatestlocations($filter('filter')($filter('filter')($scope.locations, { 'status': vm.selectedLogs}, true ), { 'login': vm.searchUser} ));
        	 }
        	
         }
         
        function all(){
        	vm.selectedLogs="ALL";
        	if($stateParams.login==undefined){
        		drawLatestlocations($filter('filter')($scope.locations, { 'login': vm.searchUser  } ));
        	}else{
        		drawLatestlocations($filter('filter')($scope.locations, { 'login': $stateParams.login } ));
        	}         	

        }
        function active(){
        	vm.selectedLogs="ACTIVE";
        		 drawLatestlocations($filter('filter')($filter('filter')($scope.locations, { 'status': vm.selectedLogs}, true ), { 'login': vm.searchUser} ));
        		 
        }
        
        function inactive(){
        	vm.selectedLogs="INACTIVE";
       	 drawLatestlocations($filter('filter')($filter('filter')($scope.locations, { 'status': vm.selectedLogs}, true ), { 'login': vm.searchUser} ));
        }
        
        function idle(){
        	vm.selectedLogs="IDLE";
        	drawLatestlocations($filter('filter')($filter('filter')($scope.locations, { 'status': vm.selectedLogs}, true ), { 'login': vm.searchUser} ));
        }
       
        function drawLatestlocations(response){
        
        	if(response){
        		 initializeMap(response,function(latlang){
        			 locations.latlng = latlang;        			
        			var bounds = new google.maps.LatLngBounds();
        			 setupMap(function(){
        				 $.each(response, function( index, value ) {
        					var infoWindow = new google.maps.InfoWindow();
        					
        					//creating a start marker
/*              				var startMarker =new google.maps.Marker({
              		            map: locations.map,
              		            position: new google.maps.LatLng(value.latitude, value.longitude),
              		            title: value.login,
              		        });*/
/*            			    bounds.extend(startMarker.position);*/
            			    
							var infoWindow2 = new google.maps.InfoWindow();
							
							//Assigning a color image marker
/*							if(value.status == 'ACTIVE'){
              					startMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
              				} else if(value.status == 'INACTIVE'){
              					startMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/red-dot.png');
              				} else if(value.status == 'IDLE'){
              					startMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/yellow-dot.png');
              				}*/
              				
              				//Create a custom marker
              				
              				var markers = [{
              					Lat: "0.0",
              					Lng: "0.0",
              					name: "default"
              				}

              			]; 
            			      var imgbg = document.createElement('img');
        						imgbg.className = "marker_back_image";
        						//imgbg.src = 'http://www.iconsdb.com/icons/preview/soylent-red/map-marker-2-xxl.png';
                				if(value.status == 'ACTIVE'){
            						imgbg.src = 'content/images/greenmarker.png'
                				} else if(value.status == 'INACTIVE'){
            						imgbg.src = 'content/images/redmarker.png'
                				} else if(value.status == 'IDLE'){
            						imgbg.src = 'content/images/yellowmarker.png'
                				}
                				
              				function customMarker(latlng, map, args) {
              					this.latlng = new google.maps.LatLng(value.latitude, value.longitude);
              					this.setMap(locations.map);
              					this.title = value.login;
              					this.icon = imgbg;
              				}
              				
            			    bounds.extend(new google.maps.LatLng(value.latitude, value.longitude));

            			    var userContent;
            			    var travelPathLink = '<a userId='+value.userid+' class="userpath" >Load travelled path</a>';            			    
            			    if(value.userImage != null){
              				   userContent =  '<div class="infoWindowhead">' + value.login + '</div><div class="infoWindowContent"><div class="rows"><div class="col-md-4"><img src="data:image/png;base64,'+value.userImage+'" style="width: 50px; height: 60px;"></div><div class="col-md-8"><p>' +value.address+'</p><p>'+travelPathLink+'</p></div></div></div>';
            			    }
            			    else{
               				   userContent =  '<div class="infoWindowhead">' + value.login + '</div><div class="infoWindowContent"><div class="rows"><div class="col-md-4"><img src="content/images/userimg.gif" style="width: 50px; height: 60px;"></div><div class="col-md-8"><p>' +value.address+'</p><p>'+travelPathLink+'</p></div></div></div>';
            			    }
              				var infowindowPopup = new google.maps.InfoWindow({
              				    content: userContent,              				    
              				    pixelOffset: new google.maps.Size(0, -50),
              				  });
              				
              				customMarker.prototype = new google.maps.OverlayView();

              				customMarker.prototype.draw = function () {
              					var self = this;
              					var div = this.div;

              					if (!div) {

              						div = this.div = document.createElement('div');
              						div.id = 'marker';
              						div.style.width = '100px';
              						div.style.height = '100px';

              						var div_pointer = document.createElement('div');
              						div_pointer.className = 'triangle';

              						var image_container = document.createElement('div');
              						image_container.className = 'image_container';
              			                    						
              			      var img = document.createElement('img');
              						img.className = "marker_image";
              						var image2 = "data:image/png;base64,";
              						image2 += value.userImage;
              						if(value.userImage == undefined){
              							image2 = "content/images/userimg.gif";
              						}
              						
              						img.src = image2;
          						//img.src = 'https://pbs.twimg.com/profile_images/616198230297214976/PeR519Mx.png';
              			      
              						var name_container = document.createElement('div');
              						name_container.className = 'name_container';

              						var exit = document.createElement('div');
              						exit.className = 'exit';
              						exit.innerHTML = '<img className="exit_image" style="width:30px; height:30px;" src="https://cdn0.iconfinder.com/data/icons/large-glossy-icons/512/Delete.png">' + '</img>';
              						exit.style.display = 'none';

              			    		var infoWindowPopUp = new google.maps.InfoWindow();
              			    		infoWindowPopUp.setContent('<div class="infoWindowhead">' + value.login + '</div><div class="infoWindowContent">' +value.address+'<br><a userId="'+value.userid+'" class="userpath" >Load travelled path old</a></div>');              			    		
              			    		
              						div.appendChild(image_container);
              						image_container.appendChild(img);
                   			        image_container.appendChild(imgbg);
              			            
              						div.appendChild(exit);

              						name_container.onmouseover = function () {
              							name_container.style.opacity = '0.6';
              							div.style.zIndex = '1000'
              						};
              						name_container.onmouseout = function () {
              							name_container.style.opacity = '0';
              							div.style.zIndex = '100'
              						};
              						
/*              						div.addEventListener('click', large, false);
              						exit.addEventListener('click', close, false);*/
              						
              						var activeInfoWindow;
        							// add content to InfoWindow for click event 
        							infoWindow2.setContent('<div class="infoWindowhead">' + value.login + '</div>' + userContent);
        							if(activeInfoWindow != null) activeInfoWindow.close();
        							
              						google.maps.event.addDomListener(imgbg, 'click', function(event) {
                  							infowindowPopup.setPosition(new google.maps.LatLng(value.latitude, value.longitude));
                  							infowindowPopup.open(locations.map);
                  							
            								if(activeInfoWindow != null) activeInfoWindow.close();
            								$(".userpath").click(function(){
            									/*if(activeInfoWindow != null) activeInfoWindow.close()*/;
            									
            									// Open InfoWindow - on click 
/*            									infoWindow2.open(locations.map);*/
            									
            									var ele= document.getElementById("userPathModal");
            									$(ele).modal('show');
            									 vm.drawUserPath($(this).attr('userid'));
            									 var cleratodatefromdate=  vm.today();
            						             //cleratodatefromdate.remove();
            								 });
       										activeInfoWindow = infoWindow2;
       									 
              						});
              					              						
              						google.maps.event.addDomListener(div, "click", function (event) {
              							google.maps.event.trigger(self, "click");
              						});

              						var panes = this.getPanes();

              						panes.overlayImage.appendChild(div);

              					}

              					var point = this.getProjection().fromLatLngToDivPixel(this.latlng);

              					if (point) {
              						div.style.left = (point.x - 50) + 'px';
              						div.style.top = (point.y - 125) + 'px';
              					}
              				}

              				customMarker.prototype.remove = function () {

              					if (this.div) {
              						this.div.parentNode.removeChild(this.div);
              						this.div = null;
              					}

              				}

              				customMarker.prototype.getPosition = function () {
              					return this.latlng;
              				}
              				markers.forEach(function (marker) {
              					//image = value.userImage;

              					var overlay = new customMarker(
              							latlang,
              							location.map, {
              							marker_id: '123',
              							colour: 'Red'
              						});
              				});
              				//end

/*              				var span='<a userId="'+value.userid+'" class="userpath1" >Load travelled path old</a>';
              				if(value.userImage != null){
                  				startMarker.content = '<div class="infoWindowContent">' +value.address+'<br>'+span+'</div>';
              				}else{
              					startMarker.content = '<div class="infoWindowContent">' +value.address+'<br>'+span+'</div>';
              				}
              				
              				$compile(startMarker.content)($scope);
              				
						google.maps.event.addListener(startMarker, 'mouseover', function(){
							infoWindow.setContent('<div class="infoWindowhead">' + startMarker.title + '</div>' + startMarker.content);
							infoWindow.open(locations.map, startMarker);
								
              		        });
		
							// on mouseout 
							google.maps.event.addListener(startMarker, 'mouseout', function() {
								infoWindow.close();	
							});*/
							
/*							var activeInfoWindow;
							// add content to InfoWindow for click event 
							infoWindow2.setContent('<div class="infoWindowhead">' + startMarker.title + '</div>' + startMarker.content);
							if(activeInfoWindow != null) activeInfoWindow.close();*/							
							
/*							google.maps.event.addDomListener(startMarker, 'click', function() {
								
								if(activeInfoWindow != null) activeInfoWindow.close();
								$(".userpath").click(function(){
									if(activeInfoWindow != null) activeInfoWindow.close();
									
									// Open InfoWindow - on click 
									infoWindow2.open(locations.map, startMarker);
									
									var ele= document.getElementById("userPathModal");
									$(ele).modal('show');
									 vm.drawUserPath($(this).attr('userid'));
									 var cleratodatefromdate=  vm.today();
						             cleratodatefromdate.remove();
										activeInfoWindow = infoWindow2;
								 });
								
								// Store new open InfoWindow in global variable
								activeInfoWindow = infoWindow2;
							});*/ 							
			
							
						});
        					locations.map.fitBounds(bounds);
        			 }); 
				});
			}
        	function setupMap(callback){
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
        	}
        	
    	function drawlines(paths){
    		vm.userPathName =paths.userName;
    		
    		if(paths.path.length > 0){
    			var data= [{"latitude" : paths.path[0].lat ,"longitude" : paths.path[0].lng}];
    		}else{
    			var data = paths.path;
    		} 
    		
    		initializeMap(data,function(latlang){
	    		userPaths.latlng=latlang;
	    		setupMap(function(){
	    			if(paths && paths.path && paths.path.length>0 ){
	    				loadUserPaths(paths);
	    	            }else{
	    	            	resizeMap();
	    	            	console.log('there are no user agents found');
	    	            }
	    		});
	    		function setupMap(callback){
	        		var myOptions = {
	                        zoom: 13,
	                        center: userPaths.latlng,
	                        mapTypeId: google.maps.MapTypeId.ROADMAP
	                    };
	                    userPaths.map = new google.maps.Map(document.getElementById("userPath"), myOptions); 
	                    userPaths.overlay = new google.maps.OverlayView();
	                    userPaths.overlay.draw = function(){};
	                    userPaths.overlay.setMap( userPaths.map);
	                    userPaths.element = document.getElementById('userPath');
	                    callback();
	        	}
	    		function loadUserPaths(paths){
	        		
	    			
	    			var flightPath = new google.maps.Polyline({
	    	          path: paths.path,
	    	          geodesic: true,
	    	          strokeColor:"#425cf4",
	    	          strokeOpacity: paths.stroke.weight,
	    	          strokeWeight: 2,
	    	        icons: [{
	    	            icon: {
	    	            	path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW
	    	            },
	    	          offset: '25px',
	                repeat: '50px'
	    	          }]
	    	        });
	    	        flightPath.setMap( userPaths.map);
	    	        drawMarkers(paths,flightPath);
	    		
	    	}
	   	 
	    	function drawMarkers(userpath,flightPath){
	    		
	    		var paths=userpath.path;
	    		var infoWindow = new google.maps.InfoWindow();
				var distance = 0;
				var path=flightPath.getPath()
				for (var i = 0; i < path.getLength() - 1; i++) {
	              distance += google.maps.geometry.spherical.computeDistanceBetween(path.getAt(i), path.getAt(i + 1));
	          }
				var startMarker =new google.maps.Marker({
		            map:  userPaths.map,
		            position: new google.maps.LatLng(paths[0].lat, paths[0].lng),
		            title: 'Started here'
		        });
				startMarker.content = '<div class="infoWindowhead">' +userpath.userName+'</div>';
		        
		        google.maps.event.addListener(startMarker, 'mouseover', function(){
		        	
		            infoWindow.setContent(startMarker.content + '<div class="infoWindowContent">' + startMarker.title + '</div>');
		            infoWindow.open( userPaths.map, startMarker);
		            
		        });
				 google.maps.event.addListener(startMarker, 'mouseout', function(){
		            infoWindow.close( userPaths.map, startMarker);
		        });
		    
		        var infoWindow1 = new google.maps.InfoWindow();
		        var endMarker =new google.maps.Marker({
		            map:  userPaths.map,
		            position: new google.maps.LatLng(paths[paths.length-1].lat, paths[paths.length-1].lng),
		            title: "Total distance travelled \n "+(distance/1000).toFixed(2)+" KM"
		        });
		      endMarker.content = '<div class="infoWindowhead">' + userpath.userName+ '</div>';
		        
		        google.maps.event.addListener(endMarker, 'mouseover', function(){
		        	
		            infoWindow1.setContent(endMarker.content + '<div class="infoWindowContent">' + endMarker.title + '</div>'  );
		            infoWindow1.open( userPaths.map, endMarker);
					
		        });
				 google.maps.event.addListener(endMarker, 'mouseout', function(){
		            infoWindow1.close( userPaths.map, endMarker);
		        });
		        resizeMap();
		
		}
	    	});
    		
    		function resizeMap(){
    			setTimeout(function(){
 		    	   google.maps.event.trigger(userPaths.map, "resize");
 		    	   userPaths.map.setCenter(userPaths.latlng);
 		       },100); 
    		}
       }
    	$scope.$on('$destroy', function() {
            $interval.cancel(updateLogs);
        });
    	
    	function initializeMap(latlangs, callback){
    		if(latlangs && latlangs.length>0){
    			 latlng = new google.maps.LatLng(latlangs[0].latitude,latlangs[0].longitude);
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
    	
    	
    	
    	 vm.datePickerOpenStatus.createdDate = false;
         vm.datePickerOpenStatus.modifiedDate = false;
         vm.datePickerOpenStatus.serviceDate = false;

         function openCalendar (date) {
             vm.datePickerOpenStatus[date] = true;
             

         }
       
         $scope.reset = function () {
             var cleratodatefromdate=  vm.today();
             //cleratodatefromdate.remove();
         }
 		
    	
    }
})();
