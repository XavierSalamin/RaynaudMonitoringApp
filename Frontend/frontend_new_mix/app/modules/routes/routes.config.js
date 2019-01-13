angular.module('frontend-new.routes')

.config([
	'$routeProvider',
function ($routeProvider) {
	'use strict';

	$routeProvider.otherwise({redirectTo: '/'});
}]);
