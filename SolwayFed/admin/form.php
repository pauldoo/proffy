<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/general.dwt" codeOutsideHTMLIsLocked="false" -->
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
/**

    This file renders HTML and does not modify the database. The HTML presented
    is used to either display existing entries or allow the user to enter the
    details of a new entry.

*/

require "utils.php";

$action = $_GET["action"];
switch ($action) {
    case "add":
        ?>
<form action="submit.php?action=add" method="post" enctype="multipart/form-data" id="addRacepoint">
    <label for="racepointName">Racepoint Name:</label>
    <input type="text" name="racepointName" id="racepointName" />
    <label for="latitude">Latitude</label>
    <input type="text" name="latitude" id="latitude" />
    <label for="longitude">Longitude:</label>
    <input type="text" name="longitude" id="longitude" />
    <label for="details">Details:</label>
    <textarea name="details" id="details"></textarea>
    <label for="day">Date:</label>
    <br/>
    <select name="day">
        <option>1</option>
        <option>2</option>
        <option>3</option>
        <option>4</option>
        <option>5</option>
        <option>6</option>
        <option>7</option>
        <option>8</option>
        <option>9</option>
        <option>10</option>
        <option>11</option>
        <option>12</option>
        <option>13</option>
        <option>14</option>
        <option>15</option>
        <option>16</option>
        <option>17</option>
        <option>18</option>
        <option>19</option>
        <option>20</option>
        <option>21</option>
        <option>22</option>
        <option>23</option>
        <option>24</option>
        <option>25</option>
        <option>26</option>
        <option>27</option>
        <option>28</option>
        <option>29</option>
        <option>30</option>
        <option>31</option>
     </select>
     <select name="month" id="month">
        <option value="1">January</option>
        <option value="2">February</option>
        <option value="3">March</option>
        <option value="4">April</option>
        <option value="5">May</option>
        <option value="6">June</option>
        <option value="7">July</option>
        <option value="8">August</option>
        <option value="9">September</option>
        <option value="10">October</option>
        <option value="11">November</option>
        <option value="12">December</option>
      </select>
      <select name="year" id="year">
        <option>2008</option>
        <option>2009</option>
        <option>2010</option>
        <option>2011</option>
        <option>2012</option>
        <option>2013</option>
        <option>2014</option>
        <option>2015</option>
     </select>
     <label for="imageUpload">Image:</label>
     <input type="file" name="imageUpload" id="imageUpload" />

     <input type="submit" class="submit" />
 </form>
        <?php
        break;

    case "edit":
        $dbh = csConnect();
        $id = $_GET["id"] + 0;
        $query = "SELECT * FROM csEvents WHERE id=" . $id;
        $result = csExecuteSingleRowQuery($dbh, $query);
        $day = (int)date('d', strtotime($result["date"]));
        $month = (int)date('m', strtotime($result["date"]));
        $year = (int)date('Y', strtotime($result["date"]));
        $friendlyDate = date('d m Y', strtotime($result["date"]));
        $friendlyMonth = date('F', strtotime($result["date"]));
        ?>

<form action="submit.php?action=edit" method="post" enctype="multipart/form-data" id="addRacepoint">
    <input type="hidden" name="id" value="<?php echo $id; ?>"/>
    <label for="racepointName">Racepoint Name:</label>
    <input type="text" name="racepointName" id="racepointName" value="<?php echo htmlspecialchars($result["racepoint"]); ?>"/>
    <label for="latitude">Latitude</label>
    <input type="text" name="latitude" id="latitude" value="<?php echo htmlspecialchars($result["latitude"]); ?>"/>
    <label for="longitude">Longitude:</label>
    <input type="text" name="longitude" id="longitude" value="<?php echo htmlspecialchars($result["longitude"]); ?>"/>
    <label for="details">Details:</label>
    <textarea name="details" id="details"><?php echo htmlspecialchars($result["details"]); ?></textarea>
    <label for="day">Date:</label>
    <br/>
    <select name="day">
        <option selected><?php echo $day; ?></option>
        <option>1</option>
        <option>2</option>
        <option>3</option>
        <option>4</option>
        <option>5</option>
        <option>6</option>
        <option>7</option>
        <option>8</option>
        <option>9</option>
        <option>10</option>
        <option>11</option>
        <option>12</option>
        <option>13</option>
        <option>14</option>
        <option>15</option>
        <option>16</option>
        <option>17</option>
        <option>18</option>
        <option>19</option>
        <option>20</option>
        <option>21</option>
        <option>22</option>
        <option>23</option>
        <option>24</option>
        <option>25</option>
        <option>26</option>
        <option>27</option>
        <option>28</option>
        <option>29</option>
        <option>30</option>
        <option>31</option>
     </select>
     <select name="month" id="month" value="<?php echo $month; ?>">
        <option selected value="<?php echo $month; ?>"><?php echo $friendlyMonth; ?></option>
        <option value="1">January</option>
        <option value="2">February</option>
        <option value="3">March</option>
        <option value="4">April</option>
        <option value="5">May</option>
        <option value="6">June</option>
        <option value="7">July</option>
        <option value="8">August</option>
        <option value="9">September</option>
        <option value="10">October</option>
        <option value="11">November</option>
        <option value="12">December</option>
      </select>
      <select name="year" id="year" value="<?php echo $year; ?>">
        <option selected><?php echo $year; ?></option>
        <option>2008</option>
        <option>2009</option>
        <option>2010</option>
        <option>2011</option>
        <option>2012</option>
        <option>2013</option>
        <option>2014</option>
        <option>2015</option>
     </select>
     <label for="imageUpload">Image:</label>
     <input type="file" name="imageUpload" id="imageUpload"/>

     <input type="submit" class="submit" />
 </form>
        <?php
        break;

    case "view":
        $dbh = csConnect();
        $id = $_GET["id"] + 0;
        $query = "SELECT * FROM csEvents WHERE id=" . $id;
        $result = csExecuteSingleRowQuery($dbh, $query);
        $friendlyDate = date('d m Y', strtotime($result["date"]));
        ?>
Id: <?php echo $result["id"]; ?><br/>
Racepoint: <?php echo htmlspecialchars($result["racepoint"]); ?><br/>
Date: <?php echo htmlspecialchars($friendlyDate); ?><br/>
Latitude: <?php echo htmlspecialchars($result["latitude"]); ?><br/>
Longitude: <?php echo htmlspecialchars($result["longitude"]); ?><br/>
Details: <?php echo htmlspecialchars($result["details"]); ?><br/>
Image: <img src="<?php echo htmlspecialchars($result["imageFilename"]); ?>" /><br/>

<a href="form.php?action=edit&id=<?php echo $result["id"]; ?>">Edit</a><br/>
       <?php
        break;

    default:
        die("Unknown action: " . $action);
}
?>



			<!-- InstanceEndEditable --></div>
			<div id="rightCol">
			   <h1>This Week's Race</h1>
			   <img src="/images/image2.jpg" alt="Image" />
			   <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Mauris a odio at felis gravida tincidunt. Vestibulum vitae. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Mauris a odio at felis gravida tincidunt. Vestibulum vitae. <a href="#">more...</a></p>
			</div>
		</div>
	</div>
	<div id="footer">
		  <? include("c:\sites_web\sarah_solwayFed\includes\footer.php"); ?>
	</div>
</body>
<!-- InstanceEnd --></html>
