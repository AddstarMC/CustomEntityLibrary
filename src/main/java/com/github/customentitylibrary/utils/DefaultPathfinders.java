package com.github.customentitylibrary.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.customentitylibrary.entities.EntityType;
import com.github.customentitylibrary.pathfinders.*;
import net.minecraft.server.v1_7_R4.*;

public class DefaultPathfinders
{
	/**
	 * Gets the default goal selectors of the entity. They are identical to those of normal entities, save the following changes:<br>
	 * No pathfinders of overlapping priority.<br>
	 * PathfinderGoalMeleeAttack and PathfinderGoalArrowAttack are both replaced by <br>
	 * <br>
	 * <code>
	 * if(type.useRanged())<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;pathfinders.add(new PathfinderCustomArrowAttack((IRangedEntity) ent, speed,
	 * &nbsp;&nbsp;&nbsp;&nbsp;type.getRangedDelay(), [int], [int]));<br>
	 * if(type.useMelee())<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;pathfinders.add(new PathfinderGoalMeleeAttack(ent, speed, [bool]));<br>
	 * PathfinderMoveToTarget((EntityCreature) ent, speed));<br>
	 * </code>
	 * <br>
	 * All instances where a constant speed or range value is inputted are replaced by type.getSpeed() and type.getRange() respectively.<br>
	 * All instances where a non accesible field from the entity is required use reflection to get that field.<br>
	 * All instances of PathfinderGoalFloats are replaced with PathfinderSwims
	 * @param ent	entity to get goal selectors for
	 * @param type	type of the entity
	 * @return		map of priorities to that priorities pathfinder
	 */
	public static List<PathfinderGoal> getGoalSelectors(EntityInsentient ent, EntityType type)
	{
		float speed = type.getSpeed();
		List<PathfinderGoal> pathfinders = new ArrayList<PathfinderGoal>();
		if(ent instanceof EntityChicken)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
	        pathfinders.add(new PathfinderGoalPanic((EntityCreature) ent, speed * 1.52f));
	        pathfinders.add(new PathfinderGoalBreed((EntityAnimal) ent, speed));
	        pathfinders.add(new PathfinderGoalTempt((EntityCreature) ent, speed, Items.SEEDS, false));
	        pathfinders.add(new PathfinderGoalFollowParent((EntityAnimal) ent, speed * 1.12f));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 6.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntityCow)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
	        pathfinders.add(new PathfinderGoalPanic((EntityCreature) ent, speed * 1.52f));
	        pathfinders.add(new PathfinderGoalBreed((EntityAnimal) ent, speed * .8f));
	        pathfinders.add(new PathfinderGoalTempt((EntityCreature) ent, speed, Items.WHEAT, false));
	        pathfinders.add(new PathfinderGoalFollowParent((EntityAnimal) ent, speed));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed * .8f));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 6.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntityCreeper)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
	        pathfinders.add(new PathfinderGoalSwell((EntityCreeper) ent));
	        pathfinders.add(new PathfinderGoalAvoidPlayer((EntityCreature) ent, EntityOcelot.class, 6.0F, speed, 0.3F));
			pathfinders.add(new PathfinderMoveToTarget((EntityCreature) ent, speed));
			if(type.useMelee())
				pathfinders.add(new PathfinderGoalMeleeAttack((EntityCreature) ent, speed, false));
	        if(type.useRanged())
	        	pathfinders.add(new PathfinderCustomArrowAttack(ent, speed, type.getRangedDelay(), 16, 4));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed * .8f));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 8.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntityIronGolem)
		{
			pathfinders.add(new PathfinderMoveToTarget((EntityCreature) ent, speed));
			if(type.useMelee())
				pathfinders.add(new PathfinderGoalMeleeAttack((EntityCreature) ent, speed, true));
			if(type.useRanged())
	        	pathfinders.add(new PathfinderCustomArrowAttack(ent, speed, type.getRangedDelay(), 16, 4));
	        pathfinders.add(new PathfinderGoalMoveTowardsTarget((EntityCreature) ent, speed, 32.0F));
	        pathfinders.add(new PathfinderGoalMoveThroughVillage((EntityCreature) ent, speed * .727f, true));
	        pathfinders.add(new PathfinderGoalMoveTowardsRestriction((EntityCreature) ent, speed * .727f));
	        pathfinders.add(new PathfinderGoalOfferFlower((EntityIronGolem) ent));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed * .727f));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 6.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntityOcelot)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
	        pathfinders.add((PathfinderGoal) getField(EntityTameableAnimal.class, ent, NMS.ANIMAL_GOALSELECTOR2));
	        pathfinders.add((PathfinderGoal) setField(EntityOcelot.class, ent, NMS.OCELOT_TEMPT, new PathfinderGoalTempt((EntityCreature) ent, 0.18F, Items.RAW_FISH, true)));
	        pathfinders.add(new PathfinderGoalAvoidPlayer((EntityCreature) ent, EntityHuman.class, 16.0F, speed, 0.4F));
	        pathfinders.add(new PathfinderGoalFollowOwner((EntityTameableAnimal) ent, speed * 1.3f, 10.0F, 5.0F));
	        pathfinders.add(new PathfinderGoalJumpOnBlock((EntityOcelot) ent, speed * 1.74f));
	        pathfinders.add(new PathfinderGoalLeapAtTarget(ent, speed * 1.3f));
	        pathfinders.add(new PathfinderGoalOcelotAttack(ent));
	        pathfinders.add(new PathfinderMoveToTarget((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalBreed((EntityAnimal) ent, speed));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 10.0F));
		} else if(ent instanceof EntityPig)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
	        pathfinders.add(new PathfinderGoalPanic((EntityCreature) ent, speed * 1.52f));
	        pathfinders.add((PathfinderGoal) setField(EntityTameableAnimal.class, ent, NMS.ANIMAL_GOALSELECTOR2, new PathfinderGoalPassengerCarrotStick(ent, speed * 1.36f)));
	        pathfinders.add(new PathfinderGoalBreed((EntityAnimal) ent, speed));
	        pathfinders.add(new PathfinderGoalTempt((EntityCreature) ent, speed * 1.2f, Items.CARROT_STICK, false));
	        pathfinders.add(new PathfinderGoalTempt((EntityCreature) ent, speed * 1.2f, Items.CARROT, false));
	        pathfinders.add(new PathfinderGoalFollowParent((EntityAnimal) ent, speed * 1.12f));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 6.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntitySheep)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
	        pathfinders.add(new PathfinderGoalPanic((EntityCreature) ent, speed * 1.652f));
	        pathfinders.add(new PathfinderGoalBreed((EntityAnimal) ent, speed));
	        pathfinders.add(new PathfinderGoalTempt((EntityCreature) ent, speed * 1.087f, Items.WHEAT, false));
	        pathfinders.add(new PathfinderGoalFollowParent((EntityAnimal) ent, speed * 1.087f));
	        pathfinders.add((PathfinderGoal) getField(EntitySheep.class, ent, NMS.SHEEP_EAT_TILE));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 6.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntitySkeleton)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
	        pathfinders.add(new PathfinderGoalRestrictSun((EntityCreature) ent));
	        pathfinders.add(new PathfinderGoalFleeSun((EntityCreature) ent, speed));
			pathfinders.add(new PathfinderMoveToTarget((EntityCreature) ent, speed));
			if(type.useMelee())
				pathfinders.add(new PathfinderGoalMeleeAttack((EntityCreature) ent, speed, true));
	        if(type.useRanged())
	        	pathfinders.add(new PathfinderCustomArrowAttack(ent, speed, type.getRangedDelay(), 16, 4, "arrow"));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 8.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntityWitch)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
			pathfinders.add(new PathfinderMoveToTarget((EntityCreature) ent, speed));
			if(type.useMelee())
				pathfinders.add(new PathfinderGoalMeleeAttack((EntityCreature) ent, speed, true));
			if(type.useRanged())
	        	pathfinders.add(new PathfinderCustomArrowAttack(ent, speed, type.getRangedDelay(), 16, 4));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 8.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntityWither)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
			pathfinders.add(new PathfinderMoveToTarget((EntityCreature) ent, speed));
			if(type.useMelee())
				pathfinders.add(new PathfinderGoalMeleeAttack((EntityCreature) ent, speed, true));
			if(type.useRanged())
	        	pathfinders.add(new PathfinderCustomArrowAttack(ent, speed, type.getRangedDelay(), 20, 4));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 8.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntityWolf)
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
			pathfinders.add(new PathfinderMoveToTarget((EntityCreature) ent, speed));
	        pathfinders.add((PathfinderGoal) getField(EntityTameableAnimal.class, ent, NMS.ANIMAL_GOALSELECTOR2));
	        pathfinders.add(new PathfinderGoalLeapAtTarget(ent, 0.4F));
			if(type.useMelee())
	        	pathfinders.add(new PathfinderGoalMeleeAttack((EntityCreature) ent, speed, true));
			if(type.useRanged())
				pathfinders.add(new PathfinderCustomArrowAttack(ent, speed, type.getRangedDelay(), 16, 4));
	        pathfinders.add(new PathfinderGoalFollowOwner((EntityTameableAnimal) ent, speed, 10.0F, 2.0F));
	        pathfinders.add(new PathfinderGoalBreed((EntityAnimal) ent, speed));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalBeg((EntityWolf) ent, 8.0F));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 8.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		} else if(ent instanceof EntityZombie)	//Includes the PigZombie, a subclass of Zombie
		{
			pathfinders.add(new PathfinderSwim(ent, type.canDive(), speed));
	        pathfinders.add(new PathfinderGoalBreakDoor(ent));
			pathfinders.add(new PathfinderMoveToTarget((EntityCreature) ent, speed));
	        if(type.useMelee())
	        	pathfinders.add(new PathfinderGoalMeleeAttack((EntityCreature) ent, speed, false));
			if(type.useRanged())
				pathfinders.add(new PathfinderCustomArrowAttack(ent, speed, type.getRangedDelay(), 16, 4));
	        pathfinders.add(new PathfinderGoalMoveTowardsRestriction((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalMoveThroughVillage((EntityCreature) ent, speed, false));
	        pathfinders.add(new PathfinderGoalRandomStroll((EntityCreature) ent, speed));
	        pathfinders.add(new PathfinderGoalLookAtPlayer(ent, EntityHuman.class, 8.0F));
	        pathfinders.add(new PathfinderGoalRandomLookaround(ent));
		}
		return pathfinders;
	}

	/**
	 * Gets the default target selectors of the entity. They are identical to those of normal entities, save the following changes:<br>
	 * No pathfinders of overlapping priority.<br>
	 * All instances of PathfinderGoalNearestAttackableTarget are replaced by PathfinderTargetSelector.<br>
	 * All instances where a constant speed or range value is inputted are replaced by type.getSpeed() and type.getRange() respectively.<br>
	 * All instances where a non accesible field from the entity is required use reflection to get that field.<br>
	 * @param ent	entity to get target selectors for
	 * @param type	type of the entity
	 * @return		map of priorities to that priorities pathfinder
	 */
	@SuppressWarnings("unchecked")
	public static List<PathfinderGoal> getTargetSelectors(EntityInsentient ent, EntityType type)
	{
		float range = type.getRange();
		List<PathfinderGoal> pathfinders = new ArrayList<PathfinderGoal>();
		if(ent instanceof EntityCreeper)
		{
	        pathfinders.add(new PathfinderTargetSelector(ent, new ClassEntitySelector(EntityHuman.class), range, type.canSeeInvisible()));
	        pathfinders.add(new PathfinderGoalHurtByTarget((EntityCreature) ent, false));
		} else if(ent instanceof EntityIronGolem)
		{
	        pathfinders.add(new PathfinderGoalDefendVillage((EntityIronGolem) ent));
	        pathfinders.add(new PathfinderGoalHurtByTarget((EntityCreature) ent, false));
	        pathfinders.add(new PathfinderTargetSelector(ent, IMonster.a, range, type.canSeeInvisible()));
		} else if(ent instanceof EntityOcelot)
		{
	        pathfinders.add(new PathfinderGoalRandomTargetNonTamed((EntityTameableAnimal) ent, EntityChicken.class, 750, false));
		} else if(ent instanceof EntitySkeleton)
		{
	        pathfinders.add(new PathfinderGoalHurtByTarget((EntityCreature) ent, false));
	        pathfinders.add(new PathfinderTargetSelector(ent, new ClassEntitySelector(EntityHuman.class), range, type.canSeeInvisible()));
		} else if(ent instanceof EntityWitch)
		{
	        pathfinders.add(new PathfinderGoalHurtByTarget((EntityCreature) ent, false));
	        pathfinders.add(new PathfinderTargetSelector(ent, new ClassEntitySelector(EntityHuman.class), range, type.canSeeInvisible()));
		} else if(ent instanceof EntityWither)
		{
	        pathfinders.add(new PathfinderGoalHurtByTarget((EntityCreature) ent, false));
	        pathfinders.add(new PathfinderTargetSelector(ent, (IEntitySelector) getField(EntityWither.class, ent, NMS.WITHER_SELECTOR), range, type.canSeeInvisible()));
		} else if(ent instanceof EntityWolf)
		{
	        pathfinders.add(new PathfinderGoalOwnerHurtByTarget((EntityTameableAnimal) ent));
	        pathfinders.add(new PathfinderGoalOwnerHurtTarget((EntityTameableAnimal) ent));
	        pathfinders.add(new PathfinderGoalHurtByTarget((EntityCreature) ent, false));
	        pathfinders.add(new PathfinderGoalRandomTargetNonTamed((EntityTameableAnimal) ent, EntitySheep.class, 200, false));
		} else if(ent instanceof EntityZombie)
		{
	        pathfinders.add(new PathfinderGoalHurtByTarget((EntityCreature) ent, false));
	        pathfinders.add(new PathfinderTargetSelector(ent, new ClassEntitySelector(EntityHuman.class, EntityVillager.class), range, type.canSeeInvisible()));
		}
		return pathfinders;
	}

	private static Object getField(Class<? extends EntityInsentient> c, EntityInsentient ent, String name)
	{
		try
		{
			Field f = c.getDeclaredField(name);
			f.setAccessible(true);
			return f.get(ent);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static Object setField(Class<? extends EntityInsentient> c, EntityInsentient ent, String name, Object value)
	{
		try
		{
			Field f = c.getDeclaredField(name);
			f.setAccessible(true);
			f.set(ent, value);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}
}
