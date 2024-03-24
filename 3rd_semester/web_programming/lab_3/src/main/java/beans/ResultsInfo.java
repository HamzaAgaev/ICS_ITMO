package beans;

import database.ResultsDAO;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.ApplicationScoped;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean(name = "results")
@ApplicationScoped
public class ResultsInfo implements Serializable {
    private ArrayList<Result> resultArrayList;
//    private Result currentResult;

    public ResultsInfo() {
        resultArrayList = new ArrayList<>();
//        currentResult = new Result();
//        currentResult.setX(0);
//        currentResult.setY(0);
//        currentResult.setR(1);
    }

//    public Result getCurrentResult() {
//        return currentResult;
//    }
//    public void setCurrentResult(Result result) {
//        currentResult = result;
//    }
    public ArrayList<Result> getResultArrayList() {
        try {
            ResultsDAO resultsDAO = new ResultsDAO();
            resultArrayList = resultsDAO.getResultArrayList();
        } catch (Throwable error) {
            System.err.println("Cannot get resultArrayList from ResultsDAO. " + error);
        }

        return resultArrayList;
    }
    public void setResultArrayList(ArrayList<Result> arrayList) {
        resultArrayList = arrayList;
    }

    public void addNewResult(CurrentResult currentResult) {
        Result copyResult = new Result();
        copyResult.setX(currentResult.getX());
        copyResult.setY(currentResult.getY());
        copyResult.setR(currentResult.getR());
        copyResult.setHitResult();
        copyResult.setCurrentDateAndTime();
//        resultArrayList.add(copyResult);
        try {
            ResultsDAO resultsDAO = new ResultsDAO();
            resultsDAO.addNewResult(copyResult);
            getResultArrayList();
        } catch (Throwable error) {
            System.err.println("Cannot add new Result to ResultsDAO. " + error);
        }
    }

    public void clearResults() {
        try {
            ResultsDAO resultsDAO = new ResultsDAO();
            resultsDAO.clear();
            getResultArrayList();
        } catch (Throwable error) {
            System.err.println("Cannot clear Results from ResultsDAO. " + error);
        }
    }
}
