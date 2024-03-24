package servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AreaCheckServlet extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect("jsp/error.jsp");
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date startDate = new Date();
        String xStr = request.getParameter("x");
        String yStr = request.getParameter("y");
        String RStr = request.getParameter("R");
        BigDecimal x = new BigDecimal(xStr);
        BigDecimal y = new BigDecimal(yStr);
        BigDecimal R = new BigDecimal(RStr);

        boolean inArea = AreaCheckServlet.isInArea(x, y, R);

        HttpSession session = request.getSession();
        List<String> row = new ArrayList<>();
        List<List<String>> result = (List<List<String>>) session.getAttribute("results");

        if (result == null) {
            result = new ArrayList<>();
        }

        Date endDate = new Date();

        row.add(dateFormat.format(startDate));
        row.add(Long.toString(endDate.getTime() - startDate.getTime()));
        row.add(xStr);
        row.add(yStr);
        row.add(RStr);
        row.add(Boolean.toString(inArea));
        result.add(row);
        session.setAttribute("results", result);

        session.setMaxInactiveInterval(20 * 60);
        request.getRequestDispatcher("jsp/tableRow.jsp").forward(request, response);
    }

    private static boolean isInArea(BigDecimal x, BigDecimal y, BigDecimal R) {
        BigDecimal zero = new BigDecimal(0);
        BigDecimal two = new BigDecimal(2);
        BigDecimal m_one = new BigDecimal(-1);
        if (x.compareTo(zero) > 0) {
            if (y.compareTo(zero) > 0) {
                return false;
            } else if ((x.subtract(y)).compareTo(R.divide(two, 2, RoundingMode.UNNECESSARY)) > 0) {
                return false;
            }
        } else {
            if (y.compareTo(zero) > 0) {
                if (x.compareTo(R.multiply(m_one)) < 0 || y.compareTo(R.divide(two, 2, RoundingMode.UNNECESSARY)) > 0) {
                    return false;
                }
            } else if (((x.pow(2)).add(y.pow(2))).compareTo(R.divide(two, 2, RoundingMode.UNNECESSARY).pow(2)) > 0) {
                return false;
            }
        }
        return true;
    }
}
