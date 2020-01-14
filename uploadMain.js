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
 * Read dropped file and start processing input.
 * 
 * @param {Object} e DragEvent.
 */
function onDrop(e) {
    console.log(e.dataTransfer.files.length);
    handleFiles(e.dataTransfer.files);
}


/**
 * Read chosen file and start processing input.
 * 
 * @param {Object} e Event.
 */
function onChange(e) {
    console.log(e.target.files.length);
    handleFiles(e.target.files);
}

/**
 * 
 * @param {*} files 
 */
function handleFiles(files) {
    console.log(files);
    var reader = new FileReader();

    if (files.length == 1) {
        reader.onload = onReaderLoad;
        reader.readAsText(files[0]);
    } else {
        readmultifiles(files);
        // multi(files);
        waitForFinish(files.length);
    }


}


function waitForFinish(threshold) {
    setTimeout(function() {
        if (fileCounter < threshold) {
            console.log('NOT DONE!');
            waitForFinish(threshold);
        } else {
            actions = actions.substr(0, actions.length-1);
            actions += ']';

            json = json.substr(0, json.length-1);
            json += ']';

            json = JSON.parse(json);
            actions = JSON.parse(actions);

            main();

            // console.log(actions);
            // console.log(json);
            // console.log(seed);
        }
    }, 50);

}


/**
 * Source: https://stackoverflow.com/questions/13975031/
 * 
 * @param {*} files 
 */
function readmultifiles(files) {
    var reader = new FileReader();
    fileCounter = 0;
    actions = '[';
    json = '[';
    function readFile(index) {
        if( index >= files.length ) return;
        var file = files[index];
        reader.onload = function(e) {

            if (file.name.includes('action')) {
                actions += '[';
                actions += e.target.result.substr(2, e.target.result.length-4);
                actions += ']';
                actions += ',';
            } else if (file.name.includes('seed')) {
                seed = e.target.result;
            } else {
                json += e.target.result;
                json += ',';
            }

            fileCounter++;

            readFile(index+1)
        }
      reader.readAsText(file);
    }
    readFile(0);

}
  
/**
 * Parse JSON and start building map.
 * 
 * @param {Object} e ProgressEvent.
 */
function onReaderLoad(e){
    json = JSON.parse(e.target.result);
    main();
}
