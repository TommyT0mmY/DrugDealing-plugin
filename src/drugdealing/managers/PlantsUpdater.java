package drugdealing.managers;

import org.bukkit.scheduler.BukkitRunnable;

import drugdealing.Main;

public class PlantsUpdater extends BukkitRunnable {
	private Main mainClass;
	public PlantsUpdater(Main mainClass) {
		this.mainClass = mainClass;
	}
	
	@Override
	public void run() {
		mainClass.console.info("Plants Updater Tick");
		
		
		
		
	}
}
