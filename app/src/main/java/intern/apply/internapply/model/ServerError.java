package intern.apply.internapply.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ServerError {
    private int code;
    private String message;

    public ServerError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * get ServerError list from Observable error
     * @param error error to convert to a ServerError
     * @return the list of server errors found in the error object
     */
    public static List<ServerError> getErrorsFromServerException(Throwable error) {
        try {
            HttpException e = (HttpException)error;
            String errorBody;
            errorBody = e.response().errorBody().string();
            Gson gson = new Gson();
            Type type = new TypeToken<List<ServerError>>() {}.getType();
            return gson.fromJson(errorBody, type);
        } catch (Exception ex) {
            List<ServerError> unknownError = new LinkedList<>();
            unknownError.add(new ServerError(0, "unknown server error"));
            return unknownError;
        }
    }
}