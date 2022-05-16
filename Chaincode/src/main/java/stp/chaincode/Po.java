/*
 * SPDX-License-Identifier: Apache-2.0
 */

package stp.chaincode;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.util.Objects;



@DataType()
public class Po {

    @Property()
    private String commissionID;
    @Property()
    private String customer;
    @Property()
    private String material;
    @Property()
    private String awardedSupplier;
    @Property()
    private String state;


    public Po(@JsonProperty("commissionID") final String commissionID,
              @JsonProperty("customer") final String customer,
              @JsonProperty("material") final String material,
              @JsonProperty("awardedSupplier") final String awardedSupplier){
    this.commissionID = commissionID;
    this.customer = customer;
    this.material = material;
    this.awardedSupplier=awardedSupplier;
    this.state= "Created";
              }

    public Po(){}

    
    public String getCommissionID() {
        return this.commissionID;
    }

    public void setCommissionID(String commissionID) {
        this.commissionID = commissionID;
    }
    public String getCustomer() {
        return this.customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    
    public String getMaterial() {
        return this.material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
    
    public String getAwardedSupplier() {
        return this.awardedSupplier;
    }

    public void setAwardedSupplier(String awardedSupplier) {
        this.awardedSupplier = awardedSupplier;
    }

    
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Po)) return false;
        Po that = (Po) o;
        return Objects.equals(getCommissionID(),that.getCommissionID())
            && Objects.equals(getCustomer(), that.getCustomer())
            && Objects.equals(getMaterial(), that.getMaterial())
            && Objects.equals(getAwardedSupplier(), that.getAwardedSupplier())
            && Objects.equals(getState(), that.getState());
    }

    @Override
    public int hashCode(){
        return Objects.hash(getCommissionID(),getCustomer(),getMaterial(),getAwardedSupplier(),getState());
    }

    @Override
    public String toString(){
        return "Po {" +
                "CommissionID = '" + commissionID + '\'' +
                ", Customer = '" + customer + '\'' +
                ", Material = '" + material + '\'' +
                ", AwardedSupplier = '" + awardedSupplier + '\'' +
                ", State = '" + state + '\'' + 
                '}';
    }

}
