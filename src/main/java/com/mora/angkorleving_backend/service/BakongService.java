//package com.mora.angkorleving_backend.service;//package com.mora.backendangkorliving.service;
////
//////import com.tongbora.bakongapiintergration.dto.CheckTransactionRequest;
////import com.mora.backendangkorliving.DTOs.request.BakongRequest;
////import com.mora.backendangkorliving.DTOs.request.CheckTransactionRequest;
////import com.mora.backendangkorliving.DTOs.response.BakongResponse;
////import kh.gov.nbc.bakong_khqr.model.KHQRData;
////import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
////
////
////public interface BakongService {
////
////    KHQRResponse<KHQRData> generateQR(BakongRequest request);
////    byte[] getQRImage(KHQRData qr);
////    BakongResponse checkTransactionByMD5(CheckTransactionRequest request);
////}
package com.mora.angkorleving_backend.service;

import com.mora.angkorleving_backend.model.Payment;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;

public interface BakongService {

    KHQRResponse<KHQRData> generateQR(Long paymentId);

    Payment verifyPayment(Long paymentId);
}
