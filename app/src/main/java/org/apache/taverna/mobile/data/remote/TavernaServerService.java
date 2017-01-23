package org.apache.taverna.mobile.data.remote;

import org.apache.taverna.mobile.data.model.Inputs;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ian on 16/01/17.
 */

public interface TavernaServerService {

    @POST("rest/runs")
    @Headers({
            "Content-type: application/vnd.taverna.t2flow+xml"
    })
    Observable<Response<ResponseBody>> uploadWorkflow(@Body RequestBody body, @Header("Authorization")
            String authorization);

    @GET("rest/runs/{uuid}/input/expected")
    @Headers({APIEndPoint.XML_ACCEPT_HEADER})
    Observable<Inputs> getInputs(@Header("Authorization")
                                                         String authorization, @Path("uuid") String runLocationID);

}
