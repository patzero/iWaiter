/* ******************************************************
 * Project iWaiter - Insta
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * ******************************************************/
package com.insta.iwaiter.tools;

public class SimParameters {
  //---HARD-CODED-PARAMETERS---//
  public static final int defaultWidth = 1150, defaultHeight = 850;
  public static final int robotPosX = 50, robotPosY = 150;
  public static final int enginePaceMillis = 100;
  public static final String brainDirectory = "src/com/insta/iwaiter/AI";
  public static final String keyBoardMsg = " Keyboard: \n" +
										   "  spacebar : play / pause \n" +
										   "  o : show obstacles layer \n";
  public static final String aboutMsg = "Waiter Robot Simlator \n" +
		  								"Version: 1.0.3 \n\n" +
		  								"SIMUL CORPORATION. \n" +
		  								"(c) Copyright SIMUL CORPORATION 2000, 2015.  All rights reserved. ";
  
  
  public static <T> T instantiate(final String className, final Class<T> type){
	    try{
	      return type.cast(Class.forName(className).newInstance());
	    } catch(final InstantiationException e){
	      throw new IllegalStateException(e);
	    } catch(final IllegalAccessException e){
	      throw new IllegalStateException(e);
	    } catch(final ClassNotFoundException e){
	      throw new IllegalStateException(e);
	    }
	  }


}
