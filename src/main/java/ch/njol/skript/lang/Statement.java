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

import ch.njol.skript.Skript;
import ch.njol.skript.lang.function.EffFunctionCall;
import ch.njol.skript.log.ParseLogHandler;
import ch.njol.skript.log.SkriptLogger;
import org.bukkit.Bukkit;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Iterator;
import java.util.logging.Level;

/**
 * Supertype of conditions and effects
 *
 * @see Condition
 * @see Effect
 */
public abstract class Statement extends TriggerItem implements SyntaxElement {

	@SuppressWarnings({"rawtypes", "unchecked", "null"})
	@Nullable
	public static Statement parse(String s, String defaultError) {
		ParseLogHandler log = SkriptLogger.startParseLogHandler();
		Bukkit.getLogger().log(Level.INFO, "");
		Bukkit.getLogger().log(Level.INFO, s);
		Bukkit.getLogger().log(Level.INFO, "");
		try {
			EffFunctionCall f = EffFunctionCall.parse(s);
			if (f != null) {
				log.printLog();
				return f;
			} else if (log.hasError()) {
				log.printError();
				return null;
			}
			log.clear();

			EffectSection section = EffectSection.parse(s, null, null, null);
			if (section != null) {
				log.printLog();
				/*
				Bukkit.getLogger().log(Level.INFO, "sectionToString: " + section);
				Bukkit.getLogger().log(Level.INFO, "sectionIndentToString: " + section.getIndentation());
				Bukkit.getLogger().log(Level.INFO, "sectionParentToString: " + section.getParent());
				sectionはnullらしい
				 */
				return new EffectSectionEffect(section);
			}
			log.clear();

			Statement statement = (Statement) SkriptParser.parse(s, (Iterator) Skript.getStatements().iterator(), defaultError);
			if (statement != null) {
				log.printLog();
				/*
				Bukkit.getLogger().log(Level.INFO, "statementToString: " + statement); //処理中のノード
				Bukkit.getLogger().log(Level.INFO, "statementIndentToString: " + statement.getIndentation()); //empty
				Bukkit.getLogger().log(Level.INFO, "statementParentToString: " + statement.getParent()); //null
				 */
				return statement;
			}

			log.printError();
			return null;
		} finally {
			log.stop();
		}
	}

}
