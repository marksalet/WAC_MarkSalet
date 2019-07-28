	var fetchoptions = { method: "DELETE", headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("myJWT") }};


function initCalculator(){
	document.querySelectorAll("#btn").forEach(input => input.addEventListener("click", function(){
		document.getElementById("display").innerHTML += input.value;
	}));
	
	document.getElementById("btn_eq").addEventListener("click", function(){
		document.getElementById("display").innerHTML = eval(document.getElementById("display").innerHTML);
	});   
	
	document.getElementById("btn_c").addEventListener("click", function(){
		document.getElementById("display").innerHTML = "";
	});
}

function initPage(){
	if(sessionStorage.getItem("refreshTimer") == null || sessionStorage.getItem("refreshTimer") > new Date().getTime() + 600000){
		fetch('https://ipapi.co/json/')
			.then(response => response.json())
			.then(function(myJson){
				document.getElementById("ip").innerHTML += myJson.ip;
				document.getElementById("city").innerHTML += myJson.city;
				document.getElementById("region").innerHTML += myJson.region;
				document.getElementById("country").innerHTML += myJson.country;
				document.getElementById("postal").innerHTML += myJson.postal;
				document.getElementById("long").innerHTML += myJson.longitude;
				document.getElementById("lat").innerHTML += myJson.latitude;
				
				sessionStorage.setItem("myJson", JSON.stringify(myJson));
				sessionStorage.setItem("refreshTimer", new Date().getTime());
				
				showWeather();
				loadCountries();
			});
	} else {
		var myJson = JSON.parse(sessionStorage.getItem("myJson"));
		document.getElementById("ip").innerHTML += myJson.ip;
		document.getElementById("city").innerHTML += myJson.city;
		document.getElementById("region").innerHTML += myJson.region;
		document.getElementById("country").innerHTML += myJson.country;
		document.getElementById("postal").innerHTML += myJson.postal;
		document.getElementById("long").innerHTML += myJson.longitude;
		document.getElementById("lat").innerHTML += myJson.latitude;
		
		sessionStorage.setItem("refreshTimer", new Date().getTime());
		
		showWeather();
		loadCountries();
	}
		document.getElementById("add").addEventListener("click", function(){openModalAdd()});
		document.getElementById("submit").addEventListener("click", function(){updateCountry()});
		document.getElementById("submit_toevoegen").addEventListener("click", function(){addCountry()});
		
}

function showWeather(){
	var data = {};
	if(sessionStorage.getItem("refreshTimerWeather") == null || sessionStorage.getItem("refreshTimerWeather") > new Date().getTime() + 600000){
		fetch("https://api.openweathermap.org/data/2.5/weather?q=Utrecht&APPID=d3de5513e0f2c12070c9655877a6bcf1&units=Metric")
		.then(response => response.json())
		.then(function(myJson){
			document.getElementById("temp").innerHTML = myJson.main.temp;
			document.getElementById("humidity").innerHTML = myJson.main.humidity;
			document.getElementById("wind").innerHTML = myJson.wind.speed;
			document.getElementById("winddirection").innerHTML = myJson.wind.speed;
			document.getElementById("sunup").innerHTML = msToTime(myJson.sys.sunrise);
			document.getElementById("sundown").innerHTML = msToTime(myJson.sys.sunset);
			
				
			sessionStorage.setItem("myJsonWeather", JSON.stringify(myJson));
			sessionStorage.setItem("refreshTimerWeather", new Date().getTime());
			
			console.log("Inserted into sessionstorage");
		});
	} else {
		var myJson = JSON.parse(sessionStorage.getItem("myJsonWeather"));
		
		document.getElementById("temp").innerHTML = myJson.main.temp;
		document.getElementById("humidity").innerHTML = myJson.main.humidity;
		document.getElementById("wind").innerHTML = myJson.wind.speed;
		document.getElementById("winddirection").innerHTML = myJson.wind.speed;
		document.getElementById("sunup").innerHTML = msToTime(myJson.sys.sunrise);
		document.getElementById("sundown").innerHTML = msToTime(myJson.sys.sunset);
	}
}

function msToTime(duration) {
	  var milliseconds = parseInt((duration % 1000) / 100),
	    seconds = Math.floor((duration / 1000) % 60),
	    minutes = Math.floor((duration / (1000 * 60)) % 60),
	    hours = Math.floor((duration / (1000 * 60 * 60)) % 24);

	  hours = (hours < 10) ? "0" + hours : hours;
	  minutes = (minutes < 10) ? "0" + minutes : minutes;
	  seconds = (seconds < 10) ? "0" + seconds : seconds;

	  return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
}

