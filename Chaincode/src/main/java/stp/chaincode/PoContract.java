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
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;

import java.util.ArrayList;
import java.util.List;

import com.owlike.genson.Genson;


@Contract(name = "PoContract",
    info = @Info(title = "PO contract",
                description = "My Smart Contract",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "Chaincode@example.com",
                                                name = "Chaincode",
                                                url = "http://Chaincode.me")))
@Default
public class PoContract implements ContractInterface{

    public  PoContract() {}

    private final Genson genson = new Genson();

    private enum PoError{
        PO_NOT_FOUND,
        PO_ALREADY_EXIST,
        PO_ALREADY_EVALUATED,
        //COMMISSIONID_INSERT_ERROR,
        PO_INVALID_STATE, 
    }


    // TODO ask about updatePODocs in stp 



    // TODO aggiungere controllo sulla commissionID
    @Transaction(intent = Transaction.TYPE.SUBMIT)
        public Po createPo ( final Context ctx, final String poID, 
                        final String commissionID, final String customer,
                        final String material, final String awardedSupplier){

            ChaincodeStub stub = ctx.getStub();
            String poKey = getPoKey(ctx, poID);

            if (poExists(ctx, poKey)){
            String errorMessage= String.format(" Purchase Order %s already exist.", poID);
            System.out.println(errorMessage);
            throw new ChaincodeException( errorMessage, PoError.PO_ALREADY_EXIST.toString());
        }
            Po purchaseOrder = new Po(commissionID, customer, material,awardedSupplier);

            String poJSON = genson.serialize(purchaseOrder);
            stub.putStringState(poKey, poJSON);

        return purchaseOrder;
     }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
        public Po readPo(final Context ctx, final String poID){
            ChaincodeStub stub = ctx.getStub();
            String poKey = getPoKey(ctx, poID);
            String poJSON = stub.getStringState(poKey);
            if (!poExists(ctx, poKey)){
                String errorMessage= String.format(" Purchase Order %s does not exist.", poID);
                System.out.println(errorMessage);
            throw new ChaincodeException( errorMessage, PoError.PO_NOT_FOUND.toString());
            }
            return genson.deserialize(poJSON, Po.class);
        }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
        public Po acceptPo(final Context ctx, final String poID, final String newState){
            ChaincodeStub stub = ctx.getStub();
            String poKey = getPoKey(ctx, poID);
            
            if(!poExists(ctx, poKey)){
                String errorMessage= String.format(" Purchase Order %s does not exist.", poID);
                System.out.println(errorMessage);
            throw new ChaincodeException( errorMessage, PoError.PO_NOT_FOUND.toString());
            }
            
            String poJSON= stub.getStringState(poKey);
            Po purchaseOrderPo = genson.deserialize(poJSON, Po.class);
            if(purchaseOrderPo.getState().equalsIgnoreCase("accepted")){
                String errorMessage = String.format("Po %s has been already accepted", poID);
                System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PoError.PO_ALREADY_EVALUATED.toString());
            }
            if(!newState.toLowerCase().equals("accepted")){
                String errorMessage= String.format("Invalid state input.");
                System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage,PoError.PO_INVALID_STATE.toString());
            }

            purchaseOrderPo.setState(newState.toLowerCase());
            String updatedPoJSON = genson.serialize(purchaseOrderPo);
            stub.putStringState(poKey, updatedPoJSON);
        return purchaseOrderPo; 
    
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getPoHistory(final Context ctx, final String poID) {

        if(poID == null){
            throw new RuntimeException("No ID given");
        }

        ChaincodeStub stub = ctx.getStub();
        List<PoHistoryDetails> historyPo = new ArrayList<>();
        String poKey=getPoKey(ctx,poID);

        QueryResultsIterator<KeyModification> resultsIterator = stub.getHistoryForKey(poKey);

        for (KeyModification history: resultsIterator) {

            String poValue = history.getStringValue();
            String txId=history.getTxId();
            String timestamp=history.getTimestamp().toString();
       

            PoHistoryDetails commissionHistoryDetail=new PoHistoryDetails(poValue,txId,timestamp); 

            historyPo.add(commissionHistoryDetail);

            System.out.println("QUA CE LA HISTORY");
            System.out.println(historyPo);
        }
    return historyPo.toString();
}

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean poExists(Context ctx, String poKey){
        ChaincodeStub stub = ctx.getStub();
        String poJSON = stub.getStringState(poKey);
        return (poJSON != null && !poJSON.isEmpty());
    }

    public String getPoKey(final Context ctx, String id){
        ChaincodeStub stub = ctx.getStub();
        String assetType="Po";
        String version="v1";
        CompositeKey ck = stub.createCompositeKey(version,assetType, id);

        if(ck == null){
            System.out.println("getPoKey() stub function returned null, generating using constructor");
            ck = new CompositeKey(version,assetType,id);
        }
        return ck.toString();
    }

}

