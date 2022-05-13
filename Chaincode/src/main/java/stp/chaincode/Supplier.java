/*
 * SPDX-License-Identifier: Apache-2.0
 */

package stp.chaincode;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

// Supplier --> Classe Fonitore

@DataType()
public class Supplier {


    /*
    =================================================================
    @Property() 
    private final  String supplierID;

    questo attributo non serve, viene passato da hyperledger
    =================================================================
     */

    @Property()
    private  String name;

    @Property()
    private  String continent;

    @Property()
    private  String address;

    @Property()
    private  String email;

    @Property()
    private  String vat; 

    @Property()
    private String state;

    public Supplier(@JsonProperty("name") final String name,
                    @JsonProperty("continent") final String continent,
                    @JsonProperty("address") final String address,
                    @JsonProperty("email") final String email,
                    @JsonProperty("vat")final String vat) {
        this.name = name;
        this.continent = continent;
        this.address = address;
        this.email = email;
        this.vat = vat;
        this.state = "waiting";
        /*
         Inside Constructor Supplier's Initial STATE must be 'waiting', it will have to be accepted later,
         in the contract there'll be the possibility to change the STATE
        */

    }


    public String getName() {
        return name;
    }

    public String getContinent() {
        return continent;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getVat() {
        return vat;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supplier)) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(getName(), supplier.getName()) && Objects.equals(getContinent(), supplier.getContinent()) && Objects.equals(getAddress(), supplier.getAddress()) && Objects.equals(getEmail(), supplier.getEmail()) && Objects.equals(getVat(), supplier.getVat()) && Objects.equals(getState(), supplier.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, continent,address,email,vat,state);
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "name='" + name + '\'' +
                ", continent='" + continent + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", vat='" + vat + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}

