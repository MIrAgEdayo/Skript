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
package ch.njol.skript.localization;

import ch.njol.skript.Skript;
import org.eclipse.jdt.annotation.Nullable;

import java.util.HashMap;

/**
 * @author Peter Güttinger
 */
public class Adjective extends Message {

	// at least in German adjectives behave differently with a definite article. Cases are still not supported though and will likely never be.
	private final static int DEFINITE_ARTICLE = -100;
	private final static String DEFINITE_ARTICLE_TOKEN = "+";

	private final HashMap<Integer, String> genders = new HashMap<>();
	@Nullable
	String def;

	public Adjective(String key) {
		super(key);
	}

	@Override
	protected void onValueChange() {
		genders.clear();
		String v = getValue();
		def = v;
		if (v == null)
			return;
		int s = v.indexOf('@'), e = v.lastIndexOf('@');
		if (s == -1)
			return;
		if (s == e) {
			Skript.error("Invalid use of '@' in the adjective '" + key + "' in the language file: " + v);
			return;
		}
		def = v.substring(0, s) + v.substring(e + 1);
		int c = s;
		do {
			int c2 = v.indexOf('@', c + 1);
			int d = v.indexOf(':', c + 1);
			if (d == -1 || d > c2) {
				Skript.error("Missing colon (:) to separate the gender in the adjective '" + key + "' in the language file at index " + c + ": " + v);
				return;
			}
			String gender = v.substring(c + 1, d);
			int g = gender.equals(DEFINITE_ARTICLE_TOKEN) ? DEFINITE_ARTICLE : Noun.getGender(gender, key);
			if (!genders.containsKey(g))
				genders.put(g, v.substring(0, s) + v.substring(d + 1, c2) + v.substring(e + 1));
			c = c2;
		} while (c < e);
	}

	@Override
	public String toString() {
		validate();
		if (Skript.testing())
			Skript.warning("Invalid use of Adjective.toString()");
		return "" + def;
	}

	public String toString(int gender, int flags) {
		validate();
		if ((flags & Language.F_DEFINITE_ARTICLE) != 0 && genders.containsKey(DEFINITE_ARTICLE))
			gender = DEFINITE_ARTICLE;
		else if ((flags & Language.F_PLURAL) != 0)
			gender = Noun.PLURAL;
		String a = genders.get(gender);
		if (a != null)
			return a;
		return "" + def;
	}

	public static String toString(Adjective[] adjectives, int gender, int flags, boolean and) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < adjectives.length; i++) {
			if (i != 0) {
				if (i == adjectives.length - 1)
					b.append(" ").append(and ? GeneralWords.and : GeneralWords.or).append(" ");
				else
					b.append(", ");
			}
			b.append(adjectives[i].toString(gender, flags));
		}
		return "" + b.toString();
	}

	public String toString(Noun n, int flags) {
		return n.toString(this, flags);
	}

}
