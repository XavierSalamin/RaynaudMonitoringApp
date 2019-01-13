'use strict';

angular.module('frontend-new', [
	'ngSanitize',
	'frontend-new.routes',
	'ngMaterial'
])

.run([
	'$rootScope',
	'$http',
function ($scope, $http) {

	// Expose app version info
	$http.get('version.json').success(function (v) {
		$scope.version = v.version;
		$scope.appName = v.name;
	});
}]);

angular.module('frontend-new.templates', []);
