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


@Contract(name = "PoContract",
    info = @Info(title = "STPPO contract",
                description = "PurchaseOrder SmartContract",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "chaincode@example.com",
                                                name = "chaincode",
                                                url = "http://chaincode.me")))
@Default
public class PoContract implements ContractInterface{

    public  PoContract() {}

    private final Genson genson = new Genson();

    private enum PoError{
        PO_NOT_FOUND,
        PO_ALREADY_EXIST,
        PO_ALREADY_EVALUATED,
        //COMMISSIONID_INSERT_ERROR,
        //PO_INVALID_STATE, 
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

            purchaseOrderPo.setState(newState.toLowerCase());
            String updatedPoJSON = genson.serialize(purchaseOrderPo);
            stub.putStringState(poKey, updatedPoJSON);
        return purchaseOrderPo; 
    
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




    /*============================AutoGenerateCodeFromIBMExtension===============================
    
    @Transaction()
    public boolean poExists(Context ctx, String poId) {
        byte[] buffer = ctx.getStub().getState(poId);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public void createPo(Context ctx, String poId, String value) {
        boolean exists = poExists(ctx,poId);
        if (exists) {
            throw new RuntimeException("The asset "+poId+" already exists");
        }
        Po asset = new Po();
        asset.setValue(value);
        ctx.getStub().putState(poId, asset.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public Po readPo(Context ctx, String poId) {
        boolean exists = poExists(ctx,poId);
        if (!exists) {
            throw new RuntimeException("The asset "+poId+" does not exist");
        }

        Po newAsset = Po.fromJSONString(new String(ctx.getStub().getState(poId),UTF_8));
        return newAsset;
    }

    @Transaction()
    public void updatePo(Context ctx, String poId, String newValue) {
        boolean exists = poExists(ctx,poId);
        if (!exists) {
            throw new RuntimeException("The asset "+poId+" does not exist");
        }
        Po asset = new Po();
        asset.setValue(newValue);

        ctx.getStub().putState(poId, asset.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public void deletePo(Context ctx, String poId) {
        boolean exists = poExists(ctx,poId);
        if (!exists) {
            throw new RuntimeException("The asset "+poId+" does not exist");
        }
        ctx.getStub().delState(poId);
    }
    */


