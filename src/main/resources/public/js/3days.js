/**
 * Created by dimaMJ on 22.05.2016.
 */

var regApp = angular.module("3daysModule", []);
regApp.controller("3daysCtrl", function ($scope, $http) {

    this.city = "";
    this.loadGif = false;

    this.showPartOfDay = function(index){
        switch (index){
            case 0: return "Ночь";
            case 1: return "Утро";
            case 2: return "День";
            case 3: return "Вечер"
        }
    };


    this.searchWeather = function () {
        this.loadGif = true;

        $http({
            url: "/search-weather",
            method: "GET",
            params: {city: this.city}
        }).success(function (res) {
            if (res.error) {
                $scope.error = res.error;
            } else {
                $scope.vm.loadGif = false;
                $scope.result = res.result;
            }
        });
    }

});
