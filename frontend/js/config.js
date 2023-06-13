backendUrl = 'http://localhost:8080';

$(document).ready(() => {
    let board = JXG.JSXGraph.initBoard('jxgbox', {boundingbox: [-6, 6, 6, -6], axis: true, showCopyright: false});


    document.getElementById('myForm').addEventListener('submit', function(event) {
        event.preventDefault();
        validateForm(board);
    });
});
