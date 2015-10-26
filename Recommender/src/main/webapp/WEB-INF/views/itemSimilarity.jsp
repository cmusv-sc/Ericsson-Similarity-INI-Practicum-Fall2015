<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Similarity of ${selectedMovieTitle}</title>
<link href="<c:url value="/bootstrap/css/bootstrap.min.css"/>"
	rel="stylesheet">

</head>
<body>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script
		src="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/js/bootstrap.min.js"></script>

	<script>
		var arrayOfIds = JSON.parse("${movieIds}");
		var arraySize = "${movieIds.size()}";
		var strLen = "${movieTitles}".length;
		var posterLen = "${posters}".length;
		var arrayOfPlots = "${moviesPlots}".split("||");
		var arrayOfTitles = "${movieTitles}".substring(1, strLen - 1)
				.split(",");
		var arrayOfPosters = "${posters}".substring(1, posterLen - 1)
				.split(",");

		var itemPosition = 0;
	
	
		function nextItem() {
			if (itemPosition > arraySize) {
				return;
			}
			var item = arrayOfIds[itemPosition];
			var code = '<h4>'
					+ arrayOfTitles[itemPosition]
					+ '</h4>'
					+ '<a href="#" data-toggle="popover" title="'+arrayOfTitles[itemPosition]+'" data-content="'+arrayOfPlots[itemPosition]+'">'
					+ '<img' 
					+ '	src="'+arrayOfPosters[itemPosition]+'"' 
					+ '	alt="Mountain View" class="img-responsive"' 
					+ '	style="height: 206px; width: 144px">'
					+ '</a>'
					//btnToolBar
	    			+ '<div>'
					+ 	'<div class="btn-toolbar" role="toolbar">'
					+ 	'	<div class="btn-group">'
					//btn Not Similar
					+ '			<button style="width: 48px" id="remove'
					+ item
					+ 			'" type="button" class="btn btn-default"'
					+ '				aria-label="Left Align" onclick="sendSimilarity('
					+ item
					+ ', 0)">'
					+ '			<span class="glyphicon  glyphicon-remove" aria-hidden="true"></span>'
					+ '		</button>'
					//Btn Don't Know
					+ '		<button style="width: 48px" id="question'
					+ item
					+ '"type="button" class="btn btn-default"'
					+ '			aria-label="Center Align" onclick="getNewItem('
					+ item
					+ ')">'
					+ '			<span class="glyphicon  glyphicon-question-sign"' 
					+ ' aria-hidden="true"></span>'
					//Btn Similar
					+ '		</button>'
					+ '		<button style="width: 48px" id="ok'
					+ item
					+ '"type="button" class="btn btn-default"'
					+ '			aria-label="Right Align" onclick="sendSimilarity('
					+ item
					+ ', 1)">'
					+ '			<span class="glyphicon  glyphicon-ok" aria-hidden="true"></span>'
					+ '		</button>' 
					+ '	</div>'
					+'</div>';
			itemPosition++;
			return code;
		}

		function getNewItem(id) {
			$("#movie" + id).fadeOut();

			if (itemPosition < arraySize) {
				$("#movie" + id).fadeIn();

				document.getElementById("movie".concat(id)).innerHTML = nextItem();
				document.getElementById("movie".concat(id)).id = "movie"
						+ arrayOfIds[itemPosition - 1];
			}
		}

		$(document).ready(function() {
			getNewItem("First");
			getNewItem("Second");
			getNewItem("Third");
			getNewItem("Fourth");
		});

		$(document).ready(function() {
			$('[data-toggle="popover"]').popover({
		        container: 'body', // Popover scrolls with body
		        placement: 'top'

		    });;
		});
		
		function sendSimilarity(id, similarity) {
			$.ajax({
				url : "/Recommender/evaluation?similarity="+similarity+"&movieId1="+ "${selectedMovieId}" + "&movieId2="+ id,
				type : "GET",
				error : function(xhr, status, error) {
					alert(xhr.responseText);
				}
			});
			getNewItem(id);
		}
	</script>

	<div class="container">
		<div class="header clearfix">
			<h3 class="text-muted">Item-Item Similarity</h3>
		</div>

		<div class="jumbotron">
			<div class="row">
				<div class="col-xs-6 col-lg-4">
					<h2>${selectedMovieTitle}</h2>
					<img src="${selectedPoster}" alt="Mountain View"
						class="img-responsive" style="height: 320px; width: 200px">
				</div>
				<div class="col-xs-6 col-lg-8">
					<h2>Plot</h2>
					<p>${synopsys}</p>
				</div>
			</div>

		</div>

		<div class="row">
			<h2>Are these movies similar to ${selectedMovieTitle}?</h2>


			<div id="movieFirst" class="col-xs-3 col-lg-3"></div>
			<div id="movieSecond" class="col-xs-3 col-lg-3"></div>
			<div id="movieThird" class="col-xs-3 col-lg-3"></div>
			<div id="movieFourth" class="col-xs-3 col-lg-3"></div>

		</div>

	</div>
</body>
</html>