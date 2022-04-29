/*
 * SPDX-License-Identifier: Apache-2.0
 */
package stp.chaincode;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import com.owlike.genson.Genson;

@Contract(name = "RequestForQuotationContract",
    info = @Info(title = "RequestForQuotation contract",
                description = "My Smart Contract",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "RequestForQuotation@example.com",
                                                name = "RequestForQuotation",
                                                url = "http://RequestForQuotation.me")))
@Default
public class RequestForQuotationContract implements ContractInterface {

    public  RequestForQuotationContract() {}

    private final Genson genson = new Genson();

    private enum RfqError{
        RFQ_NOT_FOUND,
        RFQ_ALREADY_EXIST
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public RequestForQuotation addRequestForQuotation(final Context ctx,final String requestForQuotationId
                                                ,final  String customer, final String material, final String vendorList){
        ChaincodeStub stub = ctx.getStub();
        String RFQKey = getRFQKey(ctx, requestForQuotationId);

        if(requestForQuotationExists(ctx, RFQKey)){
            String errorMessage = String.format("Rfq %s already exist.", requestForQuotationId);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage,RfqError.RFQ_ALREADY_EXIST.toString());
        }
        
        RequestForQuotation rfq = new RequestForQuotation(customer,material,vendorList);

        String rfqJSON = genson.serialize(rfq);
        stub.putStringState(RFQKey, rfqJSON);
        return rfq;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public RequestForQuotation readRFQ(final Context ctx, final String requestForQuotationId){
        ChaincodeStub stub = ctx.getStub();
        String RFQKey = getRFQKey(ctx, requestForQuotationId);
        String rfqJSON= stub.getStringState(RFQKey);

        if(requestForQuotationExists(ctx, RFQKey)){
            String errorMessage = String.format("Rfq %s does not exist.", requestForQuotationId);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage,RfqError.RFQ_NOT_FOUND.toString());
        }
        return genson.deserialize(rfqJSON, RequestForQuotation.class);
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean requestForQuotationExists(Context ctx, String RFQKey) {
        ChaincodeStub stub = ctx.getStub();
        String rfqJSON = stub.getStringState(RFQKey);
        
        return (rfqJSON !=null && !rfqJSON.isEmpty());
    }

    public String getRFQKey(final Context ctx, String id){
        ChaincodeStub stub= ctx.getStub();
        String assetType="RequestForQuotation";
        String version= "v1";
        CompositeKey ck = stub.createCompositeKey(version,assetType,id);

        if(ck == null){
            System.out.println("getRFQKey() stub function returned null, generating using constructor");
            ck= new CompositeKey(version,assetType,id);
        }
        return ck.toString();
    }


    //======================================================
    //AutoGenerated Code from Ibm exstension
    //======================================================

    @Transaction()
    public void deleteRequestForQuotation(Context ctx, String requestForQuotationId) {
        boolean exists = requestForQuotationExists(ctx,requestForQuotationId);
        if (!exists) {
            throw new RuntimeException("The asset "+requestForQuotationId+" does not exist");
        }
        ctx.getStub().delState(requestForQuotationId);
    }

}