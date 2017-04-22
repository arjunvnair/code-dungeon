/**
	© 2017 Arjun Nair
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import robot.Robot;

public class Main
{
	//Level Generation  
	private static String difficulty;
	private static int length;
	private static int height;
	private static Level l;
	
	//Level Execution
	private static Robot robot;
	private static int i;
	private static int j;
	private static Response r;
	private static JLabel moves;
	private static int movesMade;
	
	//Graphics
	private static Dimension dimMax = Toolkit.getDefaultToolkit().getScreenSize();
	private static int taskbarSize;
	private static JFrame mainScreen;
	private static JPanel levelGUI;
	private static Thread backgroundAnimation;
	private static JLabel title;
	private static JLabel copyright;
	private static JLabel robotBottom = new JLabel(), robotTop = new JLabel(), robotLeft = new JLabel(), robotRight = new JLabel();
	private static JLayeredPane tilePane;
	private static JLabel tileLabel;
	private static JLabel robotLabel;
	
	//Sound
	private static BooleanControl mute;
	private static Clip robotNoiseClip;
	private static Clip victoryJingleEfficientClip;
	private static Clip victoryJingleInefficientClip;
	private static Thread openingMusic;
	private static Thread levelMusic;
	
	public static void main(String[] args) 
	{
		initializeMusicThreads();
		initializeClips();
		Robot.uploadRobots();
		mainScreen = new JFrame();
		mainScreen.setTitle("Code Dungeon");
		mainScreen.setIconImage(new ImageIcon("src/codedungeon/art/logo.png").getImage());
		WindowAdapter adapter = new WindowAdapter()
		{
			public void windowClosing(WindowEvent arg0) 
			{
				openingMusic.interrupt();
				System.exit(0);
			}
			public void windowDeiconified(WindowEvent arg0) 
			{
				mute.setValue(false);
			}
			public void windowIconified(WindowEvent arg0) 
			{
				mute.setValue(true);
			}
			public void windowDeactivated(WindowEvent arg0) 
			{
				mute.setValue(true);
			}
			public void windowActivated(WindowEvent arg0) 
			{
				try
				{
					mute.setValue(false);
				}
				catch(Exception e) {}
			}
		};
		mainScreen.addWindowListener(adapter);
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(mainScreen.getGraphicsConfiguration());
		taskbarSize = scnMax.bottom;
		mainScreen.setSize(dimMax.width, dimMax.height - taskbarSize);
		mainScreen.setLayout(new BorderLayout());
    	mainScreen.getContentPane().setBackground(Color.GRAY);
    	try
		{
	    	title = new JLabel("Code Dungeon", SwingConstants.CENTER);
	    	title.setFont(new Font("Modern No. 20", Font.PLAIN, 128));
	    	mainScreen.add(title, BorderLayout.NORTH);
	    	copyright = new JLabel("© 2017 Arjun Nair ", SwingConstants.RIGHT);
	    	copyright.setFont(new Font("Times New Roman", Font.BOLD, 40));
	    	mainScreen.add(copyright, BorderLayout.SOUTH);
	    	backgroundAnimation = new Thread()
			{
				public void run()
				{
					int count = 0;
				    try
				    {
				    	while(!isInterrupted())
					    {
					    	count++;
					    	JLabel robot;
					    	robot = new JLabel(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/Blue right " + count + ".png")).getScaledInstance(dimMax.width * 3/10, dimMax.height * 19/20 - taskbarSize - title.getHeight() - copyright.getHeight(), Image.SCALE_DEFAULT)));
					    	robot.setBounds(0, title.getHeight(), dimMax.width, dimMax.height - taskbarSize - copyright.getHeight());
					    	robot.setOpaque(false);
					    	mainScreen.add(robot, BorderLayout.CENTER);
					    	robot.setLayout(new FlowLayout());
						    if(count == 6)
						    	count = 1;
						    mainScreen.setSize(dimMax.width - 1, dimMax.height - 1 - taskbarSize);
						    mainScreen.setSize(dimMax.width, dimMax.height - taskbarSize);
						    Thread.sleep(55);
						    mainScreen.remove(robot);
					    }
				    	mainScreen.getContentPane().removeAll();
				    	mainScreen.repaint();
				    	Thread.interrupted();
				    }
				    catch(InterruptedException e) 
				    {
				    	mainScreen.getContentPane().removeAll();
				    	mainScreen.add(title, BorderLayout.NORTH);
				    	mainScreen.add(copyright, BorderLayout.SOUTH);
				    	mainScreen.repaint();
				    }
				    catch(IOException e) {}
				}
			};
			backgroundAnimation.start();
	    	JPanel menuButtons = new JPanel();
	    	menuButtons.setLayout(new GridLayout(3, 1));
	    	menuButtons.setBackground(Color.GRAY);
	    	JButton levelBuilderButton = new JButton("Level Builder");
	    	levelBuilderButton.setBackground(Color.CYAN);
	    	levelBuilderButton.setFont(new Font("Modern No. 20", Font.PLAIN, 100));
	    	levelBuilderButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
	    	ActionListener levelBuilderButtonListener = new ActionListener()
	    	{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					mainScreen.remove(menuButtons);
					JPanel levelBuilderScreenButtons = new JPanel();
			    	levelBuilderScreenButtons.setLayout(new GridLayout(6, 1));
			    	levelBuilderScreenButtons.setBackground(Color.GRAY);
			    	levelBuilderScreenButtons.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
					backgroundAnimation.interrupt();
					difficulty = "Normal";
					length = 15;
					height = 15;
					l = new Level(difficulty, length, height);
					levelGUI = getLevelGUI();
					JPanel difficultyLevels = new JPanel();
					difficultyLevels.setLayout(new GridLayout(1,3));
					JButton easyButton = new JButton("Easy");
					JButton normalButton = new JButton("Normal");
					JButton hardButton = new JButton("Hard");
			    	easyButton.setBackground(Color.GREEN);
			    	easyButton.setFont(new Font("Modern No. 20", Font.PLAIN, 25));
					easyButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			    	easyButton.setPreferredSize(new Dimension(dimMax.width/9, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	ActionListener easyButtonListener = new ActionListener()
			    	{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							difficulty = "Easy";
							easyButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
							normalButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
							hardButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
						}
			    	};
			    	easyButton.addActionListener(easyButtonListener);
			    	normalButton.setBackground(Color.ORANGE);
			    	normalButton.setFont(new Font("Modern No. 20", Font.PLAIN, 25));
					normalButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			    	normalButton.setPreferredSize(new Dimension(dimMax.width/9, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	ActionListener normalButtonListener = new ActionListener()
			    	{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							difficulty = "Normal";
							normalButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
							easyButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
							hardButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
						}
			    	};
			    	normalButton.addActionListener(normalButtonListener);
			    	hardButton.setBackground(Color.RED);
			    	hardButton.setFont(new Font("Modern No. 20", Font.PLAIN, 25));
					hardButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			    	hardButton.setPreferredSize(new Dimension(dimMax.width/9, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	ActionListener hardButtonListener = new ActionListener()
			    	{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							difficulty = "Hard";
							hardButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
							normalButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
							easyButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
						}
			    	};
			    	hardButton.addActionListener(hardButtonListener);
					difficultyLevels.add(easyButton);
					difficultyLevels.add(normalButton);
					difficultyLevels.add(hardButton);
					JPanel sizeSettings = new JPanel();
					sizeSettings.setLayout(new GridLayout(1,3));
					JButton miniButton = new JButton("Mini");
					JButton mediumButton = new JButton("Medium");
					JButton massiveButton = new JButton("Massive");
					miniButton.setBackground(Color.MAGENTA);
			    	miniButton.setFont(new Font("Modern No. 20", Font.PLAIN, 20));
					miniButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			    	miniButton.setPreferredSize(new Dimension(dimMax.width/9, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	ActionListener miniButtonListener = new ActionListener()
			    	{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							length = 10;
							height = 10;
							miniButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
							mediumButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
							massiveButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
						}
			    	};
			    	miniButton.addActionListener(miniButtonListener);
			    	mediumButton.setBackground(Color.PINK);
			    	mediumButton.setFont(new Font("Modern No. 20", Font.PLAIN, 25));
					mediumButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			    	mediumButton.setPreferredSize(new Dimension(dimMax.width/9, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	ActionListener mediumButtonListener = new ActionListener()
			    	{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							length = 15;
							height = 15;
							mediumButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
							miniButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
							massiveButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
						}
			    	};
			    	mediumButton.addActionListener(mediumButtonListener);
			    	massiveButton.setBackground(Color.BLUE);
			    	massiveButton.setFont(new Font("Modern No. 20", Font.PLAIN, 30));
					massiveButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			    	massiveButton.setPreferredSize(new Dimension(dimMax.width/9, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	ActionListener massiveButtonListener = new ActionListener()
			    	{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							length = 20;
							height = 20;
							massiveButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
							mediumButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
							miniButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
						}
			    	};
			    	massiveButton.addActionListener(massiveButtonListener);
			    	sizeSettings.add(miniButton);
			    	sizeSettings.add(mediumButton);
			    	sizeSettings.add(massiveButton);
			    	JButton generateLevelButton = new JButton("Generate Level");
			    	generateLevelButton.setBackground(Color.CYAN);
			    	generateLevelButton.setFont(new Font("Modern No. 20", Font.PLAIN, 75));
			    	generateLevelButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	ActionListener generateLevelButtonListener = new ActionListener()
			    	{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							l = new Level(difficulty, length, height);
							mainScreen.remove(levelGUI);
							levelGUI = getLevelGUI();
							mainScreen.add(levelGUI, BorderLayout.CENTER);
						    mainScreen.setSize(dimMax.width - 1, dimMax.height - 1 - taskbarSize);
						    mainScreen.setSize(dimMax.width, dimMax.height - taskbarSize);
						}
			    	};
			    	generateLevelButton.addActionListener(generateLevelButtonListener);
			    	JTextField levelNameField = new JTextField(1);
			    	levelNameField.setBackground(Color.LIGHT_GRAY);
			    	levelNameField.setFont(new Font("Modern No. 20", Font.PLAIN, 100));
			    	levelNameField.setPreferredSize(new Dimension(dimMax.width/9, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	levelNameField.setText("Untitled");
			    	levelNameField.setEditable(true);
			    	JButton saveAsButton = new JButton("Save Level");
			    	saveAsButton.setBackground(Color.CYAN);
			    	saveAsButton.setFont(new Font("Modern No. 20", Font.PLAIN, 100));
			    	saveAsButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	ActionListener saveAsButtonListener = new ActionListener()
			    	{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							l.serialize(levelNameField.getText());
						}
			    	};
			    	saveAsButton.addActionListener(saveAsButtonListener);
			    	JButton backToMenuButton = new JButton("Back to Menu");
			    	backToMenuButton.setBackground(Color.CYAN);
			    	backToMenuButton.setFont(new Font("Modern No. 20", Font.PLAIN, 80));
			    	backToMenuButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
			    	ActionListener backToMenuButtonListener = new ActionListener()
			    	{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							mainScreen.remove(levelBuilderScreenButtons);
							mainScreen.remove(levelGUI);
							mainScreen.add(menuButtons, BorderLayout.WEST);
							mainScreen.revalidate();
							mainScreen.repaint();
							backgroundAnimation = new Thread()
							{
								public void run()
								{
									int count = 0;
								    try
								    {
								    	while(!isInterrupted())
									    {
									    	count++;
									    	JLabel robot;
									    	robot = new JLabel(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/Blue right " + count + ".png")).getScaledInstance(dimMax.width * 3/10, dimMax.height * 19/20 - taskbarSize - title.getHeight() - copyright.getHeight(), Image.SCALE_DEFAULT)));
									    	//robot.setBounds(0, title.getHeight(), dimMax.width, dimMax.height - taskbarSize - copyright.getHeight());
									    	robot.setOpaque(false);
									    	mainScreen.add(robot, BorderLayout.CENTER);
									    	robot.setLayout(new FlowLayout());
										    if(count == 6)
										    	count = 1;
										    mainScreen.setSize(dimMax.width - 1, dimMax.height - 1 - taskbarSize);
										    mainScreen.setSize(dimMax.width, dimMax.height - taskbarSize);
										    Thread.sleep(75);
										    mainScreen.remove(robot);
									    }
								    	mainScreen.getContentPane().removeAll();
								    	mainScreen.repaint();
								    	Thread.interrupted();
								    }
								    catch(InterruptedException e) 
								    {
								    	mainScreen.getContentPane().removeAll();
								    	mainScreen.add(title, BorderLayout.NORTH);
								    	mainScreen.add(copyright, BorderLayout.SOUTH);
								    	mainScreen.repaint();
								    }
								    catch(IOException e) {}
								}
							};
							backgroundAnimation.start();
						}
			    	};
			    	backToMenuButton.addActionListener(backToMenuButtonListener);
					levelBuilderScreenButtons.add(difficultyLevels);
					levelBuilderScreenButtons.add(sizeSettings);
					levelBuilderScreenButtons.add(generateLevelButton);
					levelBuilderScreenButtons.add(levelNameField);
					levelBuilderScreenButtons.add(saveAsButton);
					levelBuilderScreenButtons.add(backToMenuButton);
					mainScreen.add(levelBuilderScreenButtons, BorderLayout.WEST);
					mainScreen.add(levelGUI, BorderLayout.CENTER);
					mainScreen.repaint();
					mainScreen.revalidate();
				}
	    	};
	    	levelBuilderButton.addActionListener(levelBuilderButtonListener);
	    	JButton robotTesterButton = new JButton("Robot Tester");
	    	robotTesterButton.setBackground(Color.CYAN);
	    	robotTesterButton.setFont(new Font("Modern No. 20", Font.PLAIN, 100));
	    	robotTesterButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
	    	ActionListener robotTesterButtonListener = new ActionListener()
	    	{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
			    	try
			    	{
			    		mainScreen.remove(menuButtons);
				    	JPanel robotTesterScreenButtons = new JPanel();
				    	robotTesterScreenButtons.setLayout(new GridLayout(5, 1));
				    	robotTesterScreenButtons.setBackground(Color.GRAY);
				    	JButton fcButton = new JButton("Select File");
				    	fcButton.setBackground(Color.CYAN);
				    	fcButton.setFont(new Font("Modern No. 20", Font.PLAIN, 80));
				    	fcButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
				    	ActionListener fcButtonListener = new ActionListener()
				    	{
							@Override
							public void actionPerformed(ActionEvent arg0) 
							{
								String filename = File.separator + "ser";
								JFileChooser fc = new JFileChooser(new File(filename));
								if(!(Files.exists(Paths.get("c:\\CodeDungeon\\"))))
									try 
									{
										Files.createDirectory(Paths.get("c:\\CodeDungeon\\"));
									} 
									catch (IOException e) {}
								fc.setCurrentDirectory(new File("c:\\CodeDungeon\\"));
								fc.showOpenDialog(mainScreen);
								fcButton.setText(fc.getSelectedFile().getName());
							}
				    	};
				    	fcButton.addActionListener(fcButtonListener);
				    	JButton testButton = new JButton("Test " + Robot.getUploadedRobots().get(0).getName());
				    	testButton.setBackground(Color.CYAN);
				    	testButton.setFont(new Font("Modern No. 20", Font.PLAIN, 80));
				    	testButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
				    	ActionListener testButtonListener = new ActionListener()
				    	{
							@Override
							public void actionPerformed(ActionEvent arg0) 
							{
								if((new File("c:\\CodeDungeon\\" + fcButton.getText())).exists())
								{
									openingMusic.interrupt();
									levelMusic.start();
									movesMade = 0;
									mainScreen.remove(robotTesterScreenButtons);
									JPanel playScreenButtons = new JPanel();
									playScreenButtons.setLayout(new GridLayout(5, 1));
									playScreenButtons.setBackground(Color.GRAY);
									playScreenButtons.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
									backgroundAnimation.interrupt();
							    	mainScreen.getContentPane().removeAll();
							    	mainScreen.repaint();
									l = new Level("c:\\CodeDungeon\\" + fcButton.getText());
									levelGUI = getLevelGUI();
									try
									{
										robotBottom = new JLabel(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/robot-bottom.png"))));
										robotTop = new JLabel(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/robot-top.png"))));
										robotLeft = new JLabel(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/robot-left.png"))));
										robotRight = new JLabel(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/robot-right.png"))));
									}
									catch(IOException e) {}
									robotBottom.setOpaque(false);
									robotTop.setOpaque(false);
									robotLeft.setOpaque(false);
									robotRight.setOpaque(false);
									i = l.getBoard().length/2;
									j = l.getBoard()[0].length/2;
									tilePane = ((JLayeredPane) (((JPanel) (levelGUI.getComponent(0))).getComponent(i * l.getBoard()[0].length + j)));
									tileLabel = (JLabel) tilePane.getComponent(0);
									tilePane.remove(tileLabel);
									robotLabel = robotBottom;
									tilePane.add(robotLabel, new Integer(1));
									tilePane.add(tileLabel, new Integer(0));
									JLabel robotName = new JLabel(testButton.getText().substring(5, testButton.getText().length()), SwingConstants.CENTER);
							    	robot = Robot.getUploadedRobots().get(Robot.indexOf(robotName.getText()));
									robotName.setFont(new Font("Modern No. 20", Font.PLAIN, 80));
							    	robotName.setBackground(Color.CYAN);
							    	robotName.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
							    	String size = "Mini";
							    	if(l.getBoard().length == 15)
							    		size = "Medium";
							    	else if(l.getBoard().length == 20)
							    		size = "Massive";
									JLabel descriptor = new JLabel(l.getDifficulty() + " | " + size, SwingConstants.CENTER);
									descriptor.setFont(new Font("Modern No. 20", Font.PLAIN, 80));
							    	descriptor.setBackground(Color.CYAN);
							    	descriptor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
									moves = new JLabel(movesMade + "/" + l.getMinMoves() + " Moves", SwingConstants.CENTER);
									moves.setFont(new Font("Modern No. 20", Font.PLAIN, 80));
							    	moves.setBackground(Color.CYAN);
							    	moves.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
							    	JButton moveMaker = new JButton();
									try 
									{
										moveMaker = new JButton(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/play.png"))));
									} 
									catch (IOException e) {}
									ActionListener moveMakerListener = new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e)
										{
											boolean[][] radar = new boolean[3][3];
											for(int i1 = 0; i1 < radar.length; i1++)
											{
												for(int j1 = 0; j1 < radar[0].length; j1++)
												{
													if(0 <= i - 1 + i1 && i - 1 + i1 < l.getBoard().length && 0 <= j - 1 + j1 && j - 1 + j1 < l.getBoard()[0].length)
														radar[i1][j1] = l.getBoard()[i - 1 + i1][j - 1 + j1].isTraversable();
													else
														radar[i1][j1] = false;
												}
											}
											r = null;
											try
											{
												r = robot.getResponse(radar);
											}
											catch(Exception ex)
											{
												if(l.getBoard()[i][j].allowsExit())
												{
													JOptionPane.showMessageDialog(mainScreen, robot.getName() + " threw an exception!");
													addMove();
													updateMoves();
												}
											}
											if(r != null && 0 <= i && i < l.getBoard().length && 0 <= j && j < l.getBoard()[0].length && l.getBoard()[i][j].allowsExit())
											{
												robotNoiseClip.loop(1);
												tilePane = ((JLayeredPane) (((JPanel) (levelGUI.getComponent(0))).getComponent(i * l.getBoard()[0].length + j)));
												tilePane.remove(robotLabel);
												tilePane.repaint();
												tilePane.revalidate();
												mainScreen.repaint();
												mainScreen.revalidate();
												if(r instanceof Move)
												{
													try
													{
														if(r.getDirection() == null)
														{
															JOptionPane.showMessageDialog(mainScreen, robot.getName() + " tried to move in an unspecified direction!");
															Main.addMove();
															Main.updateMoves();
															return;
														}
														else if(r.getDirection().equals(Direction.UP))
														{
															l.getBoard()[i][j].getExited(r.getDirection());
															i--;
															robotLabel = robotTop;
														}
														else if(r.getDirection().equals(Direction.DOWN))
														{
															l.getBoard()[i][j].getExited(r.getDirection());
															i++;
															robotLabel = robotBottom;
														}
														else if(r.getDirection().equals(Direction.LEFT))
														{
															l.getBoard()[i][j].getExited(r.getDirection());
															j--;
															robotLabel = robotLeft;
														}
														else if(r.getDirection().equals(Direction.RIGHT))
														{
															l.getBoard()[i][j].getExited(r.getDirection());
															j++;
															robotLabel = robotRight;
														}
														else
														{
															JOptionPane.showMessageDialog(mainScreen, robot.getName() + " tried to move in an imaginary direction.");
															Main.addMove();
															Main.updateMoves();
															return;
														}
														if(l.getBoard()[i][j].isTraversable())
														{
															tilePane = ((JLayeredPane) (((JPanel) (levelGUI.getComponent(0))).getComponent(i * l.getBoard()[0].length + j)));
															tileLabel = (JLabel) tilePane.getComponent(0);
															tilePane.remove(tileLabel);
															tilePane.add(robotLabel, new Integer(1));
															tilePane.add(tileLabel, new Integer(0));
															mainScreen.repaint();
															mainScreen.revalidate();
														}
														l.getBoard()[i][j].getSteppedOn();
													}
													catch(ArrayIndexOutOfBoundsException ex)
													{
														Main.addMove();
														Main.updateMoves();
														JOptionPane.showMessageDialog(mainScreen, robot.getName() + " fell off the board!");			
													}
												}
												else if(r instanceof RepairBridge)
												{
													try
													{
														robotNoiseClip.loop(1);
														if(r.getDirection() == null)
														{
															Main.addMove();
															Main.updateMoves();
															tilePane = ((JLayeredPane) (((JPanel) (levelGUI.getComponent(0))).getComponent(i * l.getBoard()[0].length + j)));
															tileLabel = (JLabel) tilePane.getComponent(0);
															tilePane.remove(tileLabel);
															tilePane.add(robotLabel, new Integer(1));
															tilePane.add(tileLabel, new Integer(0));
															JOptionPane.showMessageDialog(mainScreen, robot.getName() + " tried to repair a bridge in an unspecified direction!");
															mainScreen.repaint();
															mainScreen.revalidate();
															return;
														}
														else if(r.getDirection().equals(Direction.UP))
														{
															robotLabel = robotTop;
														}
														else if(r.getDirection().equals(Direction.DOWN))
														{
															robotLabel = robotBottom;
														}
														else if(r.getDirection().equals(Direction.LEFT))
														{
															robotLabel = robotLeft;
														}
														else if(r.getDirection().equals(Direction.RIGHT))
														{
															robotLabel = robotRight;
														}
														else
														{
															Main.addMove();
															Main.updateMoves();
															tilePane = ((JLayeredPane) (((JPanel) (levelGUI.getComponent(0))).getComponent(i * l.getBoard()[0].length + j)));
															tileLabel = (JLabel) tilePane.getComponent(0);
															tilePane.remove(tileLabel);
															tilePane.add(robotLabel, new Integer(1));
															tilePane.add(tileLabel, new Integer(0));
															JOptionPane.showMessageDialog(mainScreen, robot.getName() + " tried to repair a bridge in an imaginary direction.");
															mainScreen.repaint();
															mainScreen.revalidate();
															return;
														}
														tilePane = ((JLayeredPane) (((JPanel) (levelGUI.getComponent(0))).getComponent(i * l.getBoard()[0].length + j)));
														tileLabel = (JLabel) tilePane.getComponent(0);
														tilePane.remove(tileLabel);
														tilePane.add(robotLabel, new Integer(1));
														tilePane.add(tileLabel, new Integer(0));
														mainScreen.repaint();
														mainScreen.revalidate();
														Point p = r.getDirection().getTileMoveInDirection(j, i, 1);
														if(l.getTileAt(p) instanceof Bridge)
														{
															((Bridge) l.getTileAt(p)).repairBridge();
															Main.refreshTileAt(p);
														}
														else
														{
															Main.addMove();
															Main.updateMoves();
															JOptionPane.showMessageDialog(mainScreen, robot.getName() + " tried to repair a nonexistent bridge " + r.getDirection().getName() + "!");
														}
													}
													catch(ArrayIndexOutOfBoundsException ex)
													{
														Main.addMove();
														Main.updateMoves();
														JOptionPane.showMessageDialog(mainScreen, robot.getName() + " tried to repair a nonexistent bridge " + r.getDirection().getName() + "!");
													}
												}
												else
												{
													Main.addMove();
													Main.updateMoves();
													JOptionPane.showMessageDialog(mainScreen, robot.getName() + " did not produce a viable response!");
												}
											}
										}
									};
									moveMaker.addActionListener(moveMakerListener);
									JButton backToMenuButton = new JButton("Back to Menu");
							    	backToMenuButton.setBackground(Color.CYAN);
							    	backToMenuButton.setFont(new Font("Modern No. 20", Font.PLAIN, 80));
							    	backToMenuButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
							    	ActionListener backToMenuButtonListener = new ActionListener()
							    	{
										@Override
										public void actionPerformed(ActionEvent arg0) 
										{
											interruptMusic();
											initializeMusicThreads();
											openingMusic.start();
											mainScreen.remove(playScreenButtons);
											mainScreen.remove(levelGUI);
											mainScreen.add(menuButtons, BorderLayout.WEST);
											mainScreen.revalidate();
											mainScreen.repaint();
											backgroundAnimation = new Thread()
											{
												public void run()
												{
													int count = 0;
												    try
												    {
												    	while(!isInterrupted())
													    {
													    	count++;
													    	JLabel robot;
													    	robot = new JLabel(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/Blue right " + count + ".png")).getScaledInstance(dimMax.width * 3/10, dimMax.height * 19/20 - taskbarSize - title.getHeight() - copyright.getHeight(), Image.SCALE_DEFAULT)));
													    	robot.setBounds(0, title.getHeight(), dimMax.width, dimMax.height - taskbarSize - copyright.getHeight());
													    	robot.setOpaque(false);
													    	mainScreen.add(robot, BorderLayout.CENTER);
													    	robot.setLayout(new FlowLayout());
														    if(count == 6)
														    	count = 1;
														    mainScreen.setSize(dimMax.width - 1, dimMax.height - 1 - taskbarSize);
														    mainScreen.setSize(dimMax.width, dimMax.height - taskbarSize);
														    Thread.sleep(75);
														    mainScreen.remove(robot);
													    }
												    	mainScreen.getContentPane().removeAll();
												    	mainScreen.repaint();
												    	Thread.interrupted();
												    }
												    catch(InterruptedException e) 
												    {
												    	mainScreen.getContentPane().removeAll();
												    	mainScreen.add(title, BorderLayout.NORTH);
												    	mainScreen.add(copyright, BorderLayout.SOUTH);
												    	mainScreen.repaint();
												    }
												    catch(IOException e) {}
												}
											};
											backgroundAnimation.start();
										}
							    	};
							    	backToMenuButton.addActionListener(backToMenuButtonListener);
									playScreenButtons.add(robotName);
									playScreenButtons.add(descriptor);
									playScreenButtons.add(moves);
									playScreenButtons.add(moveMaker);
									playScreenButtons.add(backToMenuButton);
									playScreenButtons.setBackground(Color.CYAN);
									mainScreen.add(playScreenButtons, BorderLayout.WEST);
									mainScreen.add(levelGUI, BorderLayout.CENTER);
									mainScreen.revalidate();
									mainScreen.repaint();
								}
							}
				    	};
				    	testButton.addActionListener(testButtonListener);
			    		JPanel forwardbackwardButtons = new JPanel();
				    	forwardbackwardButtons.setLayout(new GridLayout(1, 2));
				    	forwardbackwardButtons.setBackground(Color.GRAY);
				    	JButton forwardButton = new JButton(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/FRENTE.png")).getScaledInstance(dimMax.width/6, (dimMax.height - taskbarSize - copyright.getHeight())/3, Image.SCALE_SMOOTH)));
				    	forwardButton.setBackground(Color.BLUE);
				    	forwardButton.setPreferredSize(new Dimension(dimMax.width/6, (dimMax.height - taskbarSize - copyright.getHeight())/3));
				    	ActionListener forwardButtonListener = new ActionListener()
				    	{
							@Override
							public void actionPerformed(ActionEvent arg0) 
							{
								if(Robot.indexOf(testButton.getText().substring(5, testButton.getText().length())) == Robot.getUploadedRobots().size() - 1)
									testButton.setText("Test " + Robot.getUploadedRobots().get(0).getName());
								else
									testButton.setText("Test " + Robot.getUploadedRobots().get(Robot.indexOf(testButton.getText().substring(5, testButton.getText().length())) + 1).getName());
							}
				    	};
				    	forwardButton.addActionListener(forwardButtonListener);
				    	JButton backwardButton = new JButton(new ImageIcon(ImageIO.read(new File("src/codedungeon/art/Back.png")).getScaledInstance(dimMax.width/6, (dimMax.height - taskbarSize - copyright.getHeight())/3, Image.SCALE_SMOOTH)));
				    	backwardButton.setBackground(Color.BLUE);
				    	backwardButton.setPreferredSize(new Dimension(dimMax.width/6, (dimMax.height - taskbarSize - copyright.getHeight())/3));
				    	ActionListener backwardButtonListener = new ActionListener()
				    	{
							@Override
							public void actionPerformed(ActionEvent arg0) 
							{
								if(Robot.indexOf(testButton.getText().substring(5, testButton.getText().length())) == 0)
									testButton.setText("Test " + Robot.getUploadedRobots().get(Robot.getUploadedRobots().size() - 1).getName());
								else
									testButton.setText("Test " + Robot.getUploadedRobots().get(Robot.indexOf(testButton.getText().substring(5, testButton.getText().length())) - 1).getName());
							}
				    	};
				    	backwardButton.addActionListener(backwardButtonListener);
				    	forwardbackwardButtons.add(backwardButton);
				    	forwardbackwardButtons.add(forwardButton);
				    	JButton backToMenuButton = new JButton("Back to Menu");
				    	backToMenuButton.setBackground(Color.CYAN);
				    	backToMenuButton.setFont(new Font("Modern No. 20", Font.PLAIN, 80));
				    	backToMenuButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
				    	ActionListener backToMenuButtonListener = new ActionListener()
				    	{
							@Override
							public void actionPerformed(ActionEvent arg0) 
							{
								mainScreen.remove(robotTesterScreenButtons);
								mainScreen.add(menuButtons, BorderLayout.WEST);
							}
				    	};
				    	backToMenuButton.addActionListener(backToMenuButtonListener);
				    	robotTesterScreenButtons.add(fcButton);
				    	robotTesterScreenButtons.add(forwardbackwardButtons);
				    	robotTesterScreenButtons.add(testButton);
				    	robotTesterScreenButtons.add(backToMenuButton);
				    	mainScreen.add(robotTesterScreenButtons, BorderLayout.WEST);
			    	}
			    	catch(IOException e) {}
				}
	    	};
	    	robotTesterButton.addActionListener(robotTesterButtonListener);
	    	JButton helpButton = new JButton("Help");
	    	helpButton.setBackground(Color.CYAN);
	    	helpButton.setFont(new Font("Modern No. 20", Font.PLAIN, 100));
	    	helpButton.setPreferredSize(new Dimension(dimMax.width/3, (dimMax.height - taskbarSize - copyright.getHeight())/3));
	    	ActionListener helpButtonListener = new ActionListener()
	    	{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					JOptionPane.showMessageDialog(mainScreen, "Check src/robot/Robot.java for instructions in the comments.");
				}
	    	};
	    	helpButton.addActionListener(helpButtonListener);
	    	menuButtons.add(levelBuilderButton);
	    	menuButtons.add(robotTesterButton);
	    	menuButtons.add(helpButton);
	    	mainScreen.add(menuButtons, BorderLayout.WEST);
			mainScreen.setVisible(true);
			openingMusic.start();
		}
		catch(Exception e) {}
	}
	protected static JPanel getLevelGUI()
	{
		Tile[][] board = l.getBoard();
		JPanel sock = new JPanel();
		sock.setLayout(new GridBagLayout());
		JPanel levelFrame = new JPanel();
		int optimalPanelHeight = dimMax.height - title.getHeight() - copyright.getHeight();
		int optimalPanelWidth = optimalPanelHeight * board[0].length/board.length;
		int maxPanelWidth = dimMax.width * 2/3;
		if(optimalPanelWidth > maxPanelWidth)
		{
			optimalPanelWidth = maxPanelWidth;
			optimalPanelHeight = optimalPanelWidth * board.length/board[0].length;
		}
		levelFrame.setLayout(new GridLayout(l.getBoard()[0].length, l.getBoard().length));
		levelFrame.setBorder(BorderFactory.createLineBorder(new Color(51, 0, 102)));
		levelFrame.setBackground(Color.GRAY);
		levelFrame.setSize(optimalPanelWidth, optimalPanelHeight);
		sock.setBackground(Color.GRAY);
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[0].length; j++)
			{
				levelFrame.add(getTileLayeredPane(i, j));
			}
		}	
		sock.add(levelFrame);
		return sock;
	}
	protected static JLayeredPane getTileLayeredPane(int i, int j)
	{
		JLayeredPane p = new JLayeredPane();
		OverlayLayout o = new OverlayLayout(p);
		p.setLayout(o);
		String filename = "src/codedungeon/art/";
		Tile[][] board = l.getBoard();
		try
		{
			if(board[i][j] instanceof Path)
			{
				if(j > 0 && board[i][j - 1].isTraversable())
					filename += "left-";
				if(j < board[0].length - 1 && board[i][j + 1].isTraversable())
					filename += "right-";
				if(i > 0 && board[i - 1][j].isTraversable())
					filename += "top-";
				if(i < board.length - 1 && board[i + 1][j].isTraversable())
					filename += "bottom-";
				filename += "path";
				
			}
			else if(board[i][j] instanceof Goal)
			{
				filename += "chest";
			}
			else if(board[i][j] instanceof Bridge && board[i][j].isTraversable())
			{
				if(j > 0 && board[i][j - 1].isTraversable())
					filename += "left-";
				if(j < board[0].length - 1 && board[i][j + 1].isTraversable())
					filename += "right-";
				if(i > 0 && board[i - 1][j].isTraversable())
					filename += "top-";
				if(i < board.length - 1 && board[i + 1][j].isTraversable())
					filename += "bottom-";
				filename += "bridge";	
			}
			else
			{
				if(i > 0 && (board[i - 1][j] instanceof Path || board[i - 1][j] instanceof Goal))
					filename += "void-top";
				else
					filename += "void";
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		filename += ".png";
		try 
		{
			JLabel tileImage = new JLabel();
			tileImage = new JLabel(new ImageIcon(ImageIO.read(new File(filename))/*.getScaledInstance(tileImage.getWidth(), tileImage.getHeight(), Image.SCALE_SMOOTH)*/));
			p.add(tileImage, new Integer(0));
			tileImage.setSize(32, 32);
			p.setSize(32, 32);
			p.setOpaque(false);
		} 
		catch (IOException e) 
		{
			System.out.println(filename);
		}
		return p;
	}
	protected static JFrame getMainScreen()
	{
		return mainScreen;
	}
	protected static Point getRobotPosition()
	{
		return new Point(j, i);
	}
	protected static Robot getRobot()
	{
		return robot;
	}
	protected static Direction getDirection()
	{
		return r.getDirection();
	}
	protected static int getMoves()
	{
		return movesMade;
	}
	protected static Level getLevel()
	{
		return l;
	}
	protected static void refreshTileAt(Point p)
	{
		JLayeredPane tP = ((JLayeredPane) (((JPanel) (levelGUI.getComponent(0))).getComponent((int) p.getY() * l.getBoard()[0].length + (int) p.getX())));
		tP = ((JLayeredPane) (((JPanel) (levelGUI.getComponent(0))).getComponent((int) p.getY() * l.getBoard()[0].length + (int) p.getX())));
		JLabel tL = (JLabel) tilePane.getComponent(0);
		tP.remove(0);
		try
		{
			tL = (JLabel) (getTileLayeredPane((int) p.getY(), (int) p.getX()).getComponent(0));
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		tP.add(tL, new Integer(0));
		tP.repaint();
		tP.revalidate();
	}
	protected static void addMove()
	{
		movesMade++;
	}
	protected static void updateMoves()
	{
		moves.setText(movesMade + "/" + l.getMinMoves() + " Moves");
	}
	private static void initializeMusicThreads()
	{
		openingMusic = new Thread()
		{
			public void run()
			{
				try
				{
					File sky = new File("src/codedungeon/sounds/This Sky of Mine.wav");
					AudioInputStream skyInputStream = AudioSystem.getAudioInputStream(sky);
					Clip skyClip = AudioSystem.getClip();
					skyClip.open(skyInputStream);
					mute = (BooleanControl) skyClip.getControl(BooleanControl.Type.MUTE);
					mute.setValue(false);
					while(!this.isInterrupted())
					{
						skyClip.start();
					}
					skyClip.stop();
				}
				catch(Exception ex) {}
			}
		};
		levelMusic = new Thread()
		{
			public void run()
			{
				try
				{
					File level = new File("src/codedungeon/sounds/beep_beep_beep_level_music.wav");
					AudioInputStream levelInputStream = AudioSystem.getAudioInputStream(level);
					Clip levelClip = AudioSystem.getClip();
					levelClip.open(levelInputStream);
					mute = (BooleanControl) levelClip.getControl(BooleanControl.Type.MUTE);
					mute.setValue(false);
					((FloatControl) (levelClip.getControl(FloatControl.Type.MASTER_GAIN))).setValue(-6);
					while(!this.isInterrupted())
					{
						levelClip.start();
					}
					levelClip.stop();
				}
				catch(Exception ex) {}
			}
		};
	}
	private static void initializeClips()
	{
		try
		{
			File robotNoise = new File("src/codedungeon/sounds/robot.wav");
			AudioInputStream robotNoiseInputStream = AudioSystem.getAudioInputStream(robotNoise);
			robotNoiseClip = AudioSystem.getClip();
			robotNoiseClip.open(robotNoiseInputStream);
			((FloatControl) (robotNoiseClip.getControl(FloatControl.Type.MASTER_GAIN))).setValue(5);
		} 
		catch(LineUnavailableException e1) {} 
		catch (IOException e1) {}
		catch(UnsupportedAudioFileException e1) {}
		try
		{
			File victoryJingleInefficient = new File("src/codedungeon/sounds/inefficient-victory-jingle.wav");
			AudioInputStream victoryJingleInefficientInputStream = AudioSystem.getAudioInputStream(victoryJingleInefficient);
			victoryJingleInefficientClip = AudioSystem.getClip();
			victoryJingleInefficientClip.open(victoryJingleInefficientInputStream);
			((FloatControl) (victoryJingleInefficientClip.getControl(FloatControl.Type.MASTER_GAIN))).setValue(5);
		} 
		catch(LineUnavailableException e1) {} 
		catch (IOException e1) {}
		catch(UnsupportedAudioFileException e1) {}
		try
		{
			File victoryJingleEfficient = new File("src/codedungeon/sounds/efficient-victory-jingle.wav");
			AudioInputStream victoryJingleEfficientInputStream = AudioSystem.getAudioInputStream(victoryJingleEfficient);
			victoryJingleEfficientClip = AudioSystem.getClip();
			victoryJingleEfficientClip.open(victoryJingleEfficientInputStream);
			((FloatControl) (victoryJingleEfficientClip.getControl(FloatControl.Type.MASTER_GAIN))).setValue(5);
			
		} 
		catch(LineUnavailableException e1) {} 
		catch (IOException e1) {}
		catch(UnsupportedAudioFileException e1) {}
	}
	protected static void interruptMusic()
	{
		openingMusic.interrupt();
		levelMusic.interrupt();
	}
	protected static void playVictoryJingle()
	{
		if(movesMade <= l.getMinMoves())
			victoryJingleEfficientClip.start();
		else
			victoryJingleInefficientClip.start();
	}
}
