package com.github.customentitylibrary.pathfinders;

import net.minecraft.server.v1_7_R4.*;

public class PathfinderMoveToTarget extends PathfinderBase
{
	EntityCreature entity;
	float speed;
	PathEntity path;
	int lastUpdate = 0;
	public PathfinderMoveToTarget(EntityCreature entity, float speed)
	{
		this.entity = entity;
		this.speed = speed;
	}
	
	@Override
	public boolean shouldExecute()
	{
		EntityLiving target = entity.getGoalTarget();
		if(target == null)
			return false;
		this.path = this.entity.getNavigation().a(target.locX, target.locY, target.locZ);	//getNavigation().a sets a path to it's paramaters
		if (this.path != null)
			return true;
		else
		{
			//Vec3D.a means Vec3D.createVector, essentially. RandomPositionGenerator.a gets a random block towards the target from
			Vec3D vec3d = RandomPositionGenerator.a(this.entity, 10, 7, Vec3D.a(target.locX, target.locY, target.locZ));
			if (vec3d == null) 
				return false;
			else
			{
				this.path = this.entity.getNavigation().a(vec3d.a, vec3d.b, vec3d.c);	//getNavigation().a creates a path to it's paramaters. vec3d.a/b/c are x/y/z
				return this.path != null;
			}
		}
	}

	@Override
	public boolean continueExecuting()
	{
		if(++lastUpdate == 60)
		{
			lastUpdate = 0;
			return shouldExecute();
		}
		//getNavigation.f() checks to see if there is no path, in which case, return false
		return !(entity.getNavigation().g() || entity.getGoalTarget() == null);
	}

	@Override
	public void startExecuting()
	{
		
	}

	@Override
	public void stopExecuting()
	{
		lastUpdate = 0;
	}

	@Override
	public void update()
	{
		this.entity.getNavigation().a(path, speed);	//getNavigation().a sets a path to this.path at the speed of speed
	}

}
