function runDataUpdater() {
    var maxDataLength = 0.5 * 60; // minutes here <----

    var chartData = defineStartChartData();
    var startPriceValues = $.makeArray(updatePrices());
    var maxPrice = defineMaxPrice();
    var nearFarColors = defineNearFarColors(startPriceValues[0], startPriceValues[1]);
    var acceptableKeyValues = [6, 7, 8, 9, 10, 11, 12, 13, 14];

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
            editable: false
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
        loadonce: true
    });

    $('#orderTable').jqGrid({
        url: 'orders',
        datatype: 'json',
        mtype: 'GET',
        height: 350,
        autowidth: true,
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


    //--------------------------------------------------------------------


    setInterval(function() {
        updatePrices();
        updateIndicators();
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

                g.updateOptions({
                    'file': chartData
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

    function defineMaxPrice() {
        return startPriceValues[0] * 2; // 100% increase of nearest futures price
    }

    function defineStartChartData() {
        return [];
    }

    function defineNearFarColors(priceNear, priceFar) {
        var result = [];
        if (priceNear <= priceFar) {
            result[0] = '#008000';
            result[1] = '#DC143C';
        } else {
            result[0] = '#DC143C';
            result[1] = '#008000';
        }

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