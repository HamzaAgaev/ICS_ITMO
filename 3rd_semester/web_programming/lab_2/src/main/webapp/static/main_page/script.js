//COORDS
const GOOD_COORDS = 0;
const STR_NOT_NUM = 1;
const OUT_OF_RANGE = 2;

const BAD_COORD_VALUE = -999;

//CANVAS
let arrayOfPoints = [];
let PointPretedent = [BAD_COORD_VALUE, BAD_COORD_VALUE];
let radiusLenInPixels = 40;

const errorCats = [
    "/lab_2/static/main_page/images/cat-dancer.gif",
    "/lab_2/static/main_page/images/angry-cat.gif",
    "/lab_2/static/main_page/images/cat-types.gif",
    "/lab_2/static/main_page/images/eating-cat.gif",
    "/lab_2/static/main_page/images/poor-cat.gif"
];

window.onload = function () {
    // Send coords with AJAX
    let submit = document.getElementById("send");
    submit.addEventListener("click", checkAndSendCoordinates);

    // Draw canvas on start
    setPointsInUse();
    setLastRequestTime();
    getStartCanvas();

    // Redraw canvas on radius change
    let selectR = document.getElementById("iR");
    selectR.addEventListener("change", getStartCanvas);

    // Set function getMouseCoordsOnClick to canvas on window load
    let canvas = document.getElementById("canvas");
    canvas.addEventListener("click", drawPointOnClick);

    // Get X, Y and R on window load
    getXAndY();

    // Get Table with results on window load
    const XHR = new XMLHttpRequest();1
    let CoordsQueryStr = "/lab_2/?getTable=true";
    XHR.open("GET", CoordsQueryStr, false);
    XHR.send(null);
    XHR.addEventListener("load", console.log(XHR.status), false);
    getTableRows(XHR);
}

//COORDS
function setX(x) {
    let x_inputs = document.getElementsByClassName("input_x");

    for (let i = 0; i < x_inputs.length; i++) {
        if (x_inputs[i].value != x) {
            x_inputs[i].checked = false;
        }
    }

    let hidden_x_input = document.getElementsByName("x")[0];
    hidden_x_input.value = x;
    getXAndY();
}

function getXAndY() {
    let x_coord = document.getElementsByName("x")[0].value;
    let y_coord = document.getElementsByName("y")[0].value;
    let radius = document.getElementsByName("R")[0].value;

    let x_elem = document.getElementById("x_coord");
    x_elem.innerHTML = "X: " + x_coord;
    let y_elem = document.getElementById("y_coord");
    y_elem.innerHTML = "Y: " + y_coord;
    let radius_elem = document.getElementById("radius");
    radius_elem.innerHTML = "Radius: " + radius;
}

function checkAndSendCoordinates() {
    let CoordsAndStatus = checkCoordinates();

    if (CoordsAndStatus[0] === GOOD_COORDS) {
        let xStr = CoordsAndStatus[1];
        let yStr = CoordsAndStatus[2];
        let Rstr = CoordsAndStatus[3];
        // let XHR = sendCoords(xStr, yStr, Rstr);
        // getTableRows(XHR);

        sendCoords();

        if (JSON.stringify(PointPretedent) !== JSON.stringify([BAD_COORD_VALUE, BAD_COORD_VALUE])) {
            setPointsInUse();
            setLastRequestTime();
            getArrayOfPoints();
            addPretedentToArray();
            getStartCanvas();
            PointPretedent = [BAD_COORD_VALUE, BAD_COORD_VALUE];
        } else {
            setLastRequestTime();
        }

    } else {
        let infoText = document.getElementById("info_text");
        let infoDiv = document.getElementById("info_div");
        let catImage = document.getElementById("cat");
        infoDiv.style.backgroundColor = "#F05365";
        let catIndex = Math.round(Math.random() * (errorCats.length - 1));
        if (CoordsAndStatus[0] === STR_NOT_NUM) {
            infoText.innerHTML = "Enter decimal Number, not a String.";
            // alert("Enter decimal Number, not a String.");
        } else if (CoordsAndStatus[0] === OUT_OF_RANGE) {
            infoText.innerHTML = "Some of entered values out of range.";
            // alert("Some of entered values out of range.");
        }

        catImage.src = errorCats[catIndex];
    }
}

