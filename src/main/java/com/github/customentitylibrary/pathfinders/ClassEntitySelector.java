package com.github.customentitylibrary.pathfinders;

import java.util.Arrays;
import java.util.List;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.IEntitySelector;

public class ClassEntitySelector implements IEntitySelector
{
	List<Class<? extends Entity>> targets;
	public ClassEntitySelector(Class<? extends Entity>... targets)
	{
		this(Arrays.asList(targets));
	}
	
	public ClassEntitySelector(List<Class<? extends Entity>> targets)
	{
		this.targets = targets;
	}
	
	@Override
	public boolean a(Entity entity)
	{
		for(Class<?> t : targets)
			if(t.isAssignableFrom(entity.getClass())) return true;
		return false;
	}
}
