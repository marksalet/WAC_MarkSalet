var fetchoptions = { method: "DELETE", headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("myJWT") }};

function initLogin(){
	document.getElementById("login_button").addEventListener("click", function(){login()});
}

function initLogout(){
	document.getElementById("logout_button").addEventListener("click", function(){logout()});
}

function initRegister(){
	document.getElementById("register_button").addEventListener("click", function(){register()});
}

function login(){
	var formData = new FormData(document.querySelector("#login_form"));
	var encData = new URLSearchParams(formData.entries());
	document.querySelector("#error").innerHTML = "";
	
	fetch("/scouting/restservices/authentication", {method: 'POST', body: encData, fetchoptions})
	.then((response) => { 
		if (response.ok) {
			return response.json();
		} else {
			document.querySelector("#error").innerHTML = "Inlog mislukt!";
		}
	})
	.then((myJson) => {
		window.sessionStorage.setItem("sessionToken", myJson.JWT);
		window.sessionStorage.setItem("loggedIn", true);
		fetch("/scouting/restservices/authentication/boat", {method: 'POST', body: encData, fetchoptions})
		.then((response) => { 
			if (response.ok) {
				return response.json();
			} else {
				console.log('No Boat');
			}
		})
		.then((myJson) => {
			console.log('YEET');
			window.sessionStorage.setItem("BoatNumber", myJson.BoatNumber);
			window.location.href = "boatList.html";
		});
	});
}

function logout(){
	window.sessionStorage.removeItem("sessionToken");
	window.sessionStorage.removeItem("BoatNumber");
	window.sessionStorage.removeItem("loggedIn");
}

function register(){
	var formData = new FormData(document.querySelector("#register_form"));
	var encData = new URLSearchParams(formData.entries());
	
	fetch("/scouting/restservices/register", {method: 'POST', body: encData})
	.then((response) => {
		if (response.ok) {
			return alert("Je registratie wordt gecontroleerd door een leiding gevevend lid.");
		} else {
			console.log("Register Failed");
		}
	})
}

function initBoatList(){
	checkLoggedIn();
	if (window.sessionStorage.getItem("BoatNumber") > 0) {
		loadBoat(window.sessionStorage.getItem("BoatNumber"));
	} else if (window.sessionStorage.getItem("BoatNumber") == 0) {
		loadBoatList();
	}
	initLogout();
	document.getElementById("addButton").addEventListener("click", function(){document.getElementById("modalAdd").style.display = "block";});
	document.getElementById("submit").addEventListener("click", function(){updateBoat()});
	document.getElementById("submit_toevoegen").addEventListener("click", function(){createBoat()});
	document.getElementById("submit_onderdelen").addEventListener("click", function(){updateOnderdelen()});
}

function initUserList(){
	checkLoggedIn();
//	if (window.sessionStorage.getItem("BoatNumber") > 0) {
//		loadBoat(window.sessionStorage.getItem("BoatNumber"));
//	} else if (window.sessionStorage.getItem("BoatNumber") == 0) {
//		loadBoatList();
//	}
	loadUserList();
	initLogout();
//	document.getElementById("addButton").addEventListener("click", function(){document.getElementById("modalAdd").style.display = "block";});
//	document.getElementById("submit").addEventListener("click", function(){updateBoat()});
//	document.getElementById("submit_toevoegen").addEventListener("click", function(){createBoat()});
}

function checkLoggedIn(){
	if(window.sessionStorage.getItem("loggedIn") == true){
	} else if(window.sessionStorage.getItem("loggedIn") == null || window.sessionStorage.getItem("loggedIn") == false){
		window.location.href = "index.html";
	}
}

