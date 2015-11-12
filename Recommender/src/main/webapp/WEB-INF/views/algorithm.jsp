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

.centerBlock {
	display: table;
	margin: 0 auto;
}
</style>
</head>

<body>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>
		var arrayOfAlgorithms = "${algorithms}".split("||");
		var arrayOfEnabledAlgorithms = "${enabledAlgorithms}".split("||");

		function populateTable() {
			var myTableDiv = document.getElementById("Algorithms")
			var table = document.getElementById("table")
			var tableBody = document.createElement('TBODY')

			table.appendChild(tableBody);
			var heading = new Array();
			heading[0] = "Algorithm";
			heading[1] = "Enabled?";
			heading[2] = "";
			heading[3] = "";

			//TABLE COLUMNS
			var tr = document.createElement('TR');
			tableBody.appendChild(tr);
			for (i = 0; i < heading.length; i++) {
				var th = document.createElement('TH')
				th.width = '75';
				th.appendChild(document.createTextNode(heading[i]));
				tr.appendChild(th);
			}
			var j = 0;
			for (i = 0; i < arrayOfAlgorithms.length; i++) {
				var tr = document.createElement('TR');

				var algorithmCell = document.createElement('TD')
				algorithmCell.appendChild(document.createTextNode(arrayOfAlgorithms[i]));
				tr.appendChild(algorithmCell)

				var enabledCell = document.createElement('TD')

				if (contains(arrayOfEnabledAlgorithms, arrayOfAlgorithms[i]))
					enabledCell.appendChild(document.createTextNode("yes"));
				else
					enabledCell.appendChild(document.createTextNode("no"));
				
				tr.appendChild(enabledCell)

				var ebutton = document.createElement('button');
				ebutton.innerHTML = "Enable";
				ebutton.className = "btn btn-default";
				ebutton.id = "e".concat(i);

				var enabledButton = document.createElement('TD');
				enabledButton.appendChild(ebutton);
				tr.appendChild(enabledButton)

				var dbutton = document.createElement('button');
				dbutton.innerHTML = "Disable";
				dbutton.className = "btn btn-default";
				dbutton.id = "d".concat(i);

				var disableButton = document.createElement('TD')
				disableButton.appendChild(dbutton);
				tr.appendChild(disableButton)

				tableBody.appendChild(tr);
			}

			myTableDiv.appendChild(table)
			for (i = 0; i < arrayOfAlgorithms.length; i++) {
				document.getElementById("d"+i).addEventListener("click", function(){
					manageAlgorithm(this.id)
					
				});
				document.getElementById("e"+i).addEventListener("click", function(){
					manageAlgorithm(this.id)
				});

			}
		}

		function contains(array, string) {
			for (j = 0; j < array.length; j++)
				if (array[j].localeCompare(string) == 0)
					return true;
		}

		function logout() {
			window.location.href = "/Recommender/<c:url value="j_spring_security_logout" />";
		}

		function manageAlgorithm(id) {
			if (id.charAt(0) == 'd')
				action = 0;
			else
				action = 1;

			algorithm = arrayOfAlgorithms[id.charAt(1)];
			alert(id.charAt(1));
			$.ajax({
				url : "/Recommender/admin/algorithmManagement?algorithm="
						+ algorithm + "&action=" + action,
				type : "GET",
				error : function(xhr, status, error) {
					alert(xhr.responseText);
				}
			});
			window.location.href = "/Recommender/admin/algorithm";
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
		<div id="Algorithms" class="col-xs-4 col-lg-4 col-md-offset-4 "
			style="margin-top: 50px;">
			<h4 class="centerBlock">Algorithm Management</h4>
			<table id="table" class="table centerBlock" style="margin-top: 20px;">
			</table>
		</div>

	</div>
</body>
</html>