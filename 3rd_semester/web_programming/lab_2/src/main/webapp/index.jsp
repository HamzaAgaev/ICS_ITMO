<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>My Pretty Java Site</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Kanit&family=Slabo+27px&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/main_page/style.css">
        <script src="${pageContext.request.contextPath}/static/main_page/script.js"></script>
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/omnom.jpg">
    </head>
    <body>
        <div class="page_head">
            <span class="ph_text">
                Agaev Hamza Rustam ogly
            </span>
            <span class="ph_text">
                Group: P3234
            </span>
            <span class="ph_text">
                Variant: 3407
            </span>
        </div>
        <div class="main_content">
            <div class="about_div">
                <span class="about_text">
                    Limitations on values for coordinates: X: [-3, 5], Y: [-3, 3], R: [1, 5].
                    Other coordinate values will not be accepted as input to the program.
                </span>
            </div>
            <div class="coords">
                <span id="x_coord">X: </span>
                <span id="y_coord">Y: </span>
                <span id="radius">Radius: </span>
            </div>
            <div class="form_block">
                <form id="coordsForm" method="POST" action="/lab_2">
                    <div class="coord_form">
                        <div class="label_div">
                            <label class="label_text">X Value</label>
                        </div>
                        <input type="hidden" name="x" value="0">
                        <div class="x_block_input">
                        <label for="ix1" class="label_text">-3</label>
                        <input id="ix1" class="input_x" type="checkbox" value="-3" onclick="setX(-3)">
                        </div>
                        <div class="x_block_input">
                        <label for="ix2" class="label_text">-2</label>
                        <input id="ix2" class="input_x" type="checkbox" value="-2" onclick="setX(-2)">
                        </div>
                        <div class="x_block_input">
                        <label for="ix3" class="label_text">-1</label>
                        <input id="ix3" class="input_x" type="checkbox" value="-1" onclick="setX(-1)">
                        </div>
                        <div class="x_block_input">
                        <label for="ix4" class="label_text">0</label>
                        <input id="ix4" class="input_x" type="checkbox" value="0" checked onclick="setX(0)">
                        </div>
                        <div class="x_block_input">
                        <label for="ix5" class="label_text">1</label>
                        <input id="ix5" class="input_x" type="checkbox" value="1" onclick="setX(1)">
                        </div>
                        <div class="x_block_input">
                        <label for="ix6" class="label_text">2</label>
                        <input id="ix6" class="input_x" type="checkbox" value="2" onclick="setX(2)">
                        </div>
                        <div class="x_block_input">
                        <label for="ix7" class="label_text">3</label>
                        <input id="ix7" class="input_x" type="checkbox" value="3" onclick="setX(3)">
                        </div>
                        <div class="x_block_input">
                        <label for="ix8" class="label_text">4</label>
                        <input id="ix8" class="input_x" type="checkbox" value="4" onclick="setX(4)">
                        </div>
                        <div class="x_block_input">
                        <label for="ix9" class="label_text">5</label>
                        <input id="ix9" class="input_x" type="checkbox" value="5" onclick="setX(5)">
                        </div>
                    </div>

                    <div class="coord_form">
                        <div class="label_div">
                            <label for="iy" class="label_text">Y Value</label>
                        </div>
                        <input id="iy" type="text" name="y" value="0" placeholder="[-3, ..., 3]" onchange="getXAndY()" maxlength="7">
                    </div>

                    <div class="coord_form">
                        <div class="label_div">
                            <label for="iR" class="label_text">Radius value</label>
                        </div>
                        <select id="iR" name="R" onchange="getStartCanvas()">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                        </select>
                    </div>
                    <input type="button" id="send" value="Submit">
                </form>
            </div>
            <div class="canvas_table_div">
                <div class="canvas_div">
                    <canvas id="canvas" width="500px" height="500px">
                    </canvas>
                </div>
                <div class="table_info_div">
                    <div id="info_div">
                        <span id="info_text">Information about requests will be displayed here.</span>
                    </div>
                    <div class="table_block">
                        <table id="results_table">
                            <tr>
                                <th>Current Date</th>
                                <th>Execution time, µs</th>
                                <th>X</th>
                                <th>Y</th>
                                <th>Radius</th>
                                <th>In area</th>
                            </tr>
                        </table>
                    </div>
                    <div class="cat_img">
                        <img id="cat" src="${pageContext.request.contextPath}/static/images/kitty-cute.gif" alt="Thanks For Using!">
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>