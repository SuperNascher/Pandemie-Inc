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
            json = json.substr(0, json.length-1);
            if (threshold > 1) {
                json += ']';
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
    if (files.length > 1) {
        json = '[';
    } else {
        json = '';
    }
    function readFile(index) {
        if (index >= files.length) {
            return;
        }
        var file = files[index];
        reader.onload = function(e) {

            json += e.target.result;
            json += ',';

            fileCounter++;

            readFile(index + 1)
        }
        reader.readAsText(file);
    }
    readFile(0);

}