package net.jay.mobsexplode;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobsExplode implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("mobs-explode");

	public static final int[] mobsFuseSpeed = new int[50000];
	public static final int[] mobsFuseTime = new int[50000];
	public static final int[] lastMobsFuseTime = new int[50000];

	public static final int fuseTime = 30;

	@Override
	public void onInitialize() {
	}
}
