<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/general.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>The Solway Federation of Racing Pigeon Societies - Race Calendar</title>
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
                    $id = $_GET["id"] + 0;
                    $query = "SELECT * FROM csEvents WHERE id=" . $id;
                    $result = csExecuteSingleRowQuery($dbh, $query);
                    $friendlyDate = date('l jS F Y', strtotime($result["date"]));
                    ?>
                    <h1><?php echo htmlspecialchars($result["racepoint"]); ?></h1>
                    <h2><?php echo htmlspecialchars($friendlyDate); ?></h2>
                    <img src="/includes/thumbnail.php?s=200&path=../<?php echo htmlspecialchars($result["imageFilename"]); ?>" class="floatright" />
                    <p><?php echo htmlspecialchars($result["details"]); ?></p>
                    <div id="map" style="width:450px; height:300px; clear: both;"></div>
                    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAYrFofuFPMX8rrxchY-dtbRSEIYkWFGd2J2hsP_ngrZQDjJ82fRQXX-HGn-6ftv7DzfC1PhNNQztHRQ"
      type="text/javascript"></script>
                    <script type="text/javascript">
                            function load() {
                                      if (GBrowserIsCompatible()) {
                                        var map = new GMap2(document.getElementById("map"));
                                        map.addControl(new GSmallMapControl());
                                        map.addControl(new GMapTypeControl());

                                        var point = new GLatLng(<?php echo htmlspecialchars($result["latitude"]); ?>, <?php echo htmlspecialchars($result["longitude"]); ?>);
                                        map.setCenter(point, 13);
                                        var marker = new GMarker(point);
                                        map.addOverlay(marker);
                                        map.setMapType(G_HYBRID_MAP);
                                      }
                                    }
                            window.onload = load;
                            window.onunload = GUnload;
                    </script>


			<!-- InstanceEndEditable --></div>
			<div id="rightCol">
			   <? include("c:\sites_web\sarah_solwayFed\includes\\next.php"); ?>
			</div>
		</div>
	</div>
	<div id="footer">
		  <? include("c:\sites_web\sarah_solwayFed\includes\footer.php"); ?>
	</div>
</body>
<!-- InstanceEnd --></html>
