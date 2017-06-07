/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orion;

import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 *
 * @author Daesmond
 */
public class OrionLogger {
    public static void log(Level logLevel, Object object)
  {
    FMLLog.log("OrionMod", logLevel, String.valueOf(object), new Object[0]);
  }
  
  public static void all(Object object)
  {
    log(Level.ALL, object);
  }
  
  public static void debug(Object object)
  {
    log(Level.DEBUG, object);
  }
  
  public static void error(Object object)
  {
    log(Level.ERROR, object);
  }
  
  public static void fatal(Object object)
  {
    log(Level.FATAL, object);
  }
  
  public static void info(Object object)
  {
    log(Level.INFO, object);
  }
  
  public static void off(Object object)
  {
    log(Level.OFF, object);
  }
  
  public static void trace(Object object)
  {
    log(Level.TRACE, object);
  }
  
  public static void warn(Object object)
  {
    log(Level.WARN, object);
  }
}
