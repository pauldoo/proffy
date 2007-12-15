<?php
/**
    Connect to the MySQL server, switch to the correct database,
    and assert that everything worked.
*/
function csConnect()
{
    include "config.php";

    $dbh = mysql_pconnect($dbhost, $dbuser, $dbpassword) or
        die("mysql_connect failed: " . mysql_error());

    mysql_select_db($dbname, $dbh) or
        die("mysql_select_db failed: " . mysql_error());

    return $dbh;
}

/**
    Execute an SQL statement and assert success.
*/
function csExecuteStatement($dbh, $statement)
{
    mysql_query($statement) or
        die("mysql_query failed: " . mysql_error());
}

/**
    Delete existing tables and recreate empty.
*/
function csCreateTables($dbh)
{
    csExecuteStatement($dbh, "DROP TABLE IF EXISTS csEvents;");
    csExecuteStatement($dbh,
        "CREATE TABLE csEvents (
            id INTEGER NOT NULL AUTO_INCREMENT,
            racepoint VARCHAR(200) NOT NULL,
            date DATE NOT NULL,
            latitude REAL NOT NULL,
            longitude REAL NOT NULL,
            details TEXT NOT NULL,
            imageFilename VARCHAR(200),
            PRIMARY KEY (id),
            UNIQUE KEY (date));");

    echo 'Tables created.';
}

/**
    Read the $_POST variable and insert the new event.
*/
function csAddEvent($dbh)
{
    $racepointName = $_POST["racepointName"];
    $latitude = $_POST["latitude"] + 0.0;
    $longitude = $_POST["longitude"] + 0.0;
    $details = $_POST["details"];

    $day = $_POST["day"];
    $month = $_POST["month"];
    $year = $_POST["year"];
    $date = mktime(0, 0, 0, $month, $day, $year);

    $statement = "INSERT INTO csEvents (racepoint, date, latitude, longitude, details) VALUES(".
        "\"" . mysql_real_escape_string($racepointName) . "\", " .
        $date . ", " .
        $latitude . ", " .
        $longitude . ", " .
        "\"" . mysql_real_escape_string($details) . "\");";

    echo $statement;

    /*
    if (substr($_FILES["imageUpload"]["type"], 0, 6) == "image/") {
        move_uploaded_file($_FILES["file"]["tmp_name"], "upload/" . $_FILES["file"]["name"]);
    }
    */
}

/**
    Entry point.
*/
function csMain()
{
    date_default_timezone_set("Europe/London") or
        die("date_default_timezone_set failed");

    $dbh = csConnect();

    $action = $_GET['action'];
    switch($action) {
        case "add":
            csAddEvent($dbh);
            break;
        case "install":
            csCreateTables($dbh);
            break;
        case "view":
            break;
        default:
            die("Unknown action: " . $action);
    }
}

csMain();

?>

