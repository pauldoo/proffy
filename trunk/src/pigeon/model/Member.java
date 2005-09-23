/*
 * Member.java
 *
 * Created on 21 August 2005, 15:59
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pigeon.model;

import java.io.Serializable;

/**
 * Equality and hashing are based only on the member's name.
 * @author Paul
 */
public class Member implements Serializable, Comparable<Member> {
    
    private static final long serialVersionUID = 42L;
    
    private String name;
    private String address;
    private String telephone;
    private String ringNumberFrom;
    private String ringNumberTo;
    private String SHUNumber;
    
    /** Creates a new instance of Member */
    public Member() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return getName();
    }

    public int hashCode() {
        return name.hashCode();
    }
    
    public int compareTo(Member other) {
        if (this == other) {
            return 0;
        } else {
            return name.compareTo(other.name);
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRingNumberFrom() {
        return ringNumberFrom;
    }

    public void setRingNumberFrom(String ringNumberFrom) {
        this.ringNumberFrom = ringNumberFrom;
    }

    public String getRingNumberTo() {
        return ringNumberTo;
    }

    public void setRingNumberTo(String ringNumberTo) {
        this.ringNumberTo = ringNumberTo;
    }

    public String getSHUNumber() {
        return SHUNumber;
    }

    public void setSHUNumber(String SHUNumber) {
        this.SHUNumber = SHUNumber;
    }

    
}
