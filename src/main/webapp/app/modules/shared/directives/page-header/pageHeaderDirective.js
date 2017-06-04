(function () {
    'use strict';

    /**
     * @ngdoc function
     * @name app.controller:pageHeaderDirective
     * @description
     * # pageHeaderDirective
     * Directive of page header
     */

    angular
        .module('under-attack')
        .directive('pageHeader', pageHeader);
    function pageHeader() {
        var directive = {
            restrict: 'E',
            controller: 'PageHeaderCtrl',
            controllerAs: '$pageHeader',
            templateUrl: 'app/modules/shared/directives/page-header/page-header.html'
        };
        return directive;
    }
})();
