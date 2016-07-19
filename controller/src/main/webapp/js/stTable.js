var mygrid = $('#settingsTable');
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

jQuery(document).ready(function() {
      jQuery('#settingsTable').jqGrid({
            // url: 'example.php',
            data: myjsondata,
            datatype: 'local', //'xml'
            // mtype: 'POST',
            height: 150,
            width: 250,
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
            // pager: '#settingsPager',
            viewrecords: true,
            caption: 'Active settings'
      });

      // for (var i = 0; i <= myjsondata.length; i++) {
      //       $(mygrid).addRowData(i + 1, myjsondata[i]);
      // }
});

// $(document).ready(function() {
//       // $('#settingsTable').addToJSON(myjsondata);
//       // myjsondata = JSON.parse(myjsondata);
//       var jsonObj = JSON.parse(myjsondata);
//       alert(jsonObj.settings[0].sName + " " + jsonObj.settings[0].sValue + '\r\n' +
//             jsonObj.settings[1].sName + " " + jsonObj.settings[1].sValue + '\r\n' +
//             jsonObj.settings[2].sName + " " + jsonObj.settings[2].sValue);
// });