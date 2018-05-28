var app = angular.module('indexApp',['googlechart','ngGoogleCharts']);
app.controller('indexController',function($scope){
    $scope.myChartObject = {}; 
    //window.alert(new Date("2018-05-10 08:12:42"));
    $scope.myChartObject.type = "ColumnChart";
    $scope.onions = [
        {v: "Onions"},
        {v: 3},
    ];

    $scope.myChartObject.data = {"cols": [
        {label:"time",type:"date"},    
        {label: "Slices", type: "number"}
    ], "rows": [
        {c: [
            {v:new Date("2018-05-10 08:12:42")},    
            {v: 3}
        ]},
        {c: [
            {v:new Date("2018-05-10 08:13:42")},    
            {v: 31}
        ]},
        {c: [
            {v:new Date("2018-05-10 08:14:42")},    
            {v: 1}
        ]},
        {c: [
            {v:new Date("2018-05-10 08:15:42")},    
            {v: 2}
        ]}
    ]};

    $scope.myChartObject.options = {
        'title': 'How Much Pizza I Ate Last Night'
    };
});