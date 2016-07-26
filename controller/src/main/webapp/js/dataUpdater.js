var bid_nArr = [];
var bid_fArr = [];
var ask_nArr = [];
var ask_fArr = [];
var deal_nArr = [];
var deal_fArr = [];
var xScaleArr = [];
var myChart;

$(document).ready(function() {
    var data = {
        labels: xScaleArr,
        series: [bid_nArr, bid_fArr, ask_nArr, ask_fArr, {
            name: 'deal1',
            data: deal_nArr
        }, {
            name: 'deal2',
            data: deal_fArr
        }]
    };
    var options = {
        fullWidth: true,
        chartPadding: {
            right: 40
        }
    };
    myChart = new Chartist.Line('.chart-feature', data, options);

});



var updateTimeMls = 500;
setInterval(dataUpdRand, updateTimeMls);


function dataUpd(bidN, bidF, askN, askF, dealN, dealF) {
    bid_nArr.push(bidN);
    bid_fArr.push(bidF);
    ask_nArr.push(bidN);
    ask_fArr.push(askF);
    deal_nArr.push(dealN);
    deal_fArr.push(dealF);

    xScaleArr.push(new Date().toTimeString());
    myChart.data.labels = xScaleArr;
    myChart.data.series = [bid_nArr, bid_fArr, ask_nArr, ask_fArr, deal_nArr, deal_fArr];
    myChart.update();
}



function dataUpdRand() {
    var deal = getRandDeal();
    dataUpd(getRandomPrice(), getRandomPrice(), getRandomPrice(), getRandomPrice(), deal, deal - 3);
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function getRandomPrice() {
    return getRandomInt(1, 100);
}

function getRandDeal() {
    var temp = getRandomInt(0, 1) * getRandomInt(10, 100);
    if (temp === 0) {
        return null;
    } else {
        return temp;
    }
}