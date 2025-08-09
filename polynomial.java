import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class polynomial {

    public static void main(String[] args) throws Exception {
        // Load JSON file path from args[0]
        JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(args[0]))));

        // Extract number of roots (n) and minimum roots needed (k)
        int n = json.getJSONObject("keys").getInt("n");
        int k = json.getJSONObject("keys").getInt("k");

        // Store points (x, y)
        List<int[]> points = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            if (json.has(String.valueOf(i))) {
                JSONObject root = json.getJSONObject(String.valueOf(i));
                int x = i;
                int base = Integer.parseInt(root.getString("base"));
                String val = root.getString("value");
                int y = Integer.parseInt(val, base);
                points.add(new int[] { x, y });
            }
        }

        // Use first k points to calculate constant term
        List<int[]> pointsToUse = points.subList(0, k);

        int constantTerm = calculateConstantTerm(pointsToUse);
        System.out.println(constantTerm);
    }

    // Lagrange interpolation at x=0 gives constant term c
    private static int calculateConstantTerm(List<int[]> points) {
        int result = 0;
        int n = points.size();
        for (int i = 0; i < n; i++) {
            int xi = points.get(i)[0];
            int yi = points.get(i)[1];

            int numerator = 1;
            int denominator = 1;
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    numerator *= -points.get(j)[0];
                    denominator *= (xi - points.get(j)[0]);
                }
            }
            result += yi * numerator / denominator;
        }
        return result;
    }
}
