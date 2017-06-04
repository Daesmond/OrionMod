/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion.statics;

/**
 *
 * @author Daesmond
 */
public abstract class StaticAbstract {

    protected int ticks;
    protected boolean isupdateneeded;
    protected boolean isupdating;

    public StaticAbstract() {
        ticks = 0;
        isupdateneeded = false;
        isupdating = false;
    }
    
     public void incTicks() {
        ticks += 1;
    }

    public int getTicks() {
        return ticks;
    }

    public void resetTicks() {
        ticks = 0;
    }

    public boolean IsUpdateNeeded() {
        return isupdateneeded;
    }

    public void setForUpdate() {
        isupdateneeded = true;
    }

    public void setNotForUpdate() {
        isupdateneeded = false;
    }

}
