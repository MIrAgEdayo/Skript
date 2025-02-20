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

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.parser.ParserInstance;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.List;

/**
 * Represents a section of a trigger, e.g. a conditional or a loop
 */
public abstract class TriggerSection extends TriggerItem {

	@Nullable
	protected TriggerItem first = null;
	@Nullable
	protected TriggerItem last = null;

	/**
	 * Reserved for new Trigger(...)
	 */
	protected TriggerSection(List<TriggerItem> items) {
		setTriggerItems(items);
	}

	protected TriggerSection(SectionNode node) {
		List<TriggerSection> currentSections = ParserInstance.get().getCurrentSections();
		currentSections.add(this);
		try {
			setTriggerItems(ScriptLoader.loadItems(node));
		} finally {
			currentSections.remove(currentSections.size() - 1);
		}
	}

	/**
	 * Important when using this constructor: set the items with {@link #setTriggerItems(List)}!
	 */
	protected TriggerSection() {
	}

	/**
	 * Remember to add this section to {@link ParserInstance#getCurrentSections()} before parsing child elements!
	 *
	 * <pre>
	 * ScriptLoader.currentSections.add(this);
	 * setTriggerItems(ScriptLoader.loadItems(node));
	 * ScriptLoader.currentSections.remove(ScriptLoader.currentSections.size() - 1);
	 * </pre>
	 */
	protected void setTriggerItems(List<TriggerItem> items) {
		if (!items.isEmpty()) {
			first = items.get(0);
			last = items.get(items.size() - 1);
			last.setNext(getNext());

			for (TriggerItem item : items) {
				item.setParent(this);
			}
		}
	}

	@Override
	public TriggerSection setNext(@Nullable TriggerItem next) {
		super.setNext(next);
		if (last != null)
			last.setNext(next);
		return this;
	}

	@Override
	public TriggerSection setParent(@Nullable TriggerSection parent) {
		super.setParent(parent);
		return this;
	}

	@Override
	protected final boolean run(Event e) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nullable
	protected abstract TriggerItem walk(Event e);

	@Nullable
	protected final TriggerItem walk(Event e, boolean run) {
		debug(e, run);
		if (run && first != null) {
			return first;
		} else {
			return getNext();
		}
	}

}
