package com.eamondo2.testplugin;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;



import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;


import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginClass extends JavaPlugin implements Listener {
	ItemStack backpack;
	int count = 0;
	HashMap<Integer, Inventory> inventoryCount = new HashMap<Integer, Inventory>();
	
	String path = ("plugins/BackPack/users" + File.separator);
	File countyml = new File("plugins/BackPack/data" + File.separator + "count.yml");
	
	public void onEnable(){
		
		
		getLogger().info("SimplePack loaded");
		Bukkit.getServer().getPluginManager().registerEvents(this,this);
		backpackrecipe();
		count = (int) InventoryFile.loadFile(countyml);
		
		
		
		
		
	}
	@Override
	public void onDisable(){
		getLogger().info("SimplePack unloaded");
		InventoryFile.saveFile(countyml, count);
		
		
		
		
	}

	

	
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerCraftEvent (CraftItemEvent event){
		if (event.getRecipe().getResult().getType().equals(Material.CHEST)){
			ItemStack stack = event.getCurrentItem();
			ItemMeta meta = stack.getItemMeta();
			
			if (!meta.getDisplayName().equals("BackPack")){
				return;
			}
			count = count+1;
			String lore2 = Integer.toString(count);
			meta.setLore(Arrays.asList("Left Click to open", lore2));
			stack.setItemMeta(meta);
			
			//event.setCancelled(true);
			//Result res = Result.DENY;
			//event.setResult(res);
			event.setCurrentItem(stack);
			
			getLogger().info("BACKPACK CRAFTED");
			HumanEntity entity = event.getWhoClicked();
			Player p = (Player) entity;
			p.updateInventory();

			
			
			
			

			
			
		}
		
		
	}

	
	@EventHandler
	public void playerCloseEvent (InventoryCloseEvent event){
		Player p = (Player) event.getPlayer();
		Inventory closed = event.getInventory();
		
		ItemStack bak = new ItemStack(Material.AIR);
		if (closed.contains(Material.CHEST)){
			if (closed.getName().equals("BackPack")){
				for (ItemStack s : closed){
					if(s.getType().equals(Material.CHEST)){
						if (s.hasItemMeta()){
							ItemMeta meta = s.getItemMeta();
							String case1 = "BackPack";
							if (meta.getDisplayName().equals(case1)){
								
								//getLogger().info("BP INTERCEPT");
								closed.remove(s);
								bak = s;
								p.sendMessage("You cannot put a backpack inside another backpack!");
								break;
							}
							
						}
					}
				}
				
				Location loc = p.getLocation();
				p.getWorld().dropItem(loc, bak);
			}
		}
		
		
	}
	
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
			Inventory myInventory = Bukkit.getServer().createInventory(p, 9, "BackPack");
			
			ItemStack stack = event.getItem();
			ItemMeta meta = stack.getItemMeta();
			List<String> lore = meta.getLore();
			int invnum = Integer.parseInt(lore.get(1));
			
			
			File testfile = new File(path + invnum +"_inv.yml");
			//p.sendMessage(p.getName());
			//p.sendMessage(p.getName().toString());
			

			if (inventoryCount.containsKey(invnum)){
				myInventory = inventoryCount.get(invnum);
				
			} else if (testfile.exists()){
				InventoryFile.getInv(testfile, myInventory);
			}

			
			p.openInventory(myInventory);
			
			InventoryFile.saveInv(myInventory, testfile, true);
			inventoryCount.put(invnum, myInventory);
			
			
			
			
		}
		
		
		
	}
	private void backpackrecipe(){
		String lore2 = Integer.toString(count);
		
		backpack = new ItemStack(Material.CHEST, 1);
		
		ItemMeta meta = backpack.getItemMeta();
		
		
		meta.setDisplayName("BackPack");
		meta.setLore(Arrays.asList("Left Click to open", lore2));
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
	
	
	
	
}