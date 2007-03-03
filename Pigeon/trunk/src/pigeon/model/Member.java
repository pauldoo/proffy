/*
    Copyright (c) 2005-2007, Paul Richards
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
    
        * Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
    
        * Neither the name of Paul Richards nor the names of contributors may be
        used to endorse or promote products derived from this software without
        specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
*/

package pigeon.model;

import java.io.Serializable;

/**
 * Represents a single member within the organization.
 *
 * Equality and hashing are based only on the member's name.
 */
public class Member implements Serializable, Comparable<Member> {

    private static final long serialVersionUID = 42L;

    // Only populated in FEDERATION mode, null otherwise
    private String club;
    // Only populated in FEDERATION mode, null otherwise
    private String section;
    
    private String name;
    private String address;
    private String telephone;
    private String SHUNumber;

    public Member() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ValidationException {
        name = name.trim();
        if (name.length() == 0) {
            throw new ValidationException("Member name is empty");
        }
        this.name = name;
    }

    public String toString() {
        return getName();
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object other) {
        return equals((Member)other);
    }

    public boolean equals(Member other) {
        if (this == other) {
            return true;
        } else {
            return name.equals(other.name);
        }
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
        this.address = address.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone.trim();
    }

    public String getSHUNumber() {
        return SHUNumber;
    }

    public void setSHUNumber(String SHUNumber) {
        this.SHUNumber = SHUNumber.trim();
    }

    public String getClub()
    {
        return club;
    }

    public void setClub(String club)
    {
        this.club = club.trim();
    }

    public String getSection()
    {
        return section;
    }

    public void setSection(String section)
    {
        this.section = section.trim();
    }
}
