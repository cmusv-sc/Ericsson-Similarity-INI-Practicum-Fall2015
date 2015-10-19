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
		'<a href = "http://localhost:8080/Recommender/itemSimilarity?item='+item+'">'+'<img' +
		'	src="'+"${poster}"+'"' +
		'	alt="Mountain View" class="img-responsive"></a>';
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
		getNewItem("Fifth");
		getNewItem("Sixth");
		getNewItem("Seventh");
		getNewItem("Eighth");
		getNewItem("Ninth");
		getNewItem("Tenth");
		getNewItem("Eleventh");
		getNewItem("Twelth");
	});
</script>

	<div class="container">
		<div class="header clearfix">
			<h3 class="text-muted">Item-Item Similarity</h3>
		</div>


		<div class="row">

				<div id="movieFirst" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieSecond" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieThird" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieFourth" class="col-xs-3 col-lg-3">
				</div>

		</div>
		<div class="row">

				<div id="movieFifth" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieSixth" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieSeventh" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieEighth" class="col-xs-3 col-lg-3">
				</div>

		</div>
		<div class="row">

				<div id="movieNinth" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieTenth" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieEleventh" class="col-xs-3 col-lg-3">
				</div>
				<div id="movieTwelth" class="col-xs-3 col-lg-3">
				</div>

		</div>

	</div>
</body>
</html>