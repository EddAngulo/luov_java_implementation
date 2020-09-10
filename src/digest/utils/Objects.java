/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digest.utils;

public class Objects
{
    public static boolean areEqual(Object a, Object b)
    {
        return a == b || (null != a && null != b && a.equals(b));
    }

    public static int hashCode(Object obj)
    {
        return null == obj ? 0 : obj.hashCode();
    }
}
