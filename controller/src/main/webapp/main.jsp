<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Take the spread</title>
    <link type="text/css" rel="stylesheet" media="screen" href="css/jquery-ui-1.12.0/jquery-ui.css"/>
    <link type="text/css" rel="stylesheet" media="screen" href="css/ui.jqgrid.css" />
    <link type="text/css" rel="stylesheet" href="css/main.css" />

    <script type="text/javascript" src="js/jquery-1.12.2.min.js"></script>
    <script type="text/javascript" src="i18n/grid.locale-en.js" ></script>
    <script type="text/javascript" src="js/jquery.jqgrid.min.js"></script>
    <script type="text/javascript" src="js/jsHelper.js"></script>
    <script type="text/javascript" src="js/stTable.js"></script>

</head>
<body>
    <div class="header clearfix">
        <div class="settings">
            <table id="settingsTable"></table>
            <div id="settingsPager"></div>
        </div>

        <div class="login-info">
            <form method="post" action="" class="login">
                <p>
                    <label for="login">Login:</label>
                    <input type="text" name="login" id="login" value="your login">
                </p>

                <p>
                  <label for="password">Password:</label>
                  <input type="password" name="password" id="password" value="4815162342">
              </p>

              <p class="login-submit">
                  <button type="submit" class="login-button"></button>
              </p>
          </form>
      </div>
  </div>

  <!-- <div class="features clearfix">
    FEATURES<br>
    <div class="console-feature">
        CONSOLE_ALL<br>
        <div class="console">
            CONSOLE_LEFT
        </div>
        <div class="console-bars">
            CONSOLE_BARS
        </div>

        <div class="console-stack">
            STACK
        </div>

        <div class="console-info">
            CONSOLE_INFO
        </div>
    </div>

    <div = class="table-feature">
        TABLES
    </div>

    <div class="chart-feature">
        CHART
    </div>
</div>

<div class="footer">
    FOOTER ??? (MB MOST IMPORTANT STATISTIC)
</div> -->

<!-- 
<div id="mainInfo">
    <div id="userInfo">
        <input id="userButton" value = "testButton" type="button" onclick="handleCommand()"/>
        <textarea id="console" type="textarea" cols="40" rows="40" onkeypress="checkCommandSend(event)"> </textarea>
        <textarea id="answers" type="text" readonly wrap="soft"></textarea>
        <textarea id="cmd_stack" type="text" readonly wrap="soft"></textarea>
    </div>
    <div id="tdadingInfo">
        <table id="informTable" >
            <script type="text/javascript">
                jQuery(document).ready(function(){ 
                  jQuery("#informTable").jqGrid({
                        // url:'example.php',
                        datatype: 'xml',
                        mtype: 'GET',
                        colNames:['Inv No','Date', 'Amount','Tax','Total','Notes'],
                        colModel :[ 
                        {name:'invid', index:'invid', width:55}, 
                        {name:'invdate', index:'invdate', width:90}, 
                        {name:'amount', index:'amount', width:80, align:'right'}, 
                        {name:'tax', index:'tax', width:80, align:'right'}, 
                        {name:'total', index:'total', width:80, align:'right'}, 
                        {name:'note', index:'note', width:150, sortable:false} 
                        ],
                        pager: '#pager',
                        rowNum:10,
                        rowList:[10,20,30],
                        sortname: 'invid',
                        sortorder: 'desc',
                        viewrecords: tdue,
                        caption: 'My first grid'
                    }); 
              }); 
          </script>
      </table>
  </div>
</div>  -->

</body>
</html>