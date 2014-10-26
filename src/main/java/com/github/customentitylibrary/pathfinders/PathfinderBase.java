package com.github.customentitylibrary.pathfinders;

import net.minecraft.server.v1_7_R4.PathfinderGoal;

public abstract class PathfinderBase extends PathfinderGoal
{
	@Override
	public final boolean a()
	{
		return shouldExecute();
	}
	
	@Override
	public final boolean b()
	{
		return continueExecuting();
	}
	
	@Override
	public void c()
	{
		startExecuting();
	}
	
	@Override
	public final void d()
	{
		stopExecuting();
	}
	
	@Override
	public final void e()
	{
		update();
	}
	
	/**
	 * Should this pathfinder being executing?
	 * Called every tick when the pathfinder is not in execution.
	 * @return	if true, execution begins and the startExecuting method is called, else, the pathfinder is skipped and the next priority pathfinder is called
	 */
	public abstract boolean shouldExecute();
	
	/**
	 * Should this pathfinder continue to execute?
	 * Called every tick when the pathfinder is in execution 
	 * @return	if it true, the update method will take place, else, execution will stop and stopExecuting method is called.
	 */
	public abstract boolean continueExecuting();
	
	/**
	 * What to do when the pathfinder first begins executing.
	 */
	public abstract void startExecuting();
	
	/**
	 * What to do when the pathfinder stops executing.
	 */
	public abstract void stopExecuting();
	
	/**
	 * Called every tick while the pathfinder is executing.
	 */
	public abstract void update();
}