<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User Management</title>
<link href="<c:url value="/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">
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
	<script>
		var arrayOfUsers = "${users}".split("||");
		var arrayOfUserRoles = "${userRoles}".split("||");

		function populateTable() {
			var myTableDiv = document.getElementById("Users")
			var table = document.getElementById("table")
			var tableBody = document.createElement('TBODY')

			table.appendChild(tableBody);
			var heading = new Array();
			heading[0] = "User";
			heading[1] = "UserRole";
			heading[2] = "";

			//TABLE COLUMNS
			var tr = document.createElement('TR');
			tableBody.appendChild(tr);
			for (i = 0; i < heading.length; i++) {
				var th = document.createElement('TH')
				th.width = '75';
				th.appendChild(document.createTextNode(heading[i]));
				tr.appendChild(th);
			}
			var j = 0;
			for (i = 0; i < arrayOfUsers.length; i++) {
				var tr = document.createElement('TR');

				var userCell = document.createElement('TD')
				userCell.appendChild(document.createTextNode(arrayOfUsers[i]));
				tr.appendChild(userCell)

				var userRolesCell = document.createElement('TD')
				userRolesCell.appendChild(document.createTextNode(arrayOfUserRoles[i]));
				tr.appendChild(userRolesCell)

				var deletebutton = document.createElement('button');
				deletebutton.innerHTML = "Delete";
				deletebutton.className = "btn btn-default";
				deletebutton.id = "delete".concat(i);

				var deleteButtonCell = document.createElement('TD');
				deleteButtonCell.appendChild(deletebutton);
				tr.appendChild(deleteButtonCell)

				
				tableBody.appendChild(tr);
			}

			myTableDiv.appendChild(table)
			for (i = 0; i < arrayOfUsers.length; i++) {
				document.getElementById("delete"+i).addEventListener("click", function(){
					deleteUser(this.id);
				});

			}
		}

		function contains(array, string) {
			for (j = 0; j < array.length; j++)
				if (array[j].localeCompare(string) == 0)
					return true;
		}

		function logout() {
			window.location.href = "/Recommender/<c:url value="j_spring_security_logout" />";
		}

		function deleteUser(id) {
			user = arrayOfUsers[id.charAt(6)];
			$.ajax({
				url : "/Recommender/admin/deleteUser?username="
						+ user,
				type : "GET"
			});
			window.location.href = "/Recommender/admin/userManagement";
		}

		function addUser() {
			window.location.href = "/Recommender/admin/addUser";
		}
		
		$(document).ready(function() {
			populateTable();
		});
	</script>
	<div class="container">
		<div class="row">

			<div class="col-xs-3 col-lg-3">
				<a href="/Recommender/home" style="text-decoration: none"><h3
						class="text-muted">Item-Item Similarity</h3></a>
			</div>

			<div class="col-xs-1 col-lg-1 col-md-offset-8"
				style="margin-top: 20px;">
				<button class="btn btn-default" onclick="return logout()">
					Logout</button>
			</div>

			<!-- /.col-lg-6 -->

		</div>
		<div id="Users" class="col-xs-4 col-lg-4 col-md-offset-4 "
			style="margin-top: 50px;">
			<h4 class="centerBlock">Users Management</h4>
			<table id="table" class="table centerBlock" style="margin-top: 20px;">
			</table>
		</div>
		<div class="col-xs-4 col-lg-4 col-md-offset-4 "
			style="margin-top: 20px;">
			<button class="btn btn-default centerBlock"
				onclick="return addUser()">Add user</button>
		</div>

	</div>
</body>
</html>