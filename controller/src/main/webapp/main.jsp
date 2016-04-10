<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MainPage</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.12.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jsHelper.js"></script>
    <style>
        <%@ include file="css/main.css"%>
    </style>

</head>
<body>
<div ntId="allContent">
    <div ntId="consoleInfo" style="float:left">
        CONSOLE
        <textarea ntId="userConsole" type="textarea" cols="40" rows="40" onkeypress="checkCommandSend(event)"> </textarea>
    </div>
    <div ntId="traderInfo" style="float:left">
        TABLE
    </div>
    <div ntId="marketInfo" style="float:right">
        CHART WITH SYMBOLS
    </div>
</div>
</body>
</html>