/**
 * Prevent default behaviour.
 * 
 * @param {Object} e DragEvent.
 */
function preventDefaults (e) {
    e.preventDefault()
    e.stopPropagation()
}


/**
 * Add dark background to the drop-area.
 */
function highlight() {
    dropArea.classList.add('bg-secondary');
}


/**
 * Remove dark background from the drop-area.
 */
function unhighlight() {
    dropArea.classList.remove('bg-secondary');
}


/**
 * Handles files droppen on the drop-area.
 * 
 * @param {Object} e DragEvent.
 */
function onDrop(e) {
    handleFiles(e.dataTransfer.files);
}


/**
 * Handles files chosen in the form.
 * 
 * @param {Object} e Event.
 */
function onChange(e) {
    handleFiles(e.target.files);
}


/**
 * Handles file reading. First starts reading process, then starts waiting process.
 * 
 * @param {Object} files FileList
 */
function handleFiles(files) {
    readFiles(files);
    waitForFinish(files.length, 0);
}


/**
 * Waits until all files are read. Then parse json and start main function.
 * 
 * @param {number} threshold Number of files to read
 * @param {number} abortCounter Counter to abort waiting
 */
function waitForFinish(threshold, abortCounter) {
    setTimeout(function() {
        if (fileCounter < threshold) { // not finished
            if (abortCounter > 200) { // abort after 10 seconds
                console.log("Aborted. Reading took >10 seconds.");
                return;
            }
            waitForFinish(threshold, abortCounter + 1);
        } else { // finished reading all filed
            // remove last comma
            json = json.substr(0, json.length-1);

            if (json.substr(0,1) != '[') {
                json = '[' + json + ']';
            }

            json = JSON.parse(json);
            main();
        }
    }, 50);
}


/**
 * Reads all files one by one.
 * 
 * Source: https://stackoverflow.com/questions/13975031/
 * 
 * @param {Object} files FileList.
 */
function readFiles(files) {
    var reader = new FileReader();
    fileCounter = 0;
    json = '';
    seed = undefined;
    function readFile(index) {
        if (index >= files.length) {
            return;
        }
        var file = files[index];

        if (file.name == 'seed.txt') {
            reader.onload = function(e) {
                seed = e.target.result;
                fileCounter++;
                readFile(index + 1)
            }
            reader.readAsText(file);

        } else if (file.type == 'application/json' && !file.name.includes('_action')){
            reader.onload = function(e) {
                json += e.target.result;
                json += ',';
                fileCounter++;
                readFile(index + 1)
            }
            reader.readAsText(file);
        } else {
            fileCounter++;
            readFile(index + 1)
        }
    }
    readFile(0);

}