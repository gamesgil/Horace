package com.mygdx.horace.model;

import java.util.ArrayList;

public class Level {
	private int id;
	private int speed;
	private ArrayList<String> layout;
	
	public Level(int Id)
	{
		this.id = id;
		
		layout = new ArrayList<String>();
		layout.add("         ");
		layout.add("         ");
		
		
		String element;
		String line;
		String[] elements = {"T", "^", "[F]", "T", "T", "^"};
		int pos;
		int maxIterations;
		
		// build dynamically
		for (int i = 0; i < 10; i++) {
			line = "         ";
			element = elements[(int) Math.floor(Math.random() * elements.length)];
			
			if (element.equals("T") || element.equals("^")) {
				maxIterations = 1;
			}
			else {
				maxIterations = 1;
			}
				
			for (int j = 0; j < 1 + (int)Math.floor(Math.random() * maxIterations); j++) {
				pos = (int)Math.floor(Math.random() * (line.length() - element.length()));
				
				line = line.substring(0, pos) + element + line.substring(pos + element.length());
				layout.add(line);
			}
			
		}
		
		layout.add("  !      ");
		layout.add("  EE     ");
		
		/*layout.add("         ");
		layout.add("T       T");
		layout.add("   T     ");
		layout.add("    ^    ");
		layout.add(" ^    ^  ");
		layout.add("  [F]    ");
		layout.add("   [F]   ");
		layout.add("   [F] T ");
		layout.add(" T  [F]  ");
		layout.add(" ^   ^   ");
		layout.add("    T    ");
		layout.add("[F]  T   ");
		layout.add("    T    ");
		layout.add("  [F]T   ");
		layout.add("    T    ");
		layout.add("     T[F]");
		layout.add("    T    ");
		layout.add("  !      ");
		layout.add("  EE     ");*/
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public ArrayList<String> getLayout()
	{
		return layout;
	}
}
