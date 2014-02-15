package com.eamondo2.testplugin;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginClass extends JavaPlugin implements Listener {
	
	
	
	HashMap<String, Inventory> inventoryContents = new HashMap<String, Inventory>();
	String path = ("plugins/BackPack/users" + File.separator);
	private void backpackrecipe(){
		
		ItemStack backpack = new ItemStack(Material.CHEST, 1);
		
		ItemMeta meta = backpack.getItemMeta();
		try{
			Field f = ItemStack.class.getDeclaredField("maxStackSize");
			f.setAccessible(true);
			f.setInt(backpack, 1);
			f.setAccessible(false);
			
			
		}catch(Exception e){}
		
		meta.setDisplayName("BackPack");
		meta.setLore(Arrays.asList("Left Click to open"));
		backpack.setItemMeta(meta);
		
		ShapedRecipe backpackrecipe = new ShapedRecipe(backpack);
		backpackrecipe.shape(
				"@ @",
				" # ",
				"@ @"
				);
		backpackrecipe.setIngredient('@', Material.STRING);
		backpackrecipe.setIngredient('#', Material.CHEST);
		Bukkit.getServer().addRecipe(backpackrecipe);
		
		
		
	}
	public void onEnable(){
		getLogger().info("SimplePack loaded");
		Bukkit.getServer().getPluginManager().registerEvents(this,this);
		backpackrecipe();
		
		
		
		
		
	}
	@Override
	public void onDisable(){
		getLogger().info("SimplePack unloaded");
		
		
		
		
		
	}
	//TEST CODE
	/*public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player p = (Player)sender;
		if (label.equalsIgnoreCase("test")){
			getLogger().info("TEST COMMAND");
			Inventory inv = p.getInventory();
			ItemStack stack = new ItemStack(Material.CHEST, 1);
			ItemMeta meta = stack.getItemMeta();
			meta.setDisplayName("BackPack");
			stack.setItemMeta(meta);
			inv.addItem(stack);
		}
		
		return true;
	}*/
	
	//@EventHandler
	//public void onPlayerMove (PlayerMoveEvent event){
	//	getLogger().info("PLAYER MOVE");
	//}
	

	@EventHandler
	public void PlayerInteractEvent (org.bukkit.event.player.PlayerInteractEvent event){
		
		Player p = event.getPlayer();
		//getLogger().info("PINTERACT EVENT");
		
		
		
	
		ItemStack inhand = event.getItem();
		if (inhand == null){
			return;
			
		}
		if (inhand.getType() != Material.CHEST){
			return;
		}
		String itemdisp;
		if (inhand.getItemMeta().hasDisplayName() && inhand != null){
			itemdisp = inhand.getItemMeta().getDisplayName().toString();
		}
		else{
			return;
		}
			
		
		if (itemdisp.equals("BackPack")){
			//getLogger().info("BP");
			if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK ||event.getAction() ==  org.bukkit.event.block.Action.RIGHT_CLICK_AIR){
				event.setCancelled(true);
				return;
			}
			Inventory myInventory = Bukkit.getServer().createInventory(p, 9);
			
			File testfile = new File(path + p.getName().toString()+"_inv.yml");
			//p.sendMessage(p.getName());
			//p.sendMessage(p.getName().toString());
			

			if (inventoryContents.containsKey(p.getName().toString())){
				myInventory = inventoryContents.get(p.getName().toString());
				
			} else if (testfile.exists()){
				InventoryFile.getInv(testfile, myInventory);
			}

			
			p.openInventory(myInventory);
			
			InventoryFile.saveInv(myInventory, testfile, true);
			inventoryContents.put(p.getName().toString(), myInventory);
			
			
			
		}
		
		
		
	}
	
	
	
	
}