function loadBoatList(){
	fetch("/scouting/restservices/boatlist")
	.then(response => response.json())
	.then(function(myJson){
		for(let value of myJson){
			var div = document.createElement('div');
			div.className = 'boot-content';
			div.id = 'boot-content-' + value.Nummer;
			var number = document.createElement('div');
			number.className = 'number-content';
			var name = document.createElement('div');
			name.className = 'name-content';
			var maintenance = document.createElement('div');
			maintenance.className = 'maintenance-content';
			var view = document.createElement('div');
			view.className = 'view-content';
			var update = document.createElement('div');
			update.className = 'update-content';
			var deletion = document.createElement('div');
			deletion.className = 'deletion-content';
				
			number.id = value.Nummer;
			number.innerHTML = value.Nummer;
			name.innerHTML = value.Nummer + " - " + value.Naam;
			maintenance.innerHTML = "Lengte: " + value.Lengte + "0m | Breedte: " + value.Breedte + "0m | Hoogte: " + value.Hoogte + "0m | Diepgang: " + value.Diepgang + "0m | Onderhoud: " + value.Onderhoud;
			view.innerHTML= "<button type='button' nummer='" + value.Nummer + "' class='onderdelen' zwaard='" + value.Zwaard + "' roer='" + value.Roer + "' mast='" + value.Mast + "' giek='" + value.Giek + "' gaffel='" + value.Gaffel + "' grootzeil='" + value.Grootzeil + "' fok='" + value.Fok + "' vallen='" + value.Vallen + "' doften='" + value.Doften + "' vlonders='" + value.Vlonders + "' wrikriem='" + value.Wrikriem + "' roeiriem='" + value.Roeiriem + "' dollen='" + value.Dollen + "' >Onderdelen</button>";
			update.innerHTML = "<button type='button' nummer='" + value.Nummer + "' class='wijzig' naam='" + value.Naam + "' lengte='" + value.Lengte + "' breedte='" + value.Breedte + "' hoogte='" + value.Hoogte + "' diepgang='" + value.Diepgang + "' onderhoud='" + value.Onderhoud + "' >Wijzigen</button>";
			deletion.innerHTML = "<button type='button' class='delete' code='" + value.Nummer + "'>Delete</button>";
				
			document.getElementById('boten').appendChild(div);
			//document.getElementById('boot-content-' + value.Nummer).appendChild(number);
			document.getElementById('boot-content-' + value.Nummer).appendChild(name);
			document.getElementById('boot-content-' + value.Nummer).appendChild(maintenance);
			document.getElementById('boot-content-' + value.Nummer).appendChild(view);
			document.getElementById('boot-content-' + value.Nummer).appendChild(update);
			document.getElementById('boot-content-' + value.Nummer).appendChild(deletion);
		}
		document.querySelectorAll(".delete").forEach(del => {
			del.addEventListener("click", function(){deleteBoat(del.getAttribute("code"));});
		});
		
		document.querySelectorAll(".onderdelen").forEach(od => {
			od.addEventListener("click", function(){openModalOnderdelen(od.getAttribute("nummer"), od.getAttribute("zwaard"), od.getAttribute("roer"), od.getAttribute("mast"), od.getAttribute("giek"), od.getAttribute("gaffel"), od.getAttribute("grootzeil"), od.getAttribute("fok"), od.getAttribute("vallen"), od.getAttribute("doften"), od.getAttribute("vlonders"), od.getAttribute("wrikriem"), od.getAttribute("roeiriem"), od.getAttribute("dollen"));});
		});
		
		document.querySelectorAll(".wijzig").forEach(wijz => {
			wijz.addEventListener("click", function(){openModal(wijz.getAttribute("nummer"), wijz.getAttribute("naam"), wijz.getAttribute("lengte"), wijz.getAttribute("breedte"), wijz.getAttribute("hoogte"), wijz.getAttribute("diepgang"), wijz.getAttribute("onderhoud"))});
		});
	});
}

