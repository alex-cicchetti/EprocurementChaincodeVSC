/*
 * SPDX-License-Identifier: Apache-2.0
 */
package stp.chaincode;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;

import java.util.ArrayList;
import java.util.List;



@Contract(name = "CommissionContract",
    info = @Info(title = "STPCommission contract",
                description = "Commission SmartContract",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "Commission@example.com",
                                                name = "Commission",
                                                url = "http://Commission.me")))
@Default
public class CommissionContract implements ContractInterface {

    public  CommissionContract() {}

    private final Genson genson = new Genson();

    private enum CommissionError{
        COMMISSION_NOT_FOUND,
        COMMISSION_ALREADY_EXIST,
        COMMISSION_ALREADY_EVALUATED,
        COMMISSION_STATE_NOT_OPEN,
        COMMISSION_INVALID_STATE
    
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
        public Commission addCommission(final Context ctx, final String commissionID
                                    , final String customer, final String type
                                    , final String openingDate, final String equipmentList){
        ChaincodeStub stub = ctx.getStub();
        String commissionKey = getCommissionKey(ctx, commissionID);

        if(commissionExists(ctx, commissionKey)){
        String errorMessage= String.format("Commission %s already exist.", commissionID);
        System.out.println(errorMessage);
        throw new ChaincodeException (errorMessage, CommissionError.COMMISSION_ALREADY_EXIST.toString());
        }
        Commission commission = new Commission(customer, type, openingDate,equipmentList);

        String commissionJSON = genson.serialize(commission);
         stub.putStringState(commissionKey, commissionJSON);

        return commission;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
        public Commission updateCommissionState(final Context ctx, final String commissionID, String newState){
        ChaincodeStub stub = ctx.getStub();
        String commissionKey=getCommissionKey(ctx,commissionID);

        if(!commissionExists(ctx, commissionKey)){
            String errorMessage = String.format("Commission %s does not exist.", commissionID);
            System.out.println(errorMessage);
            throw new ChaincodeException( errorMessage, CommissionError.COMMISSION_NOT_FOUND.toString());
        }

        //Controllo se il nuovo stato inserito e valido
        String commissionJSON = stub.getStringState(commissionKey);
        Commission commission = genson.deserialize(commissionJSON, Commission.class);

       
        //controllo che lo stato della commission sia stato gia verificato
        //Questo getState e un getter della classe non di Fabric
        if(commission.getState().equals("closed won")|| commission.getState().equals("closed lost")){
            String errorMessage = String.format("Commission %s has been already evaluated", commissionID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, CommissionError.COMMISSION_ALREADY_EVALUATED.toString());
        }
            if(!newState.toLowerCase().equals("closed won") && !newState.toLowerCase().equals("closed lost")){
            String errorMessage = String.format("New state must be 'closed won' or 'closed lost'.");
            throw new ChaincodeException(errorMessage, CommissionError.COMMISSION_INVALID_STATE.toString());
        }
        //questo setState non e da confondere con i putStringState o getStringState( e un setter)
        commission.setState(newState.toLowerCase());
        String updateCommissionJSON = genson.serialize(commission);
        stub.putStringState(commissionKey,updateCommissionJSON);
        return commission;
    }

    //questa read e sulla intera key
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Commission readCommission(final Context ctx, final String commissionID){
        ChaincodeStub stub = ctx.getStub();
        String commissionKey=getCommissionKey(ctx,commissionID);
        String commissionJSON = stub.getStringState(commissionKey);

        if(!commissionExists(ctx, commissionKey)){
            String errorMessage = String.format("Commission %s does not exist.", commissionID);
            System.out.println(errorMessage);
            throw new ChaincodeException( errorMessage, CommissionError.COMMISSION_NOT_FOUND.toString());
        }
        return genson.deserialize(commissionJSON, Commission.class);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Commission updateCommissionList(final Context ctx,final String commissionID, final String newEquipmentList){
        ChaincodeStub stub = ctx.getStub();
        String commissionKey = getCommissionKey(ctx, commissionID);
      
        if(!commissionExists(ctx, commissionKey)){
            String errorMessage = String.format("Commission %s does not exist.", commissionID);
            System.out.println(errorMessage);
            throw new ChaincodeException( errorMessage, CommissionError.COMMISSION_NOT_FOUND.toString());
        }
 
        String commissionJSON = stub.getStringState(commissionKey);
        Commission commission = genson.deserialize(commissionJSON, Commission.class);
      
        //controllo che lo stato della commission sia aperto
        if (!commission.getState().equals("open")){
            String errorMessage = String.format("Commission %s is not open to evaluation", commissionID);
            throw new ChaincodeException(errorMessage,CommissionError.COMMISSION_STATE_NOT_OPEN.toString());
        }
        commission.setEquipmentList(newEquipmentList);
        String updateCommissionJSON = genson.serialize(commission);
        stub.putStringState(commissionKey,updateCommissionJSON);
        return commission;


    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getCommissionHistory(final Context ctx, final String commissionID) {

        if(commissionID == null){
            throw new RuntimeException("No ID given");
        }

        ChaincodeStub stub = ctx.getStub();
        List<CommissionHistoryDetails> historyCommission = new ArrayList<>();
        String commissionKey=getCommissionKey(ctx,commissionID);

        QueryResultsIterator<KeyModification> resultsIterator = stub.getHistoryForKey(commissionKey);

        for (KeyModification history: resultsIterator) {

            String commissionValue = history.getStringValue();
            String txId=history.getTxId();
            String timestamp=history.getTimestamp().toString();
           

            CommissionHistoryDetails commissionHistoryDetail=new CommissionHistoryDetails(commissionValue,txId,timestamp); 

            historyCommission.add(commissionHistoryDetail);

            System.out.println("QUA CE LA HISTORY");
            System.out.println(historyCommission);
        }
        return historyCommission.toString();
    }



    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean commissionExists(Context ctx, String commissionKey) {
        ChaincodeStub stub = ctx.getStub();
        String commissionJSON= stub.getStringState(commissionKey);
        return (commissionJSON != null && !commissionJSON.isEmpty());
    }

    public String getCommissionKey(final Context ctx, String id){
        ChaincodeStub stub = ctx.getStub();
        String assetType="Commission";
        String version="v1";
        CompositeKey ck = stub.createCompositeKey(version,assetType, id);

        if(ck == null){
            System.out.println("getCommissionKey() stub function returned null, generating using constructor");
            ck = new CompositeKey(version,assetType,id);
        }
        return ck.toString();
    }

}