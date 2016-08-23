<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Take the spread</title>
    <link type="text/css" rel="stylesheet" media="screen" href="css/jquery-ui-1.12.0/jquery-ui.css"/>
    <link type="text/css" rel="stylesheet" media="screen" href="css/ui.jqgrid.css" />
    <link type="text/css" rel="stylesheet" href="css/main.css" />
    <link type="text/css" rel="stylesheet" href="css/chartist.min.css" />

    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/jquery-1.12.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/i18n/grid.locale-en.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/dygraph-combined-dev.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dataUpdater.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/consoleManager.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/statusChecker.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/loginChecker.js"></script>


<!-- <script type="text/javascript" src="js/lib/jquery-1.12.2.min.js"></script>
<script type="text/javascript" src="js/lib/i18n/grid.locale-en.js" ></script>
<script type="text/javascript" src="js/lib/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="js/lib/dygraph-combined-dev.js"></script>
<script type="text/javascript" src="js/consoleManager.js"></script>
<script type="text/javascript" src="js/dataUpdater.js"></script>
<script type="text/javascript" src="js/loginChecker.js"></script> -->


</head>
<body>
    <div class="header clearfix">
        <div class="settings hide">
            <table ordId="settingsTable"></table>
        </div>

        <div class="indicators hide">
            <table ordId="indicatorTable"></table>
        </div>

        <div class="login-info">
            <p>
                <label for="login">Login:</label>
                <input type="text" name="login" ordId="login" value="your login">
            </p>

            <p>
              <label for="password">Password:</label>
              <input type="password" name="password" ordId="password" value="your password">
          </p>

          <p class="login-submit">
              <button type="submit" class="login-button" ordId="login-button"></button>
          </p>
      </div>
  </div>

  <div class="features clearfix hide">
    <div class="console-feature">
        <div class="console-messages">
            <textarea ordId="messages" type="textarea"></textarea>
        </div>
        <div class="console-bars">
            <div ordId="message-bar"></div><br>
            <div ordId="connection-bar"></div>
        </div>
        <div class="console-stack">
            <textarea ordId="stack" type="text" readonly wrap="soft"></textarea>
        </div>

        <div class="console-answers">
            <textarea ordId="answers" type="text" readonly wrap="soft"></textarea>
        </div>
    </div>

    <div class="table-feature">
        <table ordId="orderTable"></table>
        <div ordId="orderPager"></div>
    </div>

    <div class="chart-feature" >
    </div>
</div>

<div class="footer hide">
    <img src="img/leftlobster.jpg" alt="lobster say hello">
    <img src="img/leftlobster.jpg" alt="lobster say hello">
    <img src="img/leftlobster.jpg" alt="lobster say hello">
    <img src="img/leftlobster.jpg" alt="lobster say hello">
    <img src="img/leftlobster.jpg" alt="lobster say hello">
    <img src="img/leftlobster.jpg" alt="lobster say hello">
    <img src="img/leftlobster.jpg" alt="lobster say hello">
    <img src="img/leftlobster.jpg" alt="lobster say hello">
    <img src="img/rightlobster.jpg" alt="lobster say hello">
    <img src="img/rightlobster.jpg" alt="lobster say hello">
    <img src="img/rightlobster.jpg" alt="lobster say hello">
    <img src="img/rightlobster.jpg" alt="lobster say hello">
    <img src="img/rightlobster.jpg" alt="lobster say hello">
    <img src="img/rightlobster.jpg" alt="lobster say hello">
    <img src="img/rightlobster.jpg" alt="lobster say hello">
    <img src="img/rightlobster.jpg" alt="lobster say hello">
</div>
</body>
</html>