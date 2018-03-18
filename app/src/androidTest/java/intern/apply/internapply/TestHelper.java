package intern.apply.internapply;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.List;

import intern.apply.internapply.model.ServerError;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class TestHelper {

    public static String LOCAL_HOST_URL = "http://10.0.2.2:3000/";

    public static HttpException CreateHttpException(List<ServerError> errors) {
        JsonArray errorBody = new JsonArray();

        for (ServerError error : errors) {
            JsonObject jsonError = new JsonObject();
            jsonError.addProperty("code", error.getCode());
            jsonError.addProperty("message", error.getMessage());
            errorBody.add(jsonError);
        }

        return new HttpException(
                Response.error(400,
                        ResponseBody.create(
                                MediaType.parse("application/json; charset=utf-8"),
                                errorBody.toString())));
    }


    public static void findStrings(String[] expectedStrings, Solo solo) {
        for (String s : expectedStrings)
            Assert.assertTrue("text not found", solo.waitForText(s));
    }
}
