/*
 * SPDX-License-Identifier: Apache-2.0
 */

package stp.chaincode;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class Commission {

    @Property()
    private  String customer;

    @Property()
    private  String type;

    @Property()
    private  String openingDate;

    @Property()
    private  String state;

    @Property()
    private  String equipmentList;

    public Commission(@JsonProperty("customer") final String customer,
                      @JsonProperty("type") final String type,
                      @JsonProperty("openingDate") final String openingDate,
                      @JsonProperty("equipmentList") final String equipmentList) {
        this.customer = customer;
        this.type = type;
        this.openingDate = openingDate;
        this.state = "open";
        this.equipmentList = equipmentList;
    }

    public Commission() {

    }

    public String getCustomer() {
        return customer;
    }
    public String getType() {
        return type;
    }
    public String getOpeningDate() {
        return openingDate;
    }
    public String getState() {
        return state;
    }
    public String getEquipmentList() {
        return equipmentList;
    }
    public void setCustomer(String customer){
        this.customer=customer;
    }
    public void setType(String type){
        this.type= type;
    }
    public void setOpeningDate(String openingDate){
        this.openingDate=openingDate;
    }
    public void setState(String state){
        this.state= state;
    }
    public void setEquipmentList(String equipmentList){
        this.equipmentList=equipmentList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commission)) return false;
        Commission that = (Commission) o;
        return Objects.equals(getCustomer(), that.getCustomer()) && Objects.equals(getType(), that.getType()) && Objects.equals(getOpeningDate(), that.getOpeningDate()) && Objects.equals(getState(), that.getState()) && Objects.equals(getEquipmentList(), that.getEquipmentList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomer(), getType(), getOpeningDate(), getState(), getEquipmentList());
    }

    @Override
    public String toString() {
        return "Commission{" +
                "customer='" + customer + '\'' +
                ", type='" + type + '\'' +
                ", openingDate=" + openingDate +
                ", state='" + state + '\'' +
                ", equipmentList='" + equipmentList + '\'' +
                '}';
    }
}
