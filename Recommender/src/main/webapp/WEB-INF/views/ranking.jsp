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
</style>
</head>

<body>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>
		var arrayOfUsers = "${users}".split("||");
		var arrayOfRatings = "${ratings}".split("||");
		var username = "${username}";

		function populateTable() {
			var myTableDiv = document.getElementById("Ranking")
			var table = document.getElementById("table")
			var tableBody = document.createElement('TBODY')

			table.appendChild(tableBody);
			var heading = new Array();
            heading[0] = "User"
            heading[1] = "Number of Ratings"

			//TABLE COLUMNS
			var tr = document.createElement('TR');
			tableBody.appendChild(tr);
			for (i = 0; i < heading.length; i++) {
				var th = document.createElement('TH')
				th.width = '75';
				th.appendChild(document.createTextNode(heading[i]));
				tr.appendChild(th);

			}
			for (i = 0; i < arrayOfUsers.length; i++) {
				var tr = document.createElement('TR');
				
				var userCell = document.createElement('TD')
				userCell.appendChild(document.createTextNode(arrayOfUsers[i]));
				tr.appendChild(userCell)
				
				var ratingCell = document.createElement('TD')
				ratingCell.appendChild(document.createTextNode(arrayOfRatings[i]));
				tr.appendChild(ratingCell)
				
				tableBody.appendChild(tr);
			}
			
            myTableDiv.appendChild(table)
		}

		function retrieveMovies() {
			if ($("#searchPhrase").val().length < 3)
				alert("Please, enter a minimum of 3 characters to search for a movie.")
			else
				window.location.replace("/Recommender/search?searchString="
						+ $("#searchPhrase").val());
		}

		function logout() {
			window.location.href = "<c:url value="j_spring_security_logout" />";
		}
		
		function highlightUserRow(){
			$( "tr:contains('"+username+"')" ).toggleClass("warning");

		}
		
		$(document).ready(function() {
			populateTable();
			highlightUserRow();
		});
	</script>

	<div class="container">
		<div class="row">

			<div class="col-xs-3 col-lg-3">
				<a href="/Recommender/home" style="text-decoration: none"><h3
						class="text-muted">Item-Item Similarity</h3></a>
			</div>


			<div class="col-lg-6 " style="margin-top: 20px;">
				<div class="input-group">
					<input id="searchPhrase" type="text" class="form-control"
						placeholder="Search for..."> <span class="input-group-btn">
						<button class="btn btn-default" type="button"
							onclick="retrieveMovies()">Search</button>
					</span>
				</div>
				<!-- /input-group -->
			</div>

			<div class="col-xs-1 col-lg-1 col-md-offset-1"
				style="margin-top: 20px;">
				<button class="btn btn-default" onclick="return logout()">
					Logout</button>
			</div>

			<!-- /.col-lg-6 -->

		</div>


		<div id="Ranking" class="col-xs-4 col-lg-4 col-md-offset-4" style="margin-top: 50px;">
		<h4>Item-Item Similarity Ranking</h4>
		<table id="table" class="table" style="margin-top: 20px;">
		</table>
		</div>


	</div>
</body>
</html>