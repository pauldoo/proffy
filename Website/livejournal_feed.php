<?php

define('MAGPIE_DIR', '../private/magpierss/');
define('MAGPIE_CACHE_DIR', '../private/magpie_cache');

require_once(MAGPIE_DIR.'rss_fetch.inc');

$url = 'http://pdoo.livejournal.com/data/atom';
$items_to_print = 5;

$rss = fetch_rss( $url );
echo "<div class='Journal'>\n";
$count = 0;
$last_date = "";
foreach ($rss->items as $item) {
    echo "<div class='Entry'>\n";
    $link = $item['link'];
    $title = $item['title'];
    $date = date("D jS M, Y", parse_w3cdtf($item['published']));
    $content = $item['atom_content'];
    if ($date != $last_date) {
        echo "<h2>$date</h2>\n";
        $last_date = $date;
    }
    echo "<p><a class='EntryTitle' href='$link'>$title</a></p>\n";
    echo "<p>$content</p>\n";
    echo "</div>\n";
    $count ++;
    if ($count >= $items_to_print) {
        break;
    }
}
echo "</div>\n";
?>

