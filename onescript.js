/************************
 *                      *
 *     Preparation      *
 *                      *
 ************************/

// Get Round
round = parseInt(location.hash.substr(1));                          // also needed when hash changed
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

// Make coords great again ------------------------------------------------------------
city_coords = new Map();
for (let city of Object.values(json[0].cities)) {
    city_coords.set(city.name, [city.latitude, city.longitude]);
}

// Fill bubbles-array with all information
var i;
for (i = 0; i < json.length; i++) {
    bubbles[i] = [];
    for (let city of Object.values(json[i].cities)) {
        if (typeof city.events != 'undefined') {
            var j;
            for (j = 0; j < city.events.length; j++) {
                if (city.events[j].type == 'outbreak') {

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
                }
            }
        }
    }
}





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

// Initially create map with bubbles
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
updateMap(round);





/************************
 *                      *
 *      Functions       *
 *                      *
 ************************/

/**
 * 
 * @param {*} toRound 
 */
function updateMap(toRound) {
    console.log("mapupd");
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
    console.log(map)
    round = slider.value;
    console.log('Slider changed to round ' + round);
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
        console.log('Arrow key pressed. Change from round ' + round + ' to ' + newRound);
        document.getElementById('roundSlider').value = newRound;
        sliderChange();
    } else if (e.key == 'ArrowRight') {
        newRound++;
        console.log('Arrow key pressed. Change from round ' + round + ' to ' + newRound);
        document.getElementById('roundSlider').value = newRound;
        sliderChange();
    }
}

/**
 * 
 * @param {*} city 
 */
function setArcs(city) {
    console.log("AFDSGGGGGGGGGGGGGGGGGGGG")
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


// 
cityClicked = null;
d3.selectAll('.datamaps-bubble').on('click', setArcs);



