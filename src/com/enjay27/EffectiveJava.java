package com.enjay27;


import java.util.Arrays;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EffectiveJava {

    public static void main(String[] args) {

        int[][] board = {{0, 0, 0, 0, 0}, {0, 0, 1, 0, 3}, {0, 2, 5, 0, 1},
                {4, 2, 4, 4, 2}, {3, 5, 1, 3, 1}};
        int[] moves = {1, 5, 3, 5, 1, 2, 1, 4};

        System.out.println(solution(board, moves));


    }

    public static int solution(int[][] board, int[] moves) {

        int result = 0;

        Stack<Integer> integerStack = new Stack<>();

        integerStack.push(0);
        int pre = 0;

        Loop1:
        for (int m : moves) {
            for(int r = 0; r < board.length; r++){
                int gripped = board[r][m - 1];
                if(gripped > 0) {
                    board[r][m-1] = 0;
                    pre = integerStack.peek();
                    if(pre != gripped) {
                        integerStack.push(gripped);
                    }
                    else if(pre == gripped) {
                        integerStack.pop();
                    }
                    continue Loop1;
                }
            }
        }

        return integerStack.size();
    }



}

