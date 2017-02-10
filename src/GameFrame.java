import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import java.util.zip.ZipEntry;


public class GameFrame extends JFrame implements ActionListener{

	private JPanel contentPane;
	private Vector<JButton> buttonsVector; //存放81个按钮
	private Vector<JButton> numberButtonVector; //存放9个数字键
	private Vector<JRadioButton> radioButtonsvVector;  //存放3个单选框
	private JButton choosedButton;  //当前被选中的按钮，表示将要被修改
	private GameCore core; //游戏内核，含有游戏算法
	private int holeNumber = 25; //默认为简单，挖25空


	private int[][] backup; //用来备份游戏，用于清除后重新导入
	private JButton btnNewButton; //重新开始按钮



	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameFrame frame = new GameFrame();
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setTitle("数独游戏");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public GameFrame() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 545);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("\u83DC\u5355");
		menuBar.add(mnNewMenu);

		JMenuItem menuItem = new JMenuItem("\u6E38\u620F\u89C4\u5219");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "数独（すうどく，Sudoku）是一种运用纸、\n" +
						"笔进行演算的逻辑游戏。玩家需要根据9×9盘面上的已知数字，推理出所有剩余空格的数字，并满足\n" + 
						"每一行、每一列、每一个粗线宫内的数字均含1-9，不重复");
			}
		});
		mnNewMenu.add(menuItem);

		JMenuItem mntmNewMenuItem = new JMenuItem("\u7248\u672C\u4FE1\u606F");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "版本：数独1.0\n" + 
						"开发者：李繁\n" + 
						"开发时间：2014年7月16日");
			}
		});
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("\u4F7F\u7528\u624B\u518C");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "该游戏有”简单“、”一般“、”困难“三个难度级别\n" +
						"“开始新游戏”开始新的一局游戏，”清除已填“则清除当前已填数字");
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton button = new JButton("1");


		button.setFont(new Font("Batang", Font.PLAIN, 10));
		panel.add(button);

		JButton button_1 = new JButton("2");
		button_1.setFont(new Font("宋体", Font.PLAIN, 10));
		panel.add(button_1);

		JButton button_2 = new JButton("3");
		button_2.setFont(new Font("宋体", Font.PLAIN, 10));
		panel.add(button_2);

		JButton button_3 = new JButton("4");
		button_3.setFont(new Font("宋体", Font.PLAIN, 10));
		panel.add(button_3);

		JButton button_4 = new JButton("5");
		button_4.setFont(new Font("宋体", Font.PLAIN, 10));
		panel.add(button_4);

		JButton button_5 = new JButton("6");
		button_5.setFont(new Font("宋体", Font.PLAIN, 10));
		panel.add(button_5);

		JButton button_6 = new JButton("7");
		button_6.setFont(new Font("宋体", Font.PLAIN, 10));
		panel.add(button_6);

		JButton button_7 = new JButton("8");
		button_7.setFont(new Font("宋体", Font.PLAIN, 10));
		panel.add(button_7);

		JButton button_8 = new JButton("9");
		button_8.setFont(new Font("宋体", Font.PLAIN, 10));
		panel.add(button_8);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnNewButton = new JButton("\u5F00\u59CB\u65B0\u6E38\u620F");
		btnNewButton.addActionListener(this);



		panel_1.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("\u6E05\u9664\u5DF2\u586B");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//清除所有按钮监听器
				for (int i = 0; i < 9; i ++) {
					for (int j = 0; j < 9; j ++) {
						core.test[i][j] = backup[i][j]; 
					}
				}
				initGame();

			}
		});

		panel_1.add(btnNewButton_1);

		//默认为单选按钮 “简单”被选中
		JRadioButton rdbtnNewRadioButton = new JRadioButton("\u7B80\u5355", true);
		panel_1.add(rdbtnNewRadioButton);

		JRadioButton radioButton = new JRadioButton("\u4E00\u822C");
		panel_1.add(radioButton);

		JRadioButton radioButton_1 = new JRadioButton("\u96BE");
		panel_1.add(radioButton_1);

		//单选按钮组，让三个按钮互斥
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnNewRadioButton);
		group.add(radioButton);
		group.add(radioButton_1);

		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(9, 9, 0, 0));

		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setForeground(Color.BLACK);
		btnNewButton_2.setBackground(Color.PINK);
		panel_2.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("");
		btnNewButton_3.setBackground(Color.PINK);
		panel_2.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.setBackground(Color.PINK);
		panel_2.add(btnNewButton_4);

		JButton btnNewButton_5 = new JButton("");
		btnNewButton_5.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_5);

		JButton btnNewButton_6 = new JButton("");
		btnNewButton_6.setBackground(Color.CYAN);
		btnNewButton_6.addActionListener(this);
		panel_2.add(btnNewButton_6);

		JButton btnNewButton_7 = new JButton("");
		btnNewButton_7.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_7);

		JButton btnNewButton_8 = new JButton("");
		btnNewButton_8.setBackground(Color.PINK);
		panel_2.add(btnNewButton_8);

		JButton btnNewButton_9 = new JButton("");
		btnNewButton_9.setBackground(Color.PINK);
		panel_2.add(btnNewButton_9);

		JButton btnNewButton_10 = new JButton("");
		btnNewButton_10.setBackground(Color.PINK);
		panel_2.add(btnNewButton_10);

		JButton btnNewButton_11 = new JButton("");
		btnNewButton_11.setBackground(Color.PINK);
		panel_2.add(btnNewButton_11);

		JButton btnNewButton_12 = new JButton("");
		btnNewButton_12.setBackground(Color.PINK);
		panel_2.add(btnNewButton_12);

		JButton btnNewButton_13 = new JButton("");
		btnNewButton_13.setBackground(Color.PINK);
		panel_2.add(btnNewButton_13);

		JButton btnNewButton_14 = new JButton("");
		btnNewButton_14.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_14);

		JButton btnNewButton_15 = new JButton("");
		btnNewButton_15.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_15);

		JButton btnNewButton_16 = new JButton("");
		btnNewButton_16.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_16);

		JButton btnNewButton_17 = new JButton("");
		btnNewButton_17.setBackground(Color.PINK);
		panel_2.add(btnNewButton_17);

		JButton btnNewButton_18 = new JButton("");
		btnNewButton_18.setBackground(Color.PINK);
		panel_2.add(btnNewButton_18);

		JButton btnNewButton_19 = new JButton("");
		btnNewButton_19.setBackground(Color.PINK);
		panel_2.add(btnNewButton_19);

		JButton btnNewButton_20 = new JButton("");
		btnNewButton_20.setBackground(Color.PINK);
		panel_2.add(btnNewButton_20);

		JButton btnNewButton_21 = new JButton("");
		btnNewButton_21.setBackground(Color.PINK);
		panel_2.add(btnNewButton_21);

		JButton btnNewButton_22 = new JButton("");
		btnNewButton_22.setBackground(Color.PINK);
		panel_2.add(btnNewButton_22);

		JButton btnNewButton_23 = new JButton("");
		btnNewButton_23.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_23);

		JButton btnNewButton_24 = new JButton("");
		btnNewButton_24.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_24);

		JButton btnNewButton_25 = new JButton("");
		btnNewButton_25.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_25);

		JButton btnNewButton_26 = new JButton("");
		btnNewButton_26.setBackground(Color.PINK);
		panel_2.add(btnNewButton_26);

		JButton button_9 = new JButton("");
		button_9.setBackground(Color.PINK);
		panel_2.add(button_9);

		JButton btnNewButton_27 = new JButton("");
		btnNewButton_27.setBackground(Color.PINK);
		panel_2.add(btnNewButton_27);

		JButton btnNewButton_28 = new JButton("");
		btnNewButton_28.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_28);

		JButton btnNewButton_29 = new JButton("");
		btnNewButton_29.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_29);

		JButton btnNewButton_30 = new JButton("");
		btnNewButton_30.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_30);

		JButton btnNewButton_31 = new JButton("");
		btnNewButton_31.setBackground(Color.PINK);
		panel_2.add(btnNewButton_31);

		JButton btnNewButton_32 = new JButton("");
		btnNewButton_32.setBackground(Color.PINK);
		panel_2.add(btnNewButton_32);

		JButton btnNewButton_33 = new JButton("");
		btnNewButton_33.setBackground(Color.PINK);
		panel_2.add(btnNewButton_33);

		JButton btnNewButton_34 = new JButton("");
		btnNewButton_34.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_34);

		JButton btnNewButton_35 = new JButton("");
		btnNewButton_35.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_35);

		JButton btnNewButton_36 = new JButton("");
		btnNewButton_36.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_36);

		JButton btnNewButton_37 = new JButton("");
		btnNewButton_37.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_37);

		JButton btnNewButton_38 = new JButton("");
		btnNewButton_38.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_38);

		JButton btnNewButton_39 = new JButton("");
		btnNewButton_39.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_39);

		JButton btnNewButton_40 = new JButton("");
		btnNewButton_40.setBackground(Color.PINK);
		panel_2.add(btnNewButton_40);

		JButton btnNewButton_41 = new JButton("");
		btnNewButton_41.setBackground(Color.PINK);
		panel_2.add(btnNewButton_41);

		JButton btnNewButton_42 = new JButton("");
		btnNewButton_42.setBackground(Color.PINK);
		panel_2.add(btnNewButton_42);

		JButton btnNewButton_43 = new JButton("");
		btnNewButton_43.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_43);

		JButton btnNewButton_44 = new JButton("");
		btnNewButton_44.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_44);

		JButton btnNewButton_45 = new JButton("");
		btnNewButton_45.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_45);

		JButton btnNewButton_46 = new JButton("");
		btnNewButton_46.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_46);

		JButton btnNewButton_47 = new JButton("");
		btnNewButton_47.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_47);

		JButton btnNewButton_48 = new JButton("");
		btnNewButton_48.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_48);

		JButton btnNewButton_49 = new JButton("");
		btnNewButton_49.setBackground(Color.PINK);
		panel_2.add(btnNewButton_49);

		JButton btnNewButton_50 = new JButton("");
		btnNewButton_50.setBackground(Color.PINK);
		panel_2.add(btnNewButton_50);

		JButton btnNewButton_51 = new JButton("");
		btnNewButton_51.setBackground(Color.PINK);
		panel_2.add(btnNewButton_51);

		JButton btnNewButton_52 = new JButton("");
		btnNewButton_52.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_52);

		JButton btnNewButton_53 = new JButton("");
		btnNewButton_53.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_53);

		JButton btnNewButton_54 = new JButton("");
		btnNewButton_54.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_54);

		JButton btnNewButton_55 = new JButton("");
		btnNewButton_55.setBackground(Color.PINK);
		panel_2.add(btnNewButton_55);

		JButton btnNewButton_56 = new JButton("");
		btnNewButton_56.setBackground(Color.PINK);
		panel_2.add(btnNewButton_56);

		JButton btnNewButton_57 = new JButton("");
		btnNewButton_57.setBackground(Color.PINK);
		panel_2.add(btnNewButton_57);

		JButton btnNewButton_58 = new JButton("");
		btnNewButton_58.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_58);

		JButton btnNewButton_59 = new JButton("");
		btnNewButton_59.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_59);

		JButton btnNewButton_60 = new JButton("");
		btnNewButton_60.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_60);

		JButton btnNewButton_61 = new JButton("");
		btnNewButton_61.setBackground(Color.PINK);
		panel_2.add(btnNewButton_61);

		JButton btnNewButton_62 = new JButton("");
		btnNewButton_62.setBackground(Color.PINK);
		panel_2.add(btnNewButton_62);

		JButton btnNewButton_63 = new JButton("");
		btnNewButton_63.setBackground(Color.PINK);
		panel_2.add(btnNewButton_63);

		JButton btnNewButton_64 = new JButton("");
		btnNewButton_64.setBackground(Color.PINK);
		panel_2.add(btnNewButton_64);

		JButton btnNewButton_65 = new JButton("");
		btnNewButton_65.setBackground(Color.PINK);
		panel_2.add(btnNewButton_65);

		JButton btnNewButton_66 = new JButton("");
		btnNewButton_66.setBackground(Color.PINK);
		panel_2.add(btnNewButton_66);

		JButton btnNewButton_67 = new JButton("");
		btnNewButton_67.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_67);

		JButton btnNewButton_68 = new JButton("");
		btnNewButton_68.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_68);

		JButton btnNewButton_69 = new JButton("");
		btnNewButton_69.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_69);

		JButton btnNewButton_70 = new JButton("");
		btnNewButton_70.setBackground(Color.PINK);
		panel_2.add(btnNewButton_70);

		JButton btnNewButton_71 = new JButton("");
		btnNewButton_71.setBackground(Color.PINK);
		panel_2.add(btnNewButton_71);

		JButton btnNewButton_72 = new JButton("");
		btnNewButton_72.setBackground(Color.PINK);
		panel_2.add(btnNewButton_72);

		JButton btnNewButton_73 = new JButton("");
		btnNewButton_73.setBackground(Color.PINK);
		panel_2.add(btnNewButton_73);

		JButton btnNewButton_74 = new JButton("");
		btnNewButton_74.setBackground(Color.PINK);
		panel_2.add(btnNewButton_74);

		JButton btnNewButton_75 = new JButton("");
		btnNewButton_75.setBackground(Color.PINK);
		panel_2.add(btnNewButton_75);

		JButton btnNewButton_76 = new JButton("");
		btnNewButton_76.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_76);

		JButton btnNewButton_77 = new JButton("");
		btnNewButton_77.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_77);

		JButton btnNewButton_78 = new JButton("");
		btnNewButton_78.setBackground(Color.CYAN);
		panel_2.add(btnNewButton_78);

		JButton btnNewButton_79 = new JButton("");
		btnNewButton_79.setBackground(Color.PINK);
		panel_2.add(btnNewButton_79);

		JButton btnNewButton_80 = new JButton("");
		btnNewButton_80.setBackground(Color.PINK);
		panel_2.add(btnNewButton_80);

		JButton btnNewButton_81 = new JButton("");
		btnNewButton_81.setBackground(Color.PINK);
		panel_2.add(btnNewButton_81);


		buttonsVector = new Vector<JButton>(81); //存放九宫格81个按钮
		numberButtonVector = new Vector<JButton>(9); //存放9个数字键
		radioButtonsvVector = new Vector<JRadioButton>();

		backup = new int[9][9]; //题目的备份

		//把3个单选框放入容器中
		radioButtonsvVector.add(rdbtnNewRadioButton);
		radioButtonsvVector.add(radioButton);
		radioButtonsvVector.add(radioButton_1);

		//把81个格子放入容器中
		buttonsVector.add(btnNewButton_2);
		buttonsVector.add(btnNewButton_3);
		buttonsVector.add(btnNewButton_4);
		buttonsVector.add(btnNewButton_5);
		buttonsVector.add(btnNewButton_6);
		buttonsVector.add(btnNewButton_7);
		buttonsVector.add(btnNewButton_8);
		buttonsVector.add(btnNewButton_9);
		buttonsVector.add(btnNewButton_10);
		buttonsVector.add(btnNewButton_11);
		buttonsVector.add(btnNewButton_12);
		buttonsVector.add(btnNewButton_13);
		buttonsVector.add(btnNewButton_14);
		buttonsVector.add(btnNewButton_15);
		buttonsVector.add(btnNewButton_16);
		buttonsVector.add(btnNewButton_17);
		buttonsVector.add(btnNewButton_18);
		buttonsVector.add(btnNewButton_19);
		buttonsVector.add(btnNewButton_20);
		buttonsVector.add(btnNewButton_21);
		buttonsVector.add(btnNewButton_22);
		buttonsVector.add(btnNewButton_23);
		buttonsVector.add(btnNewButton_24);
		buttonsVector.add(btnNewButton_25);
		buttonsVector.add(btnNewButton_26);
		buttonsVector.add(button_9);
		buttonsVector.add(btnNewButton_27);
		buttonsVector.add(btnNewButton_28);
		buttonsVector.add(btnNewButton_29);
		buttonsVector.add(btnNewButton_30);
		buttonsVector.add(btnNewButton_31);
		buttonsVector.add(btnNewButton_32);
		buttonsVector.add(btnNewButton_33);
		buttonsVector.add(btnNewButton_34);
		buttonsVector.add(btnNewButton_35);
		buttonsVector.add(btnNewButton_36);
		buttonsVector.add(btnNewButton_37);
		buttonsVector.add(btnNewButton_38);
		buttonsVector.add(btnNewButton_39);
		buttonsVector.add(btnNewButton_40);
		buttonsVector.add(btnNewButton_41);
		buttonsVector.add(btnNewButton_42);
		buttonsVector.add(btnNewButton_43);
		buttonsVector.add(btnNewButton_44);
		buttonsVector.add(btnNewButton_45);
		buttonsVector.add(btnNewButton_46);
		buttonsVector.add(btnNewButton_47);
		buttonsVector.add(btnNewButton_48);
		buttonsVector.add(btnNewButton_49);
		buttonsVector.add(btnNewButton_50);
		buttonsVector.add(btnNewButton_51);
		buttonsVector.add(btnNewButton_52);
		buttonsVector.add(btnNewButton_53);
		buttonsVector.add(btnNewButton_54);
		buttonsVector.add(btnNewButton_55);
		buttonsVector.add(btnNewButton_56);
		buttonsVector.add(btnNewButton_57);
		buttonsVector.add(btnNewButton_58);
		buttonsVector.add(btnNewButton_59);
		buttonsVector.add(btnNewButton_60);
		buttonsVector.add(btnNewButton_61);
		buttonsVector.add(btnNewButton_62);
		buttonsVector.add(btnNewButton_63);
		buttonsVector.add(btnNewButton_64);
		buttonsVector.add(btnNewButton_65);
		buttonsVector.add(btnNewButton_66);
		buttonsVector.add(btnNewButton_67);
		buttonsVector.add(btnNewButton_68);
		buttonsVector.add(btnNewButton_69);
		buttonsVector.add(btnNewButton_70);
		buttonsVector.add(btnNewButton_71);
		buttonsVector.add(btnNewButton_72);
		buttonsVector.add(btnNewButton_73);
		buttonsVector.add(btnNewButton_74);
		buttonsVector.add(btnNewButton_75);
		buttonsVector.add(btnNewButton_76);
		buttonsVector.add(btnNewButton_77);
		buttonsVector.add(btnNewButton_78);
		buttonsVector.add(btnNewButton_79);
		buttonsVector.add(btnNewButton_80);
		buttonsVector.add(btnNewButton_81);

		numberButtonVector.add(button);    //数字键1
		numberButtonVector.add(button_1);  //数字键2
		numberButtonVector.add(button_2);  //数字键3
		numberButtonVector.add(button_3);  //数字键4
		numberButtonVector.add(button_4);  //数字键5
		numberButtonVector.add(button_5);  //数字键6
		numberButtonVector.add(button_6);  //数字键7
		numberButtonVector.add(button_7);  //数字键8
		numberButtonVector.add(button_8);  //数字键9

		core = new GameCore(holeNumber);
		//备份一份题目
		backUpProblem(); 
		initGame();


		//为81个按钮添加监视器
		for (int i = 0; i < 81; i ++) {
			buttonsVector.elementAt(i).addActionListener(this);
		}
		//为9个数字键添加监视器
		for (int i = 0; i < 9; i ++) {
			numberButtonVector.elementAt(i).addActionListener(this);
		}



	}

	//监视器
	@Override 
	public void actionPerformed(ActionEvent e) {

		//控制81个显示按钮
		JButton button = null;
		for (int i = 0; i < 81; i ++) {
			if (e.getSource() == buttonsVector.elementAt(i)) {
				button = buttonsVector.elementAt(i);

				if (button.isEnabled()) {
					if (choosedButton != null) {
						setButtonColor(choosedButton);
					}
					choosedButton = buttonsVector.elementAt(i);
					buttonsVector.elementAt(i).setBackground(Color.LIGHT_GRAY);	
				}
			}
		}

		//控制9个数字键按钮
		String numberStr;
		int num, index, x, y;;
		for (int i = 0; i < 9; i ++) {
			if (e.getSource() == numberButtonVector.elementAt(i)) {
				numberStr = numberButtonVector.elementAt(i).getText().trim();
				num = Integer.parseInt(numberStr);

				if ( isLegal(num) ) {
					choosedButton.setText(numberStr);  //如果符合规则则填入所选数字

					//修改Test布局表
					index = buttonsVector.indexOf(choosedButton);
					x = index / 9;
					y = index % 9;
					core.test[x][y] = num;

					//判读是否成功了
					if (isFinish()) 
						JOptionPane.showMessageDialog(null, "好厉害啊，游戏成功了，开始下一局吧");
				}

			}
		}
		//重新开始按钮监听器
		if (e.getSource() == btnNewButton) {

			if (radioButtonsvVector.elementAt(0).isSelected())  holeNumber = 25;
			if (radioButtonsvVector.elementAt(1).isSelected())  holeNumber = 35;
			if (radioButtonsvVector.elementAt(2).isSelected())  holeNumber = 45;
			core.setHoleNumber(holeNumber); //设置要挖的洞的数量
			core.initialGame();
			backUpProblem();  //备份问题

			initGame();



		}


	}


	//初始化函数，负责初始化显示方面的数据
	private void initGame() {

		for (int i = 0; i < 81; i ++) {
			buttonsVector.elementAt(i).setEnabled(true);
			buttonsVector.elementAt(i).setForeground(Color.BLACK);
		}


		int index = 0;
		for (int i = 0; i < 9; i ++) {
			for (int j = 0; j < 9; j ++) {

				if (core.test[i][j] != 0) {
					buttonsVector.elementAt(index).setText(String.valueOf(core.test[i][j]));
					buttonsVector.elementAt(index).setForeground(Color.RED);
					buttonsVector.elementAt(index).setEnabled(false);
				} else {
					buttonsVector.elementAt(index).setText(null);
				}

				++ index;//推进到下一个按钮
			}
		}
	}

	//判断操作是否符合游戏填数规则，符合返回TRHE
	public boolean isLegal(int num) { 
		int x, y;
		boolean flag = true;  //默认不含有相同元素 
		int index = buttonsVector.indexOf(choosedButton);
		if (index != -1) {
			//找到Button
			x = index / 9;
			y = index % 9;

			for (int i = 0; i < 9; i ++){  // 判断行和列是否含有num元素
				if (core.test[x][i] == num){ //行判断
					flag = false;
					break;
				}
				if (core.test[i][y] == num){  //列判断
					flag = false;
					break;
				}
			}

			//判断第（tempa, tempb)小九宫格是否含有num元素
			int tempa = x / 3;
			int tempb = y / 3;
			for (int i = tempa * 3; i < (3 + tempa * 3); i ++){//扫描该（a，b）所在的小九宫格
				for (int j = tempb * 3; j < (3 + tempb * 3); j ++){
					if (core.test[i][j] == num){
						flag = false;
						break;
					}
				}
			}
		}
		return flag;	
	}


	//通过按钮设置它应有的颜色
	public void setButtonColor(JButton button){ 
		int index = buttonsVector.indexOf(button);
		int x = ( index / 9 ) / 3;
		int y = ( index % 9 ) / 3;
		int i = x * 3 + y;

		if (i == 0 || i == 2 || i == 4 || i == 6 || i == 8){ //设为粉红色
			buttonsVector.elementAt(index).setBackground(Color.PINK);
		} else if (i == 1 || i == 3 || i == 5 || i == 7){ //设置为蓝色
			buttonsVector.elementAt(index).setBackground(Color.CYAN);		
		}

	}
	//判断81个格子是否都已填上数，填上则表示结束了，返回TRUE
	public boolean isFinish() {
		boolean flag = true; //默认为填完了
		for (int i = 0; i < 9; i ++) {
			for (int j = 0; j < 9; j ++) {
				if (core.test[i][j] == 0) {
					flag = false;
					break; //只要有一个还为0，即表示没有填完
				}
			}
		}
		return flag;
	}
	//用来把Test的值拷贝给a数组
	private void backUpProblem() {

		for (int i = 0; i < 9; i ++) {
			for (int j = 0; j < 9; j ++) {
				backup[i][j] = core.test[i][j];
			}
		}

	}




}




