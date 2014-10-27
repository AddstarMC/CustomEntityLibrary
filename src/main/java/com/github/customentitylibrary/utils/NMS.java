package com.github.customentitylibrary.utils;

public class NMS
{
	//Can be found in PathfinderGoalSelector class, it's the first list
	public static final String PATHFINDER_LIST = "b";
	//Found in Ocelot's second goal selector (or wolf)
	public static final String ANIMAL_GOALSELECTOR2 = "bp";
	//In Ocelot's field, private PathfinderGoalTempt <name>;
	public static final String OCELOT_TEMPT = "bq";
	//Sheep, field: private PathfinderGoalEatTile <name> = new PathfinderGoalEatTile(this);
	public static final String SHEEP_EAT_TILE = "bs";
	//Wither, field: private static final IEntitySelector <name> = new EntitySelectorNotUndead();
	public static final String WITHER_SELECTOR = "bw";
	//PathfinderGoalSelectorItem, field: private PathfinderGoal <name>;
	public static final String PATHFINDER_GOAL = "a";
	//EntityTypes, field: public static HashMap <name> = new LinkedHashMap();
	public static final String ENTITY_TYPE_EGGS = "eggInfo";
	//EntityTypes, all static maps in order, not including the egg map above
	public static final String[] ENTITY_TYPE_MAPS = new String[] {"c", "d", "e", "f", "g"};
}
