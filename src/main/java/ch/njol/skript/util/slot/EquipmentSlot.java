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
package ch.njol.skript.util.slot;

import ch.njol.skript.bukkitutil.PlayerUtils;
import ch.njol.skript.registrations.Classes;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Locale;

/**
 * Represents equipment slot of an entity.
 */
public class EquipmentSlot extends SlotWithIndex {

	public enum EquipSlot {
		TOOL {
			@Override
			@Nullable
			public ItemStack get(final EntityEquipment e) {
				return e.getItemInMainHand();
			}

			@Override
			public void set(final EntityEquipment e, final @Nullable ItemStack item) {
				e.setItemInMainHand(item);
			}
		},
		OFF_HAND(40) {
			@Override
			@Nullable
			public ItemStack get(EntityEquipment e) {
				return e.getItemInOffHand();
			}

			@Override
			public void set(EntityEquipment e, @Nullable ItemStack item) {
				e.setItemInOffHand(item);
			}

		},
		HELMET(39) {
			@Override
			@Nullable
			public ItemStack get(final EntityEquipment e) {
				return e.getHelmet();
			}

			@Override
			public void set(final EntityEquipment e, final @Nullable ItemStack item) {
				e.setHelmet(item);
			}
		},
		CHESTPLATE(38) {
			@Override
			@Nullable
			public ItemStack get(final EntityEquipment e) {
				return e.getChestplate();
			}

			@Override
			public void set(final EntityEquipment e, final @Nullable ItemStack item) {
				e.setChestplate(item);
			}
		},
		LEGGINGS(37) {
			@Override
			@Nullable
			public ItemStack get(final EntityEquipment e) {
				return e.getLeggings();
			}

			@Override
			public void set(final EntityEquipment e, final @Nullable ItemStack item) {
				e.setLeggings(item);
			}
		},
		BOOTS(36) {
			@Override
			@Nullable
			public ItemStack get(final EntityEquipment e) {
				return e.getBoots();
			}

			@Override
			public void set(final EntityEquipment e, final @Nullable ItemStack item) {
				e.setBoots(item);
			}
		};

		public final int slotNumber;

		EquipSlot() {
			slotNumber = -1;
		}

		EquipSlot(int number) {
			slotNumber = number;
		}

		@Nullable
		public abstract ItemStack get(EntityEquipment e);

		public abstract void set(EntityEquipment e, @Nullable ItemStack item);

	}

	private static final EquipSlot[] values = EquipSlot.values();

	private final EntityEquipment e;
	private final EquipSlot slot;
	private final boolean slotToString;

	public EquipmentSlot(final EntityEquipment e, final EquipSlot slot, final boolean slotToString) {
		this.e = e;
		this.slot = slot;
		this.slotToString = slotToString;
	}

	public EquipmentSlot(final EntityEquipment e, final EquipSlot slot) {
		this(e, slot, false);
	}

	@SuppressWarnings("null")
	public EquipmentSlot(HumanEntity holder, int index) {
		this.e = holder.getEquipment();
		this.slot = values[41 - index]; // 6 entries in EquipSlot, indices descending
		// So this math trick gets us the EquipSlot from inventory slot index
		this.slotToString = true; // Referring to numeric slot id, right?
	}

	@Override
	@Nullable
	public ItemStack getItem() {
		return slot.get(e);
	}

	@Override
	public void setItem(final @Nullable ItemStack item) {
		slot.set(e, item);
		if (e.getHolder() instanceof Player)
			PlayerUtils.updateInventory((Player) e.getHolder());
	}

	@Override
	public int getAmount() {
		ItemStack item = slot.get(e);
		return item != null ? item.getAmount() : 0;
	}

	@Override
	public void setAmount(int amount) {
		ItemStack item = slot.get(e);
		if (item != null)
			item.setAmount(amount);
		slot.set(e, item);
	}

	/**
	 * Gets underlying armor slot enum.
	 * @return Armor slot.
	 */
	public EquipSlot getEquipSlot() {
		return slot;
	}

	@Override
	public int getIndex() {
		return slot.slotNumber;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (slotToString) // Slot to string
			return "the " + slot.name().toLowerCase(Locale.ENGLISH) + " of " + Classes.toString(e.getHolder()); // TODO localise?
		else // Contents of slot to string
			return Classes.toString(getItem());
	}

}
