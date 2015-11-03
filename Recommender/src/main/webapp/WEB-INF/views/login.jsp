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

	<div class="container"></div>
	<div class="header clearfix ">
		<h3 class="text-muted col-md-offset-2">Item-Item Similarity</h3>
	</div>

	<div class="col-xs-6 col-lg-4  col-md-offset-4"
		style="position: relative; top: 50%; -webkit-transform: translateY(-50%); -ms-transform: translateY(50%); transform: translateY(50%);">
		<c:if test="${not empty error}">
			<div class="alert alert-danger" role="alert">
				<span class="glyphicon glyphicon-exclamation-sign"
					aria-hidden="true"></span> <span class="sr-only">Error:</span>
				${error}
			</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="alert alert-warning" role="alert">
				<span class="glyphicon glyphicon-exclamation-sign"
					aria-hidden="true"></span> <span class="sr-only">Error:</span>
				${msg}
			</div>
		</c:if>
		<form class="form-signin"
			action="<c:url value='/j_spring_security_check' />" method="POST">
			<h2 class="form-signin-heading">Please sign in</h2>
			<label for="inputEmail" class="sr-only">Email address</label> 
			<input name='username' type="text" class="form-control" placeholder="Email address" required autofocus> 
			
			<label for="inputPassword" class="sr-only">Password</label> 
			<input name='password' type="password" class="form-control" placeholder="Password" required>
			<div class="checkbox">
			</div>

			<button id="submit" type="submit"
				class="btn btn-lg btn-primary btn-block">Sign in</button>
		</form>
	</div>

</body>
</html>
