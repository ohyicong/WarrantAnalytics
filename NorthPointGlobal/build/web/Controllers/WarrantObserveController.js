const app = angular.module("warrantObserveApp",['ngMaterial','chart.js']);
app.controller("warrantObserveController",($scope,$http,$interval)=>{
    const warrantName=window.warrantName;
    const warrantCode= window.warrantCode;
    const refreshSpeed = window.refreshSpeed;
    $scope.isDone=false;
    $scope.originalDatas;
    $scope.sensitivity=0;
    var originalUnderlyingBidDatas=[],originalUnderlyingAskDatas=[],originalWarrantBidDatas=[],originalWarrantAskDatas=[];    
    var tick,warrantTick;
    var promise,varPredictedWarrantAsk,varPredictedWarrantBid;
    $scope.predictedUnderlyingBidDatas=[],$scope.predictedWarrantBidDatas=[],$scope.predictedUnderlyingAskDatas=[],$scope.predictedWarrantAskDatas=[];
    function getWarrantObservation(){
        console.log("GetWarrantobservation");
        $http({
           method:"POST",
           url:"GetWarrantObservationServlet",
           data:{
               warrantCode:warrantCode,
               refreshSpeed:refreshSpeed,
               warrantName:warrantName
           }   
        }).then((promise)=>{
            console.log("Promise received",promise.data);
            //Reset collections
            $scope.isDone=false;
            originalUnderlyingBidDatas.splice(0,originalUnderlyingBidDatas.length);
            originalUnderlyingAskDatas.splice(0,originalUnderlyingAskDatas.length);
            originalWarrantBidDatas.splice(0,originalWarrantBidDatas.length);
            originalWarrantAskDatas.splice(0,originalWarrantAskDatas.length);
            $scope.predictedUnderlyingBidDatas.splice(0,$scope.predictedUnderlyingBidDatas.length);
            $scope.predictedUnderlyingAskDatas.splice(0,$scope.predictedUnderlyingAskDatas.length);
            $scope.predictedWarrantBidDatas.splice(0,$scope.predictedWarrantBidDatas.length);
            $scope.predictedWarrantAskDatas.splice(0,$scope.predictedWarrantAskDatas.length);
            
            //Assign updated data
            $scope.originalDatas=promise.data.data;
            $scope.sensitivity=promise.data.sensitivity;
            
            angular.forEach($scope.originalDatas,function(obj,key){
                originalUnderlyingBidDatas.push(Number(obj.underlyingBid.toFixed(3)));
                originalUnderlyingAskDatas.push(Number(obj.underlyingAsk.toFixed(3)));
                originalWarrantBidDatas.push(Number(obj.warrantBid.toFixed(3)));
                originalWarrantAskDatas.push(Number(obj.warrantAsk.toFixed(3)));
            });
            //Calculate underlying and warrant tick change
            tick = Number(Math.abs(originalUnderlyingBidDatas[2]-originalUnderlyingBidDatas[3]).toFixed(3));
            for(let i =0;i<=originalWarrantBidDatas.length-1;i++){
                if(originalWarrantBidDatas[i]!=originalWarrantBidDatas[i+1]){
                    warrantTick=Number(Math.abs(originalWarrantBidDatas[i+1]-originalWarrantBidDatas[i]).toFixed(3));
                    break;
                }
            }
            console.log("Warrant Tick:"+warrantTick);
            console.log("Underlying Tick :",tick);
            console.log("Min underlying bid: ", originalUnderlyingBidDatas[0]);
            console.log("Max underlying bid: ", originalUnderlyingBidDatas[originalUnderlyingBidDatas.length-1]);
            
            //Find min and max of bid
            var minBid=originalUnderlyingBidDatas[0]-(Number(tick.toFixed(3))*50);//-50 ticks change from original min
            var maxBid= originalUnderlyingBidDatas[originalUnderlyingBidDatas.length-1];//0 ticks change from original max
            
            //Create a new collection of bidDatas
            for(let i=1;i<=49;i++){
                $scope.predictedUnderlyingBidDatas.push(minBid+(Number(tick.toFixed(3))*i));
            }
            angular.forEach(originalUnderlyingBidDatas,function(obj,key){
                $scope.predictedUnderlyingBidDatas.push(obj);
            });
            for(let i=1;i<=50;i++){
                $scope.predictedUnderlyingBidDatas.push(maxBid+(Number(tick.toFixed(3))*i));
            }
            console.log("Created underlying bid ",$scope.predictedUnderlyingBidDatas);
            
            //Find min and max of ask
            var minAsk=originalUnderlyingAskDatas[0]-(Number(tick.toFixed(3))*50);//-50 ticks change from original min
            var maxAsk= originalUnderlyingAskDatas[originalUnderlyingAskDatas.length-1];//0 ticks change from original max
            //
            //Create a new collection of askDatas
            for(let i=1;i<=49;i++){
                $scope.predictedUnderlyingAskDatas.push(minAsk+(Number(tick.toFixed(3))*i));
            }
            angular.forEach(originalUnderlyingAskDatas,function(obj,key){
                $scope.predictedUnderlyingAskDatas.push(obj);
            });
            for(let i=1;i<=50;i++){
                $scope.predictedUnderlyingAskDatas.push(maxAsk+(Number(tick.toFixed(3))*i));
            }
            
            console.log("Created underlying ask ",$scope.predictedUnderlyingAskDatas);
            
            $scope.predictedUnderlyingBidDatas.reverse();
            $scope.predictedUnderlyingAskDatas.reverse();
            
            var patternBid = getPattern(originalWarrantBidDatas);
            var patternAsk = getPattern(originalWarrantAskDatas);
            
            const predictedWarrantUpBidPrice=predictWarrantUpPrice(originalWarrantBidDatas[originalWarrantBidDatas.length-1],patternBid,warrantTick);
            const predictedWarrantDownBidPrice = predictWarrantDownPrice(originalWarrantBidDatas[0],patternBid,warrantTick);
   
            var predictedWarrantBidDatas=predictedWarrantDownBidPrice.reverse().concat(originalWarrantBidDatas).concat(predictedWarrantUpBidPrice);
            //Here is the predicted warrant ask result
            console.log("Pred warrant bid",predictedWarrantBidDatas);
            
            const predictedWarrantUpAskPrice=predictWarrantUpPrice(originalWarrantAskDatas[originalWarrantAskDatas.length-1],patternAsk,warrantTick);
            const predictedWarrantDownAskPrice = predictWarrantDownPrice(originalWarrantAskDatas[0],patternAsk,warrantTick)
            var predictedWarrantAskDatas=predictedWarrantDownAskPrice.reverse().concat(originalWarrantAskDatas).concat(predictedWarrantUpAskPrice);
            //Here is the predicted warrant ask result
            console.log("Pred warrant ask",predictedWarrantBidDatas);
            
            $scope.predictedWarrantBidDatas=predictedWarrantBidDatas.reverse();
            $scope.predictedWarrantAskDatas=predictedWarrantAskDatas.reverse();
            $scope.isDone=true;
        }).catch((e)=>{
            console.log(e);
        });
    }
    getWarrantObservation();
    $interval(getWarrantObservation,120000);
    
    function predictWarrantUpPrice (maxWarrant,pattern,warrantTick){
        var countPattern=0;
        var count=0;
        var countTick=1;
        var predictedWarrantPrice=[];
        for(let i=0;i<50;i++){
            if(count<pattern[countPattern]-1){
                predictedWarrantPrice.push(maxWarrant+(warrantTick*countTick));
                count++;
            }else{
                predictedWarrantPrice.push(maxWarrant+(warrantTick*countTick));
                countTick++;
                countPattern++;
                if(countPattern>=pattern.length){
                    countPattern=0;
                }
                count=0;
            }
        }
        console.log("predUpWarrantPrice",predictedWarrantPrice);
        return predictedWarrantPrice; 
    }
    function predictWarrantDownPrice (minWarrant,pattern,warrantTick){
        pattern=pattern.reverse();
        var countPattern=0;
        var count=0;
        var countTick=1;
        var predictedWarrantPrice=[];
        for(let i=1;i<50;i++){
            if(count<pattern[countPattern]-1){
                predictedWarrantPrice.push(minWarrant-(warrantTick*countTick));
                count++;
            }else{
                predictedWarrantPrice.push(minWarrant-(warrantTick*countTick));
                countTick++;
                countPattern++;
                if(countPattern>=pattern.length){
                    countPattern=0;
                }
                count=0;
            }
        }
        console.log("predDownWarrantPrice",predictedWarrantPrice);
        return predictedWarrantPrice; 
    }
    function getPattern(warrantDatas){
        var pattern=[],count=1;
        for(let i =0;i<warrantDatas.length;i++){
            if(warrantDatas[i]==warrantDatas[i+1]){
                count++;
            }else{
                pattern.push(count);
                count=1;
            }
        }
        console.log("Pattern: ",pattern);
        return pattern;
    }
      
});