import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Representa la pila principal del intérprete de Bitcoin Script.
 * @author Mario 
 */
public class BitcoinStack {
    private final Deque<byte[]> stack = new ArrayDeque<>();

    public void push(byte[] data) {
        stack.push(data);
    }

    public byte[] pop() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Stack Underflow: Se intentó sacar un elemento de una pila vacía.");
        }
        return stack.pop();
    }

    public byte[] peek() {
        if (stack.isEmpty()) return null;
        return stack.peek();
    }

    public int size() {
        return stack.size();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void printStack() {
        System.out.print("Pila actual: ");
        for (byte[] b : stack) {
            String representation;
            if (b.length == 1 && (b[0] == 0 || b[0] == 1)) {
                representation = (b[0] == 1) ? "TRUE (1)" : "FALSE (0)";
            } else {
                representation = new String(b);
            }
            System.out.print("[" + representation + "] ");
        }
        System.out.println();
    }
}