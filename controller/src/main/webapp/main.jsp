<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MainPage</title>
    <script type="text/javascript" src="js/jquery-1.12.2.min.js"></script>
    <script type="text/javascript" src="js/jsHelper.js"></script>
    <link rel="stylesheet" href="css/main.css" type="text/css"/>


</head>
<body>
    <div id="headInfo">
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
        <div id="marketInfo">
            <br>
            CHART  CHART CHART CHART CHART CHART CHART CHART<br>
            CHART  CHART CHART CHART CHART CHART CHART CHART<br>
            CHART  CHART CHART CHART CHART CHART CHART CHART<br>
            CHART  CHART CHART CHART CHART CHART CHART CHART<br>
            CHART  CHART CHART CHART CHART CHART CHART CHART<br>
            CHART  CHART CHART CHART CHART CHART CHART CHART<br>
            CHART  CHART CHART CHART CHART CHART CHART CHART<br>
            CHART  CHART CHART CHART CHART CHART CHART CHART<br>
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
    </div>
</body>
</html>