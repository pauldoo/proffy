<?php
require "utils.php";

$dbh = csConnect();
$result = csExecuteQuery($dbh,
    "SELECT * FROM csEvents ORDER BY date");

while ($row = mysql_fetch_assoc($result)) {
    $friendlyDate = date('d/m/Y', strtotime($row["date"]));
?>
    <p>
        <span><strong>Location:</strong></span><?php echo htmlspecialchars($row["racepoint"]); ?>
        <span><strong>Date:</strong></span><?php echo htmlspecialchars($friendlyDate); ?>
        <a href="form.php?action=view&id=<?php echo htmlspecialchars($row["id"]); ?>">View</a>
        <a href="form.php?action=edit&id=<?php echo htmlspecialchars($row["id"]); ?>">Edit</a>
        <a href="submit.php?action=delete&id=<?php echo htmlspecialchars($row["id"]); ?>">Delete</a>
    </p>
    <hr/>
<?php
}
?>

