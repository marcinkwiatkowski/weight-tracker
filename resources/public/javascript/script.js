var data = []

$("li").each(function (index, e) {
    var value = $(e).text();
    data.push(parseInt(value))
});

var categories = []

for (var i = 0; i < data.length; i++) {
    categories[i] = i+1;
}


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
                    this.x + ': ' + this.y + '°C';
            }
        },
        legend:{
            layout:'vertical',
            align:'right',
            verticalAlign:'top',
            x:-10,
            y:100,
            borderWidth:0
        },
        series:[
            {
                name:'Weight',
                data:data
            }
        ]
    });
});
