<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.List" %>

<%
    List<List<String>> result = (List<List<String>>) session.getAttribute("results");
    if (result != null) {
        for (List<String> row: result) {
            %><tr><%
            for (String value: row) {
                %><td><%=value%></td><%
            }
            %></tr>
<%
        }
    }
%>
