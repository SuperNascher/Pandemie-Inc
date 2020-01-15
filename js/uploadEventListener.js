var dropArea = document.getElementById('drop-area');

// prevent default behavior when dropping file
['dragover', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, preventDefaults, false)
});

// change color when dragging a file over the area
['dragenter', 'dragover'].forEach(eventName => {
    dropArea.addEventListener(eventName, highlight, false)
});

// change color when dragging a file and leaving the area
['dragleave', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, unhighlight, false)
});

// start processing data when file is dropped
dropArea.addEventListener('drop', onDrop, false)

// start processing data when file is chosen
document.getElementById('ulFile').addEventListener('change', onChange);