<!--

    This file renders HTML and does not modify the database. The HTML presented
    is used to either display existing entries or allow the user to enter the
    details of a new entry.

-->
<?php
require "utils.php";

$action = $_GET["action"];
switch ($action) {
    case "add":
        ?>
<form action="submit.php?action=add" method="post" enctype="multipart/form-data" id="addRacepoint">
    <label for="racepointName">Racepoint Name:</label>
    <input type="text" name="racepointName" id="racepointName" />
    <label for="latitude">Latitude</label>
    <input type="text" name="latitude" id="latitude" />
    <label for="longitude">Longitude:</label>
    <input type="text" name="longitude" id="longitude" />
    <label for="details">Details:</label>
    <textarea name="details" id="details"></textarea>
    <label for="day">Date:</label>
    <select name="day">
        <option>1</option>
        <option>2</option>
        <option>3</option>
        <option>4</option>
        <option>5</option>
        <option>6</option>
        <option>7</option>
        <option>8</option>
        <option>9</option>
        <option>10</option>
        <option>11</option>
        <option>12</option>
        <option>13</option>
        <option>14</option>
        <option>15</option>
        <option>16</option>
        <option>17</option>
        <option>18</option>
        <option>19</option>
        <option>20</option>
        <option>21</option>
        <option>22</option>
        <option>23</option>
        <option>24</option>
        <option>25</option>
        <option>26</option>
        <option>27</option>
        <option>28</option>
        <option>29</option>
        <option>30</option>
        <option>31</option>
     </select>
     <select name="month" id="month">
        <option value="1">January</option>
        <option value="2">February</option>
        <option value="3">March</option>
        <option value="4">April</option>
        <option value="5">May</option>
        <option value="6">June</option>
        <option value="7">July</option>
        <option value="8">August</option>
        <option value="9">September</option>
        <option value="10">October</option>
        <option value="11">November</option>
        <option value="12">December</option>
      </select>
      <select name="year" id="year">
        <option>2008</option>
        <option>2009</option>
        <option>2010</option>
        <option>2011</option>
        <option>2012</option>
        <option>2013</option>
        <option>2014</option>
        <option>2015</option>
     </select>
     <label for="imageUpload">Image:</label>
     <input type="file" name="imageUpload" id="imageUpload" />

     <input type="submit" class="submit" />
 </form>
        <?php
        break;

    case "view":
        $dbh = csConnect();
        $id = $_GET["id"] + 0;
        $query = "SELECT * FROM csEvents WHERE id=" . $id;
        $result = csExecuteSingleRowQuery($dbh, $query);
        ?>
Name: <?php echo $result["racepoint"]; ?><br/>
Date: <?php echo $result["date"]; ?><br/>
Details: <?php echo $result["details"]; ?><br/>
Image: <img src="<?php echo $result["imageFilename"]; ?>" /><br/>
        <?php
        break;

    default:
        die("Unknown action: " . $action);
}
?>


