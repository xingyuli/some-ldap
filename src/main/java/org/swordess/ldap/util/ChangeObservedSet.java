package org.swordess.ldap.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


public class ChangeObservedSet<E> extends ForwardingSet<E> {

	public ChangeObservedSet(Set<E> s) {
		super(new RealTimeComputeChangeImpl<E>(s));
	}
	
	public Set<E> getAddedElements() {
		return ((SetImpl<E>)s).getAddedElements();
	}
	
	public Set<E> getRemovedElements() {
		return ((SetImpl<E>)s).getRemovedElements();
	}
	
	/**
     * This method will be called once an entity has been updated
     * successfully. The changes must be cleared because we want to reuse
     * the entity this list belongs to. The reason is, if not cleared, the
     * changes will be gathered the next time when updating.
     */
	public void clearChanges() {
		((SetImpl<E>)s).clearChanges();
	}
	
	private abstract static class SetImpl<E> extends ForwardingSet<E> {

		protected SetImpl(Set<E> s) {
			super(s);
		}
		
		protected abstract Set<E> getAddedElements();
		
		protected abstract Set<E> getRemovedElements();
		
		protected abstract void clearChanges();
		
	}
	
	private static class RealTimeComputeChangeImpl<E> extends SetImpl<E> {

		private final Set<E> added = new LinkedHashSet<E>();
		private final Set<E> removed = new LinkedHashSet<E>();
		
		protected RealTimeComputeChangeImpl(Set<E> s) {
			super(s);
		}

		@Override
		public boolean add(E e) {
			boolean modified = super.add(e);
			if (modified && !removed.remove(e)) {
				added.add(e);
			}
			return modified;
		}
		
		@Override
		public boolean addAll(Collection<? extends E> c) {
			boolean modified = false;
			for (E e : c) {
				if (this.add(e)) {
					modified = true;
				}
			}
			return modified;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean remove(Object o) {
			boolean modified = super.remove(o);
			if (modified && !added.remove(o)) {
				removed.add((E) o);
			}
			return modified;
		}
		
		@Override
		public boolean removeAll(Collection<?> c) {
			boolean modified = false;
			for (Object e : c) {
				if (this.remove(e)) {
					modified = true;
				}
			}
			return modified;
		}
		
		@Override
		protected Set<E> getAddedElements() {
			return new HashSet<E>(added);
		}

		@Override
		protected Set<E> getRemovedElements() {
			return new HashSet<E>(removed);
		}

		@Override
		protected void clearChanges() {
			added.clear();
			removed.clear();
		}
		
	}
	
	@SuppressWarnings("unused")
	private static class PostComputeChangeImpl<E> extends SetImpl<E> {
		
		private final Set<E> original = new HashSet<E>();
		
		protected PostComputeChangeImpl(Set<E> s) {
			super(s);
			if (null != s) {
				original.addAll(s);
			}
		}

		@Override
		protected Set<E> getAddedElements() {
			Set<E> added = new LinkedHashSet<E>(this);
			added.removeAll(original);
			return added;
		}

		@Override
		protected Set<E> getRemovedElements() {
			Set<E> removed = new LinkedHashSet<E>(original);
			removed.removeAll(this);
			return removed;
		}

		@Override
		protected void clearChanges() {
			original.clear();
			original.addAll(this);
		}
		
	}
	
}
