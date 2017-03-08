'use strict';

angular.module('anotaai').run(function($http, $rootScope) {
	$http.get('rest/config/firebase').then(
		function(response) {
			
			var config = {
				apiKey: response.data.apiKey,
				authDomain: response.data.authDomain,
				databaseURL: response.data.databaseURL,
				storageBucket: response.data.storageBucket,
				messagingSenderId: response.data.messagingSenderId
			};
			
			firebase.initializeApp(config);
			
			firebase.auth().onAuthStateChanged(function(user) {
				if (user) {
					console.log("User is signed in.", user);
				} else {
					console.log("No user is signed in.");
				}
			});
			
		},
		$rootScope.defaultErrorCallback);
});