function checkCoordinates() {
    let x = document.getElementsByName("x")[0].value;
    let y = document.getElementsByName("y")[0].value;
    let r = document.getElementsByName("R")[0].value;

    let x_num = parseFloat(x);
    let y_num = parseFloat(y);
    let r_num = parseFloat(r);

    let RETURN_CODE = GOOD_COORDS;

    if (Object.is(x_num, NaN) || Object.is(y_num, NaN) || Object.is(r_num, NaN)) {
        RETURN_CODE = STR_NOT_NUM;
    } else if (x_num.toString() != x || y_num.toString() != y || r_num.toString() != r) {
        RETURN_CODE = STR_NOT_NUM;
    } else if (x_num < -3 || x_num > 5 || y_num < -3 || y_num > 3 || r_num < 1 || r_num > 5) {
        RETURN_CODE = OUT_OF_RANGE;
    }

    return [RETURN_CODE, x, y, r];
}

function sendCoords() { //(x, y, r)
    // const XHR = new XMLHttpRequest();
    // let CoordsQueryStr = "x=" + encodeURIComponent(x) + "&y=" + encodeURIComponent(y) + "&R=" + encodeURIComponent(r);
    // XHR.open("POST", "/lab_2/", false);
    // XHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    // XHR.send(CoordsQueryStr);
    //
    // XHR.addEventListener("load", console.log(XHR.status), false);
    // return XHR;
    let form = document.getElementById("coordsForm");
    form.submit();
}

function getTableRows(XHR) {
    let tableRowsStr = XHR.responseText;

    let table =  document.getElementById("results_table");
    while (table.childElementCount !== 1) {
        table.removeChild(table.lastElementChild);
    }

    tableRowsStr = tableRowsStr.split("\n");

    for (let i = 0; i < tableRowsStr.length; i++) {
        let tableRow = document.createElement("tr");
        tableRow.innerHTML = tableRowsStr[i];
        table.appendChild(tableRow);
    }
}

function setPointsInUse() {
    let lastRequestSecondsStr = localStorage.getItem("last_req_time");
    if (lastRequestSecondsStr != null) {
        let lastRequestSeconds = parseInt(lastRequestSecondsStr);
        let currentTime = new Date();
        let currentSecondsFrom1970 = Math.round(currentTime.getTime() / 1000);
        if (currentSecondsFrom1970 - lastRequestSeconds > 20 * 60) {
            localStorage.clear();
        }
    }
}

function getArrayOfPoints() {
    arrayOfPoints = JSON.parse(localStorage.getItem("points_array"));
    if (arrayOfPoints == null) {
        arrayOfPoints = [];
    }
}

function addPretedentToArray() {
    arrayOfPoints.push(PointPretedent);
    localStorage.setItem("points_array", JSON.stringify(arrayOfPoints));
}

function setLastRequestTime() {
    let requestTime = new Date();
    let secondsFrom1970 = Math.round(requestTime.getTime() / 1000);
    localStorage.setItem("last_req_time", String(secondsFrom1970));
}

// CANVAS
// let arrayOfPoints = localStorage.getItem("points_array");
// if (arrayOfPoints == null) {
//     arrayOfPoints = [];
// }

