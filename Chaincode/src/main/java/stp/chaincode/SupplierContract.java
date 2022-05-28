/*
 * SPDX-License-Identifier: Apache-2.0
 */
package stp.chaincode;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import java.util.ArrayList;
import java.util.List;

// === Fabric info for Contract====
@Contract(name = "SupplierContract",
    info = @Info(title = "Supplier contract",
                description = "My Smart Contract",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "Chaincode@example.com",
                                                name = "Chaincode",
                                                url = "http://Chaincode.me")))
@Default
public class SupplierContract implements ContractInterface {

    public SupplierContract(){}
    
    private final Genson genson = new Genson();

    private enum SupplierError{
        SUPPLIER_NOT_FOUND,
        SUPPLIER_AlREADY_EXIST,
        SUPPLIER_ALREADY_EVALUATED
    }

    //Adding a Supplier to the ledger
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Supplier addSupplier(final Context ctx, final String supplierID, final String name, final String continent,
                                final String address, final String email, final String vat){
        ChaincodeStub stub = ctx.getStub();

        //Get the suppKey for check if exist
        String supplierKey=getSupplierKey(ctx,supplierID);

        if(supplierExists(ctx, supplierKey)){
            String errorMessage = String.format("Supplier %s already exist.", supplierID);
            System.out.println(errorMessage);
            throw new ChaincodeException( errorMessage, SupplierError.SUPPLIER_AlREADY_EXIST.toString());
        }
        //if condition false ---> cerate new supplier
        Supplier supplier = new Supplier(name, continent, address, email, vat);

        //serialize the Supplier Object and put in the ledger with a Key
        String supplierJSON = genson.serialize(supplier);
        stub.putStringState(supplierKey,supplierJSON);
        
        return supplier;
    }


    // Update supplier state, it could be accepted or rejected
    @Transaction(intent = Transaction.TYPE.SUBMIT)
        public Supplier updateSupplierState(final Context ctx, final String supplierID, String newState){
        ChaincodeStub stub = ctx.getStub();
        String supplierKey=getSupplierKey(ctx,supplierID);

        if(!supplierExists(ctx, supplierKey)){
            String errorMessage = String.format("Supplier %s does not exist.", supplierID);
            System.out.println(errorMessage);
            throw new ChaincodeException( errorMessage, SupplierError.SUPPLIER_NOT_FOUND.toString());
        }

        String supplierJSON = stub.getStringState(supplierKey);
        Supplier supplier = genson.deserialize(supplierJSON, Supplier.class);

        //Check if suppState has been already evaluated
        if(supplier.getState().equals("rejected")|| supplier.getState().equals("accepted")){
            String errorMessage = String.format("Supplier %s has been already evaluated", supplierID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, SupplierError.SUPPLIER_ALREADY_EVALUATED.toString());
        }

        //Set newState for Supplier, serialize and add to ledger
        supplier.setState(newState);
        String updatedSupplierJSON= genson.serialize(supplier);
        stub.putStringState(supplierKey,updatedSupplierJSON);
        return supplier;
    }


    //funzione di update piu generica su piu campi, capire come arriveranno i dati dal client Odoo (se tutto l'ogetto ,se solo i parametri modificati)
    // @Transaction(intent = Transaction.TYPE.SUBMIT)
    // public Supplier updateSupplier(final String supplierID, final String name, final String continent,
    // final String address, final String email, final String vat){    
        
    //     //Map<Integer,String> mappa;
    //     String supplierKey=getSupplierKey(ctx,supplierID);

    //     if(!supplierExists(ctx, supplierKey)){
    //         String errorMessage = String.format("Supplier %s does not exist.", supplierID);
    //         System.out.println(errorMessage);
    //         throw new ChaincodeException( errorMessage, SupplierError.SUPPLIER_NOT_FOUND.toString());
    //     }

    //     String supplierJSON = stub.getStringState(supplierKey);
    //     Supplier supplier = genson.deserialize(supplierJSON, Supplier.class);

    //     for(String arg:args){
    //         switch (arg){
    //             case :
    //             supplier.setName(arg);
    //             break;
    //             case 'continent':
    //             supplier.setContinent(arg);
    //             break;
    //         }
    //     }

    //     ChaincodeStub stub = ctx.getStub();

    //     String supplierKey=getSupplierKey(ctx,supplierID);

    //     Supplier supplier = new Supplier(name, continent, address, email, vat);

    //     String supplierJSON = genson.serialize(supplier);
    //     stub.putStringState(supplierKey,supplierJSON);
        
    //     return supplier;

    // }

    //This read shows the moment while you're asking
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Supplier readSupplier(final Context ctx, final String supplierID){
        ChaincodeStub stub = ctx.getStub();
        String supplierKey=getSupplierKey(ctx,supplierID);
        String supplierJSON = stub.getStringState(supplierKey);

        if(supplierJSON==null||supplierJSON.isEmpty()){
            String errorMessage = String.format("Supplier %s does not exist.", supplierID);
            System.out.println(errorMessage);
            throw new ChaincodeException( errorMessage, SupplierError.SUPPLIER_NOT_FOUND.toString());
        }
        return genson.deserialize(supplierJSON, Supplier.class);
    }


    //This read all the asset on the ledger
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getAllAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Supplier> queryResults = new ArrayList<Supplier>();
        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
    

        for (KeyValue result: results) {
            
            Supplier asset = genson.deserialize(result.getStringValue(), Supplier.class);
            System.out.println(asset);
            System.out.println("===================QUESTA E' LA KEY=============================");
            System.out.println(result.getKey());
            System.out.println("===================SOPRA TROVI LA KEY=======================");
            queryResults.add(asset);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }

    // History of the supplier on the ledger
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getSupplierHistory(final Context ctx, final String supplierID) {

        if(supplierID == null){
            throw new RuntimeException("No ID given");
        }

        ChaincodeStub stub = ctx.getStub();
        List<SupplierHistoryDetails> historySupplier = new ArrayList<>();
        String supplierKey=getSupplierKey(ctx,supplierID);

        QueryResultsIterator<KeyModification> resultsIterator = stub.getHistoryForKey(supplierKey);

        for (KeyModification history: resultsIterator) {

            String supplierValue = history.getStringValue();
            String txId=history.getTxId();
            String timestamp=history.getTimestamp().toString();
           

            SupplierHistoryDetails supplierHistoryDetail=new SupplierHistoryDetails(supplierValue,txId,timestamp); 

            historySupplier.add(supplierHistoryDetail);

            System.out.println("QUA CE LA HISTORY");
            System.out.println(historySupplier);
        }
        return historySupplier.toString();
    }


    //SupplierExist on the ledger? 
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean supplierExists( final Context ctx, final String supplierKey){
        ChaincodeStub stub = ctx.getStub();// serve sempre lo STUB (Comunica con il livello piu basso della rete)
        String supplierJSON = stub.getStringState(supplierKey);
        //==================================================
        // return true se la string supplierJSON e piena
        // ( quindi l'oggetto Supplier exist)
        //==================================================
        return (supplierJSON != null && !supplierJSON.isEmpty());
    }


    //SupplierKey unique param based on supplierID
    public String getSupplierKey(final Context ctx, String id){
        ChaincodeStub stub = ctx.getStub();
        String assetType="Supplier";
        String version="v1";
        CompositeKey ck = stub.createCompositeKey(version,assetType, id);

        if(ck== null){
            System.out.println("getSupplierKey() stub function returned null, generating using constructor");
            ck = new CompositeKey(version,assetType,id);
        }
        return ck.toString();
    }

}
