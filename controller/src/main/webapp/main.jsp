<!DOCTYPE HTML>
<!-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> -->
<html>
<head>
    <meta charset="UTF-8">
    <title>Take the spread</title>
    <link type="text/css" rel="stylesheet" media="screen" href="css/jquery-ui-1.12.0/jquery-ui.css"/>
    <link type="text/css" rel="stylesheet" media="screen" href="css/ui.jqgrid.css" />
    <link type="text/css" rel="stylesheet" href="css/main.css" />
    <link type="text/css" rel="stylesheet" href="css/chartist.min.css" />

    <!-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/jquery-1.12.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/i18n/grid.locale-en.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lib/dygraph-combined-dev.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/lobstaHelper.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/consoleManager.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/loginChecker.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/dataUpdater.js"></script>
 -->
    <script type="text/javascript" src="js/lib/jquery-1.12.2.min.js"></script>
    <script type="text/javascript" src="js/lib/i18n/grid.locale-en.js" ></script>
    <script type="text/javascript" src="js/lib/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="js/lib/dygraph-combined-dev.js"></script>
    <script type="text/javascript" src="js/lobstaHelper.js"></script>
    <script type="text/javascript" src="js/consoleManager.js"></script>
    <!-- <script type="text/javascript" src="js/isZoomedIgnoreProgrammaticZoomloginChecker.js"></script> -->
    <script type="text/javascript" src="js/dataUpdater.js"></script>


</head>
<body>
    <div class="header clearfix">
        <div class="settings hide">
            <table id="settingsTable"></table>
        </div>

        <div class="indicators hide">
            <table id="indicatorTable"></table>
        </div>

        <div class="login-info">
            <p>
                <label for="login">Login:</label>
                <input type="text" name="login" id="login" value="your login">
            </p>

            <p>
              <label for="password">Password:</label>
              <input type="password" name="password" id="password" value="your password">
          </p>

          <p class="login-submit">
              <button type="submit" class="login-button" id="login-button"></button>
          </p>
      </div>
  </div>

  <div class="features clearfix hide">
    <div class="console-feature">
        <div class="console-messages">
            <textarea id="messages" type="textarea"></textarea>
        </div>
        <div class="console-bars">
            <div id="message-bar"></div><br>
            <div id="connection-bar"></div>
        </div>
        <div class="console-stack">
            <textarea id="stack" type="text" readonly wrap="soft"></textarea>
        </div>

        <div class="console-answers">
            <textarea id="answers" type="text" readonly wrap="soft"></textarea>
        </div>
    </div>

    <div class="table-feature">
        <table id="orderTable"></table>
        <div id="orderPager"></div>
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