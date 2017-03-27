(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('ReportController', ReportController)
        

    ReportController.$inject = ['$scope', '$state', "Report", '$rootScope','$timeout','$window'];

    function ReportController ($scope, $state, Report,  $rootScope,$timeout, $window) {
    	var vm = this;
		vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.reportTypeChange = reportTypeChanged;
    	vm.users=[];
		vm.geofences=[];
		$scope.reporthide = false;
		vm.select2=select2;
		$scope.report = function(){
			if((vm.reportType=='geofence' && vm.geofence=='all' )||(vm.reportType=='agent' && vm.userAgent=='all')){
				vm.id=0;
			}
				Report.getReport({id:vm.id,fromTime:vm.fromTime,toTime:vm.toTime,reportType:vm.reportType},function(response){
					
					if(response  instanceof Array){
						vm.reportRecords=response;
					}else{
						vm.reportRecords=[];
						vm.reportRecords.push(response);
					}
					$scope.search="";
					$scope.reporthide = true;
				})
				
			
		
		}
		vm.detailedReport=detailedReport;
		vm.fromTime=0;
		vm.toTime=0;
		vm.selecterange=selectRange;
		vm.calculateToDate=calculateToDate;
		vm.calculateFromDate=calculateFromDate;
		function calculateFromDate(){
			
			vm.fromDate.setHours(0,0,0,0);
			vm.fromTime=vm.fromDate.getTime();
		}
		function calculateToDate(){
		
			vm.toDate.setHours(23,59,59,999);
			vm.toTime=vm.toDate.getTime();
		}
		function selectRange(range){
			if(range=='week'){
				var date = new Date();
				date.setHours(23,59,59,999);
				vm.toTime = date.getTime();
				date.setHours(0,0,0,0);
				date.setDate(date.getDate()-7)
				vm.fromTime=date.getTime();
				
			}else if(range=='month'){
				
				var date = new Date();
				date.setHours(23,59,59,999);
				vm.toTime = date.getTime();
				date.setHours(0,0,0,0);
				date.setMonth(date.getMonth()-1);
				vm.fromTime=date.getTime();
			}else if(range=='year'){
				
				var date = new Date();
				date.setHours(23,59,59,999);
				vm.toTime = date.getTime();
				date.setHours(0,0,0,0);
				date.setYear(date.getFullYear()-1);
				vm.fromTime=date.getTime();
			}
		}
		function select2(){
			$(".select2").select2();
		}
		function reportTypeChanged(){
			$scope.reporthide = false;
			 if(vm.reportType=='agent'){
				 if(vm.users.length==0){
					 Report.getUsers('',function(response){
						 vm.users=response;
					 })
				 }
			 }else if(vm.reportType=='geofence'){
				 if(vm.geofences.length==0){
					 Report.getGeofences('',function(response){
						vm.geofences=response;
					 })
				 }
			 }
			 
		}
		
		
		function detailedReport(id){
			Report.getDetailedReport({id:id,fromTime:vm.fromTime,toTime:vm.toTime,reportType:vm.reportType},function(response){
				vm.detailedReportCounts = response;

				if(vm.reportType=='agent'){
				        var map = new google.maps.Map(document.getElementById('map_canvas'), {
				          zoom: 13,
				          center: new google.maps.LatLng( response.locations[0].lat, response.locations[0].lng)
				        });
				        var flightPath = new google.maps.Polyline({
			    	          path: response.locations,
			    	          geodesic: true,
			    	          strokeColor:"#425cf4",
			    	          strokeOpacity: 1,
			    	          strokeWeight: 2,
			    	        icons: [{
			    	            icon: {
			    	            	path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW
			    	            },
			    	          offset: '25px',
			                repeat: '50px'
			    	          }]
			    	        });
				        var startMarker =new google.maps.Marker({
				            map:  map,
				            position: new google.maps.LatLng(response.locations[0].lat, response.locations[0].lng),
				            title: 'Started here'
				        });
				        startMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
				        var endMarker =new google.maps.Marker({
				            map:  map,
				            position: new google.maps.LatLng(response.locations[response.locations.length-1].lat, response.locations[response.locations.length-1].lng),
				            title: 'Ended here'
				        });
				        endMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/red-dot.png');
				        flightPath.setMap(map);
				        setTimeout(function(){
			 		    	   google.maps.event.trigger(map, "resize");
			 		    	   map.setCenter(new google.maps.LatLng( response.locations[0].lat, response.locations[0].lng));
			 		       },100); 
				        
				}else if(vm.reportType=='geofence'){
					var coordicates=JSON.parse(response.coordinates);
				        var map = new google.maps.Map(document.getElementById('map_canvas'), {
				          zoom: 13,
				          center: new google.maps.LatLng( coordicates[0].lat, coordicates[0].lng)
				        });
				        
				       var polygon= new google.maps.Polygon({
     	                path:coordicates,
     	                strokeColor: "#ff0000",
     	                strokeOpacity: 0.8,
     	                strokeWeight: 2,
     	                fillColor: "#ff0000",
     	                fillOpacity: 0.3,
     	                editable: true
     	            })
				       polygon.setMap(map);
				       setTimeout(function(){
		 		    	   google.maps.event.trigger(map, "resize");
		 		    	   map.setCenter(new google.maps.LatLng( coordicates[0].lat, coordicates[0].lng));
		 		       },100);
				}

			})
		}
		 

		vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.modifiedDate = false;
        vm.datePickerOpenStatus.serviceDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        
    }
   
})();
