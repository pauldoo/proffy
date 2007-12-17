<?php
require "utils.php";

$action = $_GET["action"];
switch($action) {
    case "add":
        $dbh = csConnect();
        csAddEvent($dbh);
        break;
    case "install":
        $dbh = csConnect();
        csCreateTables($dbh);
        break;
    default:
        die("Unknown action: " . $action);
}

?>

