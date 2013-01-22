package org.swordess.ldap.util;

/**
 * A strategy concept evaluate the real value of an object/element when
 * constructing an {@link javax.naming.directory.Attribute} or a
 * {@link javax.naming.directory.ModificationItem}.
 * <p/>
 * 
 * In most scenarios, an {@link Evaluator} will be used when constructing an
 * Attribute or a ModificationItem which carries multiple values. For example:
 * 
 * <pre>
 * List&lt;User&gt; users = ...
 * List&lt;User&gt; admins = ...
 * 
 * Evaluator&lt;String&gt; userEvaluator = new Evaluator&lt;String&gt;() {
 *     public String eval(Object obj) {
 *         User user = (User) obj; // safe to cast to User directly, as parameter won't be null
 *         return user.getUsername();
 *     }
 * };
 * 
 * Attribute memberAttr = AttrUtils.create("member", users, userEvaluator);
 * Attribute adminAttr = AttrUtils.create("admin", admins, userEvaluator);
 * 
 * ...
 * ...
 * 
 * List&lt;User&gt; addedMembers = ...
 * ModificationItem mod = ModUtils.add("member", addedMembers, userEvaluator);
 * </pre>
 * 
 * Without an Evaluator, the code would be something messy like:
 * 
 * <pre>
 * List&lt;User&gt; users = ...
 * List&lt;User&gt; admins = ...
 * 
 * Attribute memberAttr = new BasicAttribute("member");
 * for (User user : users) {
 *     if (null != user) {
 *         if (null != user.getUsername()) {
 *             memberAttr.add(user.getUsername());
 *         }
 *     }
 * }
 * 
 * Attribute adminAttr = new BasicAttribute("admin");
 * for (User admin : admins) {
 *     if (null != admin) {
 *         if (null != admin.getUsername()) {
 *             adminAttr.add(admin.getUsername());
 *         }
 *     }
 * }
 * 
 * ...
 * ...
 * 
 * List&lt;User&gt; addedMembers = ...
 * Attribute addedMembersAttr = new BasicAttribute("member");
 * for (User addedMember : addedMembers) {
 *     if (null != addedMember) {
 *         if (null != addedMember.getUsername()) {
 *             addedMembersAttr.add(addedMember.getUsername());
 *         }
 *     }
 * }
 * ModificationItem mod = new ModificationItem(DirContext.ADD_ATTRIBUTE, addedMembersAttr);
 * </pre>
 * 
 * By comparison, we could know that,
 * <ul>
 * <li>Its safe to class cast directly, as null object already be filtered out
 * by AttrUtils and ModUtils.</li>
 * <li>Its reusable for extracting certain information from multiple values and
 * use those piece of information as the real value of Attribute and
 * ModificationItem, so the code is much more cleaner.</li>
 * </ul>
 * 
 * @author Liu Xingyu <xingyulliiuu@gmail.com>
 * 
 * @param <T>
 */
public interface Evaluator<T> {

    /**
     * Compute the real value of the given <tt>obj</tt>. Return null to indicate
     * the real value is not valid, thus it will be omitted when constructing an
     * Attribute or a ModificationItem.
     * 
     * @param obj
     *        a not null object
     * @return evaluated real value
     */
    public T eval(Object obj);
    
    /*
     * TODO When there is a need, we can provide a checker which defines what
     * effective value is rather than a simple null checker.
     */

}
