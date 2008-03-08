<? include("c:\sites_web\sarah_solwayFed\includes\checkLogin.php"); ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/admin.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>The Solway Federation of Racing Pigeon Societies - Privacy</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/css/solwayFed.css" />
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="/css/ieFixes.css" />
<!-[end if]-->
<link rel="stylesheet" type="text/css" href="/css/solwayFed_print.css" media="print" />
<script type="text/javascript" src="/scripts/solwayFed.js"></script>
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
</head>

<body onload="heights();" onresize="heights();" id="body">
	<div id="topBanner">
		<div id="search">
			 <? include("c:\sites_web\sarah_solwayFed\includes\search.php"); ?>
		</div>
		<div id="logo">
			<? include("c:\sites_web\sarah_solwayFed\includes\logo.php"); ?>
		</div>
		<div id="banner">
			  <? include("c:\sites_web\sarah_solwayFed\includes\banner.php"); ?>
		</div>
	</div>
	<div id="nav">
		<? include("c:\sites_web\sarah_solwayFed\includes\\navigation.php"); ?>
	</div>
	<div id="belowNav">
		<div id="contentArea">
			<div id="leftCol"><!-- InstanceBeginEditable name="EditRegion3" -->
<?php
require_once ("c:\sites_web\sarah_solwayFed\admin\utils.php");

$dbh = csConnect();
$result = csExecuteQuery($dbh,
    "SELECT * FROM csEvents ORDER BY date");
?>
    <table>
<?php

$count = 0;
while ($row = mysql_fetch_assoc($result)) {
    $friendlyDate = date('d/m/Y', strtotime($row["date"]));
    $count++;
    if ($count % 2 == 0) {
?>
        <tr class="even">
<?php
    } else {
?>
        <tr>
<?php
    }
?>
        <th><?php echo htmlspecialchars($row["racepoint"]); ?></th>
        <td><?php echo htmlspecialchars($friendlyDate); ?></td>
        <td><a href="form.php?action=view&id=<?php echo htmlspecialchars($row["id"]); ?>">View</a></td>
        <td><a href="form.php?action=edit&id=<?php echo htmlspecialchars($row["id"]); ?>">Edit</a></td>
        <td><a href="#" onClick="goWithConfirm('submit.php?action=delete&id=<?php echo htmlspecialchars($row["id"]); ?>', 'Are you sure you want to delete this event?');">Delete</a></td>
    </tr>
<?php
}
?>
    </table>
			<!-- InstanceEndEditable --></div>
			<div id="rightCol">
				<div id="submenu">
			   <? include("c:\sites_web\sarah_solwayFed\includes\adminSubmenu.php"); ?>
			   </div>
			</div>
		</div>
	</div>
	<div id="footer">
		  <? include("c:\sites_web\sarah_solwayFed\includes\footer.php"); ?>
	</div>
</body>
<!-- InstanceEnd --></html>
