import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws ParseException {
	 ExpressionTree e = new ExpressionTree();
	 e.parse(" 9 * ( 7 + ( 6 - 8 ) ) / 2");
	 System.out.println(e.evaluate());
    }
}
