<?php
/**

    An include used by the other php files. Never requested directly by the
    browser.

*/

date_default_timezone_set("Europe/London") or
    die("date_default_timezone_set failed");

/**
    Connect to the MySQL server, switch to the correct database,
    and assert that everything worked.
*/
function csConnect()
{
    require "config.php";

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
    Executes an SQL query that is expected to return just a single row.
*/
function csExecuteSingleRowQuery($dbh, $query)
{
    $result = mysql_query($query, $dbh) or
        die("mysql_query failed: " . mysql_error());
    $row = mysql_fetch_assoc($result) or
        die("No entry found");
    mysql_fetch_assoc($result) and
        die("Multiple entries found");

    return $row;
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

    $day = $_POST["day"] + 0;
    $month = $_POST["month"] + 0;
    $year = $_POST["year"] + 0;
    $date = $year."-".$month."-".$day;

    $statement = "INSERT INTO csEvents (racepoint, date, latitude, longitude, details) VALUES(".
        "\"" . mysql_real_escape_string($racepointName) . "\", " .
        "\"" . $date . "\", " .
        $latitude . ", " .
        $longitude . ", " .
        "\"" . mysql_real_escape_string($details) . "\");";

    csExecuteStatement($dbh, $statement);

    $id = mysql_insert_id($dbh);

    csHandleImageUpload($dbh, $id);

    header("Location: form.php?action=view&id=" . $id);
}

/**
    Read the $_POST variable and edit the existing event.
*/
function csEditEvent($dbh)
{
    $id = $_POST["id"] + 0;
    $racepointName = $_POST["racepointName"];
    $latitude = $_POST["latitude"] + 0.0;
    $longitude = $_POST["longitude"] + 0.0;
    $details = $_POST["details"];

    $day = $_POST["day"] + 0;
    $month = $_POST["month"] + 0;
    $year = $_POST["year"] + 0;
    $date = $year."-".$month."-".$day;

    $statement = "UPDATE csEvents SET " .
        "racepoint = \"" . mysql_real_escape_string($racepointName) . "\", " .
        "date = \"" . $date . "\", " .
        "latitude = " . $latitude . ", " .
        "longitude = " . $longitude . ", " .
        "details = \"" . mysql_real_escape_string($details) . "\" " .
        "WHERE id = " . $id . ";";

    csExecuteStatement($dbh, $statement);

    csHandleImageUpload($dbh, $id);

    header("Location: form.php?action=view&id=" . $id);
}

/**
    Saves the image that has been submitted in the $_FILES variable
    and copies the filename to the database.
*/
function csHandleImageUpload($dbh, $id)
{
    // TODO: Delete the previous image associated with this event.
    if (substr($_FILES["imageUpload"]["type"], 0, 6) == "image/") {
        $destinationFilename = "upload/" . $_FILES["imageUpload"]["name"];
        move_uploaded_file($_FILES["imageUpload"]["tmp_name"], $destinationFilename) or
            die("move_uploaded_file failed");
        $statement = "UPDATE csEvents SET " .
            "imageFilename = \"" . mysql_real_escape_string($destinationFilename) . "\" " .
            "WHERE id=" . $id . ";";
        csExecuteStatement($dbh, $statement);
    }
}
?>
