import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Motor de ejecución para el subconjunto de Bitcoin Script.
 * 
 * @author Mario
 */
public class Interpreter {
    private final BitcoinStack stack = new BitcoinStack();
    private final Deque<Boolean> conditionStack = new ArrayDeque<>();
    private boolean traceEnabled = false;

    public void setTrace(boolean enabled) {
        this.traceEnabled = enabled;
    }

    public boolean execute(List<String> script) {
        try {
            for (String instruction : script) {
                boolean skip = !conditionStack.isEmpty() && !conditionStack.peek();

                if (skip && !isControlFlow(instruction)) {
                    continue;
                }

                if (traceEnabled)
                    System.out.println("Ejecutando: " + instruction);

                if (instruction.startsWith("OP_")) {
                    handleOpCode(instruction);
                } else {
                    stack.push(instruction.getBytes());
                }

                if (traceEnabled)
                    stack.printStack();
            }
            return !stack.isEmpty() && isTrue(stack.peek()) && conditionStack.isEmpty();
        } catch (Exception e) {
            System.err.println("Error de ejecución: " + e.getMessage());
            return false;
        }
    }

    private boolean isControlFlow(String op) {
        return op.equals("OP_IF") || op.equals("OP_ELSE") || op.equals("OP_ENDIF");
    }

    private void handleOpCode(String op) {
        switch (op) {
            case "OP_0":
            case "OP_FALSE":
                stack.push(new byte[] { 0 });
                break;
            case "OP_1":
            case "OP_TRUE":
                stack.push(new byte[] { 1 });
                break;
            case "OP_DUP":
                stack.push(stack.peek().clone());
                break;
            case "OP_DROP":
                stack.pop();
                break;
            case "OP_ADD":
                performArithmetic((a, b) -> a + b);
                break;
            case "OP_SUB":
                performArithmetic((a, b) -> a - b);
                break;
            case "OP_EQUAL":
                executeEqual();
                break;
            case "OP_EQUALVERIFY":
                executeEqual();
                if (!isTrue(stack.pop()))
                    throw new RuntimeException("OP_EQUALVERIFY falló");
                break;
            case "OP_IF":
                conditionStack.push(isTrue(stack.pop()));
                break;
            case "OP_ELSE":
                if (conditionStack.isEmpty())
                    throw new RuntimeException("OP_ELSE sin OP_IF");
                conditionStack.push(!conditionStack.pop());
                break;
            case "OP_ENDIF":
                if (conditionStack.isEmpty())
                    throw new RuntimeException("OP_ENDIF sin OP_IF");
                conditionStack.pop();
                break;
            case "OP_HASH160":
                stack.push(("HASH(" + new String(stack.pop()) + ")").getBytes());
                break;
            case "OP_CHECKSIG":
                stack.pop();
                stack.pop(); 
                stack.push(new byte[] { 1 });
                break;
            case "OP_GREATERTHAN":
                int b_val = bytesToInt(stack.pop()); 
                int a_val = bytesToInt(stack.pop());
                stack.push((a_val > b_val) ? new byte[] { 1 } : new byte[] { 0 });
                break;
            default:
                throw new UnsupportedOperationException("Opcode no soportado: " + op);
        }
    }

    private void executeEqual() {
        byte[] a = stack.pop();
        byte[] b = stack.pop();
        stack.push(java.util.Arrays.equals(a, b) ? new byte[] { 1 } : new byte[] { 0 });
    }

    private void performArithmetic(java.util.function.BiFunction<Integer, Integer, Integer> op) {
        int b = bytesToInt(stack.pop());
        int a = bytesToInt(stack.pop());
        stack.push(String.valueOf(op.apply(a, b)).getBytes());
    }

    private boolean isTrue(byte[] data) {
        if (data == null || data.length == 0)
            return false;
        for (byte b : data)
            if (b != 0)
                return true;
        return false;
    }

    private int bytesToInt(byte[] data) {
        try {
            return Integer.parseInt(new String(data));
        } catch (Exception e) {
            return data[0];
        }
    }
}