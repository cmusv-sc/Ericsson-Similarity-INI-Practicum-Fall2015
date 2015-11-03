<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Settings</title>
<link href="<c:url value="/bootstrap/css/bootstrap.min.css"/>"
	rel="stylesheet">
</head>
<body>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.1/js/bootstrap.min.js"></script>
<script>
function checkPass()
{
    //Store the password field objects into variables ...
    var pass1 = document.getElementById('pwd');
    var pass2 = document.getElementById('confirmpwd');
    //Store the Confimation Message Object ...
    var message = document.getElementById('confirmMessage');
    //Set the colors we will be using ...
    var goodColor = "#66cc66";
    var badColor = "#ff6666";
    //Compare the values in the password field 
    //and the confirmation field
    if(pass1.value == pass2.value){
        //The passwords match. 
        //Set the color to the good color and inform
        //the user that they have entered the correct password 
        pass2.style.backgroundColor = goodColor;
        message.style.color = goodColor;
        message.innerHTML = "Passwords Match!"
    }else{
        //The passwords do not match.
        //Set the color to the bad color and
        //notify the user.
        pass2.style.backgroundColor = badColor;
        message.style.color = badColor;
        message.innerHTML = "Passwords Do Not Match!"
    }
}  
</script>
	<div class="container">
		<div class="row">

			<div class="col-xs-3 col-lg-3">
				<a href="/Recommender/home" style="text-decoration: none"><h3
						class="text-muted">Item-Item Similarity</h3></a>
			</div>


			<div class="col-xs-1 col-lg-1 col-md-offset-6"
				style="margin-top: 20px;">
				<button class="btn btn-default" onclick="return logout()">
					Logout</button>
			</div>

			<!-- /.col-lg-6 -->

		</div>
		<div class="row">
			<div class="form-group col-xs-3 col-lg-3 col-md-offset-3">
				<h2>Update Password</h2>
			</div>
		</div>
		<form method="POST" action="/Recommender/updatePassword">

			<div class="row">
				<div class="form-group col-xs-3 col-lg-3 col-md-offset-3">
					<label for="pwd">Password:</label> 
					<input type="password" class="form-control" id="pwd">
				</div>
			</div>

			<div class="row">

				<div class="form-group col-xs-3 col-lg-3 col-md-offset-3">
					<label for="pwd">Confirm Password:</label> 
					<input name="password" type="password" class="form-control" id="confirmpwd" onkeyup="checkPass(); return false;"/> 
					<span id="confirmMessage" class="confirmMessage"></span>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-3 col-lg-3 col-md-offset-3">
					<button type="submit" class="btn btn-default">Submit</button>
				</div>
			</div>
		</form>
</body>
</html>