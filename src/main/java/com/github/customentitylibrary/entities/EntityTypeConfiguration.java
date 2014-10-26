package com.github.customentitylibrary.entities;

import java.util.ArrayList;
import java.util.List;

import com.github.customentitylibrary.utils.Utils;

import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.PathfinderGoal;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

/**
 * Provides a default class for creating entity types from YAML configurations.
 */
public class EntityTypeConfiguration implements EntityType
{
	FileConfiguration config;
	String type;
	String name;
	int damage;
	int armorPierce;
	int health;
	float range;
	float speed;
	boolean disableKnockback;
	List<DamageCause> immunities = new ArrayList<DamageCause>();
	ItemStack[] items = new ItemStack[5];

	boolean melee;
	boolean ranged;
    boolean isBaby;
	boolean isWither;
	boolean isVillager;
	boolean canDive;
	boolean canSeeInvisible;
	boolean ignoreInvisible;
	int rangedDelay;
	String rangedType;
	String spawnCommand;
	String skinURL;
	List<DamageEffect> damageEffects = new ArrayList<DamageEffect>();
	List<SpecialEffect> specialEffects = new ArrayList<SpecialEffect>();

	public EntityTypeConfiguration(FileConfiguration config)
	{
		/*
		 * The nested config.get's and if config.contains blocks are there to change from one version of the config to another.
		 * They will be removed eventually
		 */
		this.config = config;
		type = config.getString("Type", "Zombie");
		name = config.getString("Name", type);
		damage = config.getInt("Damage", 2);
		armorPierce = config.getInt("ArmorPiercing", config.getInt("Armor Piercing", 0));
		if(config.contains("Armor Piercing"))
		{
			config.set("ArmorPiercing", config.get("Armor Piercing"));
			config.set("Armor Piercing", null);
		}
		health = config.getInt("MaxHealth", 20);
		range = (float) config.getDouble("Range", 16);
		speed = (float) config.getDouble("Speed", 1);
		if(type.equalsIgnoreCase("zombiepigman"))
			speed /= 4;
		disableKnockback = config.getBoolean("DisableKnockbackToSelf", false);
		isWither = config.getBoolean("IsWither", config.getBoolean("Wither", false));
		if(config.contains("Wither"))
		{
			config.set("IsWither", config.getBoolean("Wither"));
			config.set("Wither", null);
		}
		isVillager = config.getBoolean("IsVillager", false);
        isBaby = config.getBoolean("IsBaby", false);
		canDive = config.getBoolean("CanDive", false);
		ignoreInvisible = config.getBoolean("IgnoreInvisible", false);
		canSeeInvisible = config.getBoolean("CanSeeInvisible", false) && !ignoreInvisible;
		ranged = config.getBoolean("UseRanged", config.getBoolean("Use Ranged", type.equalsIgnoreCase("skeleton") && !isWither));
		if(config.contains("Use Ranged"))
		{
			config.set("UseRanged", config.getBoolean("Use Ranged"));
			config.set("Use Ranged", null);
		}
		melee = config.getBoolean("UseMelee", config.getBoolean("Use Melee", !ranged));
		if(config.contains("Use Melee"))
		{
			config.set("UseMelee", config.getBoolean("Use Melee"));
			config.set("Use Melee", null);
		}
		rangedType = config.getString("RangedAttackType", config.getString("Ranged Attack Type", "Arrow"));
		rangedDelay = config.getInt("ShootDelay", 60);
		spawnCommand = config.getString("SpawnCommand", "");
		skinURL = config.getString("SkinURL", config.getString("Skin URL", null));
		if(config.getStringList("Immunities") != null)
		{
			for(String damageCauseName : config.getStringList("Immunities"))
			{
				DamageCause damageCause = DamageCause.valueOf(damageCauseName.toUpperCase().replaceAll(" ", "_"));
				if(damageCause != null)
					immunities.add(damageCause);
			}
		}
		items[0] = new ItemStack(Material.valueOf(config.getString("Weapon", Material.AIR.toString()).toUpperCase().replaceAll(" ", "_")).getId());
		int index = 1;
		if(config.getStringList("Armor") != null)
		{
			for(String itemName : config.getStringList("Armor"))
			{
				Material material = Material.getMaterial(itemName.toUpperCase().replaceAll(" ", "_"));
				if(material == null)
					material = Material.getMaterial(Utils.parseInt(itemName, -1));
				if(material != null)
					items[index] = new ItemStack(material.getId());
				index++;
				if(index > 4)
					break;
			}
		}
		if(config.getStringList("DamageEffects") != null)
		{
			for(String effectName : config.getStringList("DamageEffects"))
			{
				String[] args = Utils.getConfigArgs(effectName);
				DamageEffect damageEffect = new DamageEffect(effectName.split("\\s")[0], args);
				damageEffects.add(damageEffect);
			}
		}
		if(config.getStringList("SpecialEffects") != null)
		{
			for(String effectName : config.getStringList("SpecialEffects"))
			{
				String[] args = Utils.getConfigArgs(effectName);
				SpecialEffect specialEffect = new SpecialEffect(effectName.split("\\s")[0], args);
				specialEffects.add(specialEffect);
			}
		}
	}

	@Override
	public double getDamage()
	{
		return damage;
	}

	@Override
	public double getArmorPiercingDamage()
	{
		return armorPierce;
	}

	@Override
	public double getMaxHealth()
	{
		return health;
	}

	@Override
	public float getRange()
	{
		return range;
	}

	public boolean useMelee()
	{
		return melee;
	}

	public boolean useRanged()
	{
		return ranged;
	}

	public String getRangedAttackType()
	{
		return rangedType;
	}

	@Override
	public float getSpeed()
	{
		return speed;
	}

	@Override
	public String getPreferredType()
	{
		return type;
	}

	@Override
	public List<DamageCause> getImmunities()
	{
		return immunities;
	}

	@Override
	public ItemStack[] getItems()
	{
		return items;
	}

	public String getSkinURL()
	{
		return skinURL;
	}

	@Override
	public void dealEffects(LivingEntity target, LivingEntity source)
	{
		if(((CraftLivingEntity) target).getHandle().hurtTicks > 0)
			return;
		for(DamageEffect effect : damageEffects)
		{
			effect.dealEffect(target, source);
		}
	}

	@Override
	public void showSpecialEffects(LivingEntity entity)
	{
		for(SpecialEffect effect : specialEffects)
		{
			effect.showEffect(entity);
		}
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public List<PathfinderGoal> getGoalSelectors(EntityInsentient ent)
	{
		return null;
	}

	@Override
	public List<PathfinderGoal> getTargetSelectors(EntityInsentient ent)
	{
		return null;
	}

	@Override
	public boolean isWither()
	{
		return isWither;
	}

	@Override
	public int getRangedType()
	{
		return Material.getMaterial(rangedType).getId();
	}

	@Override
	public int getRangedDelay()
	{
		return rangedDelay;
	}

	@Override
	public boolean isVillager()
	{
		return isVillager;
	}

	@Override
	public boolean canDive()
	{
		return canDive;
	}

	@Override
	public boolean canSeeInvisible()
	{
		return canSeeInvisible;
	}

    @Override
    public boolean isBaby()
    {
        return isBaby;
    }

	@Override
	public boolean ignoreInvisible()
	{
		return ignoreInvisible;
	}

	@Override
	public boolean disableKnockbackToSelf()
	{
		return disableKnockback;
	}

	@Override
	public String getSpawnCommand()
	{
		return spawnCommand;
	}
}
