<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Similarity of movie ${item}</title>
<link href="<c:url value="/bootstrap/css/bootstrap.min.css"/>"
	rel="stylesheet">
</head>
<body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script>
	var arrayOfIds = JSON.parse("${movieIds}");
	var arraySize = "${movieIds.size()}";
	var strLen = "${movieTitles}".length;
	var arrayOfTitles = "${movieTitles}".substring(1,strLen-1).split(",");
	
	var itemPosition = 0;

	function nextItem(){
		if (itemPosition > arraySize){
			return;
		}
		var item = arrayOfIds[itemPosition];
		var code = '<h4>'+ arrayOfTitles[itemPosition] +'</h4>' +
		'<img' +
		'	src="https://image.tmdb.org/t/p/w780/qFYwztFX1gx9PZLnTEokQw5q04G.jpg"' +
		'	alt="Mountain View" class="img-responsive">' +
		'<div class="btn-toolbar" role="toolbar">' +
		'	<div class="btn-group">' +
		'		<button type="button" class="btn btn-default"' +
		'			aria-label="Left Align" onclick="getNewItem('+ item +')">' +
		'			<span class="glyphicon  glyphicon-remove" aria-hidden="true"></span>' +
		'		</button>' +
		'		<button type="button" class="btn btn-default"' +
		'			aria-label="Center Align" onclick="getNewItem('+ item +')">' +
		'			<span class="glyphicon  glyphicon-question-sign"' +
		'				aria-hidden="true"></span>' +
		'		</button>' +
		'		<button type="button" class="btn btn-default"' +
		'			aria-label="Center Align">' +
		'			<span class="glyphicon  glyphicon glyphicon-info-sign"' +
		'				aria-hidden="true"></span>' +
		'		</button>' +
		'		<button type="button" class="btn btn-default"' +
		'			aria-label="Right Align" onclick="getNewItem('+ item +')">' +
		'			<span class="glyphicon  glyphicon-ok" aria-hidden="true"></span>' +
		'		</button>' +
		'	</div>';
		itemPosition++;
		return code;
	}	
	
	function getNewItem(id) {
		$("#movie"+id).fadeOut();
		
		if(itemPosition < arraySize){
			$("#movie"+id).fadeIn();		

			document.getElementById("movie".concat(id)).innerHTML = nextItem();
			document.getElementById("movie".concat(id)).id = "movie"+ arrayOfIds[itemPosition-1];
		}
	}
	$( document ).ready(function() {
		getNewItem("First");
		getNewItem("Second");
		getNewItem("Third");
		getNewItem("Fourth");
	});
</script>

	<div class="container">
		<div class="header clearfix">
			<h3 class="text-muted">Item-Item Similarity</h3>
		</div>

		<div class="jumbotron">
			<div class="row">
				<div class="col-xs-6 col-lg-4">
					<h2>${selectedMovieTitle}</h2>
					<img
						src="https://image.tmdb.org/t/p/w780/y7N276seR43H31xvDbWMWxEsDr.jpg"
						alt="Mountain View" class="img-responsive">
				</div>
				<div class="col-xs-6 col-lg-8">
					<h2>Synopsys</h2>
					<p>Donec id elit non mi porta gravida at eget metus. Fusce
						dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh,
						ut fermentum massa justo sit amet risus. Etiam porta sem malesuada
						magna mollis euismod. Donec sed odio dui.</p>
					<%-- <p>${synopsys}</p> --%>
				</div>
			</div>

		</div>

		<div class="row">
			<h2>Are these movies similar to ${selectedMovieTitle}?</h2>


				<div id="movieFirst" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieSecond" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieThird" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieFourth" class="col-xs-3 col-lg-3">
				</div>

		</div>

	</div>
</body>
</html>