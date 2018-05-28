const app = angular.module('warrantApp',[]);
app.controller('warrantController',($scope,$http,$interval)=>{
    $scope.pages=[true,false];
    $scope.warrantCode="CQXW";
    $scope.warrantName="DBS";
    $scope.sensitivity="";
    $scope.allWarrantSettings;
    $scope.onHome=function(){
        $scope.pages=[true,false];
        $scope.getAllWarrantSettings();
    };
    $scope.onSubscribe=function(){
        $scope.pages=[false,true];
    };
    function initVariables(){
        $scope.warrantCode="";
        $scope.warrantName="";
        $scope.sensitivity="";
    }
    $scope.onAdd=function(){
        if($scope.warrantCode==""||$scope.warrantName==""||$scope.sensitivity==""||$scope.sensitivity==0){
            window.alert("Insert vaild inputs");
            return;
        }
        $scope.warrantCode=$scope.warrantCode.toUpperCase();
        $scope.warrantName=$scope.warrantName.toUpperCase();
        $http({
           method:"POST",
           url:"AddWarrantServlet",
           data:{
               warrantCode:$scope.warrantCode,
               warrantName:$scope.warrantName,
               sensitivity:$scope.sensitivity
           }
        }).then((promise)=>{
            console.log(promise);
            $scope.getAllWarrantSettings();
        }).catch((e)=>{
            console.log(e);
        });
        initVariables();
    };
    $scope.getAllWarrantSettings=function(){
        $http({
            method:"POST",
            url:"GetWarrantSettingsServlet"
            
        }).then((promise)=>{
            console.log(promise);
            $scope.allWarrantSettings=promise.data;
        }).catch((e)=>{
            console.log(e);
        });
    };
    $scope.onClick=function(warrantName,warrantCode){
        const popup = window.open("warrantobserve.html");
        popup.warrantName=warrantName;
        popup.warrantCode=warrantCode;
    };
    
    $scope.onRemove=function(warrantName,warrantCode,sensitivity){
        $http({
            method:"POST",
            url:"RemoveWarrantServlet",
            data: {
                warrantCode:warrantCode,
                warrantName:warrantName,
                sensitivity:sensitivity
            }
        }).then((promise)=>{
            $scope.getAllWarrantSettings();
        });
            
    };
    
    $interval($scope.getAllWarrantSettings,60000);
});