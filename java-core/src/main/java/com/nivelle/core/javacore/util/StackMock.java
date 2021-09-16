package com.nivelle.core.javacore.util;

import java.util.Stack;

/**
 * Stack
 *
 * @author nivelle
 * @date 2019/10/15
 */
public class StackMock {

    public static void main(String[] args) {
        Stack stack = new Stack();
        stack.push(1);
        stack.push(2);
        System.out.println("stack1 is: " + stack);

        Object popResult = stack.pop();
        System.out.println("pop is:" + popResult);
        System.out.println("after pop stack2 is:" + stack);

        stack.push(2);
        Object peekResult1 = stack.peek();
        System.out.println("peek result1 is:" + peekResult1);

        Object peekResult2 = stack.peek();
        System.out.println("peek result2 is:" + peekResult2);

        System.out.println("stack3 is: " + stack);

        Object firstElement = stack.firstElement();
        System.out.println("firstElement is:" + firstElement);

        Object lastElement = stack.lastElement();
        System.out.println("lastElement is:" + lastElement);

        stack.push(3);

        Object lastElement2 = stack.lastElement();

        System.out.println("lastElement2 is:" + lastElement2);

    }
}

/**
 * 基于数组实现的
 */
class ArrayStack {

}

