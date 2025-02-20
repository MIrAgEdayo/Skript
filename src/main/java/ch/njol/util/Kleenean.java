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
package ch.njol.util;

import java.util.Locale;

/**
 * A three-valued logic type (true, unknown, false), named after Stephen Cole Kleene.
 *
 * @author Peter Güttinger
 */
public enum Kleenean {
	/**
	 * 100% false
	 */
	FALSE,
	/**
	 * Unknown state
	 */
	UNKNOWN,
	/**
	 * 100% true
	 */
	TRUE;

	@Override
	public final String toString() {
		return "" + name().toLowerCase(Locale.ENGLISH);
	}

	public final Kleenean is(final Kleenean other) {
		if (other == UNKNOWN || this == UNKNOWN)
			return UNKNOWN;
		if (other == this)
			return TRUE;
		return FALSE;
	}

	public final Kleenean and(final Kleenean other) {
		if (this == FALSE || other == FALSE)
			return FALSE;
		if (this == TRUE && other == TRUE)
			return TRUE;
		return UNKNOWN;
	}

	public final Kleenean or(final Kleenean other) {
		if (this == TRUE || other == TRUE)
			return TRUE;
		if (this == FALSE && other == FALSE)
			return FALSE;
		return UNKNOWN;
	}

	public final Kleenean not() {
		if (this == TRUE)
			return FALSE;
		if (this == FALSE)
			return TRUE;
		return UNKNOWN;
	}

	public final Kleenean implies(final Kleenean other) {
		if (this == FALSE || other == TRUE)
			return TRUE;
		if (this == TRUE && other == FALSE)
			return FALSE;
		return UNKNOWN;
	}

	/**
	 * @return <tt>this == TRUE</tt>
	 */
	public final boolean isTrue() {
		return this == TRUE;
	}

	/**
	 * @return <tt>this == UNKNOWN</tt>
	 */
	public final boolean isUnknown() {
		return this == UNKNOWN;
	}

	/**
	 * @return <tt>this == FALSE</tt>
	 */
	public final boolean isFalse() {
		return this == FALSE;
	}

	/**
	 * @param b
	 * @return <tt>b ? TRUE : FALSE</tt>
	 */
	public static Kleenean get(final boolean b) {
		return b ? TRUE : FALSE;
	}

	/**
	 * @param i
	 * @return <tt>i > 0 ? TRUE : i < 0 ? FALSE : UNKNOWN</tt>
	 */
	public static Kleenean get(final int i) {
		return i > 0 ? TRUE : i < 0 ? FALSE : UNKNOWN;
	}

	/**
	 * @param d
	 * @return <tt>return d > 0 ? TRUE : d < 0 ? FALSE : UNKNOWN</tt>
	 */
	public static Kleenean get(final double d) {
		return d > 0 ? TRUE : d < 0 ? FALSE : UNKNOWN;
	}

}
