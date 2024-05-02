import java.text.ParseException;
import java.util.Scanner;

public class ExpressionTree {

    private ExpTreeNode root = null;

    public ExpressionTree() {
    }


    public void parse(String line) throws ParseException {
        //Empty string or string with just whitespace
        if (line.trim().length() == 0) {
            return;
        }
        Scanner sc = new Scanner(line);
        root = expression(sc);
        sc.close();
    }


    public int evaluate() {
        return recursiveEvaluate(root);
    }

    private int recursiveEvaluate(ExpTreeNode node) {
        //empty tree
        if (node == null) {
            return 0;
        }

        //leaf node
        if (node.left == null && node.right == null) {
            return Integer.parseInt(node.el);
        }

        //recursively evaluate left and right children of root
        int leftSum = recursiveEvaluate(node.left);
        int rightSum = recursiveEvaluate(node.right);

        //Determine operation
        switch (node.el) {
            case "+":
                return leftSum + rightSum;
            case "-":
                return leftSum - rightSum;
            case "*":
                return leftSum * rightSum;
            default:
                return leftSum / rightSum;
        }
    }

    public String postfix() {
        return traversePostOrder(root);
    }


    private String traversePostOrder(ExpTreeNode node) {
        //Empty tree
        if (node == null) {
            return "";
        }
        String expression = "";

        expression += traversePostOrder(node.left);     //Traverse left subtree
        expression += traversePostOrder(node.right);    //Traverse right subtree
        return expression + (node.el + " ");
    }

 
    public String prefix() {
        return traversePreOrder(root);
    }


    private String traversePreOrder(ExpTreeNode node) {
        //Empty tree
        if (node == null) {
            return "";
        }

        String expression = "";
        expression += node.el + " ";
        expression += traversePreOrder(node.left);
        expression += traversePreOrder(node.right);
        return expression;
    }


    private ExpTreeNode expression (Scanner sc) throws ParseException {
        String oper;
        ExpTreeNode p1 , p2;
        p1 = term(sc); // calls method that parses a term
        while (sc.hasNext("\\+") || sc.hasNext("-")) {
            oper = sc.next();
            if (sc.hasNext()) {
                p2 = term(sc); // need to parse another term
                p1 = new ExpTreeNode(oper, p1, p2); // left associative
            } else {
                throw new ParseException("Incomplete expression. Last operater was: " + oper, -1);
            }
        }
        return p1 ;
    }

    private ExpTreeNode term(Scanner sc) throws ParseException{
        String oper;
        ExpTreeNode p1 , p2;
        p1 = factor(sc);
        while (sc.hasNext("\\*") || sc.hasNext("/")) {
            oper = sc.next();
            if (sc.hasNext()) {
                p2 = factor(sc); // need to parse a factor
                p1 = new ExpTreeNode(oper, p1, p2); // left associative
            } else {
                throw new ParseException("Incomplete expression. Last operater was: " + oper, -1);
            }
        }
        return p1 ;
    }

    private ExpTreeNode factor(Scanner sc) throws ParseException {
        if (sc.hasNext("\\(")) {
            sc.next();
            ExpTreeNode p1 = expression(sc);

            //Check for closing parentheses
            if (!(sc.hasNext("\\)"))) {
                throw new ParseException("Expression is missing closing parentheses", -1);
            }

            sc.next();
            return p1;
        } else {
            String next = sc.next();
            if (Character.isDigit(next.charAt(0))) {
                return new ExpTreeNode(next);
            } else {
                throw new ParseException("Invalid operand: " + next, -1);
            }
        }
    }
}
