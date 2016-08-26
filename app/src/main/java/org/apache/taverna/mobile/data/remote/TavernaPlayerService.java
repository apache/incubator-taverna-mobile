package org.apache.taverna.mobile.data.remote;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface TavernaPlayerService {

    @POST("/workflows.json")
    @Headers({
            APIEndPoint.JSON_CONTENT_HEADER,
            APIEndPoint.UTF_CONTENT_ENCODING_HEADER})
    Observable<ResponseBody> uploadWorkflow(@Body RequestBody body, @Header("Authorization") String
            authorization);

}
