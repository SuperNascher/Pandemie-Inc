/************************
 *                      *
 *     Preparation      *
 *                      *
 ************************/

let dropArea = document.getElementById('drop-area');

['dragover', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, preventDefaults, false)
});

function preventDefaults (e) {
    e.preventDefault()
    e.stopPropagation()
}

['dragenter', 'dragover'].forEach(eventName => {
    dropArea.addEventListener(eventName, highlight, false)
});

['dragleave', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, unhighlight, false)
});

function highlight(e) {
    dropArea.classList.add('highlight')
}

function unhighlight(e) {
    dropArea.classList.remove('highlight')
}

dropArea.addEventListener('drop', onDrop, false)

function onDrop(e) {
    console.log('DROPPED');

    var reader = new FileReader();
    reader.onload = onReaderLoad;
    reader.readAsText(e.dataTransfer.files[0]);

}







function onChange(event) {
    var reader = new FileReader();
    reader.onload = onReaderLoad;
    reader.readAsText(event.target.files[0]);
}

function onReaderLoad(event){
    //console.log(event.target.result);
    json = JSON.parse(event.target.result);
    start();
    // console.log(json);
}



function start() {

    // Get Round
    round = parseInt(location.hash.substr(1));
    if (isNaN(round)) {
        round = 0;
    }

    // Prepare colors for pathogens
    var colors = ['red', 'blue', 'yellow', 'pink', 'black', 'white'];
    var colorCounter = 0;
    pathogenColors = [];

    // Array with all bubble information for all rounds: bubbles[3] contains all information for round three
    bubbles = [];

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    pathogenes = {};
    pathogenes.allNames = [];

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
            var bubble = {};

            // Set basic bubble values
            bubble.city = city.name;
            bubble.latitude = city.latitude;
            bubble.longitude = city.longitude;
            bubble.economy = city.economy;
            bubble.government = city.government;
            bubble.hygiene = city.hygiene;
            bubble.awareness = city.awareness;
            bubble.population = city.population;
            bubble.connections = city.connections;
            bubble.radius = 5;

            if (typeof city.events != 'undefined') { // if any event happened
                var j;
                for (j = 0; j < city.events.length; j++) { // for each event in city
                    if (city.events[j].type == 'outbreak') { // if event outbreak happened

                        // Assign new color per pathogen
                        if (!Object.keys(pathogenColors).includes(city.events[j].pathogen.name)) {
                            pathogenColors[city.events[j].pathogen.name] = colors[colorCounter];
                            colorCounter++;
                        }

                        // Set additional bubble values
                        bubble.pathogen = city.events[j].pathogen.name;
                        bubble.fillKey = pathogenColors[city.events[j].pathogen.name];
                        bubble.borderOpacity = 0.75;
                        bubble.prevalence = city.events[j].prevalence;
                        
                        if(!pathogenes.allNames.includes(city.events[j].pathogen.name)) {
                            pathogenes.allNames.push(city.events[j].pathogen.name);
                        }

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
                            // Das hier müsste doch nach oben zum console.log(NOOOOOOO) ??? weil:
                            // Folgender Fall ist nicht abgedeckt: neues Pathogen taucht mitten im Spiel auf
                            killedHumans[city.events[j].pathogen.name] = 0;
                        }
                    }
                }
            } else { // City without any event
                // Set additional bubble values
                bubble.radius = 2;
                bubble.fillKey = 'black';
                bubble.borderWidth = 0;
            }

            bubbles[i].push(bubble);

        }

        pathogenes[i] = {};
        pathogenes[i].infectedCities = infectedCities;
        pathogenes[i].infectedHumans = infectedHumans;
        pathogenes[i].killedHumans = killedHumans;

    }

    

    console.log(pathogenes);



    

    // Initially create map and additional UI elements
    createUI();
    updateUI(round);

    startEventListeners();

}



/************************
 *                      *
 *      Functions       *
 *                      *
 ************************/

 /**
  * Create blank map (without additional elements)
  */
