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
package ch.njol.skript.registrations;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.Date;
import ch.njol.skript.util.Timespan;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.eclipse.jdt.annotation.NonNull;

// When using these fields, be aware all ClassInfo's must be registered!
public class DefaultClasses {

	public static ClassInfo<Object> OBJECT = getClassInfo(Object.class);

	public static ClassInfo<Number> NUMBER = getClassInfo(Number.class);
	public static ClassInfo<Long> LONG = getClassInfo(Long.class);
	public static ClassInfo<Boolean> BOOLEAN = getClassInfo(Boolean.class);
	public static ClassInfo<String> STRING = getClassInfo(String.class);

	public static ClassInfo<World> WORLD = getClassInfo(World.class);
	public static ClassInfo<Location> LOCATION = getClassInfo(Location.class);
	public static ClassInfo<Vector> VECTOR = getClassInfo(Vector.class);

	public static ClassInfo<Color> COLOR = getClassInfo(Color.class);
	public static ClassInfo<Date> DATE = getClassInfo(Date.class);
	public static ClassInfo<Timespan> TIMESPAN = getClassInfo(Timespan.class);
	public static ClassInfo<OfflinePlayer> OFFLINE_PLAYER = getClassInfo(OfflinePlayer.class);
	public static ClassInfo<Player> PLAYER = getClassInfo(Player.class);

	@NonNull
	private static <T> ClassInfo<T> getClassInfo(Class<T> tClass) {
		//noinspection ConstantConditions
		ClassInfo<T> classInfo = Classes.getExactClassInfo(tClass);
		if (classInfo == null)
			throw new NullPointerException();
		return classInfo;
	}

}
