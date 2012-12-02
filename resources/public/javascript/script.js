var data = []
$("#weights li").each(function (index, e) {
    var value = $(e).text();
    data.push(parseFloat(value))
});

var categories = []
$("#dates li").each(function (index, e) {
    var value = $(e).text();
    categories.push(new Date(parseInt(value)));
});

var chart;
$(document).ready(function () {
    var drawChart = function (categories, data) {
        chart = new Highcharts.Chart({
            chart:{
                renderTo:'holder',
                type:'line',
                marginRight:0,
                marginBottom:50
            },
            title:null,
            subtitle:null,
            xAxis:{
                categories:categories,
                labels:{
                    formatter:function () {
                        return Highcharts.dateFormat('%d %b', this.value);
                    }
                }
            },
            yAxis:{
                title:null,
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
                        this.x.toDateString() + ': ' + this.y + 'Kg';
                }
            },
            legend:{
                enabled:false
            },
            series:[
                {
                    name:'Weight',
                    data:data
                }
            ]
        });
    }
    $.getJSON('/api/index', function (data) {
        var categories = $.map(data, function (val) {
            return new Date(parseInt(val.date));
        });
        var data = $.map(data, function (val) {
            return parseFloat(val.weight);
        });
        drawChart(categories, data);
    });
});


