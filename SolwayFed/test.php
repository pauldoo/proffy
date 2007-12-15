<?php

function csConnect()
{
    include "config.php";

    $dbh = mysql_connect($dbhost, $dbuser, $dbpassword) or
        die("mysql_connect failed: " . mysql_error());

    mysql_select_db($dbname, $dbh) or
        die("mysql_select_db failed: " . mysql_error());

    return $dbh;
}

function csExecuteStatement($dbh, $statement)
{
    mysql_query($statement) or
        die("mysql_query failed: " . mysql_error());
}

function csCreateTables($dbh)
{
    csExecuteStatement($dbh, "DROP TABLE IF EXISTS csEvents;");
    csExecuteStatement($dbh,
        "CREATE TABLE csEvents (
            id INTEGER NOT NULL AUTO_INCREMENT,
            racepoint VARCHAR(200),
            date DATE,
            mapLatitude REAL,
            mapLongitude REAL,
            info TEXT,
            image MEDIUMBLOB,
            imageMimeType VARCHAR(100),
            PRIMARY KEY (id),
            UNIQUE KEY (date));");
}

$dbh = csConnect();

$action = $_GET['action'];

switch($action) {
    case "view":
        break;
    case "install":
        csCreateTables($dbh);
        break;
    default:
        die("Unknown action: " . $action);
}

echo "OK\n";
?>