function createUI() {

    mapDiv = document.getElementById("map");
    mapDiv.innerHTML = '';

    map = new Datamap({
        element: mapDiv,
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

    // Set slider range and value
    document.getElementById("roundSlider").max = (json.length - 1);
    document.getElementById("roundSlider").value = round;

    // Show slider value
    slider = document.getElementById("roundSlider");    // Slider object
    output = document.getElementById("sliderValue");    // Slider value output
    output.innerHTML = slider.value;                    // Display the default slider value

    document.getElementById("pathogeneData").classList.remove('d-none');
    
}


/**
 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * 
 * @param {*} round 
 */
function updateUI(round) {
    cityClicked = null;
    map.arc([], {animationSpeed: 600});

    map.bubbles(
        bubbles[round]
    , {
        popupTemplate: function(geo, data) {
        return `<div class="hoverinfo"><table>
                <tr><td class="text-right">City:</td><td>${data.city}</td></tr>
                <tr><td class="text-right">Economy:</td><td>${data.economy}</td></tr>
                <tr><td class="text-right">Government:</td><td>${data.government}</td></tr>
                <tr><td class="text-right">Hygiene:</td><td>${data.hygiene}</td></tr>
                <tr><td class="text-right">Awareness:</td><td>${data.awareness}</td></tr>
                <tr><td class="text-right">Population:</td><td>${data.population}</td></tr>
                <tr><td class="text-right">Pathogen:</td><td>${data.pathogen}</td></tr>
                <tr><td class="text-right">Prevalence:</td><td>${data.prevalence}</td></tr>
                </table></div>`;
        },
        exitDelay: 1,
    });

    d3.selectAll('.datamaps-bubble').on('click', setArcs);


    // Pathogene statistics
    for(let pathogen of pathogenes.allNames) {
        updatePathogeneCard(pathogen, round);
    }

}


function addPathogeneCard(name, round) {
    var cardParent = document.getElementById("pathogeneInfo");

    var card = document.createElement("div");
    card.setAttribute("id", name);
    card.classList.add("col-4", "mb-3", "px-2");
    card.innerHTML = `
        <div class="card">
            <div class="card-header">
                <span style="color:${pathogenColors[name]};">&#11044;</span> ${name}
            </div> 
            <div class="card-body">
                <div class="row">
                    <div class="col text-right">Cur. inf. cities:</div>
                    <div class="col" id="cic-${name}">${pathogenes[round].infectedCities[name]}</div>
                </div>
                <div class="row">
                    <div class="col text-right">Cur. inf. humans:</div>
                    <div class="col" id="cih-${name}"></div>
                </div>
                <div class="row">
                    <div class="col text-right">Cur. kil. humans:</div>
                    <div class="col" id="ckh-${name}"></div>
                </div>
                <div class="row">
                    <div class="col text-right">Tot. inf. humans:</div>
                    <div class="col" id="tih-${name}">${pathogenes[round].infectedHumans[name]}</div>
                </div>
                <div class="row">
                    <div class="col text-right">Tot. kil. humans:</div>
                    <div class="col" id="tkh-${name}">${pathogenes[round].killedHumans[name]}</div>
                </div>
            </div>
        </div>`;
    
    cardParent.appendChild(card);
}

function updatePathogeneCard(name, round) {
    if(document.getElementById(name)) {
        document.getElementById("cic-" + name).innerText = pathogenes[round].infectedCities[name];
        document.getElementById("cih-" + name).innerText = '';
        document.getElementById("ckh-" + name).innerText = '';
        document.getElementById("tih-" + name).innerText = pathogenes[round].infectedHumans[name];
        document.getElementById("tkh-" + name).innerText = pathogenes[round].killedHumans[name];
    } else {
        addPathogeneCard(name, round);
    }
}

/**
 * Called when slider value is changed
 */
function sliderChange() {
    round = slider.value;
    location.hash = "#" + round;
    output.innerText = round;
    updateUI(round);
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

document.getElementById('ulFile').addEventListener('change', onChange);


function startEventListeners() {
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
        createUI();
        updateUI(round);
    }
}