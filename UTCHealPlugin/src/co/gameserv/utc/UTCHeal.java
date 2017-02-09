package co.gameserv.utc;

import co.gameserv.utc.BleedingTask;
import co.gameserv.utc.Main;
import co.gameserv.utc.events.BleedingCustomEvent;
import co.gameserv.utc.events.HealOtherCustomEvent;
import co.gameserv.utc.events.SelfHealCustomEvent;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UTCHeal
implements Listener {
    private Plugin p;
    private HashMap<UUID, Long> redDye = new HashMap();
    private HashMap<UUID, Long> paper = new HashMap();
    private HashMap<UUID, Long> lastHeal = new HashMap();
    public static HashMap<UUID, Boolean> bleeding = new HashMap();
    Random ran = new Random();

    public UTCHeal(Main plugin) {
        plugin = (Main)this.p;
    }

    @EventHandler
    public void Heal_Self(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getType() == Material.PAPER && player.getHealth() < player.getMaxHealth() && player.hasPermission("UTCheal.self")) {
                if (player.getHealth() == 19.0) {
                    player.setHealth(player.getHealth() + 1.0);
                } else {
                    player.setHealth(player.getHealth() + 2.0);
                }
                player.getInventory().removeItem(new ItemStack[]{player.getInventory().getItemInHand()});
                if (bleeding.containsKey(player.getUniqueId())) {
                    bleeding.remove(player.getUniqueId());
                    player.sendMessage((Object)ChatColor.GREEN + "You have bandaged yourself and stopped the bleeding.");
                    Bukkit.getServer().getPluginManager().callEvent((Event)new SelfHealCustomEvent(player, player.getHealth() - 2.0, player.getHealth()));
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new SelfHealCustomEvent(player, player.getHealth() - 2.0, player.getHealth()));
                player.sendMessage((Object)ChatColor.GREEN + "You have bandaged yourself.");
            } else {
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventHandler(priority=EventPriority.HIGH)
    public void Heal_Other(EntityDamageByEntityEvent event) {
        long current = System.currentTimeMillis();
        Entity ent1 = event.getDamager();
        Entity ent2 = event.getEntity();
        System.out.println((Object)ent1);
        System.out.println((Object)ent2);
        if (!(ent2 instanceof Player)) return;
        if (!(ent1 instanceof Player)) {
            return;
        }
        Player player = (Player)ent1;
        Player interacted = (Player)ent2;
        Material type = player.getItemInHand().getType();
        ItemStack Red_Dye = new ItemStack(Material.INK_SACK, 1);
        if (player.getItemInHand().getType() == Material.SHEARS) {
            event.setCancelled(true);
            if (!this.paper.containsKey(interacted.getUniqueId())) {
                double playerHealth = interacted.getHealth();
                int intHealth = (int)playerHealth;
                player.sendMessage((Object)ChatColor.LIGHT_PURPLE + interacted.getName() + " has " + intHealth + "/20 health.");
                return;
            }
            if (this.lastHeal.containsKey(interacted.getUniqueId()) && this.lastHeal.get(interacted.getUniqueId()) > current) {
                player.sendMessage("You cannot heal " + interacted.getName() + " for another " + (this.lastHeal.get(interacted.getUniqueId()) - current) / 1000 + " seconds.");
                interacted.sendMessage("You cannot be healed for another " + (this.lastHeal.get(interacted.getUniqueId()) - current) / 1000 + " seconds.");
                this.paper.remove(interacted.getUniqueId());
                this.redDye.remove(interacted.getUniqueId());
                return;
            }
            System.out.println(this.paper.get(interacted.getUniqueId()));
            System.out.println(current);
            if (this.paper.get(interacted.getUniqueId()) < current) {
                player.sendMessage("You took too long to heal.");
                this.paper.remove(interacted.getUniqueId());
                this.redDye.remove(interacted.getUniqueId());
                return;
            }
            int duration = 200;
            boolean ointment = false;
            if (this.redDye.containsKey(interacted.getUniqueId())) {
                duration = 300;
                ointment = true;
                if (ointment) {
                    //ParticleEffect.HEART.display(0.5f, 0.2f, 0.5f, 8.0f, 10, interacted.getLocation().add(0.0, 1.25, 0.0), 15.0);
                    this.redDye.remove(interacted.getUniqueId());
                }
            }
            this.lastHeal.put(interacted.getUniqueId(), current + 300000);
            interacted.sendMessage((Object)ChatColor.GREEN + "You have been healed by " + player.getName() + " .");
            player.sendMessage((Object)ChatColor.GREEN + "You have healed " + interacted.getName() + " .");
            interacted.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, 0));
            //ParticleEffect.SPELL.display(0.5f, 0.2f, 0.5f, 10.0f, 10, interacted.getLocation().add(0.0, 1.25, 0.0), 15.0);
            this.paper.remove(interacted.getUniqueId());
            Bukkit.getServer().getPluginManager().callEvent((Event)new HealOtherCustomEvent(player, interacted));
            return;
        }
        if (type == Red_Dye.getType()) {
            event.setCancelled(true);
            if (this.redDye.containsKey(interacted.getUniqueId()) && this.redDye.get(interacted.getUniqueId()) >= current) {
                if (this.redDye.get(interacted.getUniqueId()) <= current) return;
                player.sendMessage((Object)ChatColor.YELLOW + "You already applied ointment!");
                return;
            }
            this.redDye.put(interacted.getUniqueId(), current + 2000);
            player.sendMessage((Object)ChatColor.RED + "Applied ointment.");
            return;
        }
        if (type != Material.PAPER) return;
        event.setCancelled(true);
        if (this.paper.containsKey(interacted.getUniqueId()) && this.paper.get(interacted.getUniqueId()) >= current) {
            if (this.paper.get(interacted.getUniqueId()) <= current) return;
            player.sendMessage((Object)ChatColor.YELLOW + "You already applied a bandage!");
            return;
        }
        this.paper.put(interacted.getUniqueId(), current + 2000);
        player.sendMessage((Object)ChatColor.RED + "Applied bandage.");
    }

    @EventHandler
    public void Bleeding(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getEntity();
        int chanceBleed = this.ran.nextInt(101) + 1;
        if (bleeding.containsKey(player.getUniqueId())) {
            return;
        }
        if (chanceBleed <= 3) {
            bleeding.put(player.getUniqueId(), true);
            Bukkit.getServer().getPluginManager().callEvent((Event)new BleedingCustomEvent(player));
            BleedingTask.RepeatEvent(player);
        }
    }

    public static boolean getBleeding(Player player) {
        boolean Contains = false;
        if (bleeding.containsKey(player.getUniqueId())) {
            Contains = true;
        }
        return Contains;
    }

    @EventHandler
    public void OnDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (bleeding.containsKey(player.getUniqueId())) {
            bleeding.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (bleeding.containsKey(player.getUniqueId())) {
            BleedingTask.RepeatEvent(player);
        }
    }
}