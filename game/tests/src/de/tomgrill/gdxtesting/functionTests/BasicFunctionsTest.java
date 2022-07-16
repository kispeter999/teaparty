package de.tomgrill.gdxtesting.functionTests;

import static org.junit.Assert.*;

import com.mygdx.game.*;
import org.junit.*;

public class BasicFunctionsTest {

    @Test
    public void testName(){
        GameMain testGame = new GameMain();
        assertEquals("The Nameless Park",testGame.getName());
        testGame.setName("The Named Park");
        assertEquals("The Named Park",testGame.getName());
    }

    @Test
    public void testTicket(){
        GameMain testGame = new GameMain();
        assertEquals(20,testGame.getTicketPrice());
        testGame.setTicketPrice(100);
        assertEquals(100,testGame.getTicketPrice());
    }

    /*@Test
    public void testMusic(){
        GameMain testGame = new GameMain();
        assertFalse(testGame.getMusic());
        testGame.setMusic();
        assertTrue(testGame.getMusic());
        testGame.startMusic();
        assertTrue(testGame.menuBgMusic.isPlaying());
        assertTrue(testGame.menuBgMusic.isLooping());
    }*/

    /*@Test
    public void testSFX(){
        GameMain testGame = new GameMain();
        assertTrue(testGame.getSfx());
        testGame.setSfx();
        assertFalse(testGame.getSfx());
    }*/

    @Test
    public void testTheme(){
        GameMain testGame = new GameMain();
        assertEquals(Theme.THEME1,testGame.getTheme());
        testGame.setTheme();
        assertEquals(Theme.THEME2,testGame.getTheme());
        testGame.setTheme();
        assertEquals(Theme.THEME3,testGame.getTheme());
        testGame.setTheme();
        assertEquals(Theme.THEME1,testGame.getTheme());
    }

    @Test
    public void testDifficulty(){
        GameMain testGame = new GameMain();
        assertEquals(Difficulty.EASY,testGame.getDiff());
        testGame.setDiff();
        assertEquals(Difficulty.MEDIUM,testGame.getDiff());
        testGame.setDiff();
        assertEquals(Difficulty.HARD,testGame.getDiff());
        testGame.setDiff();
        assertEquals(Difficulty.EASY,testGame.getDiff());
    }

    @Test
    public void testMoney(){
        GameMain testGame = new GameMain();
        assertEquals(10000,testGame.getMoney());
        testGame.setMoney(500);
        assertEquals(10500,testGame.getMoney());
    }

    @Test
    public void testSalaryJanitor(){
        GameMain testGame = new GameMain();
        assertEquals(125,testGame.getSalaryJanitor());
        testGame.setSalaryJanitor(300);
        assertEquals(425,testGame.getSalaryJanitor());
    }

    @Test
    public void testSalaryRepairMan(){
        GameMain testGame = new GameMain();
        assertEquals(150,testGame.getSalaryRepairMan());
        testGame.setSalaryRepairMan(10);
        assertEquals(160,testGame.getSalaryRepairMan());
    }

    @Test
    public void testVisitorFunctions(){
        GameMain testGame = new GameMain();
        assertEquals(0,testGame.getVisitors().size);
        assertEquals(0,testGame.visitorIDCounter);
        testGame.addVisitor(new Visitor(testGame.visitorIDCounter));
        testGame.addVisitor(new Visitor(testGame.visitorIDCounter));
        assertEquals(2,testGame.getVisitors().size);
        assertEquals(2,testGame.visitorIDCounter);
        testGame.remVisitor(testGame.getVisitors().get(0));
        testGame.addVisitor(new Visitor(testGame.visitorIDCounter));
        assertEquals(2,testGame.getVisitors().size);
        assertEquals(3,testGame.visitorIDCounter);
        assertFalse(testGame.getVisitors().get(1).getOnWayToBuilding());
        assertFalse(testGame.getVisitors().get(1).getInQueue());
        testGame.updateVisitor(testGame.getVisitors().get(1));
        assertTrue(testGame.getVisitors().get(1).getOnWayToBuilding());
        assertTrue(testGame.getVisitors().get(1).getInQueue());
    }

