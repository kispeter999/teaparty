package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A játékmenet képernyőjét megvalósító osztály
 */
public class GameScreen implements Screen {

    final private GameMain game;
    final private Stage stage;
    final private SpriteBatch batch;
    private final OrthographicCamera camera;
    final private AssetManager assetManager;
    private final RoadFinder roadFinder;
    private static final ShapeRenderer debugRenderer = new ShapeRenderer();

    private String menuType = "Nothing";
    private String clickType = "Nothing";
    private boolean demolish = false;
    private Type clickBuildingType = null;
    private int clickCost = 0;
    private boolean open = false;

    private long lastSpawnTime;
    private long lastStepTime;
    private long lastUpkeepTime;
    private long lastTurnTime;
    private long lastJanitorTime;
    private long lastBreakTime;

    private LocalDateTime startTime;

    public GameScreen(GameMain gam) {
        startTime = LocalDateTime.now();
        this.game = gam;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        batch = new SpriteBatch();
        assetManager = new AssetManager();
        roadFinder = new RoadFinder();

        Road gate = new Road();
        gate.x = 0;
        gate.y = 1020;
        game.addRoad(gate);
        roadFinder.addRoad(gate);
    }

    @Override
    public void show() {
        final Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        final Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm1.setColor(Color.GRAY);
        pm1.fill();

        final Table topMenu = new Table();
        topMenu.setName("topMenu");
        topMenu.add();
        table.add(topMenu).width(1920).height(1000).align(Align.top);

        table.row().expandY();

        final Table bottomMenu = new Table();
        bottomMenu.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));
        bottomMenu.setName("bottomMenu");
        TextButton start = new TextButton("Open Park!", skin);
        TextButton build = new TextButton("Build", skin);
        TextButton hire = new TextButton("Hire", skin);
        TextButton personnel = new TextButton("Management", skin);
        TextButton exit = new TextButton("Menu", skin);
        Label visitors = new Label("Visitors: " + game.getVisitors().size, skin);
        visitors.setName("visitors");
        final Label money = new Label("Money: " + game.getMoney() + "$", skin);
        money.setName("money");

        table.add(bottomMenu).width(1920).height(60).align(Align.bottom);
        bottomMenu.add(start).width(130).height(50).pad(60);
        bottomMenu.add(build).width(100).height(50).pad(60);
        bottomMenu.add(hire).width(100).height(50).pad(60);
        bottomMenu.add(personnel).width(130).height(50).pad(60);
        bottomMenu.add(exit).width(100).height(50).pad(60);
        bottomMenu.add(visitors).width(100).height(60).pad(60);
        bottomMenu.add(money).width(100).height(60).pad(60);

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickType = "Nothing";
                clickBuildingType = null;
                demolish = false;
                clickCost = 0;
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                menuType = "Nothing";
                topMenu.getCells().get(0).setActor(null);
                open = true;
                bottomMenu.removeActorAt(0, true);
            }
        });
        build.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (menuType.equals("Build")) {
                    topMenu.getCells().get(0).setActor(null);
                    menuType = "Nothing";
                } else {
                    clickType = "Nothing";
                    clickBuildingType = null;
                    demolish = false;
                    clickCost = 0;
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                    menuType = "Build";
                    addBuildMenu(topMenu, skin);
                }
            }
        });
        hire.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (menuType.equals("Hire")) {
                    topMenu.getCells().get(0).setActor(null);
                    menuType = "Nothing";
                } else {
                    clickType = "Nothing";
                    clickBuildingType = null;
                    demolish = false;
                    clickCost = 0;
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                    menuType = "Hire";
                    addHireMenu(topMenu, skin);
                }
            }
        });
        personnel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (menuType.equals("Stats")) {
                    topMenu.getCells().get(0).setActor(null);
                    menuType = "Nothing";
                } else {
                    clickType = "Nothing";
                    clickBuildingType = null;
                    demolish = false;
                    clickCost = 0;
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                    menuType = "Stats";
                    addStatsMenu(topMenu, skin);
                }
            }
        });
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (menuType.equals("Exit")) {
                    topMenu.getCells().get(0).setActor(null);
                    menuType = "Nothing";
                } else {
                    clickType = "Nothing";
                    clickBuildingType = null;
                    demolish = false;
                    clickCost = 0;
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                    menuType = "Exit";
                    addExitMenu(topMenu, skin);
                }
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Vector3 hoverPos = new Vector3();
        hoverPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(hoverPos);
        boolean blocked = false;
        int xCord = Math.floorDiv((int) hoverPos.x, 60) * 60;
        int yCord = Math.floorDiv((int) hoverPos.y, 60) * 60;

        if (yCord <= 0) {
            blocked = true;
        }
        if (yCord >= 1020 && clickBuildingType != null) {
            blocked = true;
        }
        if (yCord >= 1080) {
            blocked = true;
        }
        if (xCord >= 1860 && clickBuildingType != null) {
            blocked = true;
        }
        if (game.getMoney() < clickCost) {
            blocked = true;
        }

        if (demolish) {
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(assetManager.pm, 0, 0));
        }

        //TODO timeolt cuccok megnyitás után, template kb az hogy egy if-be "currTime - last_Time > 1000000000 * ahány mp-ként kell", last_Time-ot felvenni felül és updatelni mindig
        if (open) {

            long currTime = TimeUtils.nanoTime();

            if (currTime - lastBreakTime > 1000000000) {
                for (Building curr : game.getBuildings()) {
                    Random random = new Random();
                    if (random.nextInt(100) + 1 < curr.getType().durability) {
                        while (curr.getQueue().notEmpty()) {
                            game.updateVisitor(curr.getQueue().pop());
                        }
                        for (Road hidden : game.getRoads()) {
                            if (hidden.x == curr.x && hidden.y == curr.y && hidden.isHidden() && hidden.getState() == State.WORKING) {
                                hidden.setState(State.BROKE_DOWN);
                                visitorEmergencyMove((int)curr.x, (int)curr.y, hidden);
                            }
                        }
                    }
                }
                lastBreakTime = TimeUtils.nanoTime();
            }

            if (currTime - lastJanitorTime > 1000000000 * 0.2){
                for(Janitor janitor : game.getJanitors()){
                    for(Garbage garbage : game.getGarbages()){
                        if(janitor.x == garbage.getX() && janitor.y == garbage.getY()
                        /*|| janitor.x == garbage.getX() + 60 && janitor.y == garbage.getY()
                        || janitor.x == garbage.getX() - 60 && janitor.y == garbage.getY()
                        || janitor.x == garbage.getX() && janitor.y == garbage.getY() + 60
                        || janitor.x == garbage.getX() && janitor.y == garbage.getY() - 60*/){
                            game.remGarbages(garbage);
                        }
                    }
                    for(Road road : game.getRoads()){
                        if(janitor.x == road.getX() -60 && janitor.y == road.getY()){
                           Random random = new Random();
                           if(random.nextDouble() < 0.1){
                               janitor.x += 60;
                           }
                        }else if(janitor.x == road.getX() +60 && janitor.y == road.getY()){
                            Random random = new Random();
                            if(random.nextDouble() < 0.1){
                                janitor.x -= 60;
                            }
                        }else if(janitor.x == road.getX() && janitor.y == road.getY() - 60){
                            Random random = new Random();
                            if(random.nextDouble() < 0.1){
                                janitor.y += 60;
                            }
                        }else if(janitor.x == road.getX()&& janitor.y == road.getY() + 60){
                            Random random = new Random();
                            if(random.nextDouble() < 0.1){
                                janitor.y -= 60;
                            }
                        }
                    }
                }
                lastJanitorTime = TimeUtils.nanoTime();
            }

            //Visitor spawnolás
            if (currTime - lastSpawnTime > 1000000000 * 0.4 && game.getVisitors().size < 50) {
                Visitor newVisitor = new Visitor(game.visitorIDCounter);
                newVisitor.x = 0;
                newVisitor.y = 1020;
                game.addVisitor(newVisitor);
                game.setMoney(game.getTicketPrice());
                lastSpawnTime = TimeUtils.nanoTime();
            }

            //Visitor útkeresés (ha nincs 1 épület sem / nem elérhető egy sem akkor semmit nem csinál)
            for (Visitor curr : game.getVisitors()) {
                if (curr.getDestination() == null && game.getBuildings().size > 0 && !curr.getInQueue()) {
                    Array<Road> hiddenRoads = new Array<>();
                    Road start = new Road();
                    for (Road currRoad : game.getRoads()) {
                        if (currRoad.x == curr.x && currRoad.y == curr.y) {
                            start = currRoad;
                        }
                        if (currRoad.isHidden() && currRoad.getState() == State.WORKING) {
                            hiddenRoads.add(currRoad);
                        }
                    }
                    if (hiddenRoads.size > 0) {
                        Random random = new Random();
                        Road goal = hiddenRoads.get(random.nextInt(hiddenRoads.size));
                        curr.setDestination(roadFinder.findPath(start, goal));
                        if (curr.getDestination() != null) {
                            for (Building currBuild : game.getBuildings()) {
                                if (currBuild.x == goal.x && currBuild.y == goal.y) {
                                    curr.setDestinationType(currBuild.getType());
                                }
                            }
                            curr.toggleOnWayToBuilding();
                        }
                    }
                }
            }

            for (RepairMan curr : game.getRepairMans()) {
                if (curr.getDestination() == null && curr.getTR() == 0 && game.getBuildings().size > 0) {
                    Array<Road> hiddenRoads = new Array<>();
                    Road start = new Road();
                    for (Road currRoad : game.getRoads()) {
                        if (currRoad.x == curr.x && currRoad.y == curr.y) {
                            start = currRoad;
                        }
                        if (currRoad.isHidden() && currRoad.getState() == State.BROKE_DOWN) {
                            hiddenRoads.add(currRoad);
                        }
                    }
                    if (hiddenRoads.size > 0) {
                        Random random = new Random();
                        Road goal = hiddenRoads.get(random.nextInt(hiddenRoads.size));
                        game.remRoad(goal);
                        goal.setState(State.REPAIRING);
                        game.addRoad(goal);
                        curr.setDestination(roadFinder.findPath(start, goal));
                        if (curr.getDestination() != null) {
                            for (Building currBuild : game.getBuildings()) {
                                if (currBuild.x == goal.x && currBuild.y == goal.y) {
                                    curr.setTR(currBuild.getType().durability + 1);
                                }
                            }
                        } else {
                            game.remRoad(goal);
                            goal.setState(State.BROKE_DOWN);
                            game.addRoad(goal);
                        }
                    }
                }
            }

            //Visitor lépegetés (ha nincs úti cél a fentiek miatt, akkor semmit sem csinál)
            if (currTime - lastStepTime > 1000000000 * 0.2) {
                for (Visitor visitor : game.getVisitors()) {
                    if (visitor.getDestination() != null) {
                        if (visitor.getDestination().getCount() > visitor.getWhere()) {
                            Road next = visitor.getDestination().get(visitor.getWhere());
                            visitor.x = next.x;
                            visitor.y = next.y;
                            visitor.setWhere(1);
                        } else {
                            for (Building currBuilding : game.getBuildings()) {
                                if (visitor.x == currBuilding.x && visitor.y == currBuilding.y && !visitor.getInQueue()) {
                                    game.updateVisitor(visitor);
                                    currBuilding.addToQueue(visitor);
                                    //TODO tehát itt most ez elvileg működik de csak úgy hogy feltettem ezeket ide mert lent megint a Buildingben és nem a Visitorsben levő példány volt updatelve (asszem)
                                    if (visitor.getDestinationType() == Type.RESTAURANT1) {
                                        visitor.setHunger(visitor.getDestinationType().value);
                                        visitor.setGarbage(true);
                                    } else {
                                        visitor.setAttitude(visitor.getDestinationType().value);
                                    }
                                    game.setMoney(visitor.getDestinationType().price);
                                    visitor.setWhere(-visitor.getWhere());
                                    visitor.setDestination(null);
                                    visitor.setDestinationType(null);
                                }
                            }
                        }
                    }
                    for (Garbage trash : game.getGarbages()) {
                        if (visitor.x == trash.x && visitor.y == trash.y) {
                            visitor.setAttitude(-1);
                        }
                    }
                    for (Green currGreen : game.getGreenStuff()) {
                        if ((visitor.x + 60 == currGreen.x && visitor.y == currGreen.y)
                                || (visitor.x - 60 == currGreen.x && visitor.y == currGreen.y)
                                || (visitor.x == currGreen.x && visitor.y + 60 == currGreen.y)
                                || (visitor.x == currGreen.x && visitor.y - 60 == currGreen.y)) {
                            visitor.setAttitude(2);
                            game.playGreenerySound();
                            //Kappa
                        }
                    }
                    for (Dumpster currDump : game.getDumpsters()) {
                        if ((visitor.x + 60 == currDump.x && visitor.y == currDump.y)
                                || (visitor.x - 60 == currDump.x && visitor.y == currDump.y)
                                || (visitor.x == currDump.x && visitor.y + 60 == currDump.y)
                                || (visitor.x == currDump.x && visitor.y - 60 == currDump.y)) {
                            visitor.setGarbage(false);
                        }
                    }
                    if (visitor.getGarbage()) {
                        boolean onRoad = true;
                        for (Building building : game.getBuildings()) {
                            if (building.x == visitor.x && building.y == visitor.y) {
                                onRoad = false;
                                break;
                            }
                        }
                        Random random = new Random();
                        if (random.nextInt(100) + 1 > 80 && onRoad) {
                            game.addGarbages(new Garbage(visitor.x, visitor.y));
                            visitor.setGarbage(false);
                        }
                    }
                    visitor.setAttitude(-1);
                    visitor.setHunger(-1);
                    if (visitor.getAttitude() <= 0) {
                        game.remVisitor(visitor);
                    } else if (visitor.getHunger() <= 0) {
                        game.remVisitor(visitor);
                    }
                }
                for (RepairMan repairMan : game.getRepairMans()) {
                    if (repairMan.getDestination() != null) {
                        if (repairMan.getDestination().getCount() > repairMan.getWhere()) {
                            Road next = repairMan.getDestination().get(repairMan.getWhere());
                            repairMan.x = next.x;
                            repairMan.y = next.y;
                            repairMan.setWhere(1);
                        } else {
                            repairMan.setWhere(-repairMan.getWhere());
                            repairMan.setDestination(null);
                        }
                    }
                }
                lastStepTime = TimeUtils.nanoTime();
            }

            if (currTime - lastUpkeepTime > 1000000000 * 2.0) {
                for (Building curr : game.getBuildings()) {
                    game.setMoney(-curr.getType().upkeep);
                }
                game.setMoney(-game.getJanitors().size * game.getSalaryJanitor());
                game.setMoney(-game.getRepairMans().size * game.getSalaryRepairMan());
                lastUpkeepTime = TimeUtils.nanoTime();
            }

            if (currTime - lastTurnTime > 1000000000) {
                for (Building currBuilding : game.getBuildings()) {
                    if (currBuilding.getTurnsPlayed() < currBuilding.getType().turnTime) {
                        currBuilding.incTurnsPlayed();
                    } else {
                        currBuilding.resetTurnsPlayed();
                        int i = 0;
                        while (i < currBuilding.getType().capacity && currBuilding.getQueue().notEmpty()) {
                            game.updateVisitor(currBuilding.getQueue().pop());
                            i++;
                        }
                    }
                }
                for (RepairMan repairMan : game.getRepairMans()) {
                    if (repairMan.getDestination() == null && repairMan.getTR() > 0) {
                        repairMan.setTR(-1);
                    } else if (repairMan.getTR() == 0) {
                        for (Road currCurrCurrragtaqgaeghioagagagag : game.getRoads()) { //Mental breakdown
                            if (repairMan.x == currCurrCurrragtaqgaeghioagagagag.x && repairMan.y == currCurrCurrragtaqgaeghioagagagag.y) {
                                currCurrCurrragtaqgaeghioagagagag.setState(State.WORKING);
                            }
                        }
                    }
                }
                lastTurnTime = TimeUtils.nanoTime();
            }
        }

        //Kirajzolás kezdete --------------------------------------------------------------------------------------------------------------

        batch.begin();

        for (int i = 0; i <= 1860; i += 60) {
            for (int j = 0; j <= 1020; j += 60) {
                batch.draw(assetManager.ground, i, j);
            }
        }

        for (Road curr : game.getRoads()) {
            batch.draw(assetManager.gameTextures.get(Road.getPath()), curr.x, curr.y);
            if ((curr.x == xCord && curr.y == yCord)
                    || (curr.x - 60 == xCord && curr.y == yCord && clickBuildingType != null)
                    || (curr.x == xCord && curr.y - 60 == yCord && clickBuildingType != null)
                    || (curr.x - 60 == xCord && curr.y - 60 == yCord && clickBuildingType != null)) {
                blocked = true;
            }
        }

        for (Building curr : game.getBuildings()) {
            batch.draw(assetManager.gameTextures.get(curr.getType().path), curr.x, curr.y);
            if ((curr.x == xCord && curr.y == yCord)
                    || (curr.x + 60 == xCord && curr.y == yCord)
                    || (curr.x - 60 == xCord && curr.y == yCord && clickBuildingType != null)
                    || (curr.x == xCord && curr.y + 60 == yCord)
                    || (curr.x == xCord && curr.y - 60 == yCord && clickBuildingType != null)
                    || (curr.x + 60 == xCord && curr.y + 60 == yCord)
                    || (curr.x - 60 == xCord && curr.y - 60 == yCord && clickBuildingType != null)
                    || (curr.x + 60 == xCord && curr.y - 60 == yCord && clickBuildingType != null)
                    || (curr.x - 60 == xCord && curr.y + 60 == yCord && clickBuildingType != null)) {
                blocked = true;
            }

            for (Visitor currVisitor : curr.getQueue()) {
                batch.draw(assetManager.visitorTextures.get(currVisitor.getPath()), currVisitor.x + currVisitor.getOffsetX(), currVisitor.y + currVisitor.getOffsetY());
            }
        }

        for (Green greenery : game.getGreenStuff()) {
            batch.draw(assetManager.greenTextures.get(greenery.getPath()), greenery.x, greenery.y);
            if ((greenery.x == xCord && greenery.y == yCord)
                    || (greenery.x - 60 == xCord && greenery.y == yCord && clickBuildingType != null)
                    || (greenery.x == xCord && greenery.y - 60 == yCord && clickBuildingType != null)
                    || (greenery.x - 60 == xCord && greenery.y - 60 == yCord && clickBuildingType != null)) {
                blocked = true;
            }
        }

        for (Dumpster curr : game.getDumpsters()) {
            batch.draw(assetManager.gameTextures.get(Dumpster.getPath()), curr.x, curr.y);
            if ((curr.x == xCord && curr.y == yCord)
                    || (curr.x - 60 == xCord && curr.y == yCord && clickBuildingType != null)
                    || (curr.x == xCord && curr.y - 60 == yCord && clickBuildingType != null)
                    || (curr.x - 60 == xCord && curr.y - 60 == yCord && clickBuildingType != null)) {
                blocked = true;
            }
        }

        for (Garbage curr : game.getGarbages()) {
            batch.draw(assetManager.trash, curr.x + curr.getOffsetX(), curr.y + curr.getOffsetY());
        }

        for (Visitor curr : game.getVisitors()) {
            if (!curr.getInQueue()) {
                batch.draw(assetManager.visitorTextures.get(curr.getPath()), curr.x + curr.getOffsetX(), curr.y + curr.getOffsetY());
            }
        }
        for (Janitor curr : game.getJanitors()) {
            batch.draw(assetManager.gameTextures.get(curr.getPath()), curr.x, curr.y);
        }
        for (RepairMan curr : game.getRepairMans()) {
            batch.draw(assetManager.gameTextures.get(curr.getPath()), curr.x, curr.y);
        }

        /*
        for (Road curr : game.getRoads()) {
            if (curr.getState() != State.WORKING) {
                batch.draw(assetManager.debug, curr.x, curr.y);
            }
        }
        */

        if (!clickType.equals("Nothing")) {
            if (blocked) {
                batch.draw(assetManager.blockedSmall, xCord, yCord);
            } else {
                batch.draw(assetManager.okaySmall, xCord, yCord);
            }
        }
        if (clickType.equals("Janitor")) {
            boolean canAddJanitor = true;
            for (Road curr : game.getRoads()) {
                if (curr.x == xCord && curr.y == yCord && !curr.isHidden()) {
                    for (Janitor currJanitor : game.getJanitors()) {
                        if (currJanitor.x == xCord && currJanitor.y == yCord) {
                            canAddJanitor = false;
                            batch.draw(assetManager.blockedSmall, xCord, yCord);
                            break;
                        }
                    }
                    if (canAddJanitor) {
                        batch.draw(assetManager.okaySmall, xCord, yCord);
                    } else {
                        batch.draw(assetManager.blockedSmall, xCord, yCord);
                    }
                    break;
                } else {
                    batch.draw(assetManager.blockedSmall, xCord, yCord);
                }
            }
        }
        if (clickType.equals("Repairman")) {
            boolean canAddRepairMan = true;
            for (Road curr : game.getRoads()) {
                if (curr.x == xCord && curr.y == yCord && !curr.isHidden()) {
                    for (RepairMan currRepairMan : game.getRepairMans()) {
                        if (currRepairMan.x == xCord && currRepairMan.y == yCord) {
                            canAddRepairMan = false;
                            batch.draw(assetManager.blockedSmall, xCord, yCord);
                            break;
                        }
                    }
                    if (canAddRepairMan) {
                        batch.draw(assetManager.okaySmall, xCord, yCord);
                    } else {
                        batch.draw(assetManager.blockedSmall, xCord, yCord);
                    }
                    break;
                } else {
                    batch.draw(assetManager.blockedSmall, xCord, yCord);
                }
            }
        }
        if (clickBuildingType != null) {
            if (blocked) {
                batch.draw(assetManager.blockedLarge, xCord, yCord);
            } else {
                batch.draw(assetManager.okayLarge, xCord, yCord);
            }
        }

        batch.end();

        //Kirajzolás vége --------------------------------------------------------------------------------------------------------------

        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Road curr : roadFinder.roads) {
            for (Connection<Road> conn : roadFinder.getConnections(curr)) {
                Gdx.gl.glLineWidth(2);
                debugRenderer.setProjectionMatrix(camera.combined);
                debugRenderer.setColor(Color.RED);
                debugRenderer.line(new Vector2(curr.x + 30, curr.y + 30), new Vector2(conn.getToNode().x + 30, conn.getToNode().y + 30));
            }
        }
        debugRenderer.end();

        //Eseménykezelés kezdete -------------------------------------------------------------------------------------------------------

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && clickType.equals("Janitor")) {
            for (Road curr : game.getRoads()) {
                if (curr.x == xCord && curr.y == yCord && !curr.isHidden()) {
                    boolean canAddJanitor = true;
                    for (Janitor currJanitor : game.getJanitors()) {
                        if (currJanitor.x == xCord && currJanitor.y == yCord) {
                            canAddJanitor = false;
                            break;
                        }
                    }
                    if (canAddJanitor) {
                        Janitor newJanitor = new Janitor(game.janitorIDCounter);
                        newJanitor.x = xCord;
                        newJanitor.y = yCord;
                        game.addJanitor(newJanitor);
                    }
                    break;
                }
            }
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && clickType.equals("Repairman")) {
            for (Road curr : game.getRoads()) {
                if (curr.x == xCord && curr.y == yCord && !curr.isHidden()) {
                    boolean canAddRepairMan = true;
                    for (RepairMan currRepairMan : game.getRepairMans()) {
                        if (currRepairMan.x == xCord && currRepairMan.y == yCord) {
                            canAddRepairMan = false;
                            break;
                        }
                    }
                    if (canAddRepairMan) {
                        RepairMan newRepairMan = new RepairMan(game.repairManIDCounter);
                        newRepairMan.x = xCord;
                        newRepairMan.y = yCord;
                        game.addRepairMan(newRepairMan);
                    }
                    break;
                }
            }
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !blocked && !demolish) {
            if (clickBuildingType != null) {
                Building newBuild = new Building(clickBuildingType);
                newBuild.x = xCord;
                newBuild.y = yCord;
                game.addBuilding(newBuild);
                Road hiddenRoad = new Road();
                hiddenRoad.setHidden();
                hiddenRoad.x = xCord;
                hiddenRoad.y = yCord;
                roadFinder.addRoad(hiddenRoad);
                for (Road curr : game.getRoads()) {
                    if ((curr.x + 60 == xCord && curr.y == yCord && !curr.isHidden())
                            || (curr.x == xCord && curr.y + 60 == yCord && !curr.isHidden())
                            || (curr.x - 120 == xCord && curr.y == yCord && !curr.isHidden())
                            || (curr.x == xCord && curr.y - 120 == yCord && !curr.isHidden())
                            || (curr.x + 60 == xCord && curr.y - 60 == yCord && !curr.isHidden())
                            || (curr.x - 60 == xCord && curr.y + 60 == yCord && !curr.isHidden())
                            || (curr.x - 120 == xCord && curr.y - 60 == yCord && !curr.isHidden())
                            || (curr.x - 60 == xCord && curr.y - 120 == yCord && !curr.isHidden())) {
                        roadFinder.connectRoads(curr, hiddenRoad, 100);
                        roadFinder.connectRoads(hiddenRoad, curr, 100);
                    }
                }
                game.addRoad(hiddenRoad);
            } else if (clickType.equals("Greenery")) {
                Green newGreen = new Green();
                newGreen.x = xCord;
                newGreen.y = yCord;
                game.addGreen(newGreen);
            } else if (clickType.equals("Dumpster")) {
                Dumpster newDumpster = new Dumpster();
                newDumpster.x = xCord;
                newDumpster.y = yCord;
                game.addDumpster(newDumpster);
            } else if (clickType.equals("Road")) {
                Road newRoad = new Road();
                newRoad.x = xCord;
                newRoad.y = yCord;
                roadFinder.addRoad(newRoad);
                for (Road curr : game.getRoads()) {
                    if ((curr.x - 60 == xCord && curr.y == yCord)
                            || (curr.x + 60 == xCord && curr.y == yCord)
                            || (curr.x == xCord && curr.y - 60 == yCord)
                            || (curr.x == xCord && curr.y + 60 == yCord)) {
                        roadFinder.connectRoads(curr, newRoad, 1);
                        roadFinder.connectRoads(newRoad, curr, 1);
                    }
                    if ((curr.x + 120 == xCord && curr.y == yCord && curr.isHidden())
                            || (curr.x == xCord && curr.y + 120 == yCord && curr.isHidden())
                            || (curr.x + 60 == xCord && curr.y - 60 == yCord && curr.isHidden())
                            || (curr.x - 60 == xCord && curr.y + 60 == yCord && curr.isHidden())
                            || (curr.x + 120 == xCord && curr.y + 60 == yCord && curr.isHidden())
                            || (curr.x + 60 == xCord && curr.y + 120 == yCord && curr.isHidden())) {
                        roadFinder.connectRoads(curr, newRoad, 100);
                        roadFinder.connectRoads(newRoad, curr, 100);
                    }
                }
                game.addRoad(newRoad);
            }
            game.setMoney(-clickCost);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && demolish) {
            for (Building curr : game.getBuildings()) {
                if ((curr.x == xCord && curr.y == yCord)
                 || (curr.x + 60 == xCord && curr.y == yCord)
                 || (curr.x == xCord && curr.y + 60 == yCord)
                 || (curr.x + 60 == xCord && curr.y + 60 == yCord)) {
                    while (curr.getQueue().notEmpty()) {
                        game.updateVisitor(curr.getQueue().pop());
                    }
                    game.remBuilding(curr);
                    for (Road hidden : game.getRoads()) {
                        if ((hidden.x == xCord && hidden.y == yCord && hidden.isHidden())
                                || (hidden.x + 60 == xCord && hidden.y == yCord && hidden.isHidden())
                                || (hidden.x == xCord && hidden.y + 60 == yCord && hidden.isHidden())
                                || (hidden.x + 60 == xCord && hidden.y + 60 == yCord && hidden.isHidden())) {
                            visitorEmergencyMove(xCord, yCord, hidden);
                            game.remRoad(hidden);
                            roadFinder.remRoad(hidden);
                        }
                    }
                }
            }
            for (Road curr : game.getRoads()) {
                if (curr.x == xCord && curr.y == yCord && !(curr.x == 0 && curr.y == 1020)) {
                    visitorEmergencyMove(xCord, yCord, curr);
                    game.remRoad(curr);
                    roadFinder.remRoad(curr);
                }
            }
            for (Green curr : game.getGreenStuff()) {
                if (curr.x == xCord && curr.y == yCord) {
                    game.remGreen(curr);
                }
            }
            for (Dumpster curr : game.getDumpsters()) {
                if (curr.x == xCord && curr.y == yCord) {
                    game.remDumpster(curr);
                }
            }
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            clickType = "Nothing";
            clickBuildingType = null;
            demolish = false;
            clickCost = 0;
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }

        //Eseménykezelés vége --------------------------------------------------------------------------------------------------------------

        ((Label) stage.getRoot().findActor("money")).setText("Money: " + game.getMoney() + "$");
        ((Label) stage.getRoot().findActor("visitors")).setText("Visitors: " + game.getVisitors().size);
        stage.act(delta);
        stage.draw();

    }

    //A Build menü
    public void addBuildMenu(final Table table, Skin skin) {
        Table subTable = new Table();
        Table subSubTable = new Table();
        AutoFocusScrollPane pane = new AutoFocusScrollPane(subTable, skin);
        Window window = new Window("", skin);
        window.add(pane).width(950).height(800);
        //--------------------------------------------------------------------------------------------------------------
        subTable.row().pad(40, 40, 40, 40);
        subTable.add(subSubTable);
        subSubTable.add(assetManager.demolishTexture).maxSize(100, 100);
        subSubTable.row();
        subSubTable.add(new Label("Demolish building", skin));
        subTable.add(new Label("Cost: 0", skin));
        Label demDesc = new Label("Demolish a building\nafter the next ride ends", skin);
        demDesc.setAlignment(Align.center);
        subTable.add(demDesc);
        TextButton demButton = new TextButton("Demolish", skin);
        demButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                demolish = true;
                table.getCells().get(0).getActor().setVisible(false);
                menuType = "Nothing";
            }
        });
        subTable.add(demButton).width(140).height(40).pad(20, 20, 20, 20);
        //--------------------------------------------------------------------------------------------------------------
        subTable.row().pad(40, 40, 40, 40);
        subSubTable = new Table();
        subTable.add(subSubTable);
        subSubTable.add(assetManager.menuTextures.get(Road.getPath())).maxSize(100, 100);
        subSubTable.row();
        subSubTable.add(new Label("Road", skin));
        subTable.add(new Label("Cost: " + (Road.getCost()), skin));
        Label roadDesc = new Label("Lets people carry\nthemselves around the park", skin);
        roadDesc.setAlignment(Align.center);
        subTable.add(roadDesc);
        TextButton roadButton = new TextButton("Place Road", skin);
        roadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickType = "Road";
                clickCost = Road.getCost();
                table.getCells().get(0).getActor().setVisible(false);
                menuType = "Nothing";
            }
        });
        subTable.add(roadButton).width(140).height(40).pad(20, 20, 20, 20);
        //--------------------------------------------------------------------------------------------------------------
        subTable.row().pad(40, 40, 40, 40);
        for (final Type curr : Type.values()) {
            int num = 0;
            for (Building building : game.getBuildings()) {
                if (building.getType() == curr) {
                    num++;
                }
            }
            final String tmpName = curr.name;
            final Label labelOne = new Label(tmpName, skin);
            final Label labelTwo = new Label("Build cost: " + curr.cost + "\n\nUpkeep cost: " + curr.upkeep + "\n\nTicket price: " + curr.price + "\n\nTurn length: " + curr.turnTime, skin);
            final Label labelThree = new Label("Capacity: " + curr.capacity + "\n\nDurability: " + curr.durability + "\n\nValue: " + curr.value + "\n\nBuilt so far: " + num, skin);
            TextButton tmpButton = new TextButton("Build", skin);
            subSubTable = new Table();
            subTable.add(subSubTable);
            subSubTable.add(assetManager.menuTextures.get(curr.path)).maxSize(100, 100);
            subSubTable.row();
            subSubTable.add(labelOne);
            subTable.add(labelTwo);
            subTable.add(labelThree);
            subTable.add(tmpButton).width(140).height(40).pad(20, 20, 20, 20);
            tmpButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    clickBuildingType = curr;
                    clickCost = curr.cost;
                    table.getCells().get(0).getActor().setVisible(false);
                    menuType = "Nothing";
                }
            });
            subTable.row().pad(40, 40, 40, 40);
        }
        //--------------------------------------------------------------------------------------------------------------
        subTable.row().pad(40, 40, 40, 40);
        subSubTable = new Table();
        subTable.add(subSubTable);
        subSubTable.add(assetManager.menuTextures.get(1)).maxSize(100, 100);
        subSubTable.row();
        subSubTable.add(new Label("Greenery", skin));
        subTable.add(new Label("Cost: " + (Green.getCost()), skin));
        Label greenDesc = new Label("Makes people happy\nwhen they pass by it", skin);
        greenDesc.setAlignment(Align.center);
        subTable.add(greenDesc);
        TextButton greenButton = new TextButton("Place Greenery", skin);
        greenButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickType = "Greenery";
                clickCost = Green.getCost();
                table.getCells().get(0).getActor().setVisible(false);
                menuType = "Nothing";
            }
        });
        subTable.add(greenButton).width(140).height(40).pad(20, 20, 20, 20);
        //--------------------------------------------------------------------------------------------------------------
        subTable.row().pad(40, 40, 40, 40);
        subSubTable = new Table();
        subTable.add(subSubTable);
        subSubTable.add(assetManager.menuTextures.get(Dumpster.getPath())).maxSize(100, 100);
        subSubTable.row();
        subSubTable.add(new Label("Dumpster", skin));
        subTable.add(new Label("Cost: " + (Dumpster.getCost()), skin));
        Label dumpDesc = new Label("Lets people dispose\nof their garbage", skin);
        dumpDesc.setAlignment(Align.center);
        subTable.add(dumpDesc);
        TextButton dumpsterButton = new TextButton("Place Dumpster", skin);
        dumpsterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickType = "Dumpster";
                clickCost = Dumpster.getCost();
                table.getCells().get(0).getActor().setVisible(false);
                menuType = "Nothing";
            }
        });
        subTable.add(dumpsterButton).width(140).height(40).pad(20, 20, 20, 20);
        //--------------------------------------------------------------------------------------------------------------
        table.getCells().get(0).setActor(window).width(950).height(800);
    }

    //A Hire menü
    public void addHireMenu(final Table table, final Skin skin) {
        Window window = new Window("", skin);
        final Label janitor = new Label("Janitor\n\nSalary: " + game.getSalaryJanitor() + "$\n\nNumber: " + game.getJanitors().size, skin);
        final Label repairMan = new Label("Repairman\n\nSalary: " + game.getSalaryRepairMan() + "$\n\nNumber: " + game.getRepairMans().size, skin);
        assetManager.janitorTexture.setScaling(Scaling.fillY);
        assetManager.repairManTexture.setScaling(Scaling.fillY);
        Button addJan = new TextButton("Hire", skin);
        Button remJan = new TextButton("Fire", skin);
        Button addRep = new TextButton("Hire", skin);
        Button remRep = new TextButton("Fire", skin);
        window.add(assetManager.janitorTexture).align(Align.top).pad(80, 0, 0, 0);
        window.add(janitor).align(Align.top).pad(80, 0, 0, 50);
        window.add(assetManager.repairManTexture).align(Align.top).pad(80, 0, 0, 40);
        window.add(repairMan).align(Align.top).pad(80, 0, 0, 20);
        window.row();
        window.add(addJan).width(70).height(40).expand().pad(30, 0, 100, 20);
        window.add(remJan).width(70).height(40).expand().pad(30, 0, 100, 80);
        window.add(addRep).width(70).height(40).expand().pad(30, 0, 100, 40);
        window.add(remRep).width(70).height(40).expand().pad(30, 0, 100, 30);
        addJan.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.getCells().get(0).getActor().setVisible(false);
                clickType = "Janitor";
                janitor.setText("Janitor\n\nSalary: " + game.getSalaryJanitor() + "$\n\nNumber: " + game.getJanitors().size);
                menuType = "Nothing";
            }
        });
        remJan.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuType = "Fire";
                addFireMenu(table, skin, "Janitor");
            }
        });
        addRep.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.getCells().get(0).getActor().setVisible(false);
                clickType = "Repairman";
                repairMan.setText("Repairman\n\nSalary: " + game.getSalaryRepairMan() + "$\n\nNumber: " + game.getRepairMans().size);
                menuType = "Nothing";
            }
        });
        remRep.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuType = "Fire";
                addFireMenu(table, skin, "RepairMan");
            }
        });
        table.getCells().get(0).setActor(window).width(550).height(250);
    }

    //Az elbocsátásokhoz használatos menü
    public void addFireMenu(final Table table, final Skin skin, final String which) {
        final Table subTable = new Table();
        AutoFocusScrollPane pane = new AutoFocusScrollPane(subTable, skin);
        Window window = new Window("", skin);
        window.add(pane).width(600).height(700);
        subTable.row().pad(40, 40, 40, 40);
        int counter = 0;
        if (which.equals("Janitor")) {
            for (final Janitor curr : game.getJanitors()) {
                Label name = new Label("Name: " + "Zsani" + curr.getID(), skin);
                Label location = new Label("Location: (" + Math.round(curr.x) + "," + Math.round(curr.y) + ")", skin);
                final TextButton fireButton = new TextButton("Fire", skin);
                subTable.add(name);
                subTable.add(location);
                subTable.add(fireButton).width(140).height(40).pad(20, 20, 20, 20);
                final int finalCounter = counter;
                fireButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.remJanitor(curr);
                        (((Table) fireButton.getParent()).getCells().get(finalCounter).getActor()).setVisible(false);
                        (((Table) fireButton.getParent()).getCells().get(finalCounter + 1).getActor()).setVisible(false);
                        (((Table) fireButton.getParent()).getCells().get(finalCounter + 2).getActor()).setVisible(false);
                        menuType = "Nothing";
                    }
                });
                subTable.row().pad(40, 40, 40, 40);
                counter += 3;
            }
        } else {
            for (final RepairMan curr : game.getRepairMans()) {
                Label name = new Label("Name: " + curr.getID(), skin);
                Label location = new Label("Location: (" + Math.round(curr.x) + "," + Math.round(curr.getY()) + ")", skin);
                final TextButton fireButton = new TextButton("Fire", skin);
                subTable.add(name);
                subTable.add(location);
                final int finalCounter = counter;
                subTable.add(fireButton).width(140).height(40).pad(20, 20, 20, 20);
                fireButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.remRepairMan(curr);
                        (((Table) fireButton.getParent()).getCells().get(finalCounter).getActor()).setVisible(false);
                        (((Table) fireButton.getParent()).getCells().get(finalCounter + 1).getActor()).setVisible(false);
                        (((Table) fireButton.getParent()).getCells().get(finalCounter + 2).getActor()).setVisible(false);
                        menuType = "Nothing";
                    }
                });
                subTable.row().pad(40, 40, 40, 40);
                counter += 3;
            }
        }
        table.getCells().get(0).setActor(window).width(600).height(700);
    }

    private String getElapsedTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        Long currTime = Duration.between(startTime, LocalDateTime.now()).toMillis();
        return String.format("%d hours %d minutes %d seconds",
                TimeUnit.MILLISECONDS.toHours(currTime),
                TimeUnit.MILLISECONDS.toMinutes(currTime),
                TimeUnit.MILLISECONDS.toSeconds(currTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currTime)));
    }

    private void visitorEmergencyMove(int xCord, int yCord, Road curr) {
        for (Visitor visitor : game.getVisitors()) {
            GraphPath<Road> idk = visitor.getDestination();
            if (idk != null) {
                for (int j = 0; j < idk.getCount(); ++j) {
                    if (idk.get(j) == curr && (visitor.getWhere() <= j || idk.getCount() - 1 == j)) {
                        visitor.setWhere(-visitor.getWhere());
                        visitor.setDestination(null);
                        visitor.setDestinationType(null);
                    }
                }
            }
            if ((visitor.x == xCord && visitor.y == yCord)
             || (visitor.x + 60 == xCord && visitor.y == yCord)
             || (visitor.x == xCord && visitor.y + 60 == yCord)
             || (visitor.x + 60 == xCord && visitor.y + 60 == yCord)) {
                boolean found = false;
                for (int i = 0; i < game.getRoads().size; ++i) {
                    Road tmp = game.getRoads().get(i);
                    if ((tmp.x + 60 == xCord && tmp.y == yCord && !tmp.isHidden())
                            || (tmp.x == xCord && tmp.y + 60 == yCord && !tmp.isHidden())
                            || (tmp.x - 60 == xCord && tmp.y == yCord && !tmp.isHidden())
                            || (tmp.x == xCord && tmp.y - 60 == yCord && !tmp.isHidden())
                            || (tmp.x - 120 == xCord && tmp.y == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x == xCord && tmp.y - 120 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x + 60 == xCord && tmp.y - 60 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x - 60 == xCord && tmp.y + 60 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x - 120 == xCord && tmp.y - 60 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x - 60 == xCord && tmp.y - 120 == yCord && !tmp.isHidden() && curr.isHidden())) {
                        visitor.setX(tmp.x);
                        visitor.setY(tmp.y);
                        found = true;
                    }
                }
                if (!found) {
                    visitor.setX(0);
                    visitor.setY(1020);
                }
            }
        }
        for (RepairMan repairMan : game.getRepairMans()) {
            GraphPath<Road> idk = repairMan.getDestination();
            if (idk != null) {
                for (int j = 0; j < idk.getCount(); ++j) {
                    if (idk.get(j) == curr && (repairMan.getWhere() <= j || idk.getCount() - 1 == j)) {
                        repairMan.setWhere(-repairMan.getWhere());
                        repairMan.setDestination(null);
                        repairMan.setTR(-repairMan.getTR());
                    }
                }
            }
            if ((repairMan.x == xCord && repairMan.y == yCord)
                    || (repairMan.x + 60 == xCord && repairMan.y == yCord)
                    || (repairMan.x == xCord && repairMan.y + 60 == yCord)
                    || (repairMan.x + 60 == xCord && repairMan.y + 60 == yCord)) {
                boolean found = false;
                for (int i = 0; i < game.getRoads().size; ++i) {
                    Road tmp = game.getRoads().get(i);
                    if ((tmp.x + 60 == xCord && tmp.y == yCord && !tmp.isHidden())
                            || (tmp.x == xCord && tmp.y + 60 == yCord && !tmp.isHidden())
                            || (tmp.x - 60 == xCord && tmp.y == yCord && !tmp.isHidden())
                            || (tmp.x == xCord && tmp.y - 60 == yCord && !tmp.isHidden())
                            || (tmp.x - 120 == xCord && tmp.y == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x == xCord && tmp.y - 120 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x + 60 == xCord && tmp.y - 60 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x - 60 == xCord && tmp.y + 60 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x - 120 == xCord && tmp.y - 60 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x - 60 == xCord && tmp.y - 120 == yCord && !tmp.isHidden() && curr.isHidden())) {
                        repairMan.setX(tmp.x);
                        repairMan.setY(tmp.y);
                        found = true;
                    }
                }
                if (!found) {
                    repairMan.setX(0);
                    repairMan.setY(1020);
                }
            }
        }
        for (Janitor janitor : game.getJanitors()) {
            if (janitor.x == xCord && janitor.y == yCord) {
                boolean found = false;
                for (int i = 0; i < game.getRoads().size; ++i) {
                    Road tmp = game.getRoads().get(i);
                    if ((tmp.x + 60 == xCord && tmp.y == yCord && !tmp.isHidden())
                            || (tmp.x == xCord && tmp.y + 60 == yCord && !tmp.isHidden())
                            || (tmp.x - 60 == xCord && tmp.y == yCord && !tmp.isHidden())
                            || (tmp.x == xCord && tmp.y - 60 == yCord && !tmp.isHidden())
                            || (tmp.x - 120 == xCord && tmp.y == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x == xCord && tmp.y - 120 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x + 60 == xCord && tmp.y - 60 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x - 60 == xCord && tmp.y + 60 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x - 120 == xCord && tmp.y - 60 == yCord && !tmp.isHidden() && curr.isHidden())
                            || (tmp.x - 60 == xCord && tmp.y - 120 == yCord && !tmp.isHidden() && curr.isHidden())) {
                        janitor.setX(tmp.x);
                        janitor.setY(tmp.y);
                        found = true;
                    }
                }
                if (!found) {
                    janitor.setX(0);
                    janitor.setY(1020);
                }
            }
        }
    }

    //A Management menü
    public void addStatsMenu(Table table, final Skin skin) {

        Window window = new Window("", skin);
        //--------------------------------------------------------------------------------------------------------------
        window.row().pad(40, 20, 10, 40);
        Label general = new Label("General stats", skin);
        final Label name = new Label("Name: " + game.getName(), skin);
        TextButton changeNameButton = new TextButton("Change name", skin);
        final Label ticketPrice = new Label("Ticket price: " + game.getTicketPrice() + "$", skin);
        TextButton changeTicketPrice = new TextButton("Change price", skin);
        Label visitors = new Label("Visitors: " + game.getVisitors().size, skin);
        Label money = new Label("Money: " + game.getMoney() + "$", skin);
        Label time = new Label("Time elapsed: " + getElapsedTime(), skin);
        window.row().pad(40, 20, 10, 40);
        window.add(time).expand().align(Align.topLeft);
        window.row().pad(40, 20, 10, 40);
        window.add(general).expand().align(Align.topLeft);
        window.row().pad(0, 60, 0, 40);
        window.add(name).expand().align(Align.topLeft);
        window.add(changeNameButton).height(40).width(120).expand().align(Align.topRight);
        window.row().pad(0, 60, 0, 40);
        window.add(ticketPrice).height(40).expand().align(Align.topLeft);
        window.add(changeTicketPrice).height(40).width(120).expand().align(Align.topRight);
        window.row().pad(0, 60, 0, 40);
        window.add(visitors).expand().align(Align.topLeft);
        window.row().pad(0, 60, 0, 40);
        window.add(money).expand().align(Align.topLeft);
        window.row().pad(40, 40, 40, 40);

        //Dialog pop ahol meg lehet adni a park új nevét
        changeNameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialog dialog = new Dialog("Name of your new park", skin, "dialog") {
                    public void result(Object obj) {
                        if (!obj.equals(false)) {
                            TextField newNameField = (TextField) obj;
                            game.setName(newNameField.getText());
                            name.setText("Name: " + newNameField.getText());
                        }
                    }
                };
                TextField nameField = new TextField(game.getName(), skin);
                dialog.add(nameField);
                dialog.button("Cancel", false);
                dialog.button("Confirm", nameField);
                dialog.key(Input.Keys.ENTER, nameField);
                dialog.key(Input.Keys.ESCAPE, false);
                dialog.show(stage);
            }
        });

        changeTicketPrice.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Dialog dialog = new Dialog("New price of park tickets", skin) {
                    public void result(Object obj) {
                        if (!obj.equals(false)) {
                            TextField newPriceField = (TextField) obj;
                            game.setTicketPrice(Integer.parseInt(newPriceField.getText()));
                            ticketPrice.setText("Ticket price: " + newPriceField.getText() + "$");
                        }
                    }
                };
                TextField priceField = new TextField(game.getTicketPrice() + "", skin);
                dialog.add(priceField);
                dialog.button("Cancel", false);
                dialog.key(Input.Keys.ESCAPE, false);
                dialog.button("Confirm", priceField);
                dialog.key(Input.Keys.ENTER, priceField);
                dialog.show(stage);
            }
        });

        //--------------------------------------------------------------------------------------------------------------
        window.row().pad(40, 20, 10, 40);
        Label buildings = new Label("Buildings", skin);
        window.add(buildings).expand().align(Align.topLeft);
        window.row().pad(0, 60, 0, 40);
        window.row().pad(40, 40, 40, 40);
        //--------------------------------------------------------------------------------------------------------------
        window.row().pad(40, 20, 10, 40);
        Label staff = new Label("Staff", skin);
        Label janitors = new Label("Janitors employed: " + game.getJanitors().size, skin);
        Label repairMan = new Label("Repairmen employed: " + game.getRepairMans().size, skin);
        window.add(staff).expand().align(Align.topLeft);
        window.row().pad(0, 60, 0, 40);
        window.add(janitors).expand().align(Align.topLeft);
        window.row().pad(0, 60, 0, 40);
        window.add(repairMan).expand().align(Align.topLeft);
        window.row().pad(40, 40, 40, 40);
        //--------------------------------------------------------------------------------------------------------------
        table.getCells().get(0).setActor(window).width(700).height(700);
    }

    //A Menü
    public void addExitMenu(Table table, Skin skin) {
        Window window = new Window("", skin);
        final TextButton music = new TextButton("Music: " + (game.getMusic() ? "On" : "Off"), skin);
        final TextButton sfx = new TextButton("Effects: " + (game.getSfx() ? "On" : "Off"), skin);
        final TextButton theme = new TextButton("Theme:  " + (game.getTheme().name()), skin);
        final TextButton back = new TextButton("Exit", skin);
        window.add(music).width(200).height(60).pad(20);
        window.row();
        window.add(sfx).width(200).height(60).pad(20);
        window.row();
        window.add(theme).width(200).height(60).pad(20);
        window.row();
        window.add(back).width(200).height(60).pad(20);
        music.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.toggleMusic();
                music.getLabel().setText("Music: " + (game.getMusic() ? "On" : "Off"));
            }
        });
        sfx.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSfx();
                sfx.getLabel().setText("Effects: " + (game.getSfx() ? "On" : "Off"));
            }
        });
        theme.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setTheme();
                theme.getLabel().setText("Theme: " + (game.getTheme().name()));
            }
        });
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        table.getCells().get(0).setActor(window).width(300).height(400);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    public void dispose() {
        stage.dispose();
    }

}
