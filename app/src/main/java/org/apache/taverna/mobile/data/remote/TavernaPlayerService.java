package org.apache.taverna.mobile.data.remote;


import org.apache.taverna.mobile.data.model.PlayerWorkflow;
import org.apache.taverna.mobile.data.model.PlayerWorkflowDetail;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface TavernaPlayerService {

    @POST("/workflows.json")
    @Headers({
            APIEndPoint.JSON_CONTENT_HEADER,
            APIEndPoint.JSON_ACCEPT_HEADER,
            APIEndPoint.UTF_CONTENT_ENCODING_HEADER})
    Observable<PlayerWorkflow> uploadWorkflow(@Body RequestBody body, @Header("Authorization")
            String authorization);

    @POST("/users/sign_in")
    @Headers({
            APIEndPoint.XML_ACCEPT_HEADER})
    Observable<ResponseBody> playerlogin(@Header("Authorization") String
                                                 authorization);


    @GET("/runs/new")
    @Headers({
            APIEndPoint.JSON_CONTENT_HEADER,
            APIEndPoint.JSON_ACCEPT_HEADER})
    Observable<PlayerWorkflowDetail> getWorkflowDetail(@Query("workflow_id") int id);
}
