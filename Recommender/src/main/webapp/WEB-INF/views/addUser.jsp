<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add User</title>
<link href="<c:url value="/bootstrap/css/bootstrap.min.css"/>"
	rel="stylesheet">
	<link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
      <link rel="stylesheet" type="text/css" media="screen" href="http://silviomoreto.github.io/bootstrap-select/stylesheets/bootstrap-select.css">
 
<style>
h4 {
	text-align: center;
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
		src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
      <script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/js/bootstrap.min.js"></script>
      <script src="http://silviomoreto.github.io/bootstrap-select/javascripts/bootstrap-select.js"></script>

	<script>
		

		function logout() {
			window.location.href = "<c:url value="j_spring_security_logout" />";
		}
		function manageAlgorithm(id) {
			if (id.charAt(0) == 'd')
				action = 0;
			else
				action = 1;

			algorithm = arrayOfAlgorithms[id.charAt(1)];
			$.ajax({
				url : "/Recommender/admin/algorithmManagement?algorithm="
						+ algorithm + "&action=" + action,
				type : "GET"
			});
			window.location.href = "/Recommender/admin/algorithm";
		}
		function addUser() {
			var username =  $('#username').val(); 
			var e = document.getElementById("userRole");
			var userRole = e.options[e.selectedIndex].text;
			
			if(username == ""){
				alert("username cannot be null!");
				return;
			}
					
			$.ajax({
				url : "/Recommender/admin/addUserAction?username="
						+ username + "&userRole=" + userRole,
				type : "GET"
			});
			
			window.location.href = "/Recommender/admin/userManagement";
		}
		$(document).ready(function() {
            $('.selectpicker').selectpicker();

		});
	</script>
	<div class="container">
		<div class="row">

			<div class="col-xs-3 col-lg-3">
				<a href="/Recommender/home" style="text-decoration: none"><h3
						class="text-muted">Find Similar</h3></a>
			</div>

			<div class="col-xs-1 col-lg-1 col-md-offset-8"
				style="margin-top: 20px;">
				<button class="btn btn-default" onclick="return logout()">
					Logout</button>
			</div>

			<!-- /.col-lg-6 -->

		</div>
		<div class="col-xs-2 col-lg-2 col-md-offset-5 "
			style="margin-top: 60px;">
			<h2 class="centerBlock">Add User</h2>

		</div>
		<form>
			<div class="col-xs-2 col-lg-2 col-md-offset-5 "style="margin-top: 60px;">
				<div class="form-group">
					<div class="input-group ">
						<input id="username" type="text" class="form-control"
							placeholder="Username" required>
					</div>
				</div>
				</div>
				<div class="col-xs-2 col-lg-2 col-md-offset-5 " >
				
				<div class="form-group">

					<div class="selector">
						<select id="userRole" class="selectpicker">
							<option>ROLE_USER</option>
							<option>ROLE_ADMIN</option>
						</select>
					</div>
				</div>

			</div>
		</form>
		<div class="col-xs-2 col-lg-2 col-md-offset-5 "
			style="margin-top: 20px;">
			<button class="btn btn-default centerBlock"
				onclick="return addUser()">Add User</button>
		</div>
	</div>
</body>
</html>