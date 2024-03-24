class CanvasManager {
    static defaultResult = {x: 0.0, y: 0.0, r: 1.0, isHit: true, dateAndTime: '00:00:00 01-01-1970'};
    radiusLenInPixels = 50;
    canvasUnitX = 50;
    canvasUnitY = 50;
    width = 550;
    height = 550;
    canvasElement = document.getElementById("canvas");
    constructor() {
        this.getArrayOfPoints();
        this.getCurrentResult();
    }

    getArrayOfPoints() {
        let resultsArrayKey = "results-array";
        let arrayOfPointsStr = localStorage.getItem(resultsArrayKey);
        let arrayOfPoints;
        if (arrayOfPointsStr !== "") {
            arrayOfPoints = JSON.parse(arrayOfPointsStr);
            if (arrayOfPoints === null) {
                arrayOfPoints = [];
            }
        } else {
            arrayOfPoints = [];
        }
        this.arrayOfPoints = arrayOfPoints;
    }

    getCurrentResult() {
        let resultKey = "current-result";
        let currentResultStr = localStorage.getItem(resultKey);
        let currentResult;
        if (currentResultStr !== "") {
            currentResult = JSON.parse(currentResultStr);
            if (currentResult === null) {
                currentResult = CanvasManager.defaultResult;
            }
        } else {
            currentResult = CanvasManager.defaultResult;
        }
        this.currentResult = currentResult;
    }

    getStartCanvas() {
        this.getCanvasWH();
        this.getCurrentResult();
        this.getArrayOfPoints();

        let radiusUnit = parseFloat(this.currentResult.r);

        let RADIUS = this.radiusLenInPixels * radiusUnit;
        // let canvas = document.getElementById("canvas");
        // this.canvasElement = canvas;
        let cntx = this.canvasElement.getContext("2d");

        cntx.clearRect(0, 0, this.width, this.height);
        cntx.fillStyle = "#0000FF";

        // rectangle
        cntx.fillRect(this.width / 2 , this.height / 2, RADIUS, RADIUS);
        // 1/4 of circle
        cntx.beginPath();
        cntx.arc(this.width / 2, this.height / 2, RADIUS / 2, 0.5 * Math.PI, Math.PI, false);
        cntx.closePath();
        cntx.fill();
        // need for circle
        cntx.beginPath();
        cntx.moveTo(this.width / 2, this.height / 2 + RADIUS / 2);
        cntx.lineTo(this.width / 2, this.height / 2);
        cntx.lineTo(this.width / 2 - RADIUS / 2, this.height / 2);
        cntx.closePath();
        cntx.fill();
        // triangle
        cntx.beginPath();
        cntx.moveTo(this.width / 2, this.height / 2 - RADIUS);
        cntx.lineTo(this.width / 2, this.height / 2);
        cntx.lineTo(this.width / 2 - RADIUS / 2, this.height / 2);
        cntx.closePath();
        cntx.fill();

        cntx.fillStyle = "#000000";
        // line vertical
        cntx.beginPath();
        cntx.moveTo(this.width / 2, this.height / 2 + this.height / 2 * 0.95);
        cntx.lineTo(this.width / 2, this.height / 2 - this.height / 2 * 0.95);
        cntx.closePath();
        cntx.stroke();
        // line horizontal
        cntx.beginPath();
        cntx.moveTo(this.width / 2 + this.width / 2 * 0.95, this.height / 2);
        cntx.lineTo(this.width / 2 - this.width / 2 * 0.95, this.height / 2);
        cntx.closePath();
        cntx.stroke();

        // cntx.fillStyle = "#00FF00";

        for (let i = 0; i < this.arrayOfPoints.length; i++) {
            let [x, y] = this.getCoordsForXY(this.arrayOfPoints[i].x, this.arrayOfPoints[i].y);
            if (this.arrayOfPoints[i].isHit) {
                cntx.fillStyle = "#00FF00";
            } else {
                cntx.fillStyle = "#FF0000";
            }
            cntx.beginPath();
            cntx.arc(x, y, 3, 0, 2 * Math.PI, false);
            cntx.closePath();
            cntx.fill();
        }
    }

    handleClick(event) {
        const rect = this.canvasElement.getBoundingClientRect(); // Get canvas position and size
        const scaleX = this.width / rect.width;
        const scaleY = this.height / rect.height;

        const xClicked = Math.round((event.clientX - rect.left) * scaleX - this.width / 2) / this.canvasUnitX;
        const yClicked = Math.round(-1 * ((event.clientY - rect.top) * scaleY - this.height / 2)) / this.canvasUnitY;

        return [xClicked, yClicked];
    }

    getCanvasUnit() {
        this.canvasUnitX = this.width / 11;
        this.canvasUnitY = this.height / 11;
    }

    getCanvasWH() {
        // let canvas = document.getElementById("canvas");
        this.width = this.canvasElement.width;
        this.height = this.canvasElement.height;
    }

    getCoordsForXY(x, y) {
        this.getCanvasUnit();
        let coordX = this.canvasUnitX * x + this.width / 2;
        let coordY = this.height / 2 - this.canvasUnitY * y;
        return [coordX, coordY];
    }
}

function setCurrentResultToStorage(resultStr) {
    let resultKey = "current-result";
    if (resultStr !== "" && JSON.parse(resultStr) !== null) {
        localStorage.removeItem(resultKey);
        localStorage.setItem(resultKey, resultStr);
        localStorage.getItem(resultKey);
    }
}

function addNewResultToStorage(resultStr) {
    let resultsArrayKey = "results-array";
    let newResult = JSON.parse(resultStr);
    if (resultStr !== "" && newResult !== null) {
        let resultArrayStr = localStorage.getItem(resultsArrayKey);
        let resultArray = [];
        if (resultArrayStr !== "") {
            resultArray = JSON.parse(resultArrayStr);
        }
        if (resultArray === null) {
            resultArray = [];
        }
        resultArray.push(newResult);
        localStorage.setItem(resultsArrayKey, JSON.stringify(resultArray));
    }
}

function removeResultListFromStorage() {
    let resultsArrayKey = "results-array";
    if (localStorage.getItem(resultsArrayKey) !== "") {
        localStorage.removeItem(resultsArrayKey);
    }
}
