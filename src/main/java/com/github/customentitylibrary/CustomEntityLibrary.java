package com.github.customentitylibrary;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import com.github.customentitylibrary.entities.CustomEntityWrapper;
import com.github.customentitylibrary.entities.CustomGiant;
import com.github.customentitylibrary.entities.CustomPigZombie;
import com.github.customentitylibrary.listeners.LibraryEntityListener;
import com.github.customentitylibrary.utils.NMS;

import net.minecraft.server.v1_7_R4.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomEntityLibrary
{	
	public static JavaPlugin plugin;
	
	public static void enable(JavaPlugin plugin)
	{
		CustomEntityLibrary.plugin = plugin;
		loadCustomEntities();
		LibraryEntityListener eListener = new LibraryEntityListener();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		eListener.registerEvents(pm, plugin);
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				onTick();
			}
		}, 1L, 1L);
	}
	
	private static int tick = 0;
	private static void onTick()
	{
		if(tick % 20 == 0)
		{
			Iterator<Map.Entry<EntityInsentient, CustomEntityWrapper>> entities = CustomEntityWrapper.getCustomEntities().entrySet().iterator();
			while(entities.hasNext())
			{
				Map.Entry<EntityInsentient, CustomEntityWrapper> next = entities.next();
				if(!next.getKey().isAlive())
					entities.remove();
				else
					next.getValue().getType().showSpecialEffects((LivingEntity) next.getKey().getBukkitEntity());
			}
		}
		tick++;
	}

	private static void loadCustomEntities()
	{
		HashMap[] typeHashMaps = getEntityTypesHashMaps();
		HashMap eggTypeHashMap = getEntityTypesEggHashMap();

		loadCustomEntity(CustomPigZombie.class, "PigZombie", typeHashMaps, eggTypeHashMap, 57);
		loadCustomEntity(CustomGiant.class, "Giant", typeHashMaps, eggTypeHashMap, 53);
	}

	@SuppressWarnings("unchecked")
	private static void loadCustomEntity(Class entityClass, String entityName, HashMap[] typeHashMaps, HashMap eggTypeHashMap, int id)
	{
		typeHashMaps[0].put(entityName, entityClass);
		typeHashMaps[1].put(entityClass, entityName);
		typeHashMaps[2].put(id, entityClass);
		typeHashMaps[3].put(entityClass, id);
		typeHashMaps[4].put(entityName, id);
	}

	private static HashMap[] getEntityTypesHashMaps()
	{
		try
		{
			Class entityTypesClass = EntityTypes.class;
			Field[] mapFields = new Field[5];
			mapFields[0] = entityTypesClass.getDeclaredField(NMS.ENTITY_TYPE_MAPS[0]);
			mapFields[1] = entityTypesClass.getDeclaredField(NMS.ENTITY_TYPE_MAPS[1]);
			mapFields[2] = entityTypesClass.getDeclaredField(NMS.ENTITY_TYPE_MAPS[2]);
			mapFields[3] = entityTypesClass.getDeclaredField(NMS.ENTITY_TYPE_MAPS[3]);
			mapFields[4] = entityTypesClass.getDeclaredField(NMS.ENTITY_TYPE_MAPS[4]);
			HashMap[] maps = new HashMap[5];
			for(int i = 0; i < 5; i++)
			{
				Field f = mapFields[i];
				f.setAccessible(true);
				maps[i] = (HashMap) f.get(EntityTypes.class);
			}
			return maps;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static HashMap getEntityTypesEggHashMap()
	{
		try
		{
			Class entityTypesClass = EntityTypes.class;
			Field mapField = entityTypesClass.getDeclaredField(NMS.ENTITY_TYPE_EGGS);
			mapField.setAccessible(true);
			return (HashMap) mapField.get(EntityTypes.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
    
    public static void log(String msg)
    {
    	log(Level.INFO, msg);
    }
    
    public static void log(Level level, String msg)
    {
    	plugin.getLogger().log(level, msg);
    }
}
