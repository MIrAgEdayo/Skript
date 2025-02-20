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
package ch.njol.skript.lang;

import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.util.Utils;
import org.eclipse.jdt.annotation.Nullable;

import java.lang.reflect.Array;

/**
 * A list of literals. Can contain {@link UnparsedLiteral}s.
 *
 * @author Peter Güttinger
 */
public class LiteralList<T> extends ExpressionList<T> implements Literal<T> {

	public LiteralList(final Literal<? extends T>[] literals, final Class<T> returnType, final boolean and) {
		super(literals, returnType, and);
	}

	public LiteralList(final Literal<? extends T>[] literals, final Class<T> returnType, final boolean and, final LiteralList<?> source) {
		super(literals, returnType, and, source);
	}

	@SuppressWarnings("null")
	@Override
	public T[] getArray() {
		return getArray(null);
	}

	@SuppressWarnings("null")
	@Override
	public T getSingle() {
		return getSingle(null);
	}

	@SuppressWarnings("null")
	@Override
	public T[] getAll() {
		return getAll(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <R> Literal<? extends R> getConvertedExpression(final Class<R>... to) {
		final Literal<? extends R>[] exprs = new Literal[expressions.length];
		final Class<?>[] classes = new Class[expressions.length];
		for (int i = 0; i < exprs.length; i++) {
			if ((exprs[i] = (Literal<? extends R>) expressions[i].getConvertedExpression(to)) == null)
				return null;
			classes[i] = exprs[i].getReturnType();
		}
		return new LiteralList<>(exprs, (Class<R>) Utils.getSuperType(classes), and, this);
	}

	@Override
	public Literal<? extends T>[] getExpressions() {
		return (Literal<? extends T>[]) super.getExpressions();
	}

	@Override
	public Expression<T> simplify() {
		boolean isSimpleList = true;
		for (int i = 0; i < expressions.length; i++)
			isSimpleList &= expressions[i].isSingle();
		if (isSimpleList) {
			@SuppressWarnings("unchecked") final T[] values = (T[]) Array.newInstance(getReturnType(), expressions.length);
			for (int i = 0; i < values.length; i++)
				values[i] = ((Literal<? extends T>) expressions[i]).getSingle();
			return new SimpleLiteral<>(values, getReturnType(), and);
		}
		return this;
	}

}
