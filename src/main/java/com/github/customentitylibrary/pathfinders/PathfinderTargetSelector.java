package com.github.customentitylibrary.pathfinders;

import java.util.List;

import net.minecraft.server.v1_7_R4.*;

public class PathfinderTargetSelector extends PathfinderBase
{
	EntityInsentient entity;
	EntityLiving target;
	float range;
	IEntitySelector selector;
	int lastSwitch = 0;
	boolean targetInvisibles;
	public PathfinderTargetSelector(EntityInsentient entity, IEntitySelector selector, float range, boolean targetInvisibles)
	{
		this.entity = entity;
		this.range = range;
		this.selector = selector;
		this.targetInvisibles = targetInvisibles;
	}

	public PathfinderTargetSelector(EntityInsentient entity, IEntitySelector selector, float range)
	{
		this(entity, selector, range, false);
	}

	@Override
	public boolean shouldExecute()
	{
		target = getTarget();
		return target != null;
	}

	@Override
	public boolean continueExecuting()
	{
		//Only attempts to switch targets every 60 ticks, so the entity doesn't mechanically target the nearest valid entity
		//whenever the nearest valid entity changes.
		if(++lastSwitch == 60)
		{
			lastSwitch = 0;
			target = getTarget();
		}
		return isValidTarget(target);
	}

	@Override
	public void startExecuting()
	{

	}

	@Override
	public void stopExecuting()
	{
		lastSwitch = 0;
		target = null;
	}

	@Override
	public void update()
	{
		entity.setGoalTarget(target);
	}

	@SuppressWarnings("unchecked")
	private EntityLiving getTarget()
	{
		//world.a means world.findNearestEntityWithinAnArea, where the Area is this.entity.boundingBox.grow.
		List<EntityLiving> targetEntities =  this.entity.world.a(EntityLiving.class, this.entity.boundingBox.grow(this.range, this.range, this.range), selector);
		double lastDistance = this.range * this.range;
		EntityLiving lastEntity = null;
		for(EntityLiving targetEntity : targetEntities)
		{
			if(!isValidTarget(targetEntity))
				continue;
			double currentDistance = targetEntity.e(entity);
			if(currentDistance < lastDistance)
			{
				lastEntity = targetEntity;
				lastDistance = currentDistance;
			}
		}
		return lastEntity;
	}

	private boolean isValidTarget(EntityLiving entityliving)
	{
		if (entityliving == null)
			return false;
		if (entityliving == this.entity)
			return false;
		if (!entityliving.isAlive())
			return false;
		if(this.entity.e(entityliving) > range * range)
			return false;
		if(entityliving.isInvisible() && !targetInvisibles)
			return false;
		if (((this.entity instanceof EntityTameableAnimal)) && (((EntityTameableAnimal)this.entity).isTamed()))
		{
			if (entityliving == ((EntityTameableAnimal)this.entity).getOwner())
				return false;
		}
		else if (((entityliving instanceof EntityHuman)) && (((EntityHuman)entityliving).abilities.isFlying))
			return false;
		return true;
	}
}