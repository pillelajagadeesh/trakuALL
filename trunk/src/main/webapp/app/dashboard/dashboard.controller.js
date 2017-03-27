(function() {
    'use strict';

    angular
        .module('trakeyeApp')
        .controller('DashboardController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService','AlertService','ParseLinks', '$state','$rootScope','pagingParams', 'paginationConstants','DashboardService','$filter','UserListAndDistance','UserListAndDistanceSearch', '$interval'];

    function HomeController ($scope, Principal, LoginService,AlertService,ParseLinks, $state,$rootScope,pagingParams, paginationConstants,DashboardService,$filter,UserListAndDistance,UserListAndDistanceSearch, $interval) {
        var vm = this;  
        
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.userslist=[];
        vm.loadAll=loadAll;
        vm.filterSearch=filterSearch;
        vm.users=users;
        vm.notifications=notifications;
        vm.services=services;
        vm.cases=cases;
        vm.casepriority=casepriority;
        vm.geofence=geofence;
        
        
        loadAll();
        users();
        notifications();
        services();
        cases();
        casepriority();
        geofence();
        
        var updateDashboard= $interval(function(){
        	loadAll();
        	users();
        	 notifications();
             services();
             cases();
             casepriority();
             geofence();
         },300000);
        $scope.$on('$destroy', function() {
            $interval.cancel(updateDashboard);
        });
        
        var date = new Date();
        
        var weekday = new Array(7);
        weekday[0]=  "Sunday";
        weekday[1] = "Monday";
        weekday[2] = "Tuesday";
        weekday[3] = "Wednesday";
        weekday[4] = "Thursday";
        weekday[5] = "Friday";
        weekday[6] = "Saturday";
       vm.day = weekday[date.getDay()];
       
       var month = new Array();
       month[0] = "January";
       month[1] = "February";
       month[2] = "March";
       month[3] = "April";
       month[4] = "May";
       month[5] = "June";
       month[6] = "July";
       month[7] = "August";
       month[8] = "September";
       month[9] = "October";
       month[10] = "November";
       month[11] = "December";
       vm.month = month[date.getMonth()];
       vm.todayDate= date.getDate();
       vm.year = date.getFullYear();
       var dateFormat = 'hh:mm';
       vm.time = $filter('date')(date, dateFormat);
       
       function notifications(){
   		    DashboardService.notifications(function(data){
        		vm.notifications=data.notifications;
        	});
       }	
        	function users(){
        		DashboardService.users(function(data){
	        		vm.users=data.users;
	        		
	        	});
        	}
        	function services(){
        	DashboardService.services(function(data){
        		if(data.serviceType.INPROGRESS || data.serviceType.PENDING || data.serviceType.CLOSED || data.serviceType.CANCELLED)
        		var donut = new Morris.Donut({
                    element: 'service-chart',
                    resize: true,
                    colors: ["#65b8fc", "#D2B48C", "#008000","ff0000"],
                    data: [
                      {label: "In Progress", value: data.serviceType.INPROGRESS},
                      {label: "Pending", value: data.serviceType.PENDING},
                      {label: "Closed", value: data.serviceType.CLOSED},
                      {label: "Cancelled" , value: data.serviceType.CANCELLED}
                    ],
                    hideHover: 'auto'
                  });
        	});
        	}
        	function cases(){
        	DashboardService.cases(function(data){
        		if(data.caseType.NEW >0 || data.caseType.INPROGRESS || data.caseType.PENDING || data.caseType.RESOLVED || data.caseType.CANCELLED)
        		var donut = new Morris.Donut({
                    element: 'case-chart',
                    resize: true,
                    colors: ["#65b8fc", "#D2B48C", "#008000","ff0000","	#FFA500","#ffff00"], 
                    data: [
						{label: "New", value: data.caseType.NEW},
						{label: "In Progress", value: data.caseType.INPROGRESS},
						{label: "Pending", value: data.caseType.PENDING},
						{label: "Assigned", value: data.caseType.ASSIGNED},
						{label: "Resolved", value: data.caseType.RESOLVED},
						{label: "Cancelled" , value: data.caseType.CANCELLED}
						
                    ],
                    hideHover: 'auto'
                  });
        	});
        	}
        	function casepriority(){
        	DashboardService.casesbypriority(function(data){
        		vm.casepriority=data.casePriority;
        		
        	});
        	}
        	
        	function filterSearch(){
        		if(vm.search!=null && vm.search!="" && !angular.isUndefined(vm.search)){
        			UserListAndDistanceSearch.query({login:vm.search},{
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage,
                        sort: sort()
                    }, onSuccess, onError);
        		}else{
        			loadAll();
        		}
        	}
        	
        	function loadAll () {
        		UserListAndDistance.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
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
                 vm.userslist = data;
                 vm.page = pagingParams.page;
             }
             function onError(error) {
                 AlertService.error(error.data.message);
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
            
            function geofence(){
        		
        	DashboardService.geofences(function(data){
        		if(data.geofences.length>0)
        		/*var bar = new Morris.Bar({
                    element: 'bar-chart',
                    resize: true,
                    data: data.geofences,
                    barColors: ['#008000', '#ffff00','#ff0000'],
                    xkey: 'LABEL',
                    ykeys: ['ACTIVE', 'IDLE','INACTIVE'],
                    labels: ['Active Users', 'Idle Users','InActive Users'],
                    hideHover: 'auto'
                  });*/
				  $scope.geo = data.geofences;
					 $scope.active=[];
					 $scope.inactive=[];
					 $scope.idle=[];
					 $scope.label=[];
					angular.forEach($scope.geo, function(value, key){
						$scope.active.push(value.ACTIVE);
						 $scope.inactive.push(value.INACTIVE);
						 $scope.idle.push(value.IDLE);
						 $scope.label.push(value.LABEL);
				   });
					
				Highcharts.chart('geo', {
					chart: {
						type: 'column'
					},
					title: {
						text: ''
					},
					xAxis: {
						categories: $scope.label //Getting labels
					},
					yAxis: {
						min: 0,
						title: {
							text: ''
						},
						stackLabels: {
							enabled: false,
							style: {
								fontWeight: 'bold',
								color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
							}
						}
					},
					credits: {
						  enabled: false
					  },
					tooltip: {
						headerFormat: '<b>{point.x}</b><br/>',
						pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
					},
					plotOptions: {
						column: {
							stacking: 'normal',
							dataLabels: {
								enabled: false,
								/*formatter: function(){
								console.log(this);
								var val = this.y;
								if (val == 0) {
									return '';
								}
								return val;
							},*/
								//color: '#000',
								 style: {
								//textShadow: false 
							}
							}
						}
					},
					series: [{
						name: 'ACTIVE',
						data: $scope.active,
						color:'#4ba704'
					}, {
						name: 'INACTIVE',
						data: $scope.inactive,
						color: '#ee483b'
					}, {
						name: 'IDLE',
						data: $scope.idle,
						color: '#f5a500'
					}]
				});
				  
        	});
            }
		}
    
})();
