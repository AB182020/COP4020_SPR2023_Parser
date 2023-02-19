import edu.ufl.cise.plcsp23.CompilerComponentFactory;
import edu.ufl.cise.plcsp23.IParser;
import edu.ufl.cise.plcsp23.LexicalException;
import edu.ufl.cise.plcsp23.PLCException;

public class Main
{
    public static void main(String[] args)
    {
        String input = "-3";
        try {
            IParser parser =  CompilerComponentFactory.makeAssignment2Parser(input);
            parser.parse();
        } catch (LexicalException e) {
            throw new RuntimeException(e);
        } catch (PLCException e) {
            throw new RuntimeException(e);
        }

    }
}