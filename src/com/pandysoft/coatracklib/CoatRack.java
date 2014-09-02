package com.pandysoft.coatracklib;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class CoatRack extends JavaPlugin
{

	public Logger log;

	public CoatRack()
	{
	}

	@Override
	public void onEnable()
	{
		log = Logger.getLogger("Minecraft");

		PluginDescriptionFile pdf = this.getDescription();

		this.log.info(pdf.getName() + " version " + pdf.getVersion()
				+ " is now enabled.");
	}
}