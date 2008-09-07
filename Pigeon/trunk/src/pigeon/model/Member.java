/*
    Copyright (C) 2005, 2006, 2007, 2008  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package pigeon.model;

import java.io.Serializable;

/**
    Represents a single member within the organization.

    Equality and hashing are based only on the member's name.
*/
public final class Member implements Serializable, Comparable<Member> {

    private static final long serialVersionUID = 3235931567494968807L;

    /// Only populated in FEDERATION mode, null/empty otherwise
    private final String club;
    /// Only populated in FEDERATION mode, null/empty otherwise
    private final String section;

    private final String name;
    private final String address;
    private final String telephone;
    private final String SHUNumber;

    private Member(String club, String section, String name, String address, String telephone, String SHUNumber) {
        this.club = club;
        this.section = section;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.SHUNumber = SHUNumber;
    }
    
    public static Member createEmpty()
    {
        return new Member("", "", "", "", "", "");
    }

    public String getName() {
        return name;
    }

    public Member repSetName(String name) throws ValidationException {
        name = name.trim();
        if (name.length() == 0) {
            throw new ValidationException("Member name is empty");
        }
        return new Member(club, section, name, address, telephone, SHUNumber);
    }
    
    private String getNameAndClubAndSection() {
        StringBuffer result = new StringBuffer(this.getName());
        if (this.getClub() != null) {
            result.append(", " + this.getClub());
        }
        if (this.getSection() != null) {
            result.append(", " + this.getSection());
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return getNameAndClubAndSection();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
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

    @Override
    public int compareTo(Member other) {
        if (this == other) {
            return 0;
        } else {
            return name.compareTo(other.name);
        }
    }

    public String getAddress() {
        if (address != null) {
            return address;
        } else {
            return "";
        }
    }

    public Member repSetAddress(String address) {
        return new Member(club, section, name, address.trim(), telephone, SHUNumber);
    }

    public String getTelephone() {
        return telephone;
    }

    public Member repSetTelephone(String telephone) {
        return new Member(club, section, name, address, telephone.trim(), SHUNumber);
    }

    public String getSHUNumber() {
        return SHUNumber;
    }

    public Member repSetSHUNumber(String SHUNumber) {
        return new Member(club, section, name, address, telephone, SHUNumber.trim());
    }

    public String getClub()
    {
        return club;
    }

    public Member repSetClub(String club)
    {
        return new Member(club.trim(), section, name, address, telephone, SHUNumber);
    }

    public String getSection()
    {
        return section;
    }

    public Member repSetSection(String section)
    {
        return new Member(club, section.trim(), name, address, telephone, SHUNumber);
    }
}
