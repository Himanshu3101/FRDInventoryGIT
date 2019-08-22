package com.yoeki.kalpnay.frdinventry.Api;

import com.yoeki.kalpnay.frdinventry.InventoryCounting.JiournalResponse;
import com.yoeki.kalpnay.frdinventry.InventoryCounting.RequestBodyIVCDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.GetRequisitionPending;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.InventoryPendingModel;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ParticularRequisitionDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.Model.ResponseShippingDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.RequestBodyShipDetails;
import com.yoeki.kalpnay.frdinventry.InventoryShipperPicker.UserIDModel;
import com.yoeki.kalpnay.frdinventry.MRN.Model.MrnNumberDetailResponse;
import com.yoeki.kalpnay.frdinventry.MRN.Model.PostingJsonRequest;
import com.yoeki.kalpnay.frdinventry.MRN.Model.PostingJsonResponse;
import com.yoeki.kalpnay.frdinventry.MRN.Model.mrnNumberDetailsRequest;
import com.yoeki.kalpnay.frdinventry.Model.ChangePaswd;
import com.yoeki.kalpnay.frdinventry.Login.loginModel.LoginUser;
import com.yoeki.kalpnay.frdinventry.Model.MRN.GetMRNModels;
import com.yoeki.kalpnay.frdinventry.Model.changePswdResponse;
import com.yoeki.kalpnay.frdinventry.Login.loginModel.LoginResponse;
import com.yoeki.kalpnay.frdinventry.QRDetails.RequestBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.QRDetails.ResponseBodyQRDetails;
import com.yoeki.kalpnay.frdinventry.QRDetailsQuantityUpdate.UpdateRequestDetails;
import com.yoeki.kalpnay.frdinventry.banchInventoryReceiving.RequestBodyReceiveDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("FRD/Login")
    Call<LoginResponse> login(@Body LoginUser user);

    @POST("FRD/GetCountData")
    Call<ResponseBodyQRDetails> inventoryCounting(@Body UserIDModel userIDModel);

    @POST("FRD/ChangePassword")
    Call<changePswdResponse> changePassword(@Body ChangePaswd user);

    @POST("FRD/GetMrn")
    Call<GetMRNModels> getMRNDataModels();

    @POST("FRD/GetMrnDetails")
    Call<MrnNumberDetailResponse> getMRNDetails(@Body mrnNumberDetailsRequest user);

    @POST("FRD/PackingSlip")
    Call<PostingJsonResponse> mrnPosting(@Body PostingJsonRequest postingJsonRequest);

    @POST("FRD/GetRequestNo")
    Call<GetRequisitionPending> requisitionPending(@Body UserIDModel userIDModel);

    @POST("FRD/RequestControlPending")
    Call<InventoryPendingModel> inventoryPicker(@Body ParticularRequisitionDetails userIDModel);

    @POST("FRD/RequestControlcomplete")
    Call<InventoryPendingModel> inventoryComplete(@Body ParticularRequisitionDetails userIDModel);

    @POST("FRD/GetdataQRWise")
    Call<ResponseBodyQRDetails> QRWiseData(@Body RequestBodyQRDetails requestBodyQRDetails);

    @POST("FRD/Shiping")
    Call<ResponseShippingDetails> redShipping(@Body RequestBodyShipDetails requestBodyShipDetails);

    @POST("FRD/GetCompleteRequestNo")
    Call<GetRequisitionPending> completeRequestNumber(@Body UserIDModel userIDModel);


    @POST("FRD/Receiving")
    Call<PostingJsonResponse> forBranchInventoryReceiving(@Body RequestBodyReceiveDetails requestBodyReceiveDetails);


    @POST("FRD/InventoryCountingHeader")
    Call<JiournalResponse> getJournalNum(@Body UserIDModel userIDModel);

    @POST("FRD/InventoryCountingLine")
    Call<PostingJsonResponse> inventoryCountingLine(@Body RequestBodyIVCDetails requestBodyIVCDetails);

    @POST("FRD/UpdateStickerQty")
    Call<PostingJsonResponse> updateStickerQty(@Body UpdateRequestDetails updateRequestDetails);

    @POST("FRD/DeleteJounralFromDevice")
//   Call<PostingJsonResponse> deleteJournal(@Body List<ResponseBodyQRDetails> updateRequestDetails);
    Call<PostingJsonResponse> deleteJournal(@Body UpdateJournalList responseBodyQRDetails);

}
