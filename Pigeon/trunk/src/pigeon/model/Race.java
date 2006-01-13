/*
 * Race.java
 *
 * Created on October 23, 2005, 5:33 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package pigeon.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author pauldoo
 */
public class Race implements Serializable, Comparable<Race> {
    
    private static final long serialVersionUID = 42L;
    
    private Racepoint racepoint;
    private Date date;
    private Map<Member, Map<String, Long>> results = new HashMap<Member, Map<String, Long>>();
    
    public Race(Club club) {
        for (Member m: club.getMembers()) {
            results.put(m, new HashMap<String, Long>());
        }
    }

    public Racepoint getRacepoint() {
        return racepoint;
    }

    public void setRacepoint(Racepoint racepoint) {
        this.racepoint = racepoint;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void addResult(Member member, String ringNumber, long time) {
        results.get(member).put(ringNumber, time);
    }
    
    public Map<String, Long> getResults(Member member) {
        return results.get(member);
    }
    
    public long getResult(Member member, String ringNumber) {
        return getResults(member).get(ringNumber);
    }
    
    public int compareTo(Race other) {
        if (this == other) {
            return 0;
        } else {
            return date.compareTo(other.date);
        }
    }
}
