/**
 * Controls the map creation process.
 */
function main() {

    prepareGlobalVars();
    analyzeGame();
    pathogensWrapUp();

    createUI();
    updateUI();

    startEventListeners();
}


/**
 * Sets global needed variables.
 */
function prepareGlobalVars() {

    // global value which contains the current round
    round = parseInt(location.hash.substr(1));

    // global value which contains the last round
    lastRound = json.length - 1;

    // ensure: 0 <= round <= lastRound
    if (isNaN(round) || round > lastRound || round < 0) {
        round = 0;
        location.hash = '#' + round;
    }

    // global object which maps pathogen to color
    pathogenColors = {};

    // global array with all bubble information for all rounds: bubbles[3] contains all information for round three
    bubbles = [];

    // global array with information about infected/killed cities/humans by pathogens
    pathogens = [];

    // global array with all pathogen names that occur in this game
    pathogenNames = [];

    // global map which maps all city names to their coordinates (0 = latitude, 1 = longitude)
    cityCoords = new Map();
    for (let city of Object.values(json[0].cities)) {
        cityCoords.set(city.name, [city.latitude, city.longitude]);
    }
}


/**
 * Analyzes the game and does all preparation calculations needed.
 */
function analyzeGame() {

    // prepare colors for pathogens
    var colors = ['red', 'blue', 'yellow', 'pink', 'green', 'white'];

    var i;
    for (i = 0; i <= lastRound; i++) { // for each round in game
        bubbles[i] = [];
        var infectedCities = {};
        var infectedHumans = {};
        var killedHumans = {};

        for (let city of Object.values(json[i].cities)) { // for each city in round
            var bubble = {};

            // set basic bubble values
            bubble.city = city.name;
            bubble.latitude = city.latitude;
            bubble.longitude = city.longitude;
            bubble.economy = city.economy;
            bubble.government = city.government;
            bubble.hygiene = city.hygiene;
            bubble.awareness = city.awareness;
            bubble.population = city.population;
            bubble.connections = city.connections;

            if (typeof city.events != 'undefined') { // if any event happened
                for (let event of city.events) { // for each event in city
                    if (event.type == 'outbreak') { // if event outbreak happened

                        // assign new color per pathogen
                        if (!Object.keys(pathogenColors).includes(event.pathogen.name)) {
                            pathogenColors[event.pathogen.name] = colors.shift();
                        }

                        // set additional values for bubble with outbreak event
                        bubble.pathogen = event.pathogen.name;
                        bubble.fillKey = pathogenColors[event.pathogen.name];
                        bubble.borderOpacity = 0.75;
                        bubble.prevalence = event.prevalence;
                        bubble.radius = 5;

                        // add pathogen name to pathogenNames array if it does not already exist
                        if (!pathogenNames.includes(event.pathogen.name)) {
                            pathogenNames.push(event.pathogen.name);
                        }

                        // count infected cities
                        if (typeof infectedCities[event.pathogen.name] == 'undefined') {
                            infectedCities[event.pathogen.name] = 1;
                        } else {
                            infectedCities[event.pathogen.name] += 1;
                        }

                        // count infected humans
                        if (typeof infectedHumans[event.pathogen.name] == 'undefined') {
                            infectedHumans[event.pathogen.name] = Math.round(city.population * event.prevalence);
                        } else {
                            infectedHumans[event.pathogen.name] += Math.round(city.population * event.prevalence);
                        }

                        // count killed humans
                        if (i > 0) {
                            killedHumans[event.pathogen.name] = json[i-1].cities[city.name].population - json[i].cities[city.name].population;
                        }

                    }
                }
            } else { // city without any event
                // set additional values for bubble without outbreak event
                bubble.radius = 2;
                bubble.fillKey = 'black';
                bubble.borderWidth = 0;
            }

            bubbles[i].push(bubble);
        }
        pathogens[i] = {};
        // current infected cities
        pathogens[i].cic = infectedCities;
        // current infected humans
        pathogens[i].cih = infectedHumans;
        // current killed humans
        pathogens[i].ckh = killedHumans;
    }
}

/**
 * Replaces undefined values with 0 and
 * saves total killed humans for each pathogen and round in the pathogens array.
 */
function pathogensWrapUp() {
    for (let i in pathogens) { // for each pathogen
        pathogens[i].tkh = {};
        for (let p of pathogenNames) { // for each round
            if (typeof pathogens[i].cic[p] == 'undefined') {
                pathogens[i].cic[p] = 0;
            }
            if (typeof pathogens[i].cih[p] == 'undefined') {
                pathogens[i].cih[p] = 0;
            }
            if (typeof pathogens[i].ckh[p] == 'undefined') {
                pathogens[i].ckh[p] = 0;
            }
            if (i > 0) {
                pathogens[i].tkh[p] = pathogens[i-1].tkh[p] + pathogens[i].ckh[p];
            } else {
                pathogens[i].tkh[p] = 0;
            }
        }
    }
}