function loadBoat(number) {
	fetch("/scouting/restservices/boatlist/" + number)
	.then(response => response.json())
	.then(function(myJson){
		for(let value of myJson){
			var div = document.createElement('div');
			div.className = 'boot-content';
			div.id = 'boot-content-' + value.Nummer;
			var number = document.createElement('div');
			number.className = 'number-content';
			var name = document.createElement('div');
			name.className = 'name-content';
			var maintenance = document.createElement('div');
			maintenance.className = 'maintenance-content';
			var view = document.createElement('div');
			view.className = 'view-content';
			
			number.id = value.Nummer;
			number.innerHTML = value.Nummer;
			name.innerHTML = value.Nummer + " - " + value.Naam;
			maintenance.innerHTML = "Lengte: " + value.Lengte + "0m | Breedte: " + value.Breedte + "0m | Hoogte: " + value.Hoogte + "0m | Diepgang: " + value.Diepgang + "0m | Onderhoud: " + value.Onderhoud;
			view.innerHTML= "<button type='button' nummer='" + value.Nummer + "' class='wijzig' naam='" + value.Naam + "' onderhoud='" + value.Onderhoud + "' >Onderdelen</button>";
			
			document.getElementById('boten').appendChild(div);
			//document.getElementById('boot-content-' + value.Nummer).appendChild(number);
			document.getElementById('boot-content-' + value.Nummer).appendChild(name);
			document.getElementById('boot-content-' + value.Nummer).appendChild(maintenance);
			document.getElementById('boot-content-' + value.Nummer).appendChild(view);
		}
	});
}

