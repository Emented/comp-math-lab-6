function submitForm(dto, board) {
    console.log(dto);

    $.ajax({
        type: 'POST',
        url: backendUrl + '/api/submit',
        contentType: "application/json",
        data: JSON.stringify(dto),
        dataType: 'json',

        success: (data) => {
            console.log(data);
            clearDisplayBlocks();
            generateDisplayBlock(data);
            generateGraph(data, board);
        }
    });
}

function fillTable(container, exactData, name, difference) {
    const arrayBlock = document.createElement('div');
    arrayBlock.classList.add('array-block');

    const arrayNameHeading = document.createElement('h2');
    arrayNameHeading.textContent = name;
    arrayBlock.appendChild(arrayNameHeading);

    const table = document.createElement('table');

    const tableHeaders = document.createElement('tr');
    const xHeader = document.createElement('th');
    xHeader.textContent = 'x';
    const yHeader = document.createElement('th');
    yHeader.textContent = 'y';
    tableHeaders.appendChild(xHeader);
    tableHeaders.appendChild(yHeader);
    if (difference) {
        const diffHeader = document.createElement('th');
        diffHeader.textContent = 'Difference';
        tableHeaders.appendChild(diffHeader);
    }

    table.appendChild(tableHeaders);

    for (const point of exactData) {
        const tableRow = document.createElement('tr');
        const xCell = document.createElement('td');
        xCell.textContent = point.x.toFixed(3);
        const yCell = document.createElement('td');
        if (isFinite(point.y)) {
            yCell.textContent = point.y.toFixed(3);
        } else {
            yCell.textContent = point.y;
        }

        tableRow.appendChild(xCell);
        tableRow.appendChild(yCell);

        if (difference) {
            const diffCell = document.createElement('td');
            if (isFinite(point.difference)) {
                diffCell.textContent = point.difference.toFixed(3);
            } else {
                diffCell.textContent = point.difference;
            }

            tableRow.appendChild(diffCell);
        }
        table.appendChild(tableRow);
    }

    arrayBlock.appendChild(table);
    container.appendChild(arrayBlock);
}

function generateDisplayBlock(data) {
    const displayContainer = document.createElement("div");
    displayContainer.id = "display-container";

    const exactData = data.exactPoints;

    const exactArrayBlock = document.createElement("div");
    exactArrayBlock.classList.add("array-block");

    const exactArrayNameHeading = document.createElement("h2");
    exactArrayNameHeading.textContent = 'Exact';
    exactArrayBlock.appendChild(exactArrayNameHeading);

    if (!Array.isArray(exactData) && exactData.length === 0) {
        const errorMessage = document.createElement("p");
        errorMessage.textContent = "No data available.";
        exactArrayBlock.appendChild(errorMessage);
        displayContainer.appendChild(exactArrayBlock)
    } else {
        fillTable(displayContainer, exactData, 'Exact', false);
    }
    
    const eulerData = data.eulerPoints;

    const eulerArrayBlock = document.createElement("div");
    eulerArrayBlock.classList.add("array-block");

    const eulerArrayNameHeading = document.createElement("h2");
    eulerArrayNameHeading.textContent = 'Euler';
    eulerArrayBlock.appendChild(eulerArrayNameHeading);

    if (!Array.isArray(eulerData) || eulerData.length === 0) {
        const errorMessage = document.createElement("p");
        errorMessage.textContent = data.eulerErrorMessage;
        eulerArrayBlock.appendChild(errorMessage);
        displayContainer.appendChild(eulerArrayBlock)
    } else {
        fillTable(displayContainer, eulerData, 'Euler', false);
    }

    const modifiedEulerData = data.modifiedEulerPoints;

    const modifiedEulerArrayBlock = document.createElement("div");
    modifiedEulerArrayBlock.classList.add("array-block");

    const modifiedEulerArrayNameHeading = document.createElement("h2");
    modifiedEulerArrayNameHeading.textContent = 'Modified Euler';
    modifiedEulerArrayBlock.appendChild(modifiedEulerArrayNameHeading);

    if (!Array.isArray(modifiedEulerData) || modifiedEulerData.length === 0) {
        const errorMessage = document.createElement("p");
        errorMessage.textContent = data.modifiedEulerErrorMessage;
        modifiedEulerArrayBlock.appendChild(errorMessage);
        displayContainer.appendChild(modifiedEulerArrayBlock)
    } else {
        fillTable(displayContainer, modifiedEulerData, 'Modified Euler', false);
    }

    const rungeKuttData = data.rungeKuttPoints;

    const rungeKuttArrayBlock = document.createElement("div");
    rungeKuttArrayBlock.classList.add("array-block");

    const rungeKuttArrayNameHeading = document.createElement("h2");
    rungeKuttArrayNameHeading.textContent = 'Runge-Kutt';
    rungeKuttArrayBlock.appendChild(rungeKuttArrayNameHeading);

    if (!Array.isArray(rungeKuttData) || rungeKuttData.length === 0) {
        const errorMessage = document.createElement("p");
        errorMessage.textContent = data.rungeKuttErrorMessage;
        rungeKuttArrayBlock.appendChild(errorMessage);
        displayContainer.appendChild(rungeKuttArrayBlock)
    } else {
        fillTable(displayContainer, rungeKuttData, 'Runge-Kutt', false);
    }

    const milnData = data.milnPoints;

    const milnArrayBlock = document.createElement("div");
    milnArrayBlock.classList.add("array-block");

    const milnArrayNameHeading = document.createElement("h2");
    milnArrayNameHeading.textContent = 'Miln';
    milnArrayBlock.appendChild(milnArrayNameHeading);

    if (!Array.isArray(milnData) || milnData.length === 0) {
        const errorMessage = document.createElement("p");
        errorMessage.textContent = data.milnErrorMessage;
        milnArrayBlock.appendChild(errorMessage);
        displayContainer.appendChild(milnArrayBlock)
    } else {
        fillTable(displayContainer, milnData, 'Miln', true);
    }

    document.body.appendChild(displayContainer);
}

function clearDisplayBlocks() {
    const displayContainer = document.querySelector("#display-container");
    if (displayContainer) {
        displayContainer.remove();
    }
}

function generateGraph(data, board) {

    let x_delta = Math.abs(data.xmax / 10),
        y_delta = Math.abs(data.ymax / 10)

    board = JXG.JSXGraph.initBoard('jxgbox', {
        boundingbox: [data.xmin - x_delta, data.ymax + y_delta, data.xmax + x_delta, data.ymin - y_delta],
        axis: true,
        showCopyright: false
    });

    if (data.exactPoints != null) {
        draw_points(board, data.exactPoints, '#3366cc')
    }
    if (data.eulerPoints != null) {
       draw_points(board, data.eulerPoints, '#dc3912')
    }
    if (data.modifiedEulerPoints != null) {
        draw_points(board, data.modifiedEulerPoints, '#ff9900')
    }
    if (data.rungeKuttPoints != null) {
        draw_points(board, data.rungeKuttPoints, '#ffdb00')
    }
    if (data.milnPoints != null) {
        draw_points(board, data.milnPoints, '#40ff00')
    }
}

function draw_points(board, points, color) {

    for (let i = 0; i < points.length; i++) {
        let point = points[i];
        if (!isFinite(point.y)) {
            return;
        }
    }

    points.forEach((point) => {
        board.create("point", [point.x, point.y], {fixed: true, color: color, label: "point"});
    });
}


