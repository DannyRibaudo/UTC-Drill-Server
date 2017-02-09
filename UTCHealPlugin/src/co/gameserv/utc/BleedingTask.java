package co.gameserv.utc;

import co.gameserv.utc.UTCHeal;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BleedingTask {
    public static void RepeatEvent(Player player) {
        final Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run() {
                if (UTCHeal.getBleeding(player)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0));
                    player.sendMessage("You are bleeding! Bandage up your wounds!");
                    if (player.getHealth() == 2.0) {
                        player.setHealth(player.getHealth() - 1.0);
                    }
                    if (player.getHealth() >= 3.0) {
                        player.setHealth(player.getHealth() - 2.0);
                    }
                } else {
                    t.cancel();
                }
            }
        }, 0, 35000);
    }

}


