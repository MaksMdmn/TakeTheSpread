<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Take the spread</title>
    <script type="text/javascript" src="js/jquery-1.12.2.min.js"></script>
    <script type="text/javascript" src="js/jsHelper.js"></script>
    
    <link rel="stylesheet" type="text/css" media="screen" href="css/ui.jqgrid.css" mce_href="css/ui.jqgrid.css" />
    <script type="text/javascript" src="js/i18n/grid.locale-ru.js"></script>
    <script type="text/javascript" src="js/jquery.jqgrid.min.js"></script>

    <link rel="stylesheet" href="css/main.css" type="text/css"/>


</head>
<body>
    <div class="header clearfix">
        HEADER
        <div class="settings">
            SETTINGS1
            SETTINGS2
        </div>

        <div class="login-info">
            LOG <br>
            PAS
        </div>
    </div>

    <div class="features clearfix">
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
    </div>



    <!-- <div id="headInfo">
        <div id="loginDiv">
            <input id ="name" type="text"  placeholder="usename" />
            <input id ="password" type="text" placeholder="password" />
            <br>
            <br>
            <input id="logIn" value ="login" type="button"/>
            <input id="out" value ="logout" type="button"/>
        </div>
    </div>

    <div id="mainInfo">
        <div id="userInfo">
            <input id="userButton" value = "testButton" type="button" onclick="handleCommand()"/>
            <textarea id="console" type="textarea" cols="40" rows="40" onkeypress="checkCommandSend(event)"> </textarea>
            <textarea id="answers" type="text" readonly wrap="soft"></textarea>
            <textarea id="cmd_stack" type="text" readonly wrap="soft"></textarea>
        </div>
        <div id="tradingInfo">
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
                        viewrecords: true,
                        caption: 'My first grid'
                    }); 
                  }); 
              </script>
          </table>





      </div>
      <div id="marketInfo">
        <br>

        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <table border="1">
            <tr>
                <td></td>
                <td></td>
                <td></td>
            </tr>

            <tr>
                <td></td>
                <td></td>
                <td></td>
            </tr>

            <tr>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </table>
    </div>
</div> -->
</body>
</html>