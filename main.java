import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        FileSystem fileSystem = new FileSystem();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                break;
            }

            String[] parts = input.split("\\s+");
            String command = parts[0];

            switch (command) {
                case "pwd":
                    System.out.println(fileSystem.getCurrentDirectory());
                    break;
                case "ls":
                    boolean recursive = false;
                    if (parts.length > 1 && parts[1].equals("-r")) {
                        recursive = true;
                    }
                    fileSystem.listContents(recursive);
                    break;
                case "mkdir":
                    if (parts.length < 2) {
                        System.out.println("Invalid Command: Missing directory name");
                    } else {
                        fileSystem.makeDirectory(parts[1]);
                    }
                    break;
                case "cd":
                    if (parts.length < 2) {
                        System.out.println("Invalid Command: Missing directory name");
                    } else {
                        fileSystem.changeDirectory(parts[1]);
                    }
                    break;
                case "touch":
                    if (parts.length < 2) {
                        System.out.println("Invalid Command: Missing file name");
                    } else {
                        fileSystem.createFile(parts[1]);
                    }
                    break;
                default:
                    System.out.println("Unrecognized command");
                    break;
            }
        }

        scanner.close();
    
    }
}

       
        class FileSystem {
    private String currentDirectory = "/root";
    private Map<String, List<String>> directories = new HashMap<>();

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void listContents(boolean recursive) {
        listContentsRecursive(currentDirectory, recursive);
    }

    private void listContentsRecursive(String directory, boolean recursive) {
        if (!directories.containsKey(directory)) {
            System.out.println("Directory not found");
            return;
        }
        
        List<String> contents = directories.get(directory);
        if (recursive) {
            System.out.println(directory + ":");
        }
        
        for (String item : contents) {
            System.out.println(item);
            if (recursive && directories.containsKey(directory + "/" + item)) {
                listContentsRecursive(directory + "/" + item, true);
            }
        }
    }

    public void makeDirectory(String dirName) {
        if (!directories.containsKey(currentDirectory)) {
            directories.put(currentDirectory, new ArrayList<>());
        }

        List<String> currentDirContents = directories.get(currentDirectory);
        if (currentDirContents.contains(dirName)) {
            System.out.println("Directory already exists");
        } else {
            currentDirContents.add(dirName);
            directories.put(currentDirectory, currentDirContents);
        }
    }

    public void changeDirectory(String dirName) {
        if (dirName.equals("..")) {
            int lastSlashIndex = currentDirectory.lastIndexOf('/');
            if (lastSlashIndex > 0) {
                currentDirectory = currentDirectory.substring(0, lastSlashIndex);
            }
        } else {
            if (!directories.containsKey(currentDirectory + "/" + dirName)) {
                System.out.println("Directory not found");
                return;
            }
            currentDirectory = currentDirectory + "/" + dirName;
        }
    }

    public void createFile(String fileName) {
        if (!directories.containsKey(currentDirectory)) {
            directories.put(currentDirectory, new ArrayList<>());
        }

        List<String> currentDirContents = directories.get(currentDirectory);
        if (currentDirContents.contains(fileName)) {
            System.out.println("File already exists");
        } else {
            currentDirContents.add(fileName);
            directories.put(currentDirectory, currentDirContents);
        }
    }
}
