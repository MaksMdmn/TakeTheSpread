 $(document).ready(function() {
     var maxDataLength = 5 * 60;
     var data = defineStartChartData();
     var maxPrice = defineMaxPrice();
     var nearFarColors = defineNearFarColors();

     defineNearFarColors(40, 60);

     var g = new Dygraph($('.chart-feature').get(0), data, {
         animatedZooms: false,
         drawPoints: true,
         showRoller: false,
         showLabelsOnHighlight: false,
         isZoomedIgnoreProgrammaticZoom: true,
         valueRange: [0, maxPrice],
         xRangePad: 20,
         labels: ['Time', 'near', 'far', 'n_Deals', 'f_Deals'],
         'near': {
             pointSize: 2,
             color: '#8A2BE2'
         },
         'far': {
             pointSize: 2,
             color: '#1E90FF'
         },
         'n_Deals': {
             strokeWidth: 0.0,
             pointSize: 5,
             color: nearFarColors[0]
         },
         'f_Deals': {
             strokeWidth: 0.0,
             pointSize: 5,
             color: nearFarColors[1]
         }

     });

     setInterval(testUpd, 2000);

     function updateChartData(updData) {
         if (data.length > maxDataLength) {
             data.splice(0, 1);
             data.push(updData);
         } else {
             data.push(updData);
         }

         g.updateOptions({
             'file': data
         });
     }

     function defineMaxPrice() {
         return 170.0;
     }

     function defineStartChartData() {
         return [];
     }

     function defineNearFarColors(priceNear, priceFar) {
         var result = [];
         if (priceNear <= priceFar) {
             result[0] = '#00FF7F';
             result[1] = '#800000';
         } else {
             result[0] = '#800000';
             result[1] = '#00FF7F';
         }

         return result;
     }

     function testUpd() {
         updateChartData([new Date(), getRandomInt(20, maxPrice * 0.5), getRandomInt(maxPrice * 0.5, maxPrice * 0.8), getRandomDeal(), getRandomDeal()]);
     }

     function getRandomInt(min, max) {
         return Math.floor(Math.random() * (max - min + 1)) + min;
     }

     function getRandomPrice() {
         return getRandomInt(1, maxPrice * 0.7);
     }

     function getRandomDeal() {
         var temp = getRandomInt(0, 1) * getRandomInt(10, maxPrice * 0.7);
         if (temp === 0) {
             return null;
         } else {
             return temp;
         }
     }
 });
 // setInterval(function() {
 //     var x = new Date(); // current time
 //     data.push([x, Math.random(), Math.random()]);
 //     g.updateOptions({
 //         'file': data
 //     });
 // }, 1000);

 // var t = new Date();
 // for (var i = 10; i >= 0; i--) {
 //     var x = new Date(t.getTime() - i * 1000);
 //     // data.push([x, Math.random(), Math.random()]);

 //     if (i === 4) {
 //         data.push([x, Math.random(), Math.random(), Math.random()]);
 //     } else {
 //         data.push([x, Math.random(), Math.random(), null]);
 //     }
 // }



 // var bid_nArr = [];
 // var bid_fArr = [];
 // var ask_nArr = [];
 // var ask_fArr = [];
 // var deal_nArr = [];
 // var deal_fArr = [];
 // var xScaleArr = [];
 // var myChart;

 // $(document).ready(function() {
 //     var data = {
 //         labels: xScaleArr,
 //         series: [bid_nArr, bid_fArr, ask_nArr, ask_fArr, {
 //             name: 'deal1',
 //             data: deal_nArr
 //         }, {
 //             name: 'deal2',
 //             data: deal_fArr
 //         }]
 //     };
 //     var options = {
 //         fullWidth: true,
 //         chartPadding: {
 //             right: 40
 //         }
 //     };
 //     myChart = new Chartist.Line('.chart-feature', data, options);

 // });



 // var updateTimeMls = 500;
 // setInterval(dataUpdRand, updateTimeMls);


 // function dataUpd(bidN, bidF, askN, askF, dealN, dealF) {
 //     bid_nArr.push(bidN);
 //     bid_fArr.push(bidF);
 //     ask_nArr.push(bidN);
 //     ask_fArr.push(askF);
 //     deal_nArr.push(dealN);
 //     deal_fArr.push(dealF);

 //     xScaleArr.push(new Date().toTimeString());
 //     myChart.data.labels = xScaleArr;
 //     myChart.data.series = [bid_nArr, bid_fArr, ask_nArr, ask_fArr, deal_nArr, deal_fArr];
 //     myChart.update();
 // }



 // function dataUpdRand() {
 //     var deal = getRandDeal();
 //     dataUpd(getRandomPrice(), getRandomPrice(), getRandomPrice(), getRandomPrice(), deal, deal - 3);
 // }