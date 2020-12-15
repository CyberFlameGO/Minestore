function loadError(element, errorId) {
	if(element == false || element == null)
		return;

	switch(errorId) {
		case 1:
			element.innerHTML = "Please fill all of the required fields before continuing!";
			element.style.color = "red";
			element.style.display = "block";
			break;
		case 2:
			element.innerHTML = "This Minecraft username does not exist!";
			element.style.color = "red";
			element.style.display = "block";
			break;
		case 3:
			element.innerHTML = "The authentication status received was not valid. Please authenticate again!";
			element.style.color = "red";
			element.style.display = "block";
			break;
		case 4:
			element.innerHTML = "Could not connect to the Minecraft Server!";
			element.style.color = "red";
			element.style.display = "block";
			break;
		default:
			element.style.display = "none";
			break;
	}
}

function displayAlert(element, message, color) {
	if(element == false || element == null)
		return;

	element.innerHTML = message;
	element.style.color = color;
	element.style.display = "block";
}
