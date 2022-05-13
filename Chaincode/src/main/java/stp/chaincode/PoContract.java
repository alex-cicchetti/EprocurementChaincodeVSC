/*
 * SPDX-License-Identifier: Apache-2.0
 */
package stp.chaincode;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "PoContract",
    info = @Info(title = "Po contract",
                description = "My Smart Contract",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "chaincode@example.com",
                                                name = "chaincode",
                                                url = "http://chaincode.me")))
@Default
public class PoContract implements ContractInterface {
    public  PoContract() {

    }
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

}
