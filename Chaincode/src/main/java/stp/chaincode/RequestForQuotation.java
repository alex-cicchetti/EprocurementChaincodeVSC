/*
 * SPDX-License-Identifier: Apache-2.0
 */

package stp.chaincode;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public class RequestForQuotation {

    
    @Property()
    private String customer;
    @Property()
    private String material;
    @Property()
    private String vendorList;
    @Property()
    private String bidderList;
    @Property()
    private String state;

    

    public RequestForQuotation(@JsonProperty("customer") final String customer,
                               @JsonProperty("material") final String material,
                               @JsonProperty("vendorList") final String vendorList){
        this.customer= customer;
        this.material= material;
        this.vendorList= vendorList;
        this.state= "DRAFT";
        this.bidderList= "BidderListEMPTY";
    }

    public RequestForQuotation(){
    }
    
    public String getVendorList() {
        return this.vendorList;
    }

    public void setVendorList(String vendorList) {
        this.vendorList = vendorList;
    }

    public String getMaterial() {
        return this.material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCustomer() {
        return this.customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getBidderList() {
        return bidderList;
    }

    public void setBidderList(String bidderList) {
        this.bidderList = bidderList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestForQuotation)) return false;
        RequestForQuotation that = (RequestForQuotation) o;
        return Objects.equals(getCustomer(), that.getCustomer()) 
            && Objects.equals(getMaterial(), that.getMaterial()) 
            && Objects.equals(getVendorList(), that.getVendorList()) 
            && Objects.equals(getState(), that.getState()) 
            && Objects.equals(getBidderList(), that.getBidderList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomer(), getMaterial(), getVendorList(), getState(), getBidderList());
    }

}
