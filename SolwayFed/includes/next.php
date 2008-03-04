<?php
require "../admin/utils.php";

$dbh = csConnect();
$result = csExecuteQuery($dbh,
    "SELECT * FROM csEvents WHERE date >= CURRENT_DATE() ORDER BY date LIMIT 0,1");

if ($row = mysql_fetch_assoc($result)) {
    $friendlyDate = date('d/m/Y', strtotime($row["date"]));
?>
    <h1>This Week's Race</h1>
    <a href="#"><img src="/includes/thumbnail.php?s=181&path=../<?php echo htmlspecialchars($row["imageFilename"]); ?>" alt="raceImage"/></a>
    <p><?php echo htmlspecialchars($row["details"]); ?><br/>
    <a href="#">read more...</a></p>

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
<p>None!</p>

<?php
}
?>

