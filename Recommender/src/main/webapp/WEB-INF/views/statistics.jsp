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
		

		function logout() {
			window.location.href = "<c:url value="j_spring_security_logout" />";
		}
		
		
		$(document).ready(function() {

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


	</div>
</body>
</html>