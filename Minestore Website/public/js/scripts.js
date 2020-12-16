function loadError(element, errorId) {
	if(element == false || element == null)
		return;

	switch(errorId) {
		case 1:
			displayAlert(element, "Please fill all of the required fields before continuing!", "red");
			break;
		case 2:
			displayAlert(element, "This Minecraft username does not exist!", "red");
			break;
		case 3:
			displayAlert(element, "The authentication status received was not valid. Please authenticate again!", "red");
			break;
		case 4:
			displayAlert(element, "Could not connect to the Minecraft Server!", "red");
			break;
		case 5:
			displayAlert(element, "Package information was not found on the database", "red");
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
