package com.github.customentitylibrary.pathfinders;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_7_R4.EntityInsentient;

public class PathfinderSwim extends PathfinderBase
{
	EntityInsentient entity;
	int i = 0;
	boolean canDive;
	float speed;
	public PathfinderSwim(EntityInsentient ent, boolean canDive, float speed)
	{
		entity = ent;
		this.canDive = canDive;
		this.speed = speed;
	}
	
	@Override
	public boolean shouldExecute()
	{
		//For the method that checks if the entity is in water, the best way to update that is to just check the github diffs
		if(entity.L() && !entity.isInvulnerable())	//If is in water and hasn't just been hit
		{
			Entity ent = entity.getBukkitEntity();
			Vector dir = ent.getLocation().getDirection().normalize().multiply(speed/9);
			double y = dir.getY();
			if(y > -.05 && i % 20 == 0)
				y = .3;
			if(!canDive && y < 0)
				y = 0;
			Vector vec = new Vector(dir.getX(), y, dir.getZ());
			i++;
			ent.setVelocity(vec);
		}
		return false;
	}

	@Override
	public boolean continueExecuting()
	{
		return false;
	}

	@Override
	public void startExecuting()
	{
		
	}

	@Override
	public void stopExecuting()
	{
		
	}

	@Override
	public void update()
	{
		
	}
}
