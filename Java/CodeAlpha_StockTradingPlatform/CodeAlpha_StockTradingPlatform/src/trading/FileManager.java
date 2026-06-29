package trading;

import java.io.*;
import java.util.*;

public class FileManager {
    private static final String DATA_DIR = "data";
    private static final String USERS_DIR = DATA_DIR + "/users";

    public FileManager() {
        new File(USERS_DIR).mkdirs();
    }

    // ── User save/load ────────────────────────────────────────────────────────

    /**
     * File layout (data/users/<username>.txt):
     *   Line 1:  username|passwordHash|cashBalance|createdAt
     *   Line 2+: HOLDING:<serialString>
     *   Then:    TXN:<serialString>
     */
    public void saveUser(UserAccount user) {
        File f = userFile(user.getUsername());
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            pw.println(user.getUsername() + "|" + user.getPasswordHash() + "|" +
                       String.format("%.4f", user.getCashBalance()) + "|" +
                       user.getCreatedAt());
            for (Holding h : user.getPortfolio().values())
                pw.println("HOLDING:" + h.toSerialString());
            for (Transaction t : user.getHistory())
                pw.println("TXN:" + t.toSerialString());
        } catch (IOException e) {
            System.err.println("[FileManager] Save failed: " + e.getMessage());
        }
    }

    public Optional<UserAccount> loadUser(String username) {
        File f = userFile(username);
        if (!f.exists()) return Optional.empty();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String header = br.readLine();
            if (header == null) return Optional.empty();
            String[] h = header.split("\\|");
            UserAccount acc = new UserAccount(h[0], h[1],
                Double.parseDouble(h[2]), h[3], true);

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("HOLDING:")) {
                    Holding holding = Holding.fromSerialString(line.substring(8));
                    acc.portfolioMutable().put(holding.getSymbol(), holding);
                } else if (line.startsWith("TXN:")) {
                    acc.historyMutable().add(Transaction.fromSerialString(line.substring(4)));
                }
            }
            return Optional.of(acc);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.err.println("[FileManager] Load failed: " + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean userExists(String username) {
        return userFile(username).exists();
    }

    public List<String> listUsers() {
        List<String> users = new ArrayList<>();
        File dir = new File(USERS_DIR);
        File[] files = dir.listFiles((d, n) -> n.endsWith(".txt"));
        if (files != null) {
            for (File f : files)
                users.add(f.getName().replace(".txt", ""));
        }
        return users;
    }

    private File userFile(String username) {
        return new File(USERS_DIR + "/" + username.toLowerCase() + ".txt");
    }
}
