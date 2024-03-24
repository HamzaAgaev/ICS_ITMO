<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<%!
    enum CoordStatus {
        GOOD_COORDS, STR_NOT_NUM, OUT_OF_RANGE, EMPTY_VALUE
    }

    enum ErrorCause {
        BAD_COORDS, WRONG_REQUEST
    }

    public CoordStatus CoordStatusCode;
    public ErrorCause errorCauseCode;
%>
<html lang="en">
<head>
    <title>Error Page - My Pretty Java Site</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/omnom.jpg">
</head>
<body>
    <h1>There are errors in your request.</h1>
    <%
        try {
            CoordStatusCode = CoordStatus.values()[(int) session.getAttribute("coordStatus")];
            errorCauseCode = ErrorCause.BAD_COORDS;
        }
        catch (NullPointerException NPE) {
            errorCauseCode = ErrorCause.WRONG_REQUEST;
        }
    %>

    <%
        if (errorCauseCode == ErrorCause.BAD_COORDS) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    %>
    <p>Your request is terrible.</p>
    <p>You sent bad coordinates:
    <%
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    %>
    <p>You don't have permission to this page.</p>
    <%
        }
        if (errorCauseCode == ErrorCause.BAD_COORDS) {
            if (CoordStatusCode == CoordStatus.STR_NOT_NUM)  {
    %>
    try to enter a decimal number, not a string.</p>
    <%
            } else if (CoordStatusCode == CoordStatus.OUT_OF_RANGE) {
    %>
    some of your coordinates are out of range.</p>
    <%
            } else {
    %>
    some of your inputs are empty.</p>
    <%
            }
        }
    %>
    <a href="/lab_2">Go home</a>
</body>
</html>