function getStartCanvas() {
    getArrayOfPoints();
    console.log(arrayOfPoints);
    let radius_unit = parseFloat(document.getElementById("iR").value);
    let RADIUS = radiusLenInPixels * radius_unit;
    let canvas = document.getElementById("canvas");
    let cntx = canvas.getContext("2d");

    cntx.clearRect(0, 0, canvas.width, canvas.height);
    cntx.fillStyle = "#0000FF";

    cntx.fillRect(canvas.width / 2 - RADIUS, canvas.height / 2 - RADIUS / 2, RADIUS, RADIUS / 2);

    cntx.beginPath();
    cntx.arc(canvas.width / 2, canvas.height / 2, RADIUS / 2, 0.5 * Math.PI, Math.PI, false);
    cntx.closePath();
    cntx.fill();

    cntx.beginPath();
    cntx.moveTo(canvas.width / 2, canvas.height / 2 + RADIUS / 2);
    cntx.lineTo(canvas.width / 2, canvas.height / 2);
    cntx.lineTo(canvas.width / 2 - RADIUS / 2, canvas.height / 2);
    cntx.closePath();
    cntx.fill();

    cntx.beginPath();
    cntx.moveTo(canvas.width / 2, canvas.height / 2 + RADIUS / 2);
    cntx.lineTo(canvas.width / 2, canvas.height / 2);
    cntx.lineTo(canvas.width / 2 + RADIUS / 2, canvas.height / 2);
    cntx.closePath();
    cntx.fill();

    cntx.fillStyle = "#000000";

    cntx.beginPath();
    cntx.moveTo(canvas.width / 2, canvas.height / 2 + canvas.height / 2 * 0.9);
    cntx.lineTo(canvas.width / 2, canvas.height / 2 - canvas.height / 2 * 0.9);
    cntx.closePath();
    cntx.stroke();

    cntx.beginPath();
    cntx.moveTo(canvas.width / 2 + canvas.width / 2 * 0.9, canvas.height / 2);
    cntx.lineTo(canvas.width / 2 - canvas.width / 2 * 0.9, canvas.height / 2);
    cntx.closePath();
    cntx.stroke();

    cntx.fillStyle = "#00FF00";

    for (let i = 0; i < arrayOfPoints.length; i++) {
        cntx.beginPath();
        cntx.arc(arrayOfPoints[i][0], arrayOfPoints[i][1], 3, 0, 2 * Math.PI, false);
        cntx.closePath();
        cntx.fill();
    }
}

function setXAndYByClick(x_mouse, y_mouse) {
    let x_coord = x_mouse / radiusLenInPixels;
    let y_coord = y_mouse / radiusLenInPixels;

    let x_input = document.getElementsByName("x")[0];
    x_input.value = x_coord;

    let y_input = document.getElementsByName("y")[0];
    y_input.value = y_coord;
}

function drawPointOnClick(event) {
    let AreaAxises = getMouseCoordsOnClick(event);
    addPointOnClick(AreaAxises[0], AreaAxises[1]);
}
function getMouseCoordsOnClick(event) {
    let clickArea = document.getElementById("canvas");
    let x_mouse = Math.round(event.clientX - clickArea.getBoundingClientRect().left - clickArea.getBoundingClientRect().width / 2);
    let y_mouse = Math.round(clickArea.getBoundingClientRect().height / 2 - event.clientY + clickArea.getBoundingClientRect().top);

    console.log(x_mouse, y_mouse);
    setXAndYByClick(x_mouse, y_mouse);
    getXAndY();
    let x_AreaAxis = Math.round(event.clientX - clickArea.getBoundingClientRect().left);
    let y_AreaAxis = Math.round(event.clientY - clickArea.getBoundingClientRect().top);
    return [x_AreaAxis, y_AreaAxis];
}

function addPointOnClick(x_AreaAxis, y_AreaAxis) {
    getStartCanvas();
    let canvas = document.getElementById("canvas");
    let cntx = canvas.getContext("2d");
    console.log(x_AreaAxis, y_AreaAxis);
    cntx.fillStyle = "#FF0000";
    cntx.beginPath();
    cntx.arc(x_AreaAxis, y_AreaAxis, 4, 0, 2 * Math.PI, false);
    cntx.closePath();
    cntx.fill();

    PointPretedent = [x_AreaAxis, y_AreaAxis];
}