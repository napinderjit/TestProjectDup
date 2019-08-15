<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>FORM</title>
</head>
<style>
input[type=text], select {
	width: 100%;
	padding: 12px 20px;
	margin: 8px 0;
	display: inline-block;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

input[type=date], select {
	width: 100%;
	padding: 12px 20px;
	margin: 8px 0;
	display: inline-block;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

input[type=submit] {
	width: 50%;
	background-color: #4CAF50;
	color: white;
	padding: 14px 20px;
	margin: 8px 0;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

input[type=submit]:hover {
	background-color: #45a049;
}

div {
	border-radius: 5px;
	background-color: #b6f7ee;
	padding: 10px;
}
</style>
<body>
	<h2>Form to enter and extract data</h2>
		<h3> ${message}</h3>
	
		<div>
			<form method="post" action="Insert"
				onSubmit="return validateForm(this);">
				<table>
					<tr>
						<td><label for="issuename">Issue Name</label></td>
						<td><input type="text" id="issuename" name="IssueName"
							placeholder="Issue name.."></td>
					</tr>
					<tr>
						<td><label for="coupon">Coupon</label></td>
						<td><input type="text" id="coupon " name="Coupon"
							placeholder="Enter number"></td>
					</tr>
					<tr>
						<td><label for="denomination">Denomination</label></td>
						<td><input type="text" id="denomination" name="Denomination"
							placeholder="Enter decimals"></td>
					</tr>
					<tr>
						<td><label for="calldate1">Call Date 1</label></td>
						<td><input type="date" id="calldate1" name="CallDate1"></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="Submit" value="Submit"></td>
					</tr>
				</table>

			
			
			</form>
			<form method="post" action="Extract">

				<input type="Submit" value="Extract">
			
			</form>
			<form method="post" action="ExtractToExcel">

				<input type="Submit" value="Export to Excel">
			</form>
			
		</div>
	

	<script type="text/javascript">
		function validateForm(frm) {
			
			var max = 1000000000;
			var min = -1000000000;
			
			if (frm.IssueName.value == "" && frm.Coupon.value == ""
					&& frm.Denomination.value == ""
					&& frm.CallDate1.value == "") {
				alert("Enter Data into any Field");

				return false;
			} else if (isNaN(frm.Coupon.value) || isNaN(frm.Denomination.value)) {

				alert("Enter correct Datatypes");
				return false;

			}else if (frm.Coupon.value>max||frm.Denomination.value>max||frm.Coupon.value<min||frm.Denomination.value<min) {

				alert("Enter data within range");
				return false;

			} else {

				return true;
			}
		}
	</script>
		
</body>
</html>