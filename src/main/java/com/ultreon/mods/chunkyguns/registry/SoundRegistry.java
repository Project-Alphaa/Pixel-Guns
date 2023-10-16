package com.ultreon.mods.chunkyguns.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundRegistry {

    public static final SoundEvent COMBAT_SHOTGUN_FIRE = SoundRegistry.register("combat_shotgun_fire");
    public static final SoundEvent PISTOL_FIRE = SoundRegistry.register("pistol_fire");
    public static final SoundEvent REVOLVER_FIRE = SoundRegistry.register("revolver_fire");
    public static final SoundEvent SUBMACHINE_GUN_FIRE = SoundRegistry.register("submachine_gun_fire");
    public static final SoundEvent LIGHT_ASSAULT_RIFLE_FIRE = SoundRegistry.register("light_assault_rifle_fire");
    public static final SoundEvent ASSAULT_RIFLE_FIRE = SoundRegistry.register("assault_rifle_fire");
    public static final SoundEvent ROCKET_LAUNCHER_FIRE = SoundRegistry.register("rocket_launcher_fire");
    public static final SoundEvent SNIPER_RIFLE_FIRE = SoundRegistry.register("sniper_rifle_fire");
    public static final SoundEvent INFINITY_GUN_FIRE = SoundRegistry.register("infinity_gun_fire");

    public static final SoundEvent[] COMBAT_SHOTGUN_RELOAD = {
        SoundRegistry.register("combat_shotgun_reload_1"),
        SoundRegistry.register("combat_shotgun_reload_2"),
        SoundRegistry.register("combat_shotgun_reload_3")
    };

    public static final SoundEvent[] PISTOL_RELOAD = {
        SoundRegistry.register("pistol_reload_1"),
        SoundRegistry.register("pistol_reload_2"),
        SoundRegistry.register("pistol_reload_3")
    };

    public static final SoundEvent[] REVOLVER_RELOAD = {
        SoundRegistry.register("revolver_reload_1"),
        SoundRegistry.register("revolver_reload_2"),
        SoundRegistry.register("revolver_reload_3")
    };

    public static final SoundEvent[] SUBMACHINE_GUN_RELOAD = {
        SoundRegistry.register("submachine_gun_reload_1"),
        SoundRegistry.register("submachine_gun_reload_2"),
        SoundRegistry.register("submachine_gun_reload_3")
    };

    public static final SoundEvent[] LIGHT_ASSAULT_RIFLE_RELOAD = {
        SoundRegistry.register("light_assault_rifle_reload_1"),
        SoundRegistry.register("light_assault_rifle_reload_2"),
        SoundRegistry.register("light_assault_rifle_reload_3")
    };

    public static final SoundEvent[] ASSAULT_RIFLE_RELOAD = {
        SoundRegistry.register("assault_rifle_reload_1"),
        SoundRegistry.register("assault_rifle_reload_2"),
        SoundRegistry.register("assault_rifle_reload_3")
    };

    public static final SoundEvent[] ROCKET_LAUNCHER_RELOAD = {
        SoundRegistry.register("rocket_launcher_reload_1"),
        SoundRegistry.register("rocket_launcher_reload_2"),
        SoundRegistry.register("rocket_launcher_reload_3")
    };

    public static final SoundEvent[] SNIPER_RIFLE_RELOAD = {
        SoundRegistry.register("sniper_rifle_reload_1"),
        SoundRegistry.register("sniper_rifle_reload_2"),
        SoundRegistry.register("sniper_rifle_reload_3")
    };

    public static final SoundEvent[] INFINITY_GUN_RELOAD = {
        SoundRegistry.register("infinity_gun_reload_1"),
        SoundRegistry.register("infinity_gun_reload_2"),
        SoundRegistry.register("infinity_gun_reload_3")
    };

    public static final SoundEvent KATANA_SWING = SoundRegistry.register("katana_swoop");
    public static final SoundEvent KATANA_HIT = SoundRegistry.register("katana_hit");
    public static final SoundEvent GRENADE_EXPLODE = SoundRegistry.register("grenade_explode");
    public static final SoundEvent CROWBAR_HIT = SoundRegistry.register("crowbar_hit");

    public static void init() {}

    private static SoundEvent register(String name) {
        Identifier id = com.ultreon.mods.chunkyguns.ChunkyGuns.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}

