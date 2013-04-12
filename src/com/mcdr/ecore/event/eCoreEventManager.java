package com.mcdr.ecore.event;

public abstract class eCoreEventManager {	
	public static void runEvent(EventType event){
		switch(event){
		case FINALRETREAT:
			runFinalRetreat();
			break;
		case FIRSTRETREAT:
			runTemporaryRetreat();
			break;
		default:
			break;
		}
	}
	
	private static void runFinalRetreat() {
		//TODO Run final event
	}
	
	private static void runTemporaryRetreat(){
		//TODO Run middle event
	}

	public static enum EventType{
		FIRSTRETREAT,
		FINALRETREAT;
	}
}
