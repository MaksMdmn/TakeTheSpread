$(document).ready(function() {
    var myjsondata = [{
        setting: "commission",
        value: "0.4"
    }, {
        setting: "PnL",
        value: "345$"
    }, {
        setting: "deals",
        value: "332"
    }];
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

    var settingWdth = jQuery('.settings').width();
    var indicatorWdth = jQuery('.indicators').width();
    var tableFeatureWdth = jQuery('.table-feature').width();


    jQuery('#settingsTable').jqGrid({
        // url: 'example.php',
        data: myjsondata,
        datatype: 'local', //'xml'
        // mtype: 'POST',
        height: 150,
        width: settingWdth,
        rowNum: 10,
        rowList: [10, 20, 30],
        colNames: ['NAME', 'VALUE'],
        colModel: [{
            name: 'setting',
            index: 'setting',
            width: 55,
            editable: true
        }, {
            name: 'value',
            index: 'value',
            width: 90,
            editable: true
        }],
        viewrecords: true,
        caption: 'SETTINGS'
    });

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



    // var data = {
    //     labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'],
    //     series: [
    //         [5, 2, 4, 2, 0],
    //         [2, 7, 2, 18, 8],
    //         [8, 7, 6, 3, -5]
    //     ]
    // };
    // var options = {
    //     // width: 300,
    //     // height: 200

    // };

    // new Chartist.Line('.chart-feature', data, options);
});



// jQuery(document).ready(function() {
//     jQuery('#message-bar').css('background-color', '#FFA500');
//     jQuery('#connection-bar').css('background-color', '#00FF00');
// });



// $(document).ready(function() {
//       // $('#settingsTable').addToJSON(myjsondata);
//       // myjsondata = JSON.parse(myjsondata);
//       var jsonObj = JSON.parse(myjsondata);
//       alert(jsonObj.settings[0].sName + " " + jsonObj.settings[0].sValue + '\r\n' +
//             jsonObj.settings[1].sName + " " + jsonObj.settings[1].sValue + '\r\n' +
//             jsonObj.settings[2].sName + " " + jsonObj.settings[2].sValue);
// });