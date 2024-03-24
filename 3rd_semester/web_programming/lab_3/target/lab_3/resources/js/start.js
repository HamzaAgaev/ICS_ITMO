const timeout = 13000;

window.onload = function () {
    updateClock();
    setInterval(updateClock, timeout);
};

function updateClock() {
    let date = new Date();
    let days = addZeroes(date.getDate());
    let months = addZeroes(date.getMonth() + 1);
    let years = String(date.getFullYear());
    document.getElementById('clock-date').innerText = days + '.' + months + '.' + years;
    let hours = addZeroes(date.getHours());
    let minutes = addZeroes(date.getMinutes());
    let seconds = addZeroes(date.getSeconds());
    document.getElementById('clock-time').innerText = hours + ':' + minutes + ':' + seconds;
}

function addZeroes(x) {
    if (x < 10) {
        return "0" + String(x);
    }
    return String(x);
}
