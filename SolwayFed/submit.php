<!--

    This file accepts HTTP requests and modifies the database accordingly. It
    does not present any information to the user, and instead should redirect to
    another URL once the database modifications have been made.

-->
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

