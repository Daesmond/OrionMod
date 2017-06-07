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
public class StaticLevitate extends StaticAbstract {
    private static StaticLevitate ConfigLevitate;

    public StaticLevitate() {
        super();
    }

    public static StaticLevitate getConfig() {
        if (ConfigLevitate == null) {
            ConfigLevitate = new StaticLevitate();
        }

        return ConfigLevitate;
    }
}
