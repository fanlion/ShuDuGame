import java.util.Random;


public class GameCore 
{ 
	private int[] hole = null;  
	public int[][] test = null;  //挖过洞后的数组

	private int nHole;
	private int N;
	private int num;
	private int X, Y;
	private int FillNum;
	private int Cnum;  
	private int holeNumber;  //设置挖洞数量

	//构造函数
	public GameCore(int holeNumber) {
		hole = new int[81];
		test = new int[9][9];  //存放挖好洞的布局
		this.holeNumber = holeNumber;

		initialGame();  //初始化
	}

	//设置挖洞数量
	public void setHoleNumber(int holeNumber) {
		this.holeNumber = holeNumber;
	}
	//Generator方法    ，产生原始填充数独  
	private void Generator() {  
		Cnum = 0;
		N = 9;
		nHole = 0;   

		int count = 0;  
		num = 16 + (int)(Math.random() * 6);  //产生10到16之间的随机数,不包括16,也就是10到15的整数   
		//while循环  尝试在数组中填入符合数独规则的数字  以此来减少树的分支,提高效率   
		while(count != num) {  
			X = (int)(Math.random() * 9);  
			Y = (int)(Math.random() * 9);  
			FillNum = 1 + (int)(Math.random() * 9);  
			if(bPack(X, Y, FillNum))  
			{  
				test[X][Y] = FillNum;  
				count++;  
			}  
		}  
	}  

	//bPack方法  判断是否可将 n 填入 M[y][x] 里  
	private boolean bPack (int x, int y, int n) {  
		//for循环  纵横判断  
		for (int i=0; i<N; i++)                 //若 M[0~N-1][x]  
			if (n == test[x][i] || n == test[i][y])   //或 M[y][0~N-1] 中已存在 n  
				return false;                   //则返回 false  
		//for循环 区域判断  
		int D_X, D_Y, OrderNum;  
		OrderNum=9*(x+1)+(y+1);  
		D_X=((int)(x / 3)) * 3;  
		D_Y=((int)(y / 3)) * 3;  
		for(int count = 0; count != 9; count++) {  
			if(D_X == x && D_Y == y)  
				continue;  
			if(test[(D_X + count / 3)][(D_Y + count % 3)] == n)  
				return false;  
		}  
		//返回结果  
		return true;  
	}  

	//fill方法  此方法用来填充完整已有数子的数独    
	private void fill(int num) {  
		if(Cnum == 81){  
			return;  //表示填满返回  
		}  
		if(test[Cnum / 9][Cnum % 9] != 0) {  
			++Cnum;  
			fill(Cnum);    //位置不为0,填充下一个  
			return;  
		} else {  
			//从1到9里面选择数字填进去  
			for(int x = 1; x != 11; x++) {    //x=10 是哨兵
				if(Cnum == 81){  
					return;  //表示填满返回  
				}  
				if(x == 10 && Cnum != 0) {  
					--Cnum;  
					test[(Cnum / 9)][(Cnum % 9)] = 0;  
					return;     
				}  
				if(bPack((Cnum / 9), (Cnum % 9), x)){  
					test[(Cnum / 9)][(Cnum % 9)] = x;  
					++Cnum;  
					fill(Cnum);  
				}  
			}  
		}  
	}  

	public void initialGame() {
		
		for (int i = 0; i < 9; i ++) {
			for (int j = 0; j < 9; j ++) 
				test[i][j] = 0;

		}
		for (int i = 0; i < 81; i ++) {
			hole[i] = 0;
		}

		Generator();
		fill(Cnum);
		DigHole();
	}

	//随机挖洞，生成游戏布局
	private void DigHole() {
		Random random = new Random();
		int r, x, y;
		for (int i = 0; i < holeNumber;  ){
			r = random.nextInt(81);
			x = r / 9;
			y = r % 9;
			if (test[x][y] != 0) {
				test[x][y] = 0;
				i ++;
			}
		}
	}


};