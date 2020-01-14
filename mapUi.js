/**
 * Create blank map (without additional elements).
 */
function createUI() {

    mapDiv = document.getElementById('map');
    while (mapDiv.firstChild) {
        mapDiv.firstChild.remove();
    }

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
          green: '#00ff00',
          pink: '#ff00ff',
          black: '#000000',
          white: '#ffffff'
        }
    });

    // Slider object
    slider = document.getElementById('roundSlider');
    // Slider value output
    sliderValueOutput = document.getElementById('sliderValue');

    // Set slider range and value
    slider.max = (lastRound);
    slider.value = round;

    // show slider value and remove old value if necessary
    if (sliderValueOutput.firstChild) {
        sliderValueOutput.firstChild.remove();
    }
    sliderValueOutput.appendChild(document.createTextNode(round));

    // 
    document.getElementById('dataAndSettings').classList.remove('d-none');
    
}


/**
 * Updates the whole UI (bubbles, arcs, pathogenCards, slider) to match the current round.
 */
function updateUI() {
    // global value that contains the city that currently shows its connections by arcs or null if no connections are shown
    cityClicked = null;

    // set new bubbles
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

    // show arcs on click (must be set again every time bubbles change)
    d3.selectAll('.datamaps-bubble').on('click', toggleArcs);

    // update all pathogen cards
    for (let pathogen of pathogenNames) {
        updatePathogenCard(pathogen, round);
    }

    // show outcome in round card
    var outcomeBadge = document.getElementById('outcome');
    outcomeBadge.innerText = json[round].outcome;
    outcomeBadge.classList.add('badge', 'float-right');

    if (json[round].outcome == 'win') {
        outcomeBadge.classList.remove('badge-warning');
        outcomeBadge.classList.add('badge-success');
    } else if (json[round].outcome == 'loss') {
        outcomeBadge.classList.remove('badge-warning');
        outcomeBadge.classList.add('badge-danger');
    } else {
        outcomeBadge.classList.remove('badge-success');
        outcomeBadge.classList.remove('badge-danger');
        outcomeBadge.classList.add('badge-warning');
    }
}


/**
 * Adds a new card with pathogen information.
 * 
 * @param {string} name Name of the pathogen.
 */
function addPathogenCard(name) {
    var cardParent = document.getElementById('pathogenCards');

    var card = document.createElement('div');
    card.setAttribute('id', name);
    card.classList.add('col-4', 'mb-3', 'px-2');
    card.innerHTML = `
        <div class="card">
            <div class="card-header">
                <span style="color:${pathogenColors[name]};">&#11044;</span> ${name}
            </div> 
            <div class="card-body">
                <div class="row">
                    <div class="col text-right">cur. inf. cities:</div>
                    <div class="col" id="cic-${name}">${pathogens[round].cic[name]}</div>
                </div>
                <div class="row">
                    <div class="col text-right">cur. inf. humans:</div>
                    <div class="col" id="cih-${name}">${pathogens[round].cih[name]}</div>
                </div>
                <div class="row">
                    <div class="col text-right">cur. kil. humans:</div>
                    <div class="col" id="ckh-${name}">${pathogens[round].ckh[name]}</div>
                </div>
                <div class="row">
                    <div class="col text-right">tot. kil. humans:</div>
                    <div class="col" id="tkh-${name}">${pathogens[round].tkh[name]}</div>
                </div>
            </div>
        </div>`;

    cardParent.appendChild(card);
}


/**
 * Updates the information in the pathogen card with given name to match the current round or
 * creates a new card if this pathogen card does not already exist.
 * 
 * @param {string} name Name of the pathogen.
 */
function updatePathogenCard(name) {
    if (document.getElementById(name)) {
        ['cic', 'cih', 'ckh', 'tkh'].forEach(id => {
            var element = document.getElementById(id + '-' + name);
            element.firstChild.remove();
            element.appendChild(document.createTextNode(pathogens[round][id][name]));
        });
    } else {
        addPathogenCard(name, round);
    }
}