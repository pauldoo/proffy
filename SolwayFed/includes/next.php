<?php
require_once ("c:\sites_web\sarah_solwayFed\admin\utils.php");

$dbh = csConnect();
$result = csExecuteQuery($dbh,
    "SELECT * FROM csEvents WHERE date >= CURRENT_DATE() ORDER BY date LIMIT 0,1");

if ($row = mysql_fetch_assoc($result)) {
    $friendlyDate = date('d/m/Y', strtotime($row["date"]));
?>
<script src="../Scripts/AC_RunActiveContent.js" type="text/javascript"></script>

    <h1>Next Race</h1>
    <a href="/RaceCalendar/details.php?id=<?php echo htmlspecialchars($row["id"]); ?>"><img src="/includes/thumbnail.php?s=181&path=../<?php echo htmlspecialchars($row["imageFilename"]); ?>" alt="raceImage"/></a>
    <p><?php echo htmlspecialchars($row["details"]); ?><br/>
    <a href="/RaceCalendar/details.php?id=<?php echo htmlspecialchars($row["id"]); ?>">read more...</a></p>

    <!--
    <div class="race">
    <a href="#"><img src="../<?php echo htmlspecialchars($row["imageFilename"]); ?>" alt="raceImage"/></a>
    <p><span><strong>Location:</strong></span><?php echo htmlspecialchars($row["racepoint"]); ?><br/>
    <span><strong>Date:</strong></span><?php echo htmlspecialchars($friendlyDate); ?></p>
    <p><?php echo htmlspecialchars($row["details"]); ?><br/>
    <a href="#">read more...</a></p>
    </div>
    -->

<?php
} else {
?>
<h1>Image Gallery</h1>

	<div style="margin:0 auto;"><script type="text/javascript">
AC_FL_RunContent( 'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0','width','180','height','135','src','/flash/homepage','quality','high','pluginspage','http://www.macromedia.com/go/getflashplayer','movie','/flash/homepage' ); //end AC code
</script><noscript><object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="180" height="135">
      <param name="movie" value="../flash/homepage.swf" />
      <param name="quality" value="high" />
      <embed src="/flash/homepage.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="180" height="135"></embed>
</object>
	</noscript></div>
	<p>An <a href="/Gallery">image gallery</a> of this year's Solway Federation National winners is now available online.</p>

<?php
}
?>