    @Test
    public void testJanitorFunctions(){
        GameMain testGame = new GameMain();
        assertEquals(0,testGame.getJanitors().size);
        assertEquals(0,testGame.janitorIDCounter);
        testGame.addJanitor(new Janitor(testGame.janitorIDCounter));
        testGame.addJanitor(new Janitor(testGame.janitorIDCounter));
        assertEquals(2,testGame.getJanitors().size);
        assertEquals(2,testGame.janitorIDCounter);
        testGame.remJanitor(testGame.getJanitors().get(0));
        testGame.addJanitor(new Janitor(testGame.janitorIDCounter));
        assertEquals(2,testGame.getJanitors().size);
        assertEquals(3,testGame.janitorIDCounter);
    }

    @Test
    public void testRepairmanFunctions(){
        GameMain testGame = new GameMain();
        assertEquals(0,testGame.getRepairMans().size);
        assertEquals(0,testGame.repairManIDCounter);
        testGame.addRepairMan(new RepairMan(testGame.repairManIDCounter));
        testGame.addRepairMan(new RepairMan(testGame.repairManIDCounter));
        assertEquals(2,testGame.getRepairMans().size);
        assertEquals(2,testGame.repairManIDCounter);
        testGame.remRepairMan(testGame.getRepairMans().get(0));
        testGame.addRepairMan(new RepairMan(testGame.repairManIDCounter));
        assertEquals(2,testGame.getRepairMans().size);
        assertEquals(3,testGame.repairManIDCounter);
    }

    @Test
    public void testBuildingFunctions(){
        GameMain testGame = new GameMain();
        assertEquals(0,testGame.getBuildings().size);
        testGame.addBuilding(new Building(Type.COASTER1));
        testGame.addBuilding(new Building(Type.COASTER5));
        assertEquals(2,testGame.getBuildings().size);
        testGame.remBuilding(testGame.getBuildings().get(0));
        testGame.addBuilding(new Building(Type.RESTAURANT1));
        assertEquals(2,testGame.getBuildings().size);
    }

    @Test
    public void testGreenStuffFunctions(){
        GameMain testGame = new GameMain();
        assertEquals(0,testGame.getGreenStuff().size);
        testGame.addGreen(new Green());
        testGame.addGreen(new Green());
        assertEquals(2,testGame.getGreenStuff().size);
        testGame.remGreen(testGame.getGreenStuff().get(0));
        testGame.addGreen(new Green());
        assertEquals(2,testGame.getGreenStuff().size);
    }

    @Test
    public void testDumpsterFunctions(){
        GameMain testGame = new GameMain();
        assertEquals(0,testGame.getDumpsters().size);
        testGame.addDumpster(new Dumpster());
        testGame.addDumpster(new Dumpster());
        assertEquals(2,testGame.getDumpsters().size);
        testGame.remDumpster(testGame.getDumpsters().get(0));
        testGame.addDumpster(new Dumpster());
        assertEquals(2,testGame.getDumpsters().size);
    }

    @Test
    public void testRoadFunctions(){
        GameMain testGame = new GameMain();
        assertEquals(0,testGame.getRoads().size);
        testGame.addRoad(new Road());
        testGame.addRoad(new Road());
        assertEquals(2,testGame.getRoads().size);
        testGame.remRoad(testGame.getRoads().get(0));
        testGame.addRoad(new Road());
        assertEquals(2,testGame.getRoads().size);
    }

    @Test
    public void testGarbageFunctions(){
        GameMain testGame = new GameMain();
        assertEquals(0,testGame.getGarbages().size);
        testGame.addGarbages(new Garbage(123,321));
        testGame.addGarbages(new Garbage(69,69));
        assertEquals(2,testGame.getGarbages().size);
        testGame.remGarbages(testGame.getGarbages().get(0));
        testGame.addGarbages(new Garbage(666,42));
        assertEquals(2,testGame.getGarbages().size);
    }

}
