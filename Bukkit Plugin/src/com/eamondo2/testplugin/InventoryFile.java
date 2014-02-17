package com.eamondo2.testplugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryFile {
	public static void saveInv(Inventory inv, File file, boolean override){
		
		if(inv == null || file == null) return;
		if(file.exists() && override) file.delete();
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		ItemStack[] contents = inv.getContents();
		for(int i = 0; i < contents.length; i++){
			ItemStack item = contents[i];
			if(item != null) if(item.getType() != Material.AIR) conf.set("Slot." + i, item);
		}
		try{
			conf.save(file);
			
		}catch(IOException e){
			return;
			
		}
		
	
	}
	public static void getInv(File file, Inventory inv){
		if(file == null)return;
		ItemStack[] items = null;
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		if(conf.contains("Slot") && conf.isConfigurationSection("Slot")){
            int size = conf.getInt("Slot", 9);
 
            items = new ItemStack[size];
 
            for(int i = 0; i < size; i++){
                if(conf.contains("Slot." + i)) items[i] = conf.getItemStack("Slot." + i);
                else items[i] = new ItemStack(Material.AIR);
            }
        }
		if (items != null){
			inv.setContents(items);
		}
		
		
		
	}
	public static Object loadFile(File file){
		if (file == null) return null;
		
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		
		int count = 0;
		count = conf.getInt("Count");
		return count;
		
		
		
		
		
		
	}
	public static void saveFile(File file, Object obj){
		if (file == null) return;
		if (obj == null) return;
		file.delete();
		FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
		int count = (int) obj;
		conf.set("Count", count);
		try{
			conf.save(file);
			
		}catch(IOException e){
			return;
		}
		
		
		
		
	}
	
	
	
}
