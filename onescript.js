/************************
 *                      *
 *     Preparation      *
 *                      *
 ************************/

// Get Round
round = parseInt(location.hash.substr(1));
if (isNaN(round)) {
    round = 0;
}

// Get filename
var urlParams = new URLSearchParams(window.location.search);
var file = urlParams.get('file');

// Parse file
var request = new XMLHttpRequest();
request.open("GET", file, false);
request.send(null)
var json = JSON.parse(request.responseText);

// Prepare colors for pathogens
var colors = ['red', 'blue', 'yellow', 'pink', 'black', 'white'];
var colorCounter = 0;
var pathogenColors = [];

// Array with all bubble information for all rounds: bubbles[3] contains all information for round three
bubbles = [];

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
pathogenes = {};

// Make coords great again -------------------------------------------------------------------------------------------------------------------------------------------------------
city_coords = new Map();
for (let city of Object.values(json[0].cities)) {
    city_coords.set(city.name, [city.latitude, city.longitude]);
}

// Fill bubbles-array with all information ------------------------------------------------------------------------------------------------------------------------------------------
var i;
for (i = 0; i < json.length; i++) { // for each round in game
    bubbles[i] = [];
    var infectedCities = {};
    var infectedHumans = {};
    var killedHumans = {};

    for (let city of Object.values(json[i].cities)) { // for each city in round
        if (typeof city.events != 'undefined') { // if any event happened
            var j;
            for (j = 0; j < city.events.length; j++) { // for each event in city
                if (city.events[j].type == 'outbreak') { // if event outbreak happened

                    // Assign new color per pathogen
                    if (!Object.keys(pathogenColors).includes(city.events[j].pathogen.name)) {
                        pathogenColors[city.events[j].pathogen.name] = colors[colorCounter];
                        colorCounter++;
                    }

                    // Push information to bubbles-array
                    bubbles[i].push({
                        city: city.name,
                        pathogen: city.events[j].pathogen.name,
                        radius: 5,
                        fillKey: pathogenColors[city.events[j].pathogen.name],
                        borderOpacity: 0.75,
                        latitude: city.latitude,
                        longitude: city.longitude,
                        economy: city.economy,
                        government: city.government,
                        hygiene: city.hygiene,
                        awareness: city.awareness,
                        population: city.population,
                        prevalence: city.events[j].prevalence,
                        connections: city.connections,
                    });

                    // count infected cities
                    if (typeof infectedCities[city.events[j].pathogen.name] == 'undefined') {
                        infectedCities[city.events[j].pathogen.name] = 1;
                    } else {
                        infectedCities[city.events[j].pathogen.name] += 1;
                    }

                    // count infected humans
                    if (typeof infectedHumans[city.events[j].pathogen.name] == 'undefined') {
                        infectedHumans[city.events[j].pathogen.name] = city.population * city.events[j].prevalence;
                    } else {
                        infectedHumans[city.events[j].pathogen.name] += city.population * city.events[j].prevalence;
                    }

                    // count killed humans
                    if(i > 0) { // don't do this in first round
                        killedHumans[city.events[j].pathogen.name] = pathogenes[i-1].killedHumans[city.events[j].pathogen.name];
                        if (typeof killedHumans[city.events[j].pathogen.name] == 'undefined') {
                            // console.log(json[i-1].cities[city.name].population);
                            console.log("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")
                            killedHumans[city.events[j].pathogen.name] += json[i-1].cities[city.name].population - json[i].cities[city.name].population;
                        } else {
                            killedHumans[city.events[j].pathogen.name] += json[i-1].cities[city.name].population - json[i].cities[city.name].population;
                            // console.log(pathogenes[i-1].killedHumans[city.events[j].pathogen.name]);
                        }
                    } else {
                        killedHumans[city.events[j].pathogen.name] = 0;
                    }


                    
                } else { // City with event(s), but no outbreak
                    // Push information to bubbles-array
                    bubbles[i].push({
                        city: city.name,
                        radius: 5,
                        latitude: city.latitude,
                        longitude: city.longitude,
                        economy: city.economy,
                        government: city.government,
                        hygiene: city.hygiene,
                        awareness: city.awareness,
                        population: city.population,
                        connections: city.connections,
                    });
                }
            }
        } else { // City without any event
            // Push information to bubbles-array
            bubbles[i].push({
                city: city.name,
                radius: 2,
                fillKey: 'black',
                borderWidth: 0,
                latitude: city.latitude,
                longitude: city.longitude,
                economy: city.economy,
                government: city.government,
                hygiene: city.hygiene,
                awareness: city.awareness,
                population: city.population,
                connections: city.connections,
    });
        }
    }
        // push here :D
        pathogenes[i] = {};
        pathogenes[i].infectedCities = infectedCities;
        pathogenes[i].infectedHumans = infectedHumans;
        pathogenes[i].killedHumans = killedHumans;

}

