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
package ch.njol.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.Aliases;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

@Name("Open Book")
@Description("Opens a written book to a player.")
@Examples("open book player's tool to player")
@RequiredPlugins("Minecraft 1.14.2+")
@Since("2.5.1")
public class EffOpenBook extends Effect {

	static {
		if (Skript.methodExists(Player.class, "openBook", ItemStack.class)) {
			Skript.registerEffect(EffOpenBook.class, "(open|show) book %itemtype% (to|for) %players%");
		}
	}

	private static final ItemType bookItemType = Aliases.javaItemType("written book");

	@SuppressWarnings("null")
	private Expression<ItemType> book;
	@SuppressWarnings("null")
	private Expression<Player> players;

	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		book = (Expression<ItemType>) exprs[0];
		players = (Expression<Player>) exprs[1];
		return true;
	}

	@Override
	protected void execute(final Event e) {
		ItemType itemType = book.getSingle(e);
		if (itemType != null) {
			ItemStack itemStack = itemType.getRandom();
			if (itemStack != null && bookItemType.isOfType(itemStack)) {
				for (Player player : players.getArray(e)) {
					player.openBook(itemStack);
				}
			}
		}
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "open book " + book.toString(e, debug) + " to " + players.toString(e, debug);
	}

}
