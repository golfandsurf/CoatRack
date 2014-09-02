package com.pandysoft.coatracklib;

import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Coat
{
	TempStorage coatcheck;
	
	public Coat(TempStorage coatcheck)
	{		
		this.coatcheck = coatcheck;
	}
	
	public void setContents(Inventory bInv)
	{
		coatcheck.setContents(bInv);
	}
	
	public Inventory getContents(String title, List<ItemStack> overflow, int size)
	{
		return coatcheck.getContents(title, overflow, size);
	}
	
	private static HashMap<String, Coat> coatRack = new HashMap<String,Coat>();
	public static Coat getCoat(String name, String prefix)
	{
		String coatcheck = prefix + name;
		if (!coatRack.containsKey(coatcheck))
			coatRack.put(coatcheck, new Coat(TempStorage.getCoat(name, prefix)));
		return coatRack.get(coatcheck);
	}
	
	public static void setInventory(String name, String prefix, Inventory inventory)
	{
		TempStorage.setInventory(name, prefix, inventory);
	}
}
