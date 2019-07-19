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
	
	fetch('https://ipapi.co/json/')
		.then(response => response.json())
		.then(function(myJson){
			document.getElementById("ip").innerHTML += myJson.ip;
			document.getElementById("city").innerHTML += myJson.city;
			document.getElementById("region").innerHTML += myJson.region;
			document.getElementById("country").innerHTML += myJson.country;
			document.getElementById("continent").innerHTML += myJson.continent_code;
			document.getElementById("postal").innerHTML += myJson.postal;
			
			var lat = parseFloat(myJson.latitude);
			var long = parseFloat(myJson.longitude);
			
			showWeather(lat, long);
			loadCountries();
		});
	
		document.getElementById("submit").addEventListener("click", function(){updateCountry()});
		document.getElementById("submit_toevoegen").addEventListener("click", function(){addCountry()});
		
}

function showWeather(lat, long, city){
	var data = {};
	if(lat != null){
		fetch('https://api.openweathermap.org/data/2.5/weather?lat=' + lat +'&lon=' + long + '&APPID=742a2e3e80a51ac3c2cd1b358b7550f7&units=Metric')
		.then(response => response.json())
		.then(function(myJson){
			console.log(myJson);
			document.getElementById("temp").innerHTML += myJson.main.temp;
			document.getElementById("humidity").innerHTML += myJson.main.humidity;
			document.getElementById("wind").innerHTML += myJson.wind.speed;
		});
	}	
	else{
		if(localStorage.getItem(city + "time") == null || localStorage.getItem(city + "time") > new Date().getTime() + 600000){
			fetch('https://api.openweathermap.org/data/2.5/weather?q=' + city + '&APPID=742a2e3e80a51ac3c2cd1b358b7550f7&units=Metric')
			.then(response => response.json())
			.then(function(myJson){
				document.getElementById("temp").innerHTML = "TEMPERATUUR:" + myJson.main.temp;
				document.getElementById("humidity").innerHTML = "LUCHTVOCHTIGHEID:" + myJson.main.humidity;
				document.getElementById("wind").innerHTML = "WINDSNELHEID:" + myJson.wind.speed;
				
				data.temp = myJson.main.temp;
				data.humidity = myJson.main.humidity;
				data.wind = myJson.wind.speed;
				
				localStorage.setItem(city, JSON.stringify(data));
				localStorage.setItem(city + "time", new Date().getTime());
				console.log("Inserted into localstorage");
			});
		}	
		else{
			var values = JSON.parse(localStorage.getItem(city));
			document.getElementById("temp").innerHTML = "TEMPERATUUR:" + values.temp;
			document.getElementById("humidity").innerHTML = "LUCHTVOCHTIGHEID:" + values.humidity;
			document.getElementById("wind").innerHTML = "WINDSNELHEID:" + values.wind;
		}
	}
}

function loadCountries(){
	var i = 0;
	var landen = {};
		fetch('restservices/countries')
		.then(response => response.json())
		.then(function(myJson){
			for(let value of myJson){							
				
				var row = document.getElementById("landen").insertRow(1);
				var name = row.insertCell(0);
				var capital = row.insertCell(1);
				var government = row.insertCell(2);
				var update = row.insertCell(3);
				var deletion = row.insertCell(4);
				
				name.id = value.name;
				name.innerHTML = value.name;
				capital.innerHTML = value.capital;
				government.innerHTML = value.government;
				update.innerHTML = "<button type='button' code='" + value.code + "' class='wijzig' land='" + value.name + "' hoofdstad='" + value.capital + "' overheid='" + value.government + "'>Wijzigen</button>";
				deletion.innerHTML = "<button type='button' class='delete' code='" + value.code + "'>Delete</button>";
				

				document.getElementById(value.name).addEventListener("click", function() {
						showWeather(null, null, value.capital);
				});
			}
			document.querySelectorAll(".delete").forEach(del => {
				del.addEventListener("click", function(){deleteCountry(del.getAttribute("code"));});
			});
			
			document.querySelectorAll(".wijzig").forEach(wijz => {
				wijz.addEventListener("click", function(){openModal(wijz.getAttribute("code"), wijz.getAttribute("land"), wijz.getAttribute("hoofdstad"), wijz.getAttribute("overheid"))});
			});
		});
}

function initStorage(){
	document.getElementById(storage).addEventListener("keyup", function(){
		localStorage.setItem("input", document.getElementById(storage).value);
	});
}

function initResult() {
	document.getElementById(result).innerHTML = localStorage.getItem("input");
}

function deleteCountry(code){
	console.log(code);
	
	fetch('/restservices/countries/' + code, {method: 'DELETE', headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
		.then((response) => {
			if (response.status == 200) { 
				console.log("verwijderd");
			}
		});
}

function updateCountry(){
	var code = document.getElementById("code").value;
	var formData = new FormData(document.querySelector("#wijzigen"));		
	var encData = new URLSearchParams(formData.entries());
	
	fetch("/restservices/countries/" + code, {method: 'PUT', body: encData, headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((myJson) => {
		console.log(myJson);
	});
	
	modal.style.display = "none";
}

function addCountry(){
	var formData = new FormData(document.querySelector("#toevoegen"));		
	var encData = new URLSearchParams(formData.entries());
	
	fetch('/restservices/countries', {method: 'POST', body: encData, headers : {	'Authorization': 'Bearer ' + window.sessionStorage.getItem("sessionToken") }})
	.then((response) => {
		if (response.status == 402) { 
			console.log("Error, fout");
		}
		console.log(response);
	});
	document.getElementById("toevoegen").reset();
}

function openModal(code, land, hoofdstad, overheid){
	modal.style.display = "block";
	document.getElementById("code").value = code;
	document.getElementById("land").value = land;
	document.getElementById("hoofdstad").value = hoofdstad;
}

function initLogin(){
	document.getElementById("login_button").addEventListener("click", function(){login()});
}

function login(){
	var formData = new FormData(document.querySelector("#login_form"));
	var encData = new URLSearchParams(formData.entries());
	document.querySelector("#error").innerHTML = "";
	
	fetch('/authentication', {method: 'POST', body: encData, fetchoptions})
	.then((response) => { 
		if (response.ok) {
			return response.json();
		} else {
			document.querySelector("#error").innerHTML = "Inlog mislukt!";
		}
	})
	.then((myJson) => {
		window.sessionStorage.setItem("sessionToken", myJson.JWT);
		window.location.href = "/weather.html";
	});
} 

//Get the modal
var modal = document.getElementById("myModal");

// Get the button that opens the modal
var btn = document.getElementById("myBtn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}
