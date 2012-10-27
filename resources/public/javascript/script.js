var data = []
$("#weights li").each(function (index, e) {
    var value = $(e).text();
    data.push(parseFloat(value))
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
            marginRight: 0,
            marginBottom: 50
        },
        title: null,
        subtitle: null,
        xAxis:{
            categories:categories
        },
        yAxis:{
            title: null,
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
