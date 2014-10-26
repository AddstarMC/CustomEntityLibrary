package com.github.customentitylibrary.utils;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class Utils
{
	/**
	 * Get arguments from a String gotten from a YAMLConfiguration entry, split by commas.
	 * @param regex	the String to get the arguments from
	 * @return		array of arguments
	 */
	public static String[] getConfigArgs(String regex)
	{
		String[] args = new String[0];
		if(regex.contains(" "))
		{
			String argsNotArray = regex.split("\\s")[1];
			args = argsNotArray.split(",");
		}
		return args;
	}
	
	/**
	 * Parse a String to a int
	 * @param toParse	string to be parsed
	 * @param defaultValue	default value to return if 'toParse' is not parsable
	 * @return	int value of 'toParse', or 'defaultValue' if it is not parsable
	 */
	public static int parseInt(String toParse, int defaultValue)
	{
		try
		{
			return Integer.parseInt(toParse);
		} catch(NumberFormatException e)
		{
			return defaultValue;
		}
	}
	
	/**
	 * Parse a String to a double
	 * @param toParse	string to be parsed
	 * @param defaultValue	default value to return if 'toParse' is not parsable
	 * @return	double value of 'toParse', or 'defaultValue' if it is not parsable
	 */
	public static double parseDouble(String toParse, double defaultValue)
	{
		try
		{
			return Double.parseDouble(toParse);
		} catch(NumberFormatException e)
		{
			return defaultValue;
		}
	}
	
	//I suspect the verticalModifier isn't working...
	public static Vector knockBack(LivingEntity entity, Location source, double horizontalModifier, double verticalModifier)
	{
		if(((CraftLivingEntity) entity).getHandle().hurtTicks > 0)
			return new Vector(0,0,0);
		Location target = entity.getLocation();
		double xPower = target.getX() - source.getX();
		double yPower = 1D;
		double zPower = target.getZ() - source.getZ();

		double magnitude = Math.sqrt(xPower * xPower + zPower * zPower);
		xPower /= magnitude;
		zPower /= magnitude;
		xPower /= 2;
		zPower /= 2;

		xPower *= horizontalModifier;
		yPower *= verticalModifier;
		zPower *= horizontalModifier;

		Vector knockback = new Vector(xPower, yPower, zPower);
		entity.setVelocity(knockback);
		return knockback;
	}
}
