google.load('visualization', '1.0', {'packages':['corechart']});

// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(drawChart);

function drawChart() {

    document.getElementById('holder').getElementsByTagName('li');

    var array = [['Day', '']];

    $("li").each(function(index, e){
        var value = $(e).text();
        console.info(value);
        array.push([index+1+"", parseInt(value)])
    });

    console.info(array);

    var data = google.visualization.arrayToDataTable(array);

    new google.visualization.LineChart(document.getElementById('holder')).
        draw(data, {curveType:"function",
            width:960, height:200,
            vAxis:{minValue:60},
            legend:{position:'none'},
            lineWidth: 3,
            pointSize: 5}
    );
}
