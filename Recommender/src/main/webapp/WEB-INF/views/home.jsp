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

h4 {
	text-align: center;
}
</style>
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
		var arrayOfTitles = "${movieTitles}".split("||");
		var arrayOfPosters = "${posters}".split("||");
		var itemPosition = 0;

		var popularArrayOfIds = JSON.parse("${popularMovieIds}");
		var popularArraySize = "${popularMovieIds.size()}";
		var popularStrLen = "${popularMovieTitles}".length;
		var popularPosterLen = "${popularPosters}".length;
		var popularArrayOfTitles = "${popularMovieTitles}".split("||");
		var popularArrayOfPosters = "${popularPosters}".split("||");
		var popularItemPosition = 0;


		function nextItem() {
			if (itemPosition > arraySize) {
				return;
			}
			var item = arrayOfIds[itemPosition];
			var code = '<a class="centerBlock" href = "/Recommender/itemSimilarity?item='
					+ item
					+ '">'
					+ '<img' +
		'	src="'+arrayOfPosters[itemPosition]+'"' +
		'	style="height: 206px; width: 144px"' +
		'	alt="Mountain View" class="img-responsive centerBlock"></a>' +
		'<h4 class="centerBlock" id="title' + arrayOfIds[itemPosition] + '">'
		+ arrayOfTitles[itemPosition]
		+ '</h4>';
			itemPosition++;
			return code;
		}
		function nextPopularItem() {
			if (popularItemPosition > popularArraySize) {
				return;
			}
			var item = popularArrayOfIds[popularItemPosition];
			var code = '<a class="centerBlock" href = "/Recommender/itemSimilarity?item='
					+ item
					+ '">'
					+ '<img' +
		'	src="'+popularArrayOfPosters[popularItemPosition]+'"' +
		'	style="height: 206px; width: 144px"' +
		'	alt="Mountain View" class="img-responsive centerBlock"></a>' +
		'<h4 class="centerBlock" id="title' + popularArrayOfIds[popularItemPosition] + '">'
		+ popularArrayOfTitles[popularItemPosition]
		+ '</h4>';
		popularItemPosition++;
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
		function getNewPopularItem(id) {

			if (popularItemPosition < popularArraySize) {

				document.getElementById("movie".concat(id)).innerHTML = nextPopularItem();
				document.getElementById("movie".concat(id)).id = "movie"
						+ popularArrayOfIds[popularItemPosition - 1];
				
			}
			
		}
		
		
		$(document).ready(function() {
			for(i=1; i<=12; i++){
				getNewItem("R".concat(i));
				getNewPopularItem("P".concat(i));
			}
		});

		function retrieveMovies() {
			if ($("#searchPhrase").val().length < 3)
				alert("Please, enter a minimum of 3 characters to search for a movie.")
			else
				window.location
						.replace("/Recommender/search?searchString="
								+ $("#searchPhrase").val());
		}

		function logout() {
			window.location.href = "<c:url value="j_spring_security_logout" />";
		}
		
		function ranking() {
			window.location
			.replace("/Recommender/ranking");		}

		$(document).ready(function(){
		    $(".nav-tabs a").click(function(){
		        $(this).tab('show');
		    });
		});
		
		function goToSettings(){
			window.location
			.replace("/Recommender/settings");
		}
		
		function yHandler(){
			if(itemPosition < arraySize){
				var wrap = document.getElementById('wrap');
				var contentHeight = wrap.offsetHeight;
				var yOffset = window.pageYOffset; 
				var y = yOffset + window.innerHeight;
				if(y >= contentHeight){
					wrap.innerHTML += '<div class="row"> '+
						'<div id="movieNew1" class="col-xs-3 col-lg-3" style="margin-top:25px;"></div>' + 
						'<div id="movieNew2" class="col-xs-3 col-lg-3" style="margin-top:25px;"></div>' + 
						'<div id="movieNew3" class="col-xs-3 col-lg-3" style="margin-top:25px;"></div>' + 
						'<div id="movieNew4" class="col-xs-3 col-lg-3" style="margin-top:25px;"></div>' + 
					'</div>';
				}
				getNewItem("New1");
				getNewItem("New2");
				getNewItem("New3");
				getNewItem("New4");
			}
		}
		window.onscroll = yHandler;
		
		function yPHandler(){
			if(popularItemPosition < popularArraySize){
				var wrap = document.getElementById('popularwrap');
				var contentHeight = wrap.offsetHeight;
				var yOffset = window.pageYOffset; 
				var y = yOffset + window.innerHeight;
				if(y >= contentHeight){
					wrap.innerHTML += '<div class="row"> '+
						'<div id="moviePNew1" class="col-xs-3 col-lg-3" style="margin-top:25px;"></div>' + 
						'<div id="moviePNew2" class="col-xs-3 col-lg-3" style="margin-top:25px;"></div>' + 
						'<div id="moviePNew3" class="col-xs-3 col-lg-3" style="margin-top:25px;"></div>' + 
						'<div id="moviePNew4" class="col-xs-3 col-lg-3" style="margin-top:25px;"></div>' + 
					'</div>';
				}
				getNewPopularItem("PNew1");
				getNewPopularItem("PNew2");
				getNewPopularItem("PNew3");
				getNewPopularItem("PNew4");
			}
		}
		window.onscroll = yPHandler;
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

			<div class="col-xs-1 col-lg-1 " style="margin-top: 20px;">
				<button class="btn btn-default" onclick="return ranking()">
					Ranking</button>
			</div>

			<div class="col-xs-1 col-lg-1 " style="margin-top: 20px;">
				<button class="btn btn-default" onclick="return logout()">
					Logout</button>
			</div>
			<div class="col-xs-1 col-lg-1" style="margin-top: 20px;">
				<button type="button" class="btn btn-default"
					aria-label="Left Align" onclick="return goToSettings()">
					<span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
				</button>
			</div>
			<!-- /.col-lg-6 -->

		</div>

		<ul class="nav nav-tabs">
			<li class="active"><a href="#random">Random</a></li>
			<li><a href="#popular">Popular</a></li>
		</ul>

		<div class="tab-content">
			<div role="tabpanel" class="tab-pane fade in active" id="random">

				<div class="row" style="margin-top: 20px;">

					<div id="movieR1" class="col-xs-3 col-lg-3"></div>
					<div id="movieR2" class="col-xs-3 col-lg-3"></div>
					<div id="movieR3" class="col-xs-3 col-lg-3"></div>
					<div id="movieR4" class="col-xs-3 col-lg-3"></div>

				</div>
				<div class="row" style="margin-top: 25px;">

					<div id="movieR5" class="col-xs-3 col-lg-3"></div>
					<div id="movieR6" class="col-xs-3 col-lg-3"></div>
					<div id="movieR7" class="col-xs-3 col-lg-3"></div>
					<div id="movieR8" class="col-xs-3 col-lg-3"></div>

				</div>
				<div class="row" style="margin-top: 25px;">

					<div id="movieR9" class="col-xs-3 col-lg-3"></div>
					<div id="movieR10" class="col-xs-3 col-lg-3"></div>
					<div id="movieR11" class="col-xs-3 col-lg-3"></div>
					<div id="movieR12" class="col-xs-3 col-lg-3"></div>

				</div>
				<div id="wrap"></div>

			</div>
			<div role="tabpanel" class="tab-pane fade" id="popular">

				<div class="row" style="margin-top: 20px;">

					<div id="movieP1" class="col-xs-3 col-lg-3"></div>
					<div id="movieP2" class="col-xs-3 col-lg-3"></div>
					<div id="movieP3" class="col-xs-3 col-lg-3"></div>
					<div id="movieP4" class="col-xs-3 col-lg-3"></div>

				</div>
				<div class="row" style="margin-top: 20px;">

					<div id="movieP5" class="col-xs-3 col-lg-3"></div>
					<div id="movieP6" class="col-xs-3 col-lg-3"></div>
					<div id="movieP7" class="col-xs-3 col-lg-3"></div>
					<div id="movieP8" class="col-xs-3 col-lg-3"></div>

				</div>
				<div class="row" style="margin-top: 20px;">

					<div id="movieP9" class="col-xs-3 col-lg-3"></div>
					<div id="movieP10" class="col-xs-3 col-lg-3"></div>
					<div id="movieP11" class="col-xs-3 col-lg-3"></div>
					<div id="movieP12" class="col-xs-3 col-lg-3"></div>

				</div>

				<div id="popularwrap"></div>

			</div>

		</div>

	</div>
</body>
</html>