function createBoat(){
	var formData = new FormData(document.querySelector("#toevoegen"));		
	var encData = new URLSearchParams(formData.entries());
	
	fetch('/scouting/restservices/boatlist', {method: 'POST', body: encData, headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((response) => {
		if (response.status == 402) { 
			console.log("Error, fout");
		}
		console.log(response);
	});
	document.getElementById("toevoegen").reset();
}

function updateBoat(){
	var nummer = document.getElementById("nummerModal").value;
	var formData = new FormData(document.querySelector("#wijzigen"));
	var encData = new URLSearchParams(formData.entries());
	
	fetch("/scouting/restservices/boatlist/" + nummer, {method: 'PUT', body: encData, headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((myJson) => {
		console.log(myJson);
	});
	
	modal.style.display = "none";
}

function updateOnderdelen(){
	var nummer = document.getElementById("nummerOnderdelen").value;
	var formData = new FormData(document.querySelector("#onderdelen"));
	var encData = new URLSearchParams(formData.entries());
	for (var p of encData.entries()) {
		console.log(p);
	}
	fetch("/scouting/restservices/boatlist/onderdelen/" + nummer, {method: 'PUT', body: encData, headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((myJson) => {
		console.log(myJson);
	});
	
	modal.style.display = "none";
}

function deleteBoat(code){
	console.log(code);
	if(confirm("Weet je zeker dat je de " + code + " wilt verwijderen?")){
		deleteInit(code);
	} else {
		console.log("Verwijderen cancelled")
	}
}

function deleteInit(code){
	fetch('/scouting/restservices/boatlist/' + code, {method: 'DELETE', headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((response) => {
		if (response.status == 200) { 
			console.log("Verwijderd");
			location.reload();
		}
});
}

function loadUserList(){
	fetch("/scouting/restservices/userlist")
	.then(response => response.json())
	.then(function(myJson){
		for(let value of myJson){
			var div = document.createElement('div');
			div.className = 'user-content';
			div.id = 'user-content-' + value.ID;
			var name = document.createElement('div');
			name.className = 'name-user-content';
			var info = document.createElement('div');
			info.className = 'info-user-content';
			var approve = document.createElement('div');
			approve.className = 'update-user-content';
			var update = document.createElement('div');
			update.className = 'update-user-content';
			var deletion = document.createElement('div');
			deletion.className = 'deletion-user-content';
				
			name.innerHTML = value.Voornaam + " " + value.Tussenvoegsel + value.Achternaam;
			info.innerHTML = "Email: " + value.Email + " | Telnr.: " + value.Telefoon + " | Role: " + value.Role + " | Boot: " + value.Boot_Nummer;
			approve.innerHTML = "<button type='button' userid='" + value.ID + "' role='Lid' class='approve'>Maak lid</button><br><button type='button' userid='" + value.ID + "' role='Leiding' class='approve'>Maak leiding</button>";
			update.innerHTML = "<button type='button' userid='" + value.ID + "' class='wijzig' >Wijzigen</button>";
			deletion.innerHTML = "<button type='button' class='deleteUser' code='" + value.ID + "'>Delete</button>";
				
			if (value.Role == "Afwachten") {
				document.getElementById('usersApprove').appendChild(div);
				document.getElementById('user-content-' + value.ID).appendChild(name);
				document.getElementById('user-content-' + value.ID).appendChild(info);
				document.getElementById('user-content-' + value.ID).appendChild(approve);
				document.getElementById('user-content-' + value.ID).appendChild(deletion);
			} else {
				document.getElementById('users').appendChild(div);
				document.getElementById('user-content-' + value.ID).appendChild(name);
				document.getElementById('user-content-' + value.ID).appendChild(info);
				document.getElementById('user-content-' + value.ID).appendChild(update);
				document.getElementById('user-content-' + value.ID).appendChild(deletion);
			}
			
		}
		document.querySelectorAll(".deleteUser").forEach(del => {
			del.addEventListener("click", function(){deleteUser(del.getAttribute("code"));});
		});
		
		document.querySelectorAll(".approve").forEach(app => {
			app.addEventListener("click", function(){approveUser(app.getAttribute("userid"),app.getAttribute("role"));});
		});
			
		document.querySelectorAll(".wijzig").forEach(wijz => {
			wijz.addEventListener("click", function(){openModal(wijz.getAttribute("nummer"), wijz.getAttribute("naam"), wijz.getAttribute("lengte"), wijz.getAttribute("breedte"), wijz.getAttribute("hoogte"), wijz.getAttribute("diepgang"), wijz.getAttribute("onderhoud"))});
		});
	});
}

function deleteUser(code){
	console.log(code);
	if(confirm("Weet je zeker dat je de " + code + " wilt verwijderen?")){
		deleteInitUser(code);
	} else {
		console.log("Verwijderen cancelled")
	}
}

function deleteInitUser(code){
	fetch('/scouting/restservices/userlist/' + code, {method: 'DELETE', headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((response) => {
		if (response.status == 200) { 
			console.log("Verwijderd");
			location.reload();
		}
});
}

function approveUser(code, role){
	fetch('/scouting/restservices/userlist/' + role + '/' + code , {method: 'POST', headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((response) => {
		if (response.status == 200) { 
			console.log("Role updates");
			location.reload();
		}
});
}

function openModal(nummer, naam, lengte, breedte, hoogte, diepgang, onderhoud){
	modal.style.display = "block";
	document.getElementById("nummerModal").value = nummer;
	document.getElementById("naam").value = naam;
	document.getElementById("lengte").value = lengte;
	document.getElementById("breedte").value = breedte;
	document.getElementById("hoogte").value = hoogte;
	document.getElementById("diepgang").value = diepgang;
	document.getElementById("onderhoud").value = onderhoud;
}

function openModalOnderdelen(Nummer, Zwaard, Roer, Mast, Giek, Gaffel, Grootzeil, Fok, Vallen, Doften, Vlonders, Wrikriem, Roeiriem, Dollen){
	modalOnderdelen.style.display = "block";
	document.getElementById("nummerOnderdelen").value = Nummer;
	document.getElementById("Zwaard").value = Zwaard;
	document.getElementById("Roer").value = Roer;
	document.getElementById("Mast").value = Mast;
	document.getElementById("Giek").value = Giek;
	document.getElementById("Gaffel").value = Gaffel;
	document.getElementById("Grootzeil").value = Grootzeil;
	document.getElementById("Fok").value = Fok;
	document.getElementById("Vallen").value = Vallen;
	document.getElementById("Doften").value = Doften;
	document.getElementById("Vlonders").value = Vlonders;
	document.getElementById("Wrikriem").value = Wrikriem;
	document.getElementById("Roeiriem").value = Roeiriem;
	document.getElementById("Dollen").value = Dollen;
}

//Get the modal
var modal = document.getElementById("myModal");
var modalAdd = document.getElementById("modalAdd");
var modalOnderdelen = document.getElementById("modalOnderdelen");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];
var spanAdd = document.getElementsByClassName("closeAdd")[0];
var spanOnderdelen = document.getElementsByClassName("closeOnderdelen")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}

spanAdd.onclick = function() {
	  modalAdd.style.display = "none";
}

spanOnderdelen.onclick = function() {
	  modalOnderdelen.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  } else if (event.target == modalAdd) {
	modalAdd.style.display = "none";
  } else if (event.target == modalOnderdelen) {
	modalOnderdelen.style.display = "none";
  }
}
