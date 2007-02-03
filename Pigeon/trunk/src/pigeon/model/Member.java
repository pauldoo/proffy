/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2007  Paul Richards
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSHUNumber() {
        return SHUNumber;
    }

    public void setSHUNumber(String SHUNumber) {
        this.SHUNumber = SHUNumber;
    }
}
