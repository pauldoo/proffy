// JavaScript Document
function choose(){
	fullURL = parent.document.URL;
	var imageLink = fullURL.substring(fullURL.indexOf('=')+1, fullURL.length);	
	
	document.write("<img src='"+imageLink+"' alt='The Richards family album' />");
}

function chooseBanner(){
	var no = 0;
	while(no<1){
		var no = Math.round(5*Math.random());
	}
	document.write("<img src='images/banner"+no+".jpg' alt='The Richards family online album' />");
}