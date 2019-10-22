package pack1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JPanel;
import javax.swing.Timer;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class draw extends JPanel implements ActionListener{

	//Add your own click sound by dragging a .wav into the sound Folder.
	
	int w, h, floor, wall;
	
	double jump = 100000;
	boolean clack = false;
	
	//Masses
	double m1 = 1, m2 = Math.pow(100, 4);
	
	//Velocity
	double v1 = 0, v2 = -1.5	/ jump;
	
	//Pos
	double x1 = 100, x2 = 250;
	
	int numCol = 0;
	
	//Rectangles
	Rectangle2D r1, r2;
	int r1Size = 30, r2Size = 100;
	
	Timer t;
	boolean finished = false;
	
	public draw() {
		t = new Timer(20, this);
		t.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		w = getWidth();
		h = getHeight();
		floor = h - 50;
		wall = 50;

		g2d.setBackground(color(40));
		g2d.clearRect(0, 0, w, h);
		
		//Floor
		g2d.setColor(Color.white);
		g2d.drawLine(0, floor, w, floor);
		
		//Wall
		g2d.setColor(Color.white);
		g2d.drawLine(wall, 0, wall, h);
		
		for(int i = 0; i < jump; i++) {
			if(x1 <= wall) {
				wall();
			}

			if(x1+r1Size > x2) {
				x1 = x2 - r1Size;
				collision();
			}
			
			update();
		}
		
		if(clack) {	
			playSound("sound/click.wav");
			clack = false;
		}
		
		if(x1 < w) {
			r1 = new Rectangle2D.Double(x1, floor - r1Size, r1Size, r1Size);
			r2 = new Rectangle2D.Double(x2, floor - r2Size, r2Size, r2Size);
			
			fill(g2d);
		}
		
		g2d.setFont(new Font("Arial", Font.BOLD, 35));
		g2d.drawString(Integer.toString(numCol), 100, 100);	
		
		if(v1 > 0 && v1 <= v2) {
			g2d.drawString("Finished", 100, 50);	
			t.stop();
		}
	}

	void fill(Graphics2D g2d) {
		g2d.fill(r1);
		g2d.fill(r2);
	}
	
	void update() {
		if(x1 + v1 <= wall) {
			x1 = wall;
		}else {
			x1 = x1 + v1;
		}
		
		if(x2 + v2 <= wall + r1Size) {
			x2 = wall + r1Size;
		}else {
			x2 = x2 + v2;
		}
	}

	void wall() {
		v1 *= -1;
		
		clack = true;
		
		numCol++;
	}
	
	void collision() {
		double u1 = v1;
		double u2 = v2;
		
		v1 = ((m1 - m2) / (m1 + m2)) * u1 + ((2*m2) / (m1 + m2)) * u2;
		v2 = ((2*m1) / (m1 + m2)) * u1 + ((m2 - m1) / (m1 + m2)) * u2;

		clack = true;
		
		numCol++;
	}
	
	private void print1() {
		System.out.printf("X1: %.3f	V1: %.3f	Mass1: %.3f\n", x1, v1, m1);
	}
	
	private void print2() {
		System.out.printf("X2: %.3f	V2: %.3f	Mass2: %.3f\n", x2, v2, m2);
	}
	
	/** Loads and plays a .wav File.
	 * @param FilePath the Filepath.
	 */
	private static void playSound(String FilePath) {
		InputStream sound;
		
		try {
			sound = new FileInputStream(new File(FilePath));
			AudioStream audio = new AudioStream(sound);
			AudioPlayer.player.start(audio);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/** Creates a grey-scale Color based on an Integer value. Similar to p5.js color().
	 * @param i the Integer.
	 * @return the Color.
	 */
	private Color color(int i) {
		return new Color(i, i, i);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
}
