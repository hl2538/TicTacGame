package com.tic_tac_game.ai;

import java.util.Random;

import com.tic_tac_game.Protocol;

public class AIPlayer {
	
	static final int INFINITY = 100 ;   
	static final int WIN = +INFINITY ;   
	static final int LOSE = -INFINITY ;   
	static final int DOUBLE_LINK = INFINITY / 2 ;   
	static final int INPROGRESS = 1 ;   
	static final int DRAW = 0 ;   
	static final int CIRCLE = -1;
	static final int CROSS = 1;
	static final int [][] WIN_STATUS =   {  
										      { 0, 1, 2 },  
										      { 3, 4, 5 },  
										      { 6, 7, 8 },  
										      { 0, 3, 6 },  
										      { 1, 4, 7 },  
										      { 2, 5, 8 },  
										      { 0, 4, 8 },  
										      { 2, 4, 6 }  
										};
	
	
	
	static final int[] INITIAL_POS_VALUE   = {  
										      3, 2, 3,  
										      2, 4, 2,  
										      3, 2, 3  
		
	
	};  
	
	private int[] tieClient = {4,2,8,1,3};
	private int[] tieServer = {5,6,0,7};
	private int cindex = 0;
	private int sindex = 0;
	
	
	public int AIposition(boolean flag) {
		if(flag == true)return tieClient[cindex++];
		else return tieServer[sindex++];
	}
	
	
	public int gameState ( int[] board ) {  
	       int result = INPROGRESS ;  
	       boolean isFull = isFull(board);
	       int sum = 0;  
	       int index = 0;
	       
	       for ( int pos=0; pos<9; pos++){  
	             int chess = board[pos];  
                  sum += chess;  
                  index = pos;  
	      }  
	        
	       boolean isInitial = (sum==Protocol.TYPE_CROSS||sum==Protocol.TYPE_CIRCLE);  
	       if (isInitial){
	    	   return (sum== CROSS ?1:-1)*INITIAL_POS_VALUE[index];  
	      }  
	        
	       // is Max win/lose?  
	       for ( int[]status : WIN_STATUS ){  
	             int chess = board[status[0]];  
	             if (chess== Protocol.TYPE_NONE ){  
	                  continue;  
	            }  
	            int i = 1;
	            for(; i<status.length; i++){  
                   if (board[status[i]]!=chess)continue ;  
	            }  
	             if (i==status.length){  
	                  result = chess== Protocol.TYPE_CROSS ? WIN : LOSE ;  
	                   continue;  
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
	                   for ( int [] status : WIN_STATUS ){  
	                         int chess = Protocol.TYPE_NONE;  
	                         boolean hasEmpty = false ;  
	                         int   count = 0;  
	                         for(int i=0; i<status.length; i++){  
	                               if (board[status[i]]==Protocol.TYPE_NONE){  
	                                    hasEmpty = true ;  
	                               }else {  
	                                     if (chess== Protocol.TYPE_NONE ){  
	                                          chess = board[status[i]];  
	                                    }  
	                                     if (board[status[i]]==chess){  
	                                          count++;  
	                                    }  
	                              }  
	                         }  
	                         if (hasEmpty && count>1){  
	                               if (chess == Protocol.TYPE_NONE ){  
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
	
	
	public   int   xminimax( int[] board, int depth){   //depth = 6; max = 8
	       int [] bestMoves =   new   int [9];  
	       int   index = 0;  
	        
	       int   bestValue = - INFINITY ;  
	       for ( int   pos=0; pos<9; pos++){  
	              
	             if (board[pos]== Protocol.TYPE_NONE){  
	                  board[pos] =  Protocol.TYPE_CROSS ;  
	                    
	                   int   value = max(board, depth, - INFINITY , + INFINITY );  
	                   if (value>bestValue){  
	                        bestValue = value;  
	                        index = 0;  
	                        bestMoves[index] = pos;  
	                  } else  
	                   if (value==bestValue){  
	                        index++;  
	                        bestMoves[index] = pos;  
	                  }  
	                    
	                  board[pos] = Protocol.TYPE_NONE ;  
	            }  
	              
	      }  
	        
	       if (index>1){  
	            index = ( new   Random (System. currentTimeMillis ()).nextInt()>>>1)%index;  
	      }  
	       return   bestMoves[index];      
	}  
	
	public   int   ominimax( int[] board, int depth){   //depth = 6; max = 8
	       int [] bestMoves =   new   int [9];  
	       int   index = 0;  
	        
	       int   bestValue = INFINITY ;  
	       for ( int   pos=0; pos<9; pos++){  
	              
	             if (board[pos]== Protocol.TYPE_NONE){  
	                  board[pos] =  Protocol.TYPE_CROSS ;  
	                    
	                   int   value = max(board, depth, - INFINITY , + INFINITY );  
	                   if (value<bestValue){  
	                        bestValue = value;  
	                        index = 0;  
	                        bestMoves[index] = pos;  
	                  } else  
	                   if (value==bestValue){  
	                        index++;  
	                        bestMoves[index] = pos;  
	                  }  
	                    
	                  board[pos] = Protocol.TYPE_NONE ;  
	            }  
	              
	      }  
	        
	       if (index>1){  
	            index = ( new   Random (System. currentTimeMillis ()).nextInt()>>>1)%index;  
	      }  
	       return   bestMoves[index];      
	}  
	

	public int max( int[] board, int depth, int alpha, int beta){  
	        
	       int   evalValue = gameState (board);  
	        
	       boolean   isGameOver = (evalValue== WIN   || evalValue== LOSE   || evalValue== DRAW );  
	       if (beta<=alpha){  
	             return   evalValue;  
	      }  
	       if (depth==0 || isGameOver){  
	             return   evalValue;  
	      }  
	        
	       int   bestValue = - INFINITY ;  
	       for ( int   pos=0; pos<9; pos++){  
	              
	             if (board[pos]== Protocol.TYPE_NONE ){  
	                   // try  
	                  board[pos] =  Protocol.TYPE_CROSS ;  
	                    
	                   //   maximixing  
	                  bestValue = Math.max(bestValue, min(board, depth-1, Math.max(bestValue, alpha), beta));  
	                    
	                   // reset  
	                  board[pos] =  Protocol.TYPE_NONE;  
	            }  
	              
	      }  
	        
	       return   evalValue;  
	        
	}  

	public   int   min( int [] board,   int   depth,   int   alpha,   int   beta){  
	        
	       int evalValue = gameState (board);  
	        
	       boolean   isGameOver = (evalValue== WIN || evalValue== LOSE || evalValue== DRAW );  
	       if (alpha>=beta){  
	             return  evalValue;  
	      }  
	       // try  
	       if (depth==0 || isGameOver || alpha>=beta){  
	             return   evalValue;  
	      }  
	        
	       int   bestValue = + INFINITY ;  
	       for ( int   pos=0; pos<9; pos++){  
	              
	             if (board[pos]== Protocol.TYPE_NONE){  
	                   // try  
	                  board[pos] =  Protocol.TYPE_CIRCLE;  
	                    
	                   //   minimixing  
	                  bestValue = Math.min(bestValue, max(board, depth-1, alpha, Math.min(bestValue, beta)));  
	                    
	                   // reset  
	                  board[pos] = Protocol.TYPE_NONE ;  
	            }  
	              
	      }  
	        
	       return   evalValue;  
	        
	}  
	public boolean isFull(int[] chessboard) {
		for(int i =0; i<chessboard.length; i++) {
			if(chessboard[i] == Protocol.TYPE_NONE) {
				return false;
			}
		}
		return true;
	}
}