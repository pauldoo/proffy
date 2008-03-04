// JavaScript Document
function alertSize() {

  window.alert( 'Width = ' + myWidth );
  window.alert( 'Height = ' + myHeight );
}


function heights(){

	var topBanner = document.getElementById("topBanner");
	var footer = document.getElementById("footer");
	var belowNav = document.getElementById("belowNav");
	var contentArea = document.getElementById("contentArea");
	var rightCol = document.getElementById("rightCol");

  var myHeight = 0;
  if( typeof( window.innerWidth ) == 'number' ) {
    //Non-IE
    myHeight = window.innerHeight;
  } else if( document.documentElement &&
      ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
    //IE 6+ in 'standards compliant mode'
    myHeight = document.documentElement.clientHeight;
  } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
    //IE 4 compatible
    myHeight = document.body.clientHeight;
  }

  myHeight = myHeight - 35;

	if (belowNav.offsetHeight < (myHeight- footer.offsetHeight - topBanner.offsetHeight)){
		belowNav.style.height = (myHeight - footer.offsetHeight - topBanner.offsetHeight)+'px';
		contentArea.style.height = (myHeight - footer.offsetHeight - topBanner.offsetHeight)+'px';
	}

	rightCol.style.height = (contentArea.offsetHeight - 40)+'px';


}

function goWithConfirm(url, message)
{
    if (confirm(message) == true) {
        window.location = url;
    }
}
