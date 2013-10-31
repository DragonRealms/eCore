package com.mcdr.ecore.task;

import com.mcdr.ecore.eCore;
import com.mcdr.ecore.eLogger;

public abstract class TaskManager {
	public static Integer drawFlameTaskId = null;
	public static void start() {
		if(eCore.server.getPlayer(eCore.name)!=null)
			startFlameEffect();
		scheduleSyncRepeatingTask(new NightTask(), 5.0);
		scheduleSyncRepeatingTask(new DataSaveTask(), 60.0);
		eLogger.d("Started all tasks");
	}
	
	
	private static int scheduleSyncRepeatingTask(BaseTask baseTask, double period) {
		if (period > 0) {
			long periodInTicks = (long) (period * 20);
			return eCore.scheduler.scheduleSyncRepeatingTask(eCore.in, baseTask, periodInTicks, periodInTicks);
		}
		else return -1;
	}
	
	public static void stop() {
		eCore.scheduler.cancelTasks(eCore.in);
		eLogger.d("Stopped all tasks");
	}
	
	public static void startFlameEffect(){
		if(drawFlameTaskId == null || drawFlameTaskId == -1){
			drawFlameTaskId = scheduleSyncRepeatingTask(new DrawFlameEffect(), 0.5);
			eLogger.d("Started FlameEffectTask");
		}
	}
	
	public static void stopFlameEffect(){
		if(drawFlameTaskId != null){
			eCore.scheduler.cancelTask(drawFlameTaskId);
			drawFlameTaskId = null;
			eLogger.d("Stopped FlameEffectTask");
		}
	}
	
	public static void restart() {
		stop();
		start();
	}
}

abstract class BaseTask implements Runnable {}
