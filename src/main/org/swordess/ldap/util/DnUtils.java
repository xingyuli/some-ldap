package org.swordess.ldap.util;

/**
 * This class provide several convenient methods for getting a sub dn or selecting a token for the given dn.
 * <p/>
 * 
 * The <tt>level</tt> can be treated in both directions:<br/>
 * <ul>
 * <li>Backward Direction: from the most left(current) to the most right(top) is regarded as level -1 to level -n</li>
 * <li>Forward Direction: from the most right to the most left is regarded level 1 to level n</li>
 * </ul>
 * So if you intend to get the current dn, the level shall be -1 for convenient, while if you want the top sub dn then the level shall be 1
 * for convenient.
 * <p/>
 * 
 * And, if you intend to get the most left token, the level shall be -1, while if you want the most right token then the level shall be 1.
 * <p/>
 * 
 * For example, assume that the <tt>dn</tt> is named "ou=hosts,ou=foo,o=bar":<br/>
 * <table>
 * <tr>
 * <th>level</th>
 * <th>{@link #sub(String, int) sub}</th>
 * <th>{@link #select(String, int) select}</th>
 * <th>{@link #selectName(String, int) select name}</th>
 * <th>{@link #selectValue(String, int) select value}</th>
 * </tr>
 * 
 * <tr>
 * <td>-3 or 1</td>
 * <td>o=bar</td>
 * <td>o=bar</td>
 * <td>o</td>
 * <td>bar</td>
 * </tr>
 * 
 * <tr>
 * <td>-2 or 2</td>
 * <td>ou=foo,o=bar</td>
 * <td>ou=foo</td>
 * <td>ou</td>
 * <td>foo</td>
 * </tr>
 * 
 * <tr>
 * <td>-1 or 3</td>
 * <td>ou=hosts,ou=foo,o=bar</td>
 * <td>ou=hosts</td>
 * <td>ou</td>
 * <td>hosts</td>
 * </tr>
 * </table>
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 */
public class DnUtils {

    public static String current(String dn) {
        return dn;
    }

    public static String parent(String dn) {
        return sub(dn, -2);
    }

    public static String top(String dn) {
        return sub(dn, 1);
    }

    public static String select(String dn, int level) {
        return select(dn.split(","), level);
    }

    public static String selectName(String dn, int level) {
        return select(dn, level).split("=")[0];
    }

    public static String selectValue(String dn, int level) {
        return select(dn, level).split("=")[1];
    }

    public static String select(String[] dnTokens, int level) {
        if (level > 0) {
            return dnTokens[dnTokens.length - level];
        } else {
            return dnTokens[-1 - level];
        }
    }

    public static String sub(String dn, int level) {
        return sub(dn.split(","), level);
    }

    public static String sub(String[] dnTokens, int level) {
        int from = (level > 0) ? (dnTokens.length - level) : (-1 - level);
        int to = dnTokens.length;

        StringBuilder builder = new StringBuilder();
        for (int i = from; i < to; i++) {
            builder.append(dnTokens[i]);
            if (i != to - 1) {
                builder.append(',');
            }
        }
        return builder.toString();
    }

    private DnUtils() {
    }

}