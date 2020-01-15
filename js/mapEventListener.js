/**
 * Starts all eventListeners needed for the map.
 */
function startEventListeners() {
    document.addEventListener('keydown', moveSliderByKey);

    slider.addEventListener('input', onSliderInput);
    slider.addEventListener('focus', onSliderFocus);
    slider.addEventListener('change', onSliderChange);

    window.addEventListener('resize', onWindowResize);
}


/**
 * Called when any key is pressed.
 * Changes the round +1 when right arrow and -1 when left arrow is pressed.
 * 
 * @param {Object} e KeyboardEvent.
 */
function moveSliderByKey(e) {
    if (e.key == 'ArrowLeft') {
        slider.stepDown();
        onSliderChange();
    } else if (e.key == 'ArrowRight') {
        slider.stepUp();
        onSliderChange();
    }
}


/**
 * Update the round value while dragging the slider.
 */
function onSliderInput(){
    sliderValueOutput.firstChild.remove();
    sliderValueOutput.appendChild(document.createTextNode(slider.value));
}


/**
 * Prevent slider being focused (dragging the slider and then using the keys fires keydown and change event. Round would change by 2 instead of 1).
 */
function onSliderFocus(){
    slider.blur();
}


/**
 * Update UI when slider has been changed.
 */
function onSliderChange() {
    if (slider.value in json) { // round is available
        round = slider.value;
        location.hash = '#' + round;
        sliderValueOutput.firstChild.remove();
        sliderValueOutput.appendChild(document.createTextNode(round));
        updateUI(round);
    } else { // round is not available
        if (round > parseInt(slider.value)) {
            slider.stepDown();
        } else {
            slider.stepUp();
        }
        onSliderChange();
    }
}


/**
 * Recreate map if window gets resized to fit the new resolution.
 */
function onWindowResize() {
    createUI();
    updateUI(round);
}


/**
 * Show/Hide all connections of the city that was clicked on.
 * 
 * @param {Object} city City that was clicked on.
 */
function toggleArcs(city) {
    map.arc([]);
    if (cityClicked === city) {
        cityClicked = null;
        return
    }

    var arcs = [];

    for (let destCity of city.connections) {
        var destCoords = cityCoords.get(destCity);
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
    map.arc(arcs);
}
