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
<style>
.ellipsis {
        overflow: hidden;
}
.centerBlock {
	display: table;
	margin: 0 auto;
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
		var arrayOfPlots = "${moviesPlots}".split("||");
		var arrayOfTitles = "${movieTitles}".split("||");
		var arrayOfYears = "${years}".split("||");;
		var arrayOfPosters = "${posters}".split("||");
	
		var itemPosition = 0;

		function nextItem() {
			if (itemPosition > arraySize) {
				return;
			}
			var item = arrayOfIds[itemPosition];
			if(arrayOfPlots[itemPosition] == "")
				arrayOfPlots[itemPosition] = "Sorry, we don't have that plot!"
			var code = '<div id="title" >'
					+ '<h4  class="ellipsis centerBlock" style="text-align: center; font-size:15px; width: 180px; height: 20px; margin-top:20px;">'
					+ arrayOfTitles[itemPosition]
					+ '</h4>'
					+ '<h4  class="centerBlock" style="text-align: center; font-size:15px; width: 180px; height: 20px;">'
					+ arrayOfYears[itemPosition]
					+ '</h4>'
					+ '</div>'
					+ '<a data-toggle="popover" title="'+arrayOfTitles[itemPosition]+'" data-content="'+arrayOfPlots[itemPosition]+'">'
					+ '<img' 
				+ '	src="'+arrayOfPosters[itemPosition]+'"' 
				+ '	alt="Mountain View" class="img-responsive centerBlock"' 
				+ '	style="height: 206px; width: 144px">'
					+ '</a>'
					//btnToolBar
					+ '<div>'
					+ '<div class="btn-toolbar" style="margin-left: 16px;" role="toolbar">'
					+ '	<div class="btn-group">'
					//btn Not Similar
					+ '			<button style="width: 49px" id="remove'
					+ item
					+ '" type="button" class="btn btn-default"'
					+ '				aria-label="Left Align" onclick="sendSimilarity('
					+ item
					+ ', 0)">'
					+ '			<span class="glyphicon  glyphicon-remove" aria-hidden="true"></span>'
					+ '		</button>'
					//Btn Don't Know
					+ '		<button style="width: 50px" id="question'
					+ item
					+ '"type="button" class="btn btn-default"'
					+ '			aria-label="Center Align" onclick="getNewItem('
					+ item
					+ ')">'
					+ '			<span class="glyphicon  glyphicon-question-sign"' 
				+ ' aria-hidden="true"></span>'
					//Btn Similar
					+ '		</button>'
					+ '		<button style="width: 49px" id="ok'
					+ item
					+ '"type="button" class="btn btn-default"'
					+ '			aria-label="Right Align" onclick="sendSimilarity('
					+ item
					+ ', 1)">'
					+ '			<span class="glyphicon  glyphicon-ok" aria-hidden="true"></span>'
					+ '		</button>' + '	</div>' + '</div>';
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
				sendVisualizedMovies(arrayOfIds[itemPosition - 1]);
			}
			popoverRefresh();
			$(".ellipsis").ellipsis();

		}

		$(document).ready(function() {
			getNewItem("First");
			getNewItem("Second");
			getNewItem("Third");
			getNewItem("Fourth");
			getNewItem("Fifth");
			getNewItem("Sixth");
			getNewItem("Seventh");
			getNewItem("Eighth");
		});

		$(document).ready(function() {
			popoverRefresh();
		});

		function popoverRefresh() {
			$('[data-toggle="popover"]').popover({
				container : 'body', // Popover scrolls with body
				placement : 'left',
				trigger : 'hover',
				html : 'true'
			});
			;
		}

		function retrieveMovies() {
			if ($("#searchPhrase").val().length < 3)
				alert("Please, enter a minimum of 3 characters to search for a movie.")
			else
				window.location.replace("/Recommender/search?searchString="
						+ $("#searchPhrase").val());
		}
		
		function sendVisualizedMovies(id) {
			$.ajax({
				url : "/Recommender/visualized?movieId1=" + "${selectedMovieId}" + "&movieId2="+ id,
				type : "GET"
			});
		}
		
		
		function sendSimilarity(id, similarity) {
			$.ajax({
				url : "/Recommender/evaluation?similarity=" + similarity
						+ "&movieId1=" + "${selectedMovieId}" + "&movieId2="
						+ id,
				type : "GET"
			});
			getNewItem(id);
		}
		
		function logout() {
			window.location.href = "<c:url value="j_spring_security_logout" />";
		}

		//Ellipsis handler
		(function($) {
			$.fn.ellipsis = function() {
				return this.each(function() {
					var el = $(this);

					if (el.css("overflow") == "hidden") {
						var text = el.html();
						var multiline = el.hasClass('multiline');
						var t = $(this.cloneNode(true)).hide().css('position',
								'absolute').css('overflow', 'visible').width(
								multiline ? el.width() : 'auto').height(
								multiline ? 'auto' : el.height());

						el.after(t);

						function height() {
							return t.height() > el.height();
						}
						;
						function width() {
							return t.width() > el.width();
						}
						;

						var func = multiline ? height : width;

						while (text.length > 0 && func()) {
							text = text.substr(0, text.length - 1);
							t.html(text + "...");
						}

						el.html(t.html());
						t.remove();
					}
				});
			};
		})(jQuery);

		$(document).ready(function() {
			$(".ellipsis").ellipsis();
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

			<div class="col-xs-1 col-lg-1 col-md-offset-2"
				style="margin-top: 20px;">
				<button class="btn btn-default" onclick="return logout()">
					Logout</button>
			</div>

			<!-- /.col-lg-6 -->

		</div>
		<div class="col-md-offset-3">
			<div class="row ">
				<h3 class="centerBlock" style="margin-top: 20px; text-align:center">Are these movies similar to ${selectedMovieTitle}?</h3>
			</div>
		</div>
		<div class="row">

			<div class="col-xs-6 col-lg-3">
				<div class="jumbotron" style="min-height:600px; padding-top: 10px;padding-bottom: 0px;padding-left: 30px;padding-right: 30px;">
   						<p>My salary is: <c:out value="${salary}"/><p>
					<div class="row">
						<h4 style="text-align: center;">${selectedMovieTitle}</h4>
						<img src="${selectedPoster}" alt="Mountain View"
							class="img-responsive centerBlock" style="height: 247px; width: 173px">
					</div>
					<div class="row">
						<h4>Plot</h4>
						<c:if test='${synopsys == ""}'>
							<p style="font-size: 15px">Sorry, we don't have that plot!</p>
						</c:if>
						<c:if test='${synopsys != ""}'>
							<p style="font-size: 15px">${synopsys}</p>
						</c:if>
					</div>
				</div>

			</div>
			<div class="col-xs-6 col-lg-9">


				<div class="row">
					<div id="movieFirst" class="col-xs-3 col-lg-3"></div>
					<div id="movieSecond" class="col-xs-3 col-lg-3"></div>
					<div id="movieThird" class="col-xs-3 col-lg-3"></div>
					<div id="movieFourth" class="col-xs-3 col-lg-3"></div>
				</div>

				<div class="row">
					<div id="movieFifth" class="col-xs-3 col-lg-3"></div>
					<div id="movieSixth" class="col-xs-3 col-lg-3"></div>
					<div id="movieSeventh" class="col-xs-3 col-lg-3"></div>
					<div id="movieEighth" class="col-xs-3 col-lg-3"></div>
				</div>

			</div>
		</div>
	</div>

</body>
</html>