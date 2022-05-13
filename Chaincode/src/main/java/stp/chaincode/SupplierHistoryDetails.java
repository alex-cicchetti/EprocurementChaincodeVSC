package stp.chaincode;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.lang.String;

//details for Supplier, when ask for history gives 
//more information
@DataType
public class SupplierHistoryDetails {

    @Property()
private String value;
@Property()
private String txId;
@Property()
private String timeStamp; 

public SupplierHistoryDetails(@JsonProperty("value") final String value,
                @JsonProperty("txId")final String txId,
                @JsonProperty("timeStamp")final String timeStamp){
    this.value= value;
    this.txId=txId;
    this.timeStamp=timeStamp;
}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTxId() {
		return this.txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public String getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
   
	@Override
    public String toString() {
        return "SupplierHistoryDetails{" +
                "value='" + value + '\'' +
                ", txId='" + txId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }




}
