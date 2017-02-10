import java.util.Random;


public class GameCore 
{ 
	private int[] hole = null;  
	public int[][] test = null;  //�ڹ����������

	private int nHole;
	private int N;
	private int num;
	private int X, Y;
	private int FillNum;
	private int Cnum;  
	private int holeNumber;  //�����ڶ�����

	//���캯��
	public GameCore(int holeNumber) {
		hole = new int[81];
		test = new int[9][9];  //����ںö��Ĳ���
		this.holeNumber = holeNumber;

		initialGame();  //��ʼ��
	}

	//�����ڶ�����
	public void setHoleNumber(int holeNumber) {
		this.holeNumber = holeNumber;
	}
	//Generator����    ������ԭʼ�������  
	private void Generator() {  
		Cnum = 0;
		N = 9;
		nHole = 0;   

		int count = 0;  
		num = 16 + (int)(Math.random() * 6);  //����10��16֮��������,������16,Ҳ����10��15������   
		//whileѭ��  ��������������������������������  �Դ����������ķ�֧,���Ч��   
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

	//bPack����  �ж��Ƿ�ɽ� n ���� M[y][x] ��  
	private boolean bPack (int x, int y, int n) {  
		//forѭ��  �ݺ��ж�  
		for (int i=0; i<N; i++)                 //�� M[0~N-1][x]  
			if (n == test[x][i] || n == test[i][y])   //�� M[y][0~N-1] ���Ѵ��� n  
				return false;                   //�򷵻� false  
		//forѭ�� �����ж�  
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
		//���ؽ��  
		return true;  
	}  

	//fill����  �˷���������������������ӵ�����    
	private void fill(int num) {  
		if(Cnum == 81){  
			return;  //��ʾ��������  
		}  
		if(test[Cnum / 9][Cnum % 9] != 0) {  
			++Cnum;  
			fill(Cnum);    //λ�ò�Ϊ0,�����һ��  
			return;  
		} else {  
			//��1��9����ѡ���������ȥ  
			for(int x = 1; x != 11; x++) {    //x=10 ���ڱ�
				if(Cnum == 81){  
					return;  //��ʾ��������  
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

	//����ڶ���������Ϸ����
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