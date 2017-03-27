(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('ReportController', ReportController)
        

    ReportController.$inject = ['$scope', '$state', "Report", '$rootScope','$timeout','$window','$filter'];

    function ReportController ($scope, $state, Report,  $rootScope,$timeout, $window, $filter) {
    	var vm = this;
		vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.reportTypeChange = reportTypeChanged;
    	vm.users=[];
    	vm.batteryusers = [];
		vm.geofences=[];
		$scope.reporthide = false;
		vm.select2=select2;
		 var dateFormat = 'dd EEE HH:mm';
		 var date = "dd EEE";
		 vm.today = today;
		 vm.today();
		 vm.select2();
		 Report.getUsers('',function(response){
			 vm.batteryusers=response;
			 
		 })
		 
		  $(document).ready(function(){
			$scope.generatePDF = function(){
				kendo.drawing.drawDOM($("#getpdf")).then(function(group) {
				
				    kendo.drawing.pdf.saveAs(group, "Geofence_"+vm.detailedReportCounts.name+".pdf");
				  });
			};
			$scope.generateagentPDF = function(){
				kendo.drawing.drawDOM($("#getagentpdf")).then(function(group) {
				    kendo.drawing.pdf.saveAs(group, "Agent_"+vm.detailedReportCounts.login+".pdf");
				  });
			};
			//create pdf
			

			// create canvas object
			
		 });
		 
	 function today () {
            var today = new Date();
            vm.toDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());
            vm.fromDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        }	
		
		
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
        
        $scope.batteryreport = function(){
        	vm.fromDate.setHours(0,0,0,0);
			vm.fromTime=vm.fromDate.getTime();
			vm.toDate.setHours(23,59,59,999);
			vm.toTime=vm.toDate.getTime();
        	 
        	Report.getbatteryReport(vm.userid,vm.fromTime,vm.toTime,function(response){
        		 $scope.batteryline = response;
    		     $scope.battery=[];
					 $scope.createTime=[];
					angular.forEach($scope.batteryline, function(value, key){
						$scope.battery.push(value.batteryValue);
						$scope.createTime.push($filter('date')(value.createTime,dateFormat));
				   });
					
					
        		Highcharts.chart('battery_graph', {

        		    title: {
        		        text: 'Battery Usage Report'
        		    },

        		    subtitle: {
        		        text: ''
        		    },

        		    yAxis: {
        		        title: {
        		            text: 'Battery Percentage'
        		        }
        		    },
        		    legend: {
        		        layout: 'vertical',
        		        align: 'right',
        		        verticalAlign: 'middle'
        		    },
        		    xAxis: {
        	            categories: $scope.createTime
        	        },

        		    

        		    series: [{
        		        name: ' ',
        		        data: $scope.battery
        		    }]

        		});
        		
        		
        	})
        }
		
		
		$scope.distancereport = function(){
        	vm.fromDate.setHours(0,0,0,0);
			vm.fromTime=vm.fromDate.getTime();
			vm.toDate.setHours(23,59,59,999);
			vm.toTime=vm.toDate.getTime();
        	 
        	Report.getDistanceReport(vm.userid,vm.fromTime,vm.toTime,function(response){
        		 console.log(vm.fromTime);
				 $scope.distanceData = response;
    		     $scope.distance=[];
				 $scope.date=[];
					 
					angular.forEach($scope.distanceData, function(value, key){
						$scope.distance.push(value.distance);
						
						//$scope.date.push($filter('date')(value.date,"dd EEE"));
						$scope.date.push(value.date);
						//console.log(value.date);
						//console.log($filter('date')(value.date,dateFormat));
				   });
					
				
					console.log($scope.distanceData);
					console.log(JSON.stringify($scope.distanceData));
					console.log($scope.distance)
					console.log($scope.date)
        		    Highcharts.chart('distance_graph', {

        		    title: {
        		        text: 'Distance Travelled Report'
        		    },

        		    subtitle: {
        		        text: ''
        		    },

        		    yAxis: {
        		        title: {
        		            text: 'Distance Travelled in KM'
        		        }
        		    },
        		    legend: {
        		        layout: 'vertical',
        		        align: 'right',
        		        verticalAlign: 'middle'
        		    },
        		    xAxis: {
        	            categories: $scope.date
        	        },

        		    

        		    series: [{
        		        name: 'Distance in km',
        		        data: $scope.distance
        		    }]

        		});
        		
        		
        	})
        }
        
    }
   
})();
