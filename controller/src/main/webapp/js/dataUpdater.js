function runDataUpdater() {
    var maxDataLength = 2 * 60; // minutes here <----

    var chartData = defineStartChartData();
    var maxPrice = 100;
    var nearFarColors = defineNearFarColors();

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

    $('#settingsTable').jqGrid({
        url: 'settings',
        datatype: 'json',
        mtype: 'GET',
        height: '100%',
        autowidth: true,
        hiddengrid: true,
        rowNum: 20,
        scrollOffset: 0,
        caption: 'SETTINGS',
        colNames: ['Setting', 'Value'],
        colModel: [{
            name: 'name',
            index: 'name',
            width: 120,
            editable: false,
        }, {
            name: 'value',
            index: 'value',
            width: 50,
            editable: true
        }],
        cellEdit: true,
        cellsubmit: 'clientArray',
        ajaxGridOptions: {
            async: false
        },
        afterSaveCell: function(rowid, cellname, value, iRow, iCol) {
            setNewSettings($('#settingsTable'), rowid, value, iRow, iCol);
        }
    });

    $('#indicatorTable').jqGrid({
        url: 'indicator',
        datatype: 'json',
        mtype: 'GET',
        caption: 'INDICATORS',
        height: '100%',
        autowidth: true,
        rowNum: 1,
        scrollOffset: 0,
        colNames: ['POS_N', 'POS_F', 'SPOT_N', 'SPOT_F', 'CALC_SPRD', 'MRT_SPRD', 'MRT_DEV', 'CASH', 'BUYPW', 'D', 'COMMIS', 'PnL'],
        colModel: [{
            name: 'pos_n',
            index: 'pos_n',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'pos_f',
            index: 'pos_f',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'spot_n',
            index: 'spot_n',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'spot_f',
            index: 'spot_f',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'calcSpr',
            index: 'calcSpr',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'curSpr',
            index: 'curSpr',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'curDev',
            index: 'curDev',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'cash',
            index: 'cash',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'buyPw',
            index: 'buyPw',
            width: 55,
            editable: true
        }, {
            name: 'deals',
            index: 'deals',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'commis',
            index: 'commis',
            width: 55,
            editable: true,
            align: 'center'
        }, {
            name: 'pnl',
            index: 'pnl',
            width: 55,
            editable: true,
            align: 'center'
        }]
    });

    $('#orderTable').jqGrid({
        url: 'orders',
        datatype: 'json',
        mtype: 'GET',
        height: 350,
        autowidth: true,
        rowNum: 1000,
        rowList: [1000],
        caption: 'ORDERS',
        scrollOffset: 0,
        colNames: ['Date', 'Term', 'Deal', 'Type', 'Price', 'Size', 'PriceFilled', 'Filled', 'Status'],
        colModel: [{
            name: 'date',
            index: 'date',
            width: 80,
            editable: false,
            align: 'center'
        }, {
            name: 'term',
            index: 'term',
            width: 30,
            editable: false,
            align: 'center'
        }, {
            name: 'deal',
            index: 'deal',
            width: 30,
            editable: false,
            align: 'center'
        }, {
            name: 'type',
            index: 'type',
            width: 30,
            editable: false,
            align: 'center'
        }, {
            name: 'price',
            index: 'price',
            width: 40,
            editable: false,
            align: 'center'
        }, {
            name: 'size',
            index: 'size',
            width: 30,
            editable: false,
            align: 'center'
        }, {
            name: 'priceFilled',
            index: 'priceFilled',
            width: 60,
            editable: false,
            align: 'center'
        }, {
            name: 'sizeFilled',
            index: 'sizeFilled',
            width: 40,
            editable: false,
            align: 'center'
        }, {
            name: 'status',
            index: 'status',
            width: 50,
            editable: false,
            align: 'center'
        }],
        pager: 'orderPager',
    });


    //--------------------------------------------------------------------


    setInterval(function() {
        updatePrices();
        updateIndicators();
        updateOrders();
    }, 1000);

    $(window).resize(function() {
        setNewTablesWidth();
    });


    //--------------------------------------------------------------------

    function updatePrices() {
        var tempArr;
        $.ajax({
            url: 'chart',
            type: 'GET',
            success: function(data) {
                if (chartData.length > maxDataLength) {
                    chartData.splice(0, 1);
                }

                tempArr = $.makeArray(data);
                tempArr.unshift(new Date());

                for (var i = 0; i < tempArr.length; i++) {
                    if (tempArr[i] === 0) {
                        tempArr[i] = null;
                    }
                }

                chartData.push(tempArr);

                // var spr = Math.abs(tempArr[1] - tempArr[2]);
                // var dwn = (tempArr[1] <= tempArr[2]) ? tempArr[1] : tempArr[2];
                // var up = (dwn === tempArr[1]) ? tempArr[2] : tempArr[1];
                var dwn = 0.6;
                var up = 0.6;
                g.updateOptions({
                    'file': chartData,
                    'valueRange': [tempArr[1] - dwn, tempArr[2] + up]
                });
            },
        });

        return tempArr;
    }


    function updateIndicators() {
        $.ajax({
            url: 'indicator',
            type: 'GET',
            dataType: 'json',
            success: function(data) {
                var tempObj = data[0];
                for (var prop in tempObj) {
                    if (tempObj.hasOwnProperty(prop)) {
                        $('#indicatorTable').jqGrid("setCell", 1, prop, tempObj[prop]);
                    }
                }
            }
        });
    }

    function updateOrders() {
        $.ajax({
            url: 'orders',
            type: 'GET',
            dataType: 'json',
            success: function(data) {
                if (data !== "") {
                    $('#orderTable')[0].addJSONData(data);
                }
            }
        });
    }

    function defineStartChartData() {
        return [];
    }

    function defineNearFarColors() {
        var result = [];
        result[0] = '#00FFFF';
        result[1] = '#9ACD32';
        return result;
    }

    function setNewTablesWidth() {
        var setWdt = $('.settings').width();
        var indWdt = $('.indicators').width();
        var ordWdt = $('.table-feature').width();
        var prcWdt = $('chart-feature').width();

        $('#orderTable').setGridWidth(ordWdt);
        $('#indicatorTable').setGridWidth(indWdt);
        $('#settingsTable').setGridWidth(setWdt);

        g.update({
            'width': prcWdt
        });

    }

    function setNewSettings(grid, rowid, value, iRow, iCol) {
        var name = grid.getCell(rowid, 'name');
        var answer;
        $.ajax({
            url: 'settingschange',
            type: 'GET',
            data: {
                key: name,
                value: value
            },
            datatype: 'text',
            async: false,
            success: function(response) {
                answer = response;
            }
        });


        if (answer.localeCompare(value) !== 0) {
            grid.restoreCell(iRow, iCol);
        }
    }
}