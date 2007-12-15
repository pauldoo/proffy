<?php

function sf_connect()
{
    include 'config.php';

    $dbh = mysql_connect($dbhost, $dbuser, $dbpassword) or
        die('mysql_connect failed: ' . mysql_error());

    mysql_select_db($dbname, $dbh) or
        die('mysql_select_db failed: ' . mysql_error());

    return $dbh;
}

$dbh = sf_connect();

$action = $_GET['action'];

switch($action) {
    case "view":
        ?>BOO<?php
        break;
    default:
        die("Unknown action: " . $action);
}

echo "OK\n";
?>

