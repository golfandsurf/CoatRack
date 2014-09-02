package com.pandysoft.coatracklib;

import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class TempStorage extends WorldSavedData
{
	public static final String PREFIX = "coat_";
	public static final String ITEMS = "items";
	public static final String SLOT = "Slot";
	public static int maxsize = 81;
	
	public ItemStack[] items = new ItemStack[getMaxSize()];
		
	public int getMaxSize()
	{
		return maxsize;
	}
	public void setMaxSize(int size)
	{
		maxsize = size;
	}
	
	protected void setContents(Inventory bInv)
	{		
		int size = items.length;
		if (size > bInv.getSize())
			size = bInv.getSize();
		for(int i = 0; i < size; i++)
		{			
			items[i] = CraftItemStack.asNMSCopy(bInv.getItem(i));
		}
		this.setDirty(true);
	}
	
	protected Inventory getContents(String title,List<org.bukkit.inventory.ItemStack> overflow,int size)
	{
		//org.bukkit.inventory.ItemStack[] bitems= new org.bukkit.inventory.ItemStack[size];		
		Inventory bInv = Bukkit.getServer().createInventory(null,size,title);
		
		for (int i = 0 ; i < size; i++)		
			bInv.setItem(i,CraftItemStack.asCraftMirror(items[i]));
		for (int i = size; i < items.length; i++)
		{
			if (items[i] != null)
			{
				overflow.add(CraftItemStack.asCraftMirror(items[i]));
				items[i] = null;
			}
		}
		return bInv;		
	}

	public static HashMap<String, TempStorage> coatCache = new HashMap<String, TempStorage>();
	
	protected static TempStorage getCoat(String name)
	{
		return getCoat(name,PREFIX);//,maxsize);
	}
	protected static TempStorage getCoat(String name,String prefix)
	{
		String coatcheck = prefix + name;
		//maxsize = size;

		if (coatCache.containsKey(coatcheck))
			return coatCache.get(coatcheck);
		else
		{
			CraftWorld cw = (CraftWorld) Bukkit.getServer().getWorlds().get(0);

			World ws = (World) cw.getHandle();
			return loadCoat(coatcheck, ws);//,size);
		}
	}
	
	public static Inventory getInventory(String name, String prefix, int size, String title, List<org.bukkit.inventory.ItemStack> overflow)
	{
		TempStorage coat = getCoat(name,prefix);//,size);
		return coat.getContents(title,overflow,size);
	}
	
	public static void setInventory(String name, String prefix, Inventory bInv)
	{
		TempStorage coat = getCoat(name,prefix);
		coat.setContents(bInv);		
	}

	private static TempStorage loadCoat(String coatcheck, World world)//,int size)
	{
		TempStorage coat = (TempStorage) world.loadItemData(TempStorage.class,
				coatcheck);
		
		if (coat == null)
		{
			coat = new TempStorage(coatcheck);
			
			coat.markDirty();
			world.setItemData(coatcheck, coat);
		}
		//coat.setMaxSize(size);
		return coat;
	}

	public TempStorage(String coatcheck)
	{
		super(coatcheck);
		coatCache.put(coatcheck, this);
	}

	public void readData(NBTTagCompound root, String tagName, ItemStack[] items)
	{
		NBTTagList taglist = root.getTagList(tagName);

		for (int i = 0; i < taglist.tagCount(); i++)
		{
			NBTTagCompound child = (NBTTagCompound) taglist.tagAt(i);

			int slotNum = child.getByte(SLOT) & 0xFF;
			if ((slotNum >= 0) && (slotNum < items.length))
			{
				items[slotNum] = ItemStack.loadItemStackFromNBT(child);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound root)
	{
		readData(root, ITEMS, this.items);
	}

	public void writeData(NBTTagCompound root, String tagName, ItemStack[] items)
	{
		NBTTagList taglist = new NBTTagList();

		for (int i = 0; i < items.length; i++)
		{
			if (items[i] != null)
			{
				NBTTagCompound child = new NBTTagCompound();
				child.setByte(SLOT, (byte) i);
				items[i].writeToNBT(child);
				taglist.appendTag(child);
			}
		}

		root.setTag(tagName, taglist);
	}

	@Override
	public void writeToNBT(NBTTagCompound root)
	{
		writeData(root, ITEMS, this.items);
	}

	public int getMaxStackSize()
	{
		return 64;
	}
}
