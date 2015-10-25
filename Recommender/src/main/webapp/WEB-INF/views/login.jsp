<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

<title>Movie Similarity System</title>

<!-- Bootstrap core CSS -->
<link href="<c:url value="/bootstrap/css/bootstrap.min.css"/>"
	rel="stylesheet">


</head>

<body>
	<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
	<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>
		$('#button').click(function() {
			window.location = "www.example.com/index.php?id=";
		});
	</script>

	<div class="container"></div>
	<div class="header clearfix ">
		<h3 class="text-muted col-md-offset-2">Item-Item Similarity</h3>
	</div>
	<div class="col-xs-6 col-lg-4  col-md-offset-4"
		style="position: relative; top: 50%; -webkit-transform: translateY(-50%); -ms-transform: translateY(50%); transform: translateY(50%);">
		<form class="form-signin" action="http://localhost:8080/Recommender/home">
			<h2 class="form-signin-heading">Please sign in</h2>
			<label for="inputEmail" class="sr-only">Email address</label> <input
				type="email" id="inputEmail" class="form-control"
				placeholder="Email address" required autofocus> <label
				for="inputPassword" class="sr-only">Password</label> <input
				type="password" id="inputPassword" class="form-control"
				placeholder="Password" required>
			<div class="checkbox">
				<label> <input type="checkbox" value="remember-me">
					Remember me
				</label>
			</div>
			
			<button id="button" class="btn btn-lg btn-primary btn-block">Sign in</button>
		</form>
	</div>

</body>
</html>
