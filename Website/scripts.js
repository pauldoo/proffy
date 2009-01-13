// JavaScript Documents

function generalHeights(){
	
		 var content = document.getElementById('content');
		 var rightCol = document.getElementById('rightCol');
		 var submenu = document.getElementById('submenu');
		 var extraBox = document.getElementById('extraBox');
		 
		 
    	 var h = Math.max(content.offsetHeight, rightCol.offsetHeight);
		 
   		 if (content.offsetHeight!=h) {
			 content.style.height=h+'px';
		 }
		 
		 if (rightCol.offsetHeight!=h){
			 
			 rightCol.style.height=h+'px';
			 var addHeight = h - submenu.offsetHeight - 8;
			 extraBox.style.height=addHeight+'px';
			 
		 }

	
}
