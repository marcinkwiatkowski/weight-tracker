var data = []
$("#weights li").each(function (index, e) {
    var value = $(e).text();
    data.push(parseInt(value))
});

var categories = []
$("#dates li").each(function (index, e) {
    var value = $(e).text();
    categories.push(new Date(parseInt(value)).toDateString());
});

var chart;
$(document).ready(function () {
    chart = new Highcharts.Chart({
        chart:{
            renderTo:'holder',
            type:'line',
            marginRight:130,
            marginBottom:25
        },
        title:{
            text:'Weight Tracker',
            x:-20 //center
        },
        xAxis:{
            categories:categories        },
        yAxis:{
            title:{
                text:'Weight (Kg)'
            },
            plotLines:[
                {
                    value:0,
                    width:1,
                    color:'#808080'
                }
            ]
        },
        tooltip:{
            formatter:function () {
                return '<b>' + this.series.name + '</b><br/>' +
                    this.x + ': ' + this.y + 'Kg';
            }
        },
        legend:{
            enabled: false
        },
        series:[
            {
                name:'Weight',
                data:data
            }
        ]
    });
});
