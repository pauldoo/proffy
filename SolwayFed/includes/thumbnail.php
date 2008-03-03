<?php
if(isset($_GET['s'])) {
    $wym=(int)$_GET['s'];
} else {
    $wym=100;
}
$error=0;
define('BLAD','Image Opening Error!');
if($error==0) {

    $zdj=$_GET['path'];
    if($zdj!='') {
        $zdj=$zdj;

        if(strtolower(substr($zdj,-3))=='jpg') {
            $im=@imagecreatefromjpeg($zdj);
        } else {
            $error=1;
        }

        if($im!=FALSE&&$error==0) {
            $w=imagesx($im); // original width
            $w2=$w;
            $h=imagesy($im); // original height
            $h2=$h;

            if($wym>0&&$w>$wym) {
                // decrease x
                $w2=$wym;
                $h2=floor($wym*$h/$w);
            }
            $thumb=imagecreatetruecolor($w2,$h2);

            if($thumb!=FALSE) {
                imagecopyresampled($thumb,$im,0,0,0,0,$w2,$h2,$w,$h); // new small one
                imagedestroy($im);
                $czarny=imagecolorallocate($thumb,82,130,127);
                imagepolygon($thumb,array(0,0,$w2-1,0,$w2-1,$h2-1,0,$h2-1),4,$czarny); // border
            } else {
                $error=1;
            }
        } else {
            $error=1;
        }

        if($error==0) {
            header("Content-type: image/jpeg");
            imagejpeg($thumb,'',90);
            imagedestroy($thumb);
        } else {
            echo BLAD;
        }
    } else {
        echo BLAD;
    }
} else {
    echo BLAD;
}
?>