function loadCountries(){
	var i = 0;
	var landen = {};
		fetch('/firstapp/restservices/countries')
		.then(response => response.json())
		.then(function(myJson){
			for(let value of myJson){							
				
				var row = document.getElementById("landen").insertRow(1);
				var name = row.insertCell(0);
				var capital = row.insertCell(1);
				var government = row.insertCell(2);
				var surface = row.insertCell(3);
				var population = row.insertCell(4);
				var update = row.insertCell(5);
				var deletion = row.insertCell(6);
				
				name.id = value.name;
				name.innerHTML = value.name;
				capital.innerHTML = value.capital;
				government.innerHTML = value.government;
				surface.innerHTML = value.surface + " km<sup>2</sup>";
				population.innerHTML = value.population;
				update.innerHTML = "<button type='button' code='" + value.code + "' class='wijzig' population='" + value.population + "' surface='" + value.surface + "' land='" + value.name + "' hoofdstad='" + value.capital + "' overheid='" + value.government + "'>Wijzigen</button>";
				deletion.innerHTML = "<button type='button' class='delete' code='" + value.code + "'>Delete</button>";
				

				document.getElementById(value.name).addEventListener("click", function() {
						showWeather(null, null, value.capital);
				});
			}
			document.querySelectorAll(".delete").forEach(del => {
				del.addEventListener("click", function(){deleteCountry(del.getAttribute("code"));});
			});
			
			document.querySelectorAll(".wijzig").forEach(wijz => {
				wijz.addEventListener("click", function(){openModal(wijz.getAttribute("code"), wijz.getAttribute("land"), wijz.getAttribute("hoofdstad"), wijz.getAttribute("overheid"), wijz.getAttribute("surface"), wijz.getAttribute("population"))});
			});
		});
}

function initStorage(){
	document.getElementById("storage").addEventListener('keyup', function(){
		console.log(document.getElementById("storage").value);
		localStorage.setItem("input", document.getElementById("storage").value);
	});
}

function initResult() {
	document.getElementById("result").innerHTML = localStorage.getItem("input");
}

function deleteCountry(code){
	console.log(code);
	
	fetch('/firstapp/restservices/countries/' + code, {method: 'DELETE', headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
		.then((response) => {
			if (response.status == 200) { 
				console.log("verwijderd");
				location.reload();
			}
		});
}

function updateCountry(){
	var code = document.getElementById("code").value;
	var formData = new FormData(document.querySelector("#wijzigen"));		
	var encData = new URLSearchParams(formData.entries());
	
	fetch("/firstapp/restservices/countries/" + code, {method: 'PUT', body: encData, headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((myJson) => {
		console.log(myJson);
	});
	
	modal.style.display = "none";
	location.reload();
}

function addCountry(){
	var formData = new FormData(document.querySelector("#toevoegen"));		
	var encData = new URLSearchParams(formData.entries());
	
	fetch('/firstapp/restservices/countries', {method: 'POST', body: encData, headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((response) => {
		if (response.status == 402) { 
			console.log("Error, fout");
		}
		console.log(response);
	});
	document.getElementById("toevoegen").reset();
	location.reload();
}

function openModal(code, land, hoofdstad, overheid, surface, population){
	modal.style.display = "block";
	document.getElementById("code").value = code;
	document.getElementById("land").value = land;
	document.getElementById("hoofdstad").value = hoofdstad;
	document.getElementById("surface").value = surface;
	document.getElementById("populatie").value = population;
}

function openModalAdd() {
	modalAdd.style.display = "block";
}

function initLogin(){
	document.getElementById("login_button").addEventListener("click", function(){login()});
}

function login(){
	var formData = new FormData(document.querySelector("#login_form"));
	var encData = new URLSearchParams(formData.entries());
	document.querySelector("#error").innerHTML = "";
	
	fetch('/firstapp/restservices/authentication', {method: 'POST', body: encData, fetchoptions})
	.then((response) => { 
		if (response.ok) {
			return response.json();
		} else {
			document.querySelector("#error").innerHTML = "Inlog mislukt!";
		}
	})
	.then((myJson) => {
		window.sessionStorage.setItem("sessionToken", myJson.JWT);
		window.location.href = "/firstapp/weather.html";
	});
} 

//Get the modal
var modal = document.getElementById("myModal");
var modalAdd = document.getElementById("myModalAdd");

// Get the button that opens the modal
var btn = document.getElementById("myBtn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];
var spanAdd = document.getElementsByClassName("closeAdd")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}
spanAdd.onclick = function() {
	  modalAdd.style.display = "none";
}


// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  } else if (event.target == modalAdd) {
	  modalAdd.style.display = "none";
  }
}
