/*
 * SPDX-License-Identifier: Apache-2.0
 */

package stp.chaincode;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.Genson;

@DataType()
public class Po {

    private final static Genson genson = new Genson();

    @Property()
    private String value;

    public Po(){
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toJSONString() {
        return genson.serialize(this).toString();
    }

    public static Po fromJSONString(String json) {
        Po asset = genson.deserialize(json, Po.class);
        return asset;
    }
}
