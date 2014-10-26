package com.github.customentitylibrary.entities;

import java.util.List;

import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.PathfinderGoal;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public interface EntityType
{
	/**
	 * What do be done to a target entity when it is attacked by an entity of this type.
	 * @param target	entity being attacked
	 * @param source	source of attack
	 */
	public void dealEffects(LivingEntity target, LivingEntity source);

	/**
	 * Amount of damage done on attack that completely ignores armor.
	 * @return	damage
	 */
	public double getArmorPiercingDamage();

	/**
	 * Where or not this entity will follow it's target underwater
	 */
	public boolean canDive();

	/**
	 * Whether or not this entity can see inivisble entities.
	 */
	public boolean canSeeInvisible();

	/**
	 * Whether or not to disable knockback dealt to this entity.
	 * @return	knockback disabled
	 */
	public boolean disableKnockbackToSelf();

	/**
	 * Amount of normal damage done on attack.
	 * @return	damage
	 */
	public double getDamage();

	/**
	 * Get a list of goal pathfinders.
	 * @param ent	instance of entity getting the pathfinders
	 * @return		list of pathfinders
	 */
	public List<PathfinderGoal> getGoalSelectors(EntityInsentient ent);

	/**
	 * Get the maximum health of the entity. May be adjusted after the entity has spawned.
	 * @return	max health
	 */
	public double getMaxHealth();

	/**
	 * Get a list of damage causes that this entity is immune to.
	 * @return	list of damage causes
	 */
	public List<DamageCause> getImmunities();

	/**
	 * Get the items this entity wears by default.
	 * @return	an array of 5 items, where 0 index is sword, 1 index is helmet, 2 index is chestplate, 3 index is leggings, 4 index is bots
	 */
	public ItemStack[] getItems();

	/**
	 * Get the preferred type of entity this EntityType should be used for.
	 * @return	name of type of entity, or an empty string if there is none, or it doesn't matter.
	 */
	public String getPreferredType();

	/**
	 * Get the range of this entities targetting.
	 * @return	range
	 */
	public float getRange();

	/**
	 * Get the delay between this entities ranged attacks.
	 * @return	delay, in ticks (1/20 of a second)
	 */
	public int getRangedDelay();

	/**
	 * Get the id of the ranged attack of this entity.
	 * @return	id
	 */
	public int getRangedType();

	/**
	 * Get the movement speed of this entity. For reference, the default speed of a zombie is 0.23F.
	 * @return	speed
	 */
	public float getSpeed();

	/**
	 * Get a list of target pathfinders.
	 * @param ent	instance of entity getting the pathfinders
	 * @return		list of pathfinders
	 */
	public List<PathfinderGoal> getTargetSelectors(EntityInsentient ent);

	/**
	 * Whether or not to entirely ignore invisible entities.
	 * @return true if it is to ignore invisibles, false otherwise
	 */
	public boolean ignoreInvisible();

    /**
     * Whether or not the entity is a baby.
     */
    public boolean isBaby();

	/**
	 * Used for zombies to indicate if it is a zombie villager or a normal zombie.
	 * @return	true if is zombie villager, else false
	 */
	public boolean isVillager();

	/**
	 * Used for skeletons to indicate if it is a wither skeleton or a normal skeleton.
	 * @return	true if is wither skeleton, else false
	 */
	public boolean isWither();

	/**
	 * Called every 20 ticks (1 second) to show any special effects for this entity.
	 * May be used for other things that require a constant repeating call, if necessary.
	 * @param entity	entity special effects are being used on
	 */
	public void showSpecialEffects(LivingEntity entity);

	/**
	 * Returns the name of this entity type.
	 * @return	name
	 */
	public String getName();

	/**
	 * Whether or not to use melee. Mainly used for determining the default pathfinders, if they are not specified.
	 * Can also be checked by external plugins. Using melee does not necessarily mean the entity does not also use ranged.
	 * @return	whether or not this entity uses melee
	 */
	public boolean useMelee();

	/**
	 * Whether or not to use ranged. Mainly used for determining the default pathfinders, if they are not specified.
	 * Can also be checked by external plugins. Using ranged does not necessarily mean the entity does not also use melee.
	 * @return	whether or not this entity uses ranged
	 */
	public boolean useRanged();

	/**
	 * A command to be run when this entity spawns. If an empty string, no command is run.
	 * @return command to run
	 */
	public String getSpawnCommand();
}
