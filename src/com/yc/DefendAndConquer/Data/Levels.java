package com.yc.DefendAndConquer.Data;

public class Levels{
	private final static String[] name = new String[]{
		"森林 Lv.1","森林 Lv.2","森林 Lv.3","森林 Lv.4","森林 Lv.5","森林 Lv.6"
	};
	
	//城市
	private final static int[][][] cities = new int[][][]{
		{	
			{0,65,70,1,2,4},  //{id,x,y,belong,level,maxLevel}
			{1,213,100,-1,0,0},  //纯粹的路径点
			{2,284,104,-1,0,0}, //2
			{3,197,128,0,1,4}, //3
			{4,340,109,-1,0,0}, //4
			{5,370,-30,0,2,4}, //5
			{6,355,130,0,2,4}, //6
			{7,475,281,-1,0,0}, //7
			{8,210,280,2,1,4}, //8
			{9,460,335,2,2,4}, //9
			{10,591,275,-1,0,0}, //10
			{11,620,260,0,1,4} //11
		},
		{	
			{0,185,-10,1,4,4},  //{id,x,y,belong,level,maxLevel}
			{1,260,75,-1,0,0},  //纯粹的路径点
			{2,300,-10,0,2,4}, //2
			{3,260,110,-1,0,0}, //3
			{4,88,149,0,3,4}, //4
			{5,360,195,-1,0,0}, //5
			{6,249,165,0,3,4}, //6
			{7,223,274,-1,0,0}, //7
			{8,210,279,0,3,4}, //8
			{9,338,261,0,4,4}, //9
			{10,472,321,-1,0,0}, //10
			{11,429,192,2,1,4}, //11
			{12,427,318,2,1,4}, //12
			{13,503,267,2,4,4} //13
		},
		{	
			{0,103,56,2,3,5},  //{id,x,y,belong,level,maxLevel}
			{1,208,175,-1,0,0},  //纯粹的路径点
			{2,227,81,2,3,5}, //2
			{3,169,174,0,3,5}, //3
			{4,157,321,-1,0,0}, //4
			{5,164,340,0,2,5}, //5
			{6,311,284,2,4,5}, //6
			{7,463,301,2,2,5}, //7
			{8,247,204,0,2,5}, //8
			{9,546,29,1,3,5}, //9
			{10,622,149,-1,0,0}, //10
			{11,499,106,1,3,5}, //11
			{12,565,140,1,3,5} //12
		},
		{	
			{0,217,23,1,3,5},  //{id,x,y,belong,level,maxLevel}
			{1,258,152,-1,0,0},  //纯粹的路径点
			{2,215,160,-1,0,0}, //2
			{3,104,96,1,3,5}, //3
			{4,152,193,0,2,5}, //4
			{5,264,102,0,3,5}, //5
			{6,518,64,0,4,5}, //6
			{7,577,251,-1,0,0}, //7
			{8,594,178,0,2,5}, //8
			{9,525,315,-1,0,0}, //9
			{10,395,218,2,2,5}, //10
			{11,425,300,2,2,5}, //11
			{12,539,286,2,2,5}, //12
			{13,582,352,2,4,5} //13
		},
		{	
			{0,181,0,0,2,5},  //{id,x,y,belong,level,maxLevel}
			{1,196,159,-1,0,0},  //纯粹的路径点
			{2,75,78,0,1,5}, //2
			{3,117,164,1,3,5}, //3
			{4,176,268,-1,0,0}, //4
			{5,213,187,1,3,5}, //5
			{6,214,97,0,3,5}, //6
			{7,396,171,-1,0,0}, //7
			{8,439,44,0,3,5}, //8
			{9,571,88,-1,0,0}, //9
			{10,618,107,-1,0,0}, //10
			{11,598,0,0,3,5}, //11
			{12,615,215,-1,0,0}, //12
			{13,523,137,0,5,5}, //13
			{14,602,305,-1,0,0}, //14
			{15,632,235,2,2,5}, //15
			{16,600,374,-1,0,0}, //16
			{17,600,347,2,3,5}, //17
			{18,431,311,2,3,5}, //18
			{19,408,368,-1,0,0}, //19
			{20,352,249,2,2,5} //20
		},
		{	
			{0,137,7,1,3,5},  //{id,x,y,belong,level,maxLevel}
			{1,130,156,-1,0,0},  //纯粹的路径点
			{2,16,48,1,4,5}, //2
			{3,122,203,0,3,5}, //3
			{4,21,308,0,4,5}, //4
			{5,241,199,0,4,5}, //5
			{6,408,276,-1,0,0}, //6
			{7,414,146,0,1,5}, //7
			{8,480,153,-1,0,0}, //8
			{9,429,41,0,3,5}, //9
			{10,554,86,0,2,5}, //10
			{11,672,105,2,3,5}, //11
			{12,730,231,-1,0,0}, //12
			{13,644,237,2,2,5}, //13
			{14,552,341,-1,0,0}, //14
			{15,407,320,2,3,5} //15
		}
	};
	
