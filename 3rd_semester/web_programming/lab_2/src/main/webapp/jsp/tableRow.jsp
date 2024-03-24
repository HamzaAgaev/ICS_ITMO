<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.List" %>

<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Results for you Request - My Pretty Java Site</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Kanit&family=Slabo+27px&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/result_page/style.css">
    <script src="${pageContext.request.contextPath}/static/result_page/script.js"></script>
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
        <div class="result_div">
            <h1>There are results of your request.</h1>
            <table id="results_table">
                <tr>
                    <th>Current Date</th>
                    <th>Execution time, µs</th>
                    <th>X</th>
                    <th>Y</th>
                    <th>Radius</th>
                    <th>In area</th>
                </tr>
                <%
                    List<List<String>> result = (List<List<String>>) session.getAttribute("results");
                    if (result != null) {
                        List<String> row = result.get(result.size() - 1);
                        %><tr><%
                        for (String value: row) {
                        %><td><%=value%></td><%
                        }
                    %></tr>
                    <%
                    }
                %>
            </table>
            <a href="/lab_2">Go back</a>
        </div>
        <div class="cat_img">
            <img id="cat" src="" alt="Good Results!">
        </div>
    </div>
</body>
</html>
