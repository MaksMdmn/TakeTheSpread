 $(document).ready(function() {
     var maxDataLength = 0.5 * 60;
     var settingWdth = jQuery('.settings').width();
     var indicatorWdth = jQuery('.indicators').width();
     var tableFeatureWdth = jQuery('.table-feature').width();

     var chartData = defineStartChartData();
     var startPriceValues = $.makeArray(updatePrices());
     var maxPrice = defineMaxPrice();
     var nearFarColors = defineNearFarColors(startPriceValues[0], startPriceValues[1]);

     var g = new Dygraph($('.chart-feature').get(0), chartData, {
         animatedZooms: false,
         drawPoints: true,
         showRoller: false,
         showLabelsOnHighlight: false,
         isZoomedIgnoreProgrammaticZoom: true,
         valueRange: [0, maxPrice],
         xRangePad: 20,
         labels: ['time', 'near', 'far', 'n_Deals', 'f_Deals'],
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

     jQuery('#settingsTable').jqGrid({
         url: 'update',
         datatype: 'json',
         postData: {
             'whichTable': 'settings'
         },
         mtype: 'GET',
         height: '100%',
         width: settingWdth,
         hiddengrid: true,
         rowNum: 20,
         scrollOffset: 0,
         caption: 'SETTINGS',
         colNames: ['Setting', 'Value'],
         colModel: [{
             name: 'name',
             index: 'name',
             width: 120,
             editable: false
         }, {
             name: 'value',
             index: 'value',
             width: 50,
             editable: true
         }],
     });

     jQuery('#indicatorTable').jqGrid({
         url: 'update',
         datatype: 'json',
         postData: {
             'whichTable': 'indicators'
         },
         mtype: 'GET',
         caption: 'INDICATORS',
         height: '100%',
         width: indicatorWdth,
         rowNum: 1,
         scrollOffset: 0,
         colNames: ['POS_N', 'POS_F', 'SPOT_N', 'SPOT_F', 'CALC_SPRD', 'MRT_SPRD', 'CASH', 'BUYPW', 'D', 'D+', 'D-', 'COMMIS', 'PnL'],
         colModel: [{
             name: 'pos_n',
             index: 'pos_n',
             width: 55,
             editable: true
         }, {
             name: 'pos_f',
             index: 'pos_f',
             width: 55,
             editable: true
         }, {
             name: 'spot_n',
             index: 'spot_n',
             width: 55,
             editable: true
         }, {
             name: 'spot_f',
             index: 'spot_f',
             width: 55,
             editable: true
         }, {
             name: 'calcSpr',
             index: 'calcSpr',
             width: 55,
             editable: true
         }, {
             name: 'curSpr',
             index: 'curSpr',
             width: 55,
             editable: true
         }, {
             name: 'cash',
             index: 'cash',
             width: 55,
             editable: true
         }, {
             name: 'buyPw',
             index: 'buyPw',
             width: 55,
             editable: true
         }, {
             name: 'deals',
             index: 'deals',
             width: 55,
             editable: true
         }, {
             name: 'deals_prf',
             index: 'deals_prf',
             width: 55,
             editable: true
         }, {
             name: 'deals_ls',
             index: 'deals_ls',
             width: 55,
             editable: true
         }, {
             name: 'commis',
             index: 'commis',
             width: 55,
             editable: true
         }, {
             name: 'pnl',
             index: 'pnl',
             width: 55,
             editable: true
         }],
     });

     jQuery('#orderTable').jqGrid({
         url: 'update',
         datatype: 'json',
         postData: {
             'whichTable': 'orders'
         },
         mtype: 'GET',
         height: 300,
         width: tableFeatureWdth,
         rowNum: 10,
         rowList: [10, 20, 30],
         viewrecords: true,
         caption: 'ORDERS',
         scrollOffset: 0,
         colNames: ['DATE', 'INSTR', 'DEAL', 'TYPE', 'PRICE', 'SIZE', 'PRICE_FLD', 'SIZE_FLD', 'STATUS'],
         colModel: [{
             name: 'date',
             index: 'date',
             width: 100,
             editable: false
         }, {
             name: 'instrument',
             index: 'instrument',
             width: 50,
             editable: false
         }, {
             name: 'deal',
             index: 'deal',
             width: 30,
             editable: false
         }, {
             name: 'type',
             index: 'type',
             width: 30,
             editable: false
         }, {
             name: 'price',
             index: 'price',
             width: 40,
             editable: false
         }, {
             name: 'size',
             index: 'size',
             width: 30,
             editable: false
         }, {
             name: 'priceFilled',
             index: 'priceFilled',
             width: 60,
             editable: false
         }, {
             name: 'sizeFilled',
             index: 'sizeFilled',
             width: 50,
             editable: false
         }, {
             name: 'status',
             index: 'status',
             width: 40,
             editable: false
         }],
         pager: 'orderPager'
     });

     setInterval(updatePrices, 1000);

     function updatePrices() {
         $.ajax({
             url: 'update',
             type: 'GET',
             data: {
                 'whichTable': 'prices'
             },
             success: function(data) {
                 if (chartData.length > maxDataLength) {
                     chartData.splice(0, 1);
                 }

                 var tempArr = $.makeArray(data);
                 tempArr.unshift(new Date());

                 for (var i = 0; i < tempArr.length; i++) {
                     if (tempArr[i] === 0) {
                         tempArr[i] = null;
                     }
                 }

                 chartData.push(tempArr);

                 g.updateOptions({
                     'file': chartData
                 });

                 return tempArr;
             },
         });
     }


     function defineMaxPrice() {
         return startPriceValues[0] * 2; // 100% increase of nearest futures price
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

     defineNearFarColors(40, 60);


 });