console.log(pathogenes);




/************************
 *                      *
 *       Output         *
 *                      *
 ************************/

// Set slider range and value
document.getElementById("roundSlider").max = (json.length - 1);
document.getElementById("roundSlider").value = round;

// Show slider value
slider = document.getElementById("roundSlider");    // Slider object
output = document.getElementById("sliderValue");    // Slider value output
output.innerHTML = slider.value;                    // Display the default slider value

// Initially create map and additional elements
createMap();
updateMap(round);





/************************
 *                      *
 *      Functions       *
 *                      *
 ************************/

 /**
  * Create blank map (without additional elements)
  */
function createMap() {
    map = new Datamap({
        element: document.getElementById("map"),
        geographyConfig: {
          popupOnHover: false,
          highlightOnHover: false
        },
        fills: {
          defaultFill: '#ABDDA4',
          red: '#ff0000',
          blue: '#0000ff',
          yellow: '#ffff00',
          pink: '#ff00ff',
          black: '#000000',
          white: '#ffffff'
        }
    });
}


/**
 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * 
 * @param {*} toRound 
 */
function updateMap(toRound) {
    cityClicked = null;
    map.arc([], {animationSpeed: 600});

    map.bubbles(
        bubbles[toRound]
    , {
        popupTemplate: function(geo, data) {
        return `<div class="hoverinfo"><table>
                <tr><td class="first-td">City:</td><td>${data.city}</td></tr>
                <tr><td class="first-td">Pathogen:</td><td>${data.pathogen}</td></tr>
                <tr><td class="first-td">Economy:</td><td>${data.economy}</td></tr>
                <tr><td class="first-td">Government:</td><td>${data.government}</td></tr>
                <tr><td class="first-td">Hygiene:</td><td>${data.hygiene}</td></tr>
                <tr><td class="first-td">Awareness:</td><td>${data.awareness}</td></tr>
                <tr><td class="first-td">Population:</td><td>${data.population}</td></tr>
                <tr><td class="first-td">Prevalence:</td><td>${data.prevalence}</td></tr>
                </table></div>`;
        },
        exitDelay: 1,
    });
    d3.selectAll('.datamaps-bubble').on('click', setArcs);
}

/**
 * Called when slider value is changed
 */
function sliderChange() {
    round = slider.value;
    document.location.href = "http://localhost/worldmap/?file=ic2.json#" + round;
    output.innerText = round;
    updateMap(round);
}

/**
 * Called when key is pressed.
 */
function logKey(e) {
    var newRound = round;
    if (e.key == 'ArrowLeft') {
        newRound--;
        document.getElementById('roundSlider').value = newRound;
        sliderChange();
    } else if (e.key == 'ArrowRight') {
        newRound++;
        document.getElementById('roundSlider').value = newRound;
        sliderChange();
    }
}

/**
 * ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * @param {*} city 
 */
function setArcs(city) {
    map.arc([], {animationSpeed: 600});
    if (cityClicked === city) {
        cityClicked = null;
        return
    }

    var arcs = [];

    for (let i in city.connections) {
        // console.log(destination);
        var destCoords = city_coords.get(city.connections[i]);
        arcs.push({
            origin: {
                latitude: city.latitude,
                longitude: city.longitude
            },
            destination: {
                latitude: destCoords[0],
                longitude: destCoords[1]
            }
        });
    }
    cityClicked = city;
    map.arc( arcs, {strokeWidth: 1, arcSharpness: 1.4});
}


/************************
 *                      *
 *    EventListener     *
 *                      *
 ************************/

// Keypress
document.addEventListener('keydown', logKey);

// Update the current slider value (each time you drag the slider handle)
slider.oninput = function() {
    output.innerHTML = this.value;
}

// Override mouseover 
datamapMouseoverFunc = d3.selectAll('.datamaps-bubble').on('mouseover');
d3.selectAll('.datamaps-bubble').on('mouseover', function(city) {
    datamapMouseoverFunc.call(this, city);

});

// Override mouseout
datamapMouseoutFunc = d3.selectAll('.datamaps-bubble').on('mouseout');
d3.selectAll('.datamaps-bubble').on('mouseout', function(city) {
    datamapMouseoutFunc.call(this, city);
    //map.arc([], {animationSpeed: 600});
});

// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
cityClicked = null;
d3.selectAll('.datamaps-bubble').on('click', setArcs);

// Recreate map if window gets resized
window.onresize = function() {
    document.getElementById("map").innerHTML = '';
    createMap();
    updateMap(round);
}