package ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import asciiPanel.AsciiPanel;
import game.Creature;
import game.EatScreen;
import game.FieldOfView;
import game.Item;
import game.StuffFactory;
import game.Tile;
import game.World;
import game.WorldBuilder;

public class PlayScreen implements Screen {
	private World world;
	private Creature player;
	private int screenWidth;
	private int screenHeight;
	private List<String> messages;
	private FieldOfView fov;
	private Screen subscreen;
	
	public PlayScreen(){
		screenWidth = 80;
		screenHeight = 23;
		messages = new ArrayList<String>();
		createWorld();
		fov = new FieldOfView(world);
		
		StuffFactory factory = new StuffFactory(world);
		createCreatures(factory);
		factory.createItems(world);
	}

	private void createCreatures(StuffFactory factory){
		player = factory.newPlayer(messages, fov);
		
		for (int z = 0; z < world.depth(); z++){
			for (int i = 0; i < 4; i++){
				factory.newFungus(z);
			}
			for (int i = 0; i < 10; i++){
				factory.newBat(z);
			}
			for (int i = 0; i < z * 2 + 1; i++){
				factory.newZombie(z, player);
				factory.newGoblin(z, player);
			}
		}
	}

	private void createWorld(){
		world = new WorldBuilder(90, 32, 5)
					.makeCaves()
					.build();
	}
	
	public int getScrollX() { return Math.max(0, Math.min(player.x - screenWidth / 2, world.width() - screenWidth)); }
	
	public int getScrollY() { return Math.max(0, Math.min(player.y - screenHeight / 2, world.height() - screenHeight)); }
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		int left = getScrollX();
		int top = getScrollY(); 
		
		displayTiles(terminal, left, top);
		displayMessages(terminal, messages);
		
		String stats = String.format(" %3d/%3d hp   %d/%d mana   %8s", player.hp(), player.maxHp(), player.mana(), player.maxMana(), hunger());
		terminal.write(stats, 1, 23);
		
		if (subscreen != null)
			subscreen.displayOutput(terminal);
	}
	
	private String hunger(){
		if (player.food() < player.maxFood() * 0.10)
			return "Starving";
		else if (player.food() < player.maxFood() * 0.25)
			return "Hungry";
		else if (player.food() > player.maxFood() * 0.90)
			return "Stuffed";
		else if (player.food() > player.maxFood() * 0.75)
			return "Full";
		else
			return "";
	}

	private void displayMessages(AsciiPanel terminal, List<String> messages) {
		terminal(terminal, messages);
		if (subscreen == null)
			messages.clear();
	}

	private void terminal(AsciiPanel terminal, List<String> messages) {
		int top = screenHeight - messages.size();
		terminal1(terminal, messages, top);
	}

	private void terminal1(AsciiPanel terminal, List<String> messages, int top) {
		for (int i = 0; i < messages.size(); i++) {
			terminal.writeCenter(messages.get(i), top + i);
		}
	}

	private void displayTiles(AsciiPanel terminal, int left, int top) {
		fov.update(player.x, player.y, player.z, player.visionRadius());
		
		DisplayTiles(terminal, left, top);
	}

	private void DisplayTiles(AsciiPanel terminal, int left, int top) {
		for (int x = 0; x < screenWidth; x++) {
			terminal3(terminal, left, top, x);
		}
	}

	private void terminal3(AsciiPanel terminal, int left, int top, int x) {
		for (int y = 0; y < screenHeight; y++) {
			terminal4(terminal, left, top, x, y);
		}
	}

	private void terminal4(AsciiPanel terminal, int left, int top, int x, int y) {
		int wx = x + left;
		int wy = y + top;
		if (player.canSee(wx, wy, player.z))
			terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z));
		else
			terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, Color.darkGray);
	}
	
	@Override
	public Screen respondToUserInput(KeyEvent key) {
		int level = player.level();
		
		if (subscreen != null) {
			subscreen = subscreen.respondToUserInput(key);
		} else {
			switch (key.getKeyCode()){
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_H: player.moveBy(-1, 0, 0); break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_L: player.moveBy( 1, 0, 0); break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_K: player.moveBy( 0,-1, 0); break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_J: player.moveBy( 0, 1, 0); break;
			case KeyEvent.VK_Y: player.moveBy(-1,-1, 0); break;
			case KeyEvent.VK_U: player.moveBy( 1,-1, 0); break;
			case KeyEvent.VK_B: player.moveBy(-1, 1, 0); break;
			case KeyEvent.VK_N: player.moveBy( 1, 1, 0); break;
			case KeyEvent.VK_D: subscreen = new DropScreen(player); break;
			case KeyEvent.VK_E: subscreen = new EatScreen(player); break;
			case KeyEvent.VK_W: subscreen = new EquipScreen(player); break;
			case KeyEvent.VK_X: subscreen = new ExamineScreen(player); break;
			case KeyEvent.VK_SEMICOLON: subscreen = new LookScreen(player, "Looking", 
					player.x - getScrollX(), 
					player.y - getScrollY()); break;
			case KeyEvent.VK_T: subscreen = new ThrowScreen(player,
					player.x - getScrollX(), 
					player.y - getScrollY()); break;
			case KeyEvent.VK_F: 
				if (player.weapon() == null || player.weapon().rangedAttackValue() == 0)
					player.notify("You don't have a ranged weapon equiped.");
				else
					subscreen = new FireWeaponScreen(player,
						player.x - getScrollX(), 
						player.y - getScrollY()); break;
			case KeyEvent.VK_Q: subscreen = new QuaffScreen(player); break;
			case KeyEvent.VK_R: subscreen = new ReadScreen(player,
						player.x - getScrollX(), 
						player.y - getScrollY()); break;
			}
			
			switch (key.getKeyChar()){
			case 'g':
			case ',': player.pickup(); break;
			case '<': 
				if (userIsTryingToExit())
					return userExits();
				else
					player.moveBy( 0, 0, -1); break;
			case '>': player.moveBy( 0, 0, 1); break;
			case '?': subscreen = new HelpScreen(); break;
			}
		}

		if (player.level() > level)
			subscreen = new LevelUpScreen(player, player.level() - level);
		
		if (subscreen == null)
			world.update();
		
		if (player.hp() < 1)
			return new LoseScreen(player);
		
		return this;
	}

	private boolean userIsTryingToExit(){
		return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
	}
	
	private Screen userExits(){
		for (Item item : player.inventory().getItems()){
			if (item != null && item.name().equals("teddy bear"))
				return new WinScreen();
		}
		player.modifyHp(0, "Died while cowardly fleeing the caves.");
		return new LoseScreen(player);
	}
}
