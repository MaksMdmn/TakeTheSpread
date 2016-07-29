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


     var settingWdth = jQuery('.settings').width();
     var indicatorWdth = jQuery('.indicators').width();
     var tableFeatureWdth = jQuery('.table-feature').width();


     jQuery('#settingsTable').jqGrid({
         url: 'update',
         datatype: 'json',
         mtype: 'POST',
         height: 150,
         width: settingWdth,
         rowNum: 10,
         rowList: [10, 20, 30],
         viewrecords: true,
         caption: 'SETTINGS',
         colNames: ['Setting', 'Val'],
         colModel: [{
             name: 'name',
             index: 'name',
             width: 120,
             editable: true
         }, {
             name: 'value',
             index: 'value',
             width: 50,
             editable: true
         }],

     });


     var myjsondata2 = [{
         profit: "+3444$",
         deals: "321",
         commis: "-23$",
         status: "WORKING",
         dich1: "TUT",
         dich2: "ZDES",
         dich3: "YA",
         dich4: "AA?!"
     }];

     var myjsondata3 = [{
         id: "1",
         deal: "BUY",
         type: "MARKET",
         price: "46.23",
         quantity: "12",
         filled: "12",
         pos_after: "32",
         result: "-43$"
     }, {
         id: "2",
         deal: "BUY",
         type: "MARKET",
         price: "46.23",
         quantity: "12",
         filled: "12",
         pos_after: "32",
         result: "-43$"
     }, {
         id: "3",
         deal: "BUY",
         type: "MARKET",
         price: "46.23",
         quantity: "12",
         filled: "12",
         pos_after: "32",
         result: "-43$"
     }];

     jQuery('#indicatorTable').jqGrid({
         data: myjsondata2,
         datatype: 'local', //'xml'
         height: 40,
         width: indicatorWdth,
         rowNum: 10,
         rowList: [10, 20, 30],
         colNames: ['PROFIT', 'DEALS', 'COMMIS', 'STATUS', 'DICH1', 'DICH2', 'DICH3', 'DICH4'],
         colModel: [{
             name: 'profit',
             index: 'profit',
             width: 55,
             editable: true
         }, {
             name: 'deals',
             index: 'deals',
             width: 55,
             editable: true
         }, {
             name: 'commis',
             index: 'commis',
             width: 55,
             editable: true
         }, {
             name: 'status',
             index: 'status',
             width: 55,
             editable: true
         }, {
             name: 'dich1',
             index: 'dich1',
             width: 55,
             editable: true
         }, {
             name: 'dich2',
             index: 'dich2',
             width: 55,
             editable: true
         }, {
             name: 'dich3',
             index: 'dich3',
             width: 55,
             editable: true
         }, {
             name: 'dich4',
             index: 'dich4',
             width: 55,
             editable: true
         }],
         viewrecords: true,
         caption: 'INDICATORS'
     });

     jQuery('#orderTable').jqGrid({
         data: myjsondata3,
         datatype: 'local',
         height: 320,
         width: tableFeatureWdth,
         rowNum: 20,
         rowList: [10, 20, 30],
         colNames: ['ID', 'DEAL', 'TYPE', 'PRICE', 'QUANTITY', 'FILLED', 'POSITION_AFTER', 'RESULT'],
         colModel: [{
             name: 'id',
             index: 'id',
             width: 55,
             editable: false
         }, {
             name: 'deal',
             index: 'deal',
             width: 55,
             editable: false
         }, {
             name: 'type',
             index: 'type',
             width: 70,
             editable: false
         }, {
             name: 'price',
             index: 'price',
             width: 70,
             editable: false
         }, {
             name: 'quantity',
             index: 'quantity',
             width: 55,
             editable: false
         }, {
             name: 'filled',
             index: 'filled',
             width: 55,
             editable: false
         }, {
             name: 'pos_after',
             index: 'pos_after',
             width: 55,
             editable: false
         }, {
             name: 'result',
             index: 'result',
             width: 70,
             editable: false
         }],
         pager: 'orderPager',
         viewrecords: true,
         caption: 'ORDERS'
     });

 });