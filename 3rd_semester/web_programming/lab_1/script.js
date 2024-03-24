function setX(x) {
    let hidden_input = document.getElementsByName("X")[0];
    hidden_input.value = x;
}

window.onload = function () {
    getTable(0, 0, 0, "start");
    var submit = document.getElementById("send");
    submit.addEventListener("click", sendInputs);
}

function sendInputs() {
    let pos_list = isCorrect();

    let info_text = document.getElementById("info_text");

    if (pos_list != 0 && pos_list != 1 && pos_list != 2) {
        let X = pos_list[1];
        let Y = pos_list[2];
        let R = pos_list[3];
        // getTableRow(X, Y, R);
        getTable(X, Y, R, "exec");
        // СТРОКА ТАБЛИЦЫ ДОЛЖНА СОЗДАВАТЬСЯ В PHP
        info_text.style.color = "green";
        info_text.innerHTML = "The request was successfully processed.";
    } else {
        info_text.style.color = "red";

        if (pos_list == 0) {
            info_text.innerHTML = "Enter a number, not a string.";
        } else if (pos_list == 1) {
            info_text.innerHTML = "Some of the entered values are out of tolerance.";
        } else {
            info_text.innerHTML = "The number of digits in the decimal part of all values shall not exceed 4.";
        }
    }
}

function isCorrect() {
    let x = document.getElementsByName("X")[0].value;
    let y = document.getElementsByName("Y")[0].value;
    let r = document.getElementsByName("R")[0].value;

    let X = Math.round(parseFloat(x) * 10000) / 10000;
    let Y = Math.round(parseFloat(y) * 10000) / 10000;
    let R = Math.round(parseFloat(r) * 10000) / 10000;

    console.log(X, Y, R)

    if (X.toString() == x && Y.toString() == y && R.toString() == r && R >= 0) {
        if (X >= -3 && X <= 5 && Y >= -3 && Y <= 5 && R >= 1 && R <= 5) {
            return [true, X, Y, R];
        } else {
            return 1;
        }        
    } else {
        if (parseFloat(x) - X != 0 || parseFloat(y) - Y != 0 || parseFloat(r) - R != 0) {
            if (Object.is(X, NaN) || Object.is(Y, NaN) || Object.is(R, NaN)) {
                return 0;
            } else {
                return 2;
            }            
        } else {
            return 0;
        }
    }
}

// function getTableRow(x, y, r) {
//     let httpRequest = new XMLHttpRequest();
//     let URL = "/script.php?X=" + x + "&Y=" + y + "&R=" + r;
//     httpRequest.open("GET", URL, false);
//     httpRequest.send(null);

//     httpRequest.addEventListener("load", console.log(httpRequest.status), false);

//     let table_row_str = httpRequest.responseText;

//     let table =  document.getElementById("restab");
//     let table_row = document.createElement("tr");
//     table_row.innerHTML = table_row_str;

//     table.appendChild(table_row);
// }

function getTable(x, y, r, mode) {
    let httpRequest = new XMLHttpRequest();
    let URL = "./script.php?X=" + x + "&Y=" + y + "&R=" + r + "&mode=" + mode;
    httpRequest.open("GET", URL, false);
    httpRequest.send(null);

    httpRequest.addEventListener("load", console.log(httpRequest.status), false);

    let table_rows_str = httpRequest.responseText;

    let table =  document.getElementById("restab");
    while (table.childElementCount != 1) {
        table.removeChild(table.lastElementChild);
    }

    table_rows_str = table_rows_str.split("\n");

    for (i = 0; i < table_rows_str.length; i++) {
        let table_row = document.createElement("tr");
        table_row.innerHTML = table_rows_str[i];    
        table.appendChild(table_row);
    }
}