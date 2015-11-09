<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Similarity Project</title>
<link href="<c:url value="/bootstrap/css/bootstrap.min.css"/>"
	rel="stylesheet">
<style>
h4 {
	text-align: center;
}
.centerBlock {
	display: table;
	margin: 0 auto;
}
</style>
</head>

<body>
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>
		var algorithms = "${algorithms}".split("||");
		var falsePositives = "${falsePositives}".split("||");
		var totalEvaluations = "${totalEvaluations}".split("||");

		function populateTable() {
			var myTableDiv = document.getElementById("Statistics")
			var table = document.getElementById("table")
			var tableBody = document.createElement('TBODY')

			table.appendChild(tableBody);
			var heading = new Array();
            heading[0] = "Algorithm"
            heading[1] = "False Positives"
            heading[2] = "Total Evaluations"

			//TABLE COLUMNS
			var tr = document.createElement('TR');
			tableBody.appendChild(tr);
			for (i = 0; i < heading.length; i++) {
				var th = document.createElement('TH')
				th.width = '75';
				th.appendChild(document.createTextNode(heading[i]));
				tr.appendChild(th);

			}
			for (i = 0; i < totalEvaluations.length; i++) {
				var tr = document.createElement('TR');
				
				var algorithmCell = document.createElement('TD')
				algorithmCell.appendChild(document.createTextNode(algorithms[i]));
				tr.appendChild(algorithmCell)
				
				var falsePositivesCell = document.createElement('TD')
				falsePositivesCell.appendChild(document.createTextNode(falsePositives[i]));
				tr.appendChild(falsePositivesCell)
				
				var totalEvaluationCell = document.createElement('TD')
				totalEvaluationCell.appendChild(document.createTextNode(totalEvaluations[i]));
				tr.appendChild(totalEvaluationCell)
				
				tableBody.appendChild(tr);
			}
			
            myTableDiv.appendChild(table)
		}

		
		
		
		
		
		google.load('visualization', '1', {
			packages : [ 'corechart', 'bar' ]
		});
		google.setOnLoadCallback(drawBasic);

		function drawBasic() {
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'algorithms');
			data.addColumn('number', 'percentage');

			for (i = 0; i < algorithms.length; i++) {
				var percentage = 100 * (parseInt(falsePositives[i]) / parseInt(totalEvaluations[i]));
				data.addRow([ algorithms[i], percentage ]);
			}
			var options = {
				title : 'False Positives Percentage Per Algorithm',
				hAxis : {
					title : 'Algorithm'
				},
				vAxis : {
					title : 'False Positives Percentage',
					viewWindowMode : 'explicit',
					viewWindow : {
						max : 100,
						min : 0
					},
					format : '#\'%\''
				},
				legend : {
					position : "none"
				}
			};

			var chart = new google.visualization.ColumnChart(document
					.getElementById('chartDiv'));

			chart.draw(data, options);
		}

		function logout() {
			window.location.href = "<c:url value="j_spring_security_logout" />";
		}

		$(document).ready(function() {
			populateTable();
		});
	</script>

	<div class="container">
		<div class="row">

			<div class="col-xs-3 col-lg-3">
				<a href="/Recommender/home" style="text-decoration: none"><h3
						class="text-muted">Item-Item Similarity</h3></a>
			</div>




			<div class="col-xs-1 col-lg-1 col-md-offset-7"
				style="margin-top: 20px;">
				<button class="btn btn-default" onclick="return logout()">
					Logout</button>
			</div>

			<!-- /.col-lg-6 -->

		</div>

		<div class="row">
			<div id="Statistics" class="col-xs-4 col-lg-4 col-md-offset-4"
				style="margin-top: 50px;">
				<h4>Algorithms Statistics</h4>
				<table id="table" class="table" style="margin-top: 20px;">
				</table>
			</div>
		</div>

		<div class="row">
			<div class="centerBlock" id="chartDiv"></div>
		</div>
	</div>
</body>
</html>