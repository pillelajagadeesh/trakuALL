(function() {
    'use strict';

    angular
        .module('trakeyeApp', [
            'ngStorage', 
            'tmh.dynamicLocale',
            'pascalprecht.translate', 
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'angularUtils.directives.dirPagination'
//            'uiGmapgoogle-maps'
        ])
        .run(run)
        //Added for Before and After login design changes
       .run([
           '$log', '$rootScope', '$window', '$state', '$location','$timeout',
           function($log, $rootScope, $window, $state, $location, $timeout) {
               $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
            	   
            	   if (toState.url == "/") {
            	        $('body').addClass('layout-top-nav').removeClass('sidebar-mini tryitbg');
            	        $('.main-footer').addClass('home-footer');
            	    } else {
            	        $('body').removeClass('layout-top-nav').addClass('sidebar-mini');
            	        $('.content-wrapper').removeClass('home-container tryitbg');
            	        $('.main-footer').removeClass('home-footer');
            	    }
            	   if (toState.url == "/login" || toState.url == "/reset/request" ||toState.url == "/reset/finish?key" || toState.url == "/accessdenied") {
            		   $('body').addClass('layout-top-nav').removeClass('sidebar-mini');  
           	        	$('.content-wrapper').addClass('main-wrapper');
           	        	$('.main-footer').addClass('login-footer');
            	   } else { 
           	        	$('.content-wrapper').removeClass('main-wrapper tryitbg').addClass('sidebar-mini');
           	        	$('.main-footer').removeClass('login-footer');
           	    }
				if (toState.url == "/register") {
            	        $('body').addClass('layout-top-nav tryitbg').removeClass('sidebar-mini');  
           	        	$('.content-wrapper').addClass('main-wrapper tryitbg');
            	        $('.main-footer').addClass('home-footer');
            	    } else {
						 $('body').removeClass('tryitbg').addClass('sidebar-mini');  
            	        $('.content-wrapper').removeClass('main-wrapper tryittrans').addClass('sidebar-mini');
            	        $('.main-footer').removeClass('home-footer');
            	    }
					//Added for redirecting to login page
					if (toState.url == "/accessdenied") {
						$timeout(function(){
							$state.go('login');
						},5000);
					}

               });
           }
        ])
        //Added for upload required functionality
        .directive('validFile',function(){
		  return {
		    require:'ngModel',
		    link:function(scope,el,attrs,ngModel){
		      //change event is fired when file is selected
		      el.bind('change',function(){
		        scope.$apply(function(){
		          ngModel.$setViewValue(el.val());
		          ngModel.$render();
		        })
		      })
		    }
		  }
		});
        

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
window.addEventListener('load',function(){

	  var script = document.createElement('script');
	  script.type = 'text/javascript';
	  script.src = '//maps.googleapis.com/maps/api/js?key=AIzaSyDLVWFeNy9E2WZLGCHZHvFyvl1ZtRUble0&libraries=geometry,drawing';
	  document.body.appendChild(script);
	});