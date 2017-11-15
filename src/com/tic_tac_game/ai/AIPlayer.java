package com.tic_tac_game.ai;

import java.util.Random;

public class AIPlayer {
	
	static final int INFINITY = 100 ;   // ��ʾ�����ֵ  
	static final int WIN = +INFINITY ;   // MAX���������Ϊ������  
	static final int LOSE = -INFINITY ;   // MAX����С���棨��MIN�������棩Ϊ������  
	static final int DOUBLE_LINK = INFINITY / 2 ;   // ���ͬһ�С��л�Խ�������������������  
	static final int INPROGRESS = 1 ;   // �Կɼ����£�û��ʤ����;֣�  
	static final int DRAW = 0 ;   // �;�  
	static final int CIRCLE = -1;
	static final int CROSS = 1;
	static final int [][] WIN_STATUS =   {  
										      {   0, 1, 2 },  
										      { 3, 4, 5 },  
										      { 6, 7, 8 },  
										      { 0, 3, 6 },  
										      { 1, 4, 7 },  
										      { 2, 5, 8 },  
										      { 0, 4, 8 },  
										      { 2, 4, 6   }  
										};
	
	
	
	static final int[] INITIAL_POS_VALUE   = {  
										      3, 2, 3,  
										      2, 4, 2,  
										      3, 2, 3  
		
	
	};  
		/** 
		 * ��ֵ�������ṩһ������ʽ��ֵ����������ϷAI�ĸߵ� 
		 */  
	public int gameState ( char[] board ) {  
	       int result = INPROGRESS ;  
	       boolean isFull = true ;
	       
	       int sum = 0;  
	       int index = 0;
	       
	       // is game over?  
	       for ( int pos=0; pos<9; pos++){  
	             char chess = board[pos];  
	             if ( chess == '\0'){  
	                  isFull = false ;  
	            } else {  
	                  sum += chess;  
	                  index = pos;  
	            }  
	      }  
	        
	       // ����ǳ�ʼ״̬����ʹ�ÿ��ֿ�  
	       boolean isInitial = (sum== CROSS ||sum== CIRCLE );  
	       if (isInitial){  
	             return (sum== CROSS ?1:-1)*INITIAL_POS_VALUE[index];  
	      }  
	        
	       // is Max win/lose?  
	       for ( int[]status : WIN_STATUS ){  
	             char   chess = board[status[0]];  
	             if (chess== '\0' ){  
	                   break ;  
	            }  
	             int   i = 1;  
	             for (; i<status.length; i++){  
	                   if (board[status[i]]!=chess){  
	                         break ;  
	                  }  
	            }  
	             if (i==status.length){  
	                  result = chess== CROSS ? WIN : LOSE ;  
	                   break ;  
	            }  
	      }  
	        
	       if (result!= WIN & result!= LOSE ){  
	              
	             if (isFull){  
	                   // is draw  
	                  result = DRAW ;  
	            } else {  
	                   // check double link  
	                   // finds[0]->'x', finds[1]->'o'  
	                   int [] finds =   new   int [2];  
	                   for ( int [] status :   WIN_STATUS ){  
	                         char   chess =  '\0' ;  
	                         boolean   hasEmpty =   false ;  
	                         int   count = 0;  
	                         for ( int   i=0; i<status.length; i++){  
	                               if (board[status[i]]=='\0'){  
	                                    hasEmpty =   true ;  
	                              } else {  
	                                     if (chess== '\0' ){  
	                                          chess = board[status[i]];  
	                                    }  
	                                     if (board[status[i]]==chess){  
	                                          count++;  
	                                    }  
	                              }  
	                        }  
	                         if (hasEmpty && count>1){  
	                               if (chess== '0' ){  
	                                    finds[0]++;  
	                              } else {  
	                                    finds[1]++;  
	                              }  
	                        }  
	                  }  
	                   // check if two in one line  
		                   if (finds[1]>0){  
		                        result = - DOUBLE_LINK ;  
		                  } else  
		                   if (finds[0]>0){  
		                        result =   DOUBLE_LINK ;  
		                  }  
		            }  
		      }  
		  return result;  
		}  
	
	
	public   int   minimax( char [] board,   int   depth){  
	       int [] bestMoves =   new   int [9];  
	       int   index = 0;  
	        
	       int   bestValue = - INFINITY ;  
	       for ( int   pos=0; pos<9; pos++){  
	              
	             if (board[pos]== '\0'){  
	                  board[pos] =  'X' ;  
	                    
	                   int   value = min(board, depth, - INFINITY , + INFINITY );  
	                   if (value>bestValue){  
	                        bestValue = value;  
	                        index = 0;  
	                        bestMoves[index] = pos;  
	                  } else  
	                   if (value==bestValue){  
	                        index++;  
	                        bestMoves[index] = pos;  
	                  }  
	                    
	                  board[pos] = '\0' ;  
	            }  
	              
	      }  
	        
	       if (index>1){  
	            index = ( new   Random (System. currentTimeMillis ()).nextInt()>>>1)%index;  
	      }  
	       return   bestMoves[index];  
	        
	}  
	/** 
	 * ����'x'����ֵԽ�����Խ���� 
	 */  
	public   int   max( char [] board,   int   depth,   int   alpha,   int   beta){  
	        
	       int   evalValue =   gameState (board);  
	        
	       boolean   isGameOver = (evalValue== WIN   || evalValue== LOSE   || evalValue== DRAW );  
	       if (beta<=alpha){  
	             return   evalValue;  
	      }  
	       if (depth==0 || isGameOver){  
	             return   evalValue;  
	      }  
	        
	       int   bestValue = - INFINITY ;  
	       for ( int   pos=0; pos<9; pos++){  
	              
	             if (board[pos]== '\0' ){  
	                   // try  
	                  board[pos] =  'X' ;  
	                    
	                   //   maximixing  
	                  bestValue = Math. max (bestValue, min(board, depth-1, Math. max (bestValue, alpha), beta));  
	                    
	                   // reset  
	                  board[pos] =  '\0' ;  
	            }  
	              
	      }  
	        
	       return   evalValue;  
	        
	}  
	/** 
	 * ����'o'����ֵԽС����Խ���� 
	 */  
	public   int   min( char [] board,   int   depth,   int   alpha,   int   beta){  
	        
	       int   evalValue =   gameState (board);  
	        
	       boolean   isGameOver = (evalValue== WIN   || evalValue== LOSE   || evalValue== DRAW );  
	       if (alpha>=beta){  
	             return   evalValue;  
	      }  
	       // try  
	       if (depth==0 || isGameOver || alpha>=beta){  
	             return   evalValue;  
	      }  
	        
	       int   bestValue = + INFINITY ;  
	       for ( int   pos=0; pos<9; pos++){  
	              
	             if (board[pos]== '\0' ){  
	                   // try  
	                  board[pos] =   'O';  
	                    
	                   //   minimixing  
	                  bestValue = Math.min(bestValue, max(board, depth-1, alpha, Math.min(bestValue, beta)));  
	                    
	                   // reset  
	                  board[pos] =  '\0' ;  
	            }  
	              
	      }  
	        
	       return   evalValue;  
	        
	}   
}