	//相邻路径点
	private final static int[][][]  related = new int[][][]{
		{
			{1},      //0
			{0,2},    //1
			{1,3,4},  //2
			{2},      //3
			{2,5,6},  //4
			{4},      //5
			{4,7},    //6
			{6,8,9,10},  //7
			{7},      //8
			{7},      //9
			{7,11},   //10
			{10}      //11
		},
		{
			{1},      //0
			{0,2,3},    //1
			{1},  //2
			{1,4,5},      //3
			{3,7},  //4
			{3,6},      //5
			{5,7},    //6
			{4,6,8},  //7
			{7,9},      //8
			{8,10},      //9
			{9,11,12,13},   //10
			{10},      //11
			{10},      //12
			{10}      //13
		},
		{
			{1},      //0
			{0,2,3},    //1
			{1},  //2
			{1,4,8},      //3
			{3,5},  //4
			{4,6},      //5
			{5,7,8},    //6
			{6},  //7
			{3,6,9},      //8
			{8,10},      //9
			{9,11,12},   //10
			{10,12},      //11
			{11,10}  //12
		},
		{
			{1},      //0
			{0,2,5},    //1
			{1,3,4},  //2
			{2},      //3
			{2},  //4
			{1,6},      //5
			{5,7},    //6
			{6,8,9},  //7
			{7},      //8
			{7,10,11,12},      //9
			{9},   //10
			{9},      //11
			{9,13},    //12
			{12}   //13
		},
		{
			{1},      //0
			{0,2,3,6},    //1
			{1},  //2
			{1,4},      //3
			{3,5},  //4
			{4},      //5
			{1,7},    //6
			{6,8},  //7
			{7,9},      //8
			{8,10},      //9
			{9,11,12},   //10
			{10},      //11
			{10,13,14},    //12
			{12},  //13
			{12,15,16},  //14
			{14}, //15
			{14,17,18}, //16
			{16},  //17
			{16,19}, //18
			{18,20}, //19
			{19} //20
		},
		{
			{1},      //0
			{0,2,3},    //1
			{1},  //2
			{1,4,5},      //3
			{3},  //4
			{3,6},      //5
			{5,7,8,14},    //6
			{6,8},  //7
			{6,7,9,10},      //8
			{8},      //9
			{8,11,12},   //10
			{10,12},      //11
			{10,11,13},    //12
			{12,14},  //13
			{13,6,15},  //14
			{14} //15
		}
	};
	
	public static String getName(int level){
		return name[level-1];
	}
	public static int getCityNum(int level){
		return cities[level-1].length;
	}
	public static int[] getCity(int level,int index){
		return cities[level-1][index];
	}
	
	public static int[] getRelated(int level,int index){
		return related[level-1][index];
	}
}