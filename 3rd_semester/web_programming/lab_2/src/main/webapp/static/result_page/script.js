const successCats = [
    "/lab_2/static/result_page/images/cute-happy-cat.gif",
    "/lab_2/static/result_page/images/dima-cat.gif",
    "/lab_2/static/result_page/images/happy-cat.gif",
    "/lab_2/static/result_page/images/milky-cat.gif",
    "/lab_2/static/result_page/images/strong-cat.jpg"
]

function setSuccessCat() {
    let catImage = document.getElementById("cat");
    let catIndex = Math.round(Math.random() * (successCats.length - 1));
    catImage.src = successCats[catIndex];
}

window.onload = function () {
    setSuccessCat();
    setInterval(setSuccessCat, 3500);
}