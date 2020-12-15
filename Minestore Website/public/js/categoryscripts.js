function openPopupMenu(name) {
  document.getElementById(name + "-popup").style.display = "block";
}

function closePopupMenu(name) {
  document.getElementById(name + "-popup").style.display = "none";
}

function buyItem(item) {
  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", "/requests/saveitem", true);
  xhttp.setRequestHeader('Content-Type', 'application/json');
  xhttp.send("item=" + item);
}

const elements = document.querySelectorAll("*");
window.onclick = (event) => {
  for(var i = 0; i < elements.length; i++) {
    if(!elements[i].id.endsWith('popup'))
      continue;
    if(event.target == elements[i]) {
      elements[i].style.display = "none";
    }
  }
}
