package milestone1;

import java.util.*;
public class test9
{
   public static void main(String args[])
   {
      HashMap<String,String> names = new HashMap<String,String>();
	
      names.put("S101", "John");
      names.put("S102","Glen");
      names.put("S101","John Smith");
      names.put("S102","Glen Benjamin");
	  
      System.out.println(names.size());
   }
}
