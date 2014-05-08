/**
 * CompareStringList is a class that implements Comparator to compare two String and return 
 * if the length is equals, more o less length.
 * 
 * @author Sara Craba
 * @version 1.0
 */

package word;

import java.util.Comparator;

public class CompareStringList implements Comparator<String> 
{
 	
    public int compare(String o1, String o2) 
    {
        if (o1.length() > o2.length()) 
        {
            return -1;
        } 
        else if (o1.length() < o2.length()) 
        {
            return 1;
        } 
        else 
        {
            return 0;
        }
    }
}