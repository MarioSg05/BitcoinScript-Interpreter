import java.util.Arrays;
import java.util.List;

/**
 * Clase principal para la demostración del intérprete de Bitcoin Script.
 * @author Mario 
 */
public class Main {
    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();

        interpreter.setTrace(true);

        
        List<String> scriptP2PKH = Arrays.asList(
                "mi_firma", 
                "mi_llave_publica", 

                "OP_DUP",          
                "OP_HASH160",     
                "HASH(mi_llave_publica)", 
                "OP_EQUALVERIFY",  
                "OP_CHECKSIG"     
        );

        System.out.println("--- Iniciando Ejecución de Script P2PKH ---");
        
        boolean esValida = interpreter.execute(scriptP2PKH);
        
        System.out.println("\n--- Resumen de Validación ---");
        System.out.println("¿Transacción autorizada?: " + (esValida ? "SÍ" : "NO"));
    }
}