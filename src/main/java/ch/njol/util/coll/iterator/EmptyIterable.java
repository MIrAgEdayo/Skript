/**
 * This file is part of Skript.
 * <p>
 * Skript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Skript is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.util.coll.iterator;

import org.eclipse.jdt.annotation.Nullable;

import java.util.Iterator;

/**
 * @author Peter Güttinger
 */
public final class EmptyIterable<T> implements Iterable<T> {

	public final static EmptyIterable<Object> instance = new EmptyIterable<>();

	@SuppressWarnings("unchecked")
	public static <T> EmptyIterable<T> get() {
		return (EmptyIterable<T>) instance;
	}

	@Override
	public Iterator<T> iterator() {
		return EmptyIterator.get();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		return obj instanceof EmptyIterable;
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
