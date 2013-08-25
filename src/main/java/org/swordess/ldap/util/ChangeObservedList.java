package org.swordess.ldap.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class ChangeObservedList<E> extends ForwardingList<E> {

	public ChangeObservedList(List<E> l) {
		super(new RealTimeComputeChangeImpl<E>(l));
	}
	
	public List<E> getAddedElements() {
		return ((ListImpl<E>)l).getAddedElements();
	}
	
	public List<E> getRemovedElements() {
		return ((ListImpl<E>)l).getRemovedElements();
	}
	
	/**
     * This method will be called once an entity has been updated
     * successfully. The changes must be cleared because we want to reuse
     * the entity this list belongs to. The reason is, if not cleared, the
     * changes will be gathered the next time when updating.
     */
	public void clearChanges() {
		((ListImpl<E>)l).clearChanges();
	}
	
	private abstract static class ListImpl<E> extends ForwardingList<E> {
		
		protected ListImpl(List<E> l) {
			super(l);
		}
		
		protected abstract List<E> getAddedElements();
		
		protected abstract List<E> getRemovedElements();
		
		protected abstract void clearChanges();
		
	}

	private static class RealTimeComputeChangeImpl<E> extends ListImpl<E> {

		private final List<E> added = new LinkedList<E>();
		private final List<E> removed = new LinkedList<E>();
		
		protected RealTimeComputeChangeImpl(List<E> l) {
			super(l);
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
		public void add(int index, E element) {
			super.add(index, element);
			if (!removed.remove(element)) {
				added.add(element);
			}
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
		
		@Override
		public boolean addAll(int index, Collection<? extends E> c) {
			int oldLen = size();
			int offset = 0;
			for (E e : c) {
				this.add(index + offset, e);
				offset++;
			}
			int newLen = size();
			return newLen != oldLen;
		}
		
		@Override
		public E remove(int index) {
			E removedObj = super.remove(index);
			if (null != removedObj && !added.remove(removedObj)) {
				removed.add(removedObj);
			}
			return removedObj;
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
		protected List<E> getAddedElements() {
			return new ArrayList<E>(added);
		}

		@Override
		protected List<E> getRemovedElements() {
			return new ArrayList<E>(removed);
		}
		
		@Override
		protected void clearChanges() {
			added.clear();
			removed.clear();
		}
		
	}
	
	@SuppressWarnings("unused")
	private static class PostComputeChangeImpl<E> extends ListImpl<E> {

		private final List<E> original = new ArrayList<E>();
		
		protected PostComputeChangeImpl(List<E> l) {
			super(l);
			if (null != l) {
				original.addAll(l);
			}
		}

		@Override
		protected List<E> getAddedElements() {
			List<E> added = new LinkedList<E>(this);
			List<E> copyOfOriginal = new LinkedList<E>(original);
			for (Iterator<E> iter = added.iterator(); iter.hasNext();) {
				E current = iter.next();
				if (copyOfOriginal.contains(current)) {
					iter.remove();
					copyOfOriginal.remove(current);
				}
			}
			return added;
		}

		@Override
		protected List<E> getRemovedElements() {
			List<E> removed = new LinkedList<E>(original);
			List<E> copyOfThis = new LinkedList<E>(this);
			for (Iterator<E> iter = removed.iterator(); iter.hasNext();) {
				E current = iter.next();
				if (copyOfThis.contains(current)) {
					iter.remove();
					copyOfThis.remove(current);
				}
			}
			return removed;
		}

		@Override
		protected void clearChanges() {
			original.clear();
			original.addAll(this);
		}
		
	}
	
}
