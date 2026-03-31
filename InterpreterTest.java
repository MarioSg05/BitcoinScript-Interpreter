import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class InterpreterTest {

    private Interpreter interpreter;

    @BeforeEach
    public void setUp() {
        interpreter = new Interpreter();
    }

    @Test
    public void testAritmeticaBasica() {
        List<String> script = Arrays.asList("10", "4", "OP_SUB");
        interpreter.execute(script);
        assertTrue(interpreter.execute(script), "La resta 10-4 debería resultar en un valor verdadero");
    }

    @Test
    public void testCondicionalTrue() {
        List<String> script = Arrays.asList("OP_1", "OP_IF", "GANE", "OP_ELSE", "PERDI", "OP_ENDIF");
        assertTrue(interpreter.execute(script));
    }

    @Test
    public void testCondicionalFalse() {
        List<String> script = Arrays.asList("OP_0", "OP_IF", "GANE", "OP_ELSE", "PERDI", "OP_ENDIF");
        assertTrue(interpreter.execute(script));
    }

    @Test
    public void testComparacionMayorQue() {
        List<String> script = Arrays.asList("10", "5", "OP_GREATERTHAN");
        assertTrue(interpreter.execute(script));
        
        List<String> scriptFalse = Arrays.asList("5", "10", "OP_GREATERTHAN");
        assertFalse(interpreter.execute(scriptFalse));
    }

    @Test
    public void testP2PKH_Exitoso() {
        List<String> script = Arrays.asList(
            "sig", "pubk", "OP_DUP", "OP_HASH160", "HASH(pubk)", "OP_EQUALVERIFY", "OP_CHECKSIG"
        );
        assertTrue(interpreter.execute(script), "El flujo P2PKH estándar debería ser válido");
    }

    @Test
    public void testErrorPilaVacia() {
        List<String> script = Arrays.asList("OP_ADD");
        assertFalse(interpreter.execute(script), "Un script con Stack Underflow debe retornar false");
    }
}