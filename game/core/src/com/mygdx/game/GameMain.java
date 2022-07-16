package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Random;

/**
 * Aktuális játékot és az adatait reprezentáló osztály
 */
public class GameMain extends Game {


	private String parkName = "The Nameless Park";
	private int ticketPrice = 20;

	private boolean playMusic = false;
	private boolean playSfx = true;
	private Theme theme = Theme.THEME1;
	private Difficulty diff = Difficulty.EASY;

	private int salaryJanitor = 125;
	private int salaryRepairMan = 150;
	private int money = 10000;

	private Array<Visitor> visitors = new Array<>();
	public int visitorIDCounter = 0;
	private Array<Janitor> janitors = new Array<>();
	public int janitorIDCounter = 0;
	private Array<RepairMan> repairMans = new Array<>();
	public int repairManIDCounter = 0;

	private Array<Building> buildings = new Array<>();
	private Array<Road> roads = new Array<>();
	private Array<Green> greenStuff = new Array<>();
	private Array<Dumpster> dumpsters = new Array<>();
	private Array<Garbage> garbages = new Array<>();

	public Music menuBgMusic;
	private Sound greenerySound1;
	private Sound greenerySound2;

	@Override
	public void create() {
		menuBgMusic = Gdx.audio.newMusic(Gdx.files.internal("forarax.mp3"));
		greenerySound1 = Gdx.audio.newSound(Gdx.files.internal("nice.mp3"));
		greenerySound2 = Gdx.audio.newSound(Gdx.files.internal("ohye.mp3"));

		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose() {
		menuBgMusic.dispose();
	}

	public String getName()   { return this.parkName; }
	public void setName(String newName)   { this.parkName = newName; }

	public int getTicketPrice()	{ return this.ticketPrice; }
	public void setTicketPrice(int newPrice) { this.ticketPrice = newPrice; }

	public boolean getMusic()   { return this.playMusic; }
	public void setMusic()   { this.playMusic = !this.playMusic; }

	public boolean getSfx()   { return this.playSfx; }
	public void setSfx()   { this.playSfx = !this.playSfx; }

	public Difficulty getDiff() { return this.diff; }
	public void setDiff()   { this.diff = this.diff.next(); }

	public Theme getTheme()   { return this.theme; }
	public void setTheme()   { this.theme = this.theme.next(); }

	public int getMoney()   { return this.money; }
	public void setMoney(int amount)   { this.money += amount; }

	public int getSalaryJanitor() {
		return this.salaryJanitor;
	}
	public void setSalaryJanitor(int amount)   { this.salaryJanitor += amount; }

	public int getSalaryRepairMan() {
		return this.salaryRepairMan;
	}
	public void setSalaryRepairMan(int amount)   { this.salaryRepairMan += amount; }

	public Array<Visitor> getVisitors() {
		return visitors;
	}
	public void addVisitor(Visitor visitor) { this.visitors.add(visitor); visitorIDCounter++; }
	public void remVisitor(Visitor curr) {
		visitors.removeValue(curr,true);
	}
	public void updateVisitor(Visitor curr) {
		for (int i = 0; i < visitors.size; i++){
			if (visitors.get(i).getID() == curr.getID()){
				visitors.get(i).toggleInQueue();
				visitors.get(i).toggleOnWayToBuilding();
			}
		}
	}

	public Array<Janitor> getJanitors() {
		return janitors;
	}
	public void addJanitor(Janitor janitor) { this.janitors.add(janitor); janitorIDCounter++; }
	public void remJanitor(Janitor curr) {
		janitors.removeValue(curr,true);
	}

	public Array<RepairMan> getRepairMans() {
		return repairMans;
	}
	public void addRepairMan(RepairMan repairman) { this.repairMans.add(repairman); repairManIDCounter++; }
	public void remRepairMan(RepairMan curr) {
		repairMans.removeValue(curr,true);
	}

	public Array<Building> getBuildings() { return buildings; }
	public void addBuilding(Building building) { this.buildings.add(building); }
	public void remBuilding(Building curr) {
		buildings.removeValue(curr,true);
	}

	public Array<Green> getGreenStuff() { return greenStuff; }
	public void addGreen(Green green) { this.greenStuff.add(green); }
	public void remGreen(Green curr) {
		greenStuff.removeValue(curr,true);
	}

	public Array<Dumpster> getDumpsters() { return dumpsters; }
	public void addDumpster(Dumpster dumpster) { this.dumpsters.add(dumpster); }
	public void remDumpster(Dumpster curr) {
		dumpsters.removeValue(curr,true);
	}

	public Array<Road> getRoads() { return roads; }
	public void addRoad(Road road) { this.roads.add(road); }
	public void remRoad(Road curr) {
		roads.removeValue(curr,true);
	}

	public Array<Garbage> getGarbages() { return garbages; }
	public void addGarbages(Garbage garbage) { this.garbages.add(garbage); }
	public void remGarbages(Garbage curr) {
		garbages.removeValue(curr,true);
	}

	public void startMusic() {
		if (this.playMusic && !menuBgMusic.isPlaying()) {
		menuBgMusic.play();
		menuBgMusic.setLooping(true);
		}
	}
	public void toggleMusic()
	{
		if (menuBgMusic.isPlaying()) { menuBgMusic.pause(); this.playMusic = false; }
		else { menuBgMusic.play(); this.playMusic = true;}
	}

	public void playGreenerySound(){
		Random random = new Random();
		if(random.nextDouble() < 0.006){
			if(random.nextDouble() > 0.6){
				greenerySound1.play(0.02F);
			}else{
				greenerySound2.play(0.01F);
			}
		}
	}

}