const readline = require('readline');

class Main {
    constructor() {
        this.fileSystem = new FileSystem();
    }

    start() {
        const rl = readline.createInterface({
            input: process.stdin,
            output: process.stdout
        });

        rl.on('line', (input) => {
            input = input.trim();
            if (input.toLowerCase() === 'quit') {
                rl.close();
            } else {
                this.processInput(input);
            }
        });
    }

    processInput(input) {
        const parts = input.split(/\s+/);
        const command = parts[0];

        switch (command) {
            case "pwd":
                console.log(this.fileSystem.getCurrentDirectory());
                break;
            case "ls":
                const recursive = parts.includes("-r");
                this.fileSystem.listContents(recursive);
                break;
            case "mkdir":
                if (parts.length < 2) {
                    console.log("Invalid Command: Missing directory name");
                } else {
                    this.fileSystem.makeDirectory(parts[1]);
                }
                break;
            case "cd":
                if (parts.length < 2) {
                    console.log("Invalid Command: Missing directory name");
                } else {
                    this.fileSystem.changeDirectory(parts[1]);
                }
                break;
            case "touch":
                if (parts.length < 2) {
                    console.log("Invalid Command: Missing file name");
                } else {
                    this.fileSystem.createFile(parts[1]);
                }
                break;
            default:
                console.log("Unrecognized command");
                break;
        }
    }
}

class FileSystem {
    constructor() {
        this.currentDirectory = "/root";
        this.directories = {};
        this.directories[this.currentDirectory] = [];
    }

    getCurrentDirectory() {
        return this.currentDirectory;
    }

    listContents(recursive) {
        this.listContentsRecursive(this.currentDirectory, recursive);
    }

    listContentsRecursive(directory, recursive) {
        if (!this.directories.hasOwnProperty(directory)) {
            console.log("Directory not found");
            return;
        }

        const contents = this.directories[directory];
        if (recursive) {
            console.log(directory + ":");
        }

        for (const item of contents) {
            console.log(item);
            if (recursive && this.directories.hasOwnProperty(directory + "/" + item)) {
                this.listContentsRecursive(directory + "/" + item, true);
            }
        }
    }

    makeDirectory(dirName) {
        if (!this.directories.hasOwnProperty(this.currentDirectory)) {
            this.directories[this.currentDirectory] = [];
        }

        const currentDirContents = this.directories[this.currentDirectory];
        if (currentDirContents.includes(dirName)) {
            console.log("Directory already exists");
        } else {
            currentDirContents.push(dirName);
            this.directories[this.currentDirectory] = currentDirContents;
        }
    }

    changeDirectory(dirName) {
        if (dirName === "..") {
            const lastSlashIndex = this.currentDirectory.lastIndexOf('/');
            if (lastSlashIndex > 0) {
                this.currentDirectory = this.currentDirectory.substring(0, lastSlashIndex);
            }
        } else {
            if (!this.directories.hasOwnProperty(this.currentDirectory + "/" + dirName)) {
                console.log("Directory not found");
                return;
            }
            this.currentDirectory = this.currentDirectory + "/" + dirName;
        }
    }

    createFile(fileName) {
        if (!this.directories.hasOwnProperty(this.currentDirectory)) {
            this.directories[this.currentDirectory] = [];
        }

        const currentDirContents = this.directories[this.currentDirectory];
        if (currentDirContents.includes(fileName)) {
            console.log("File already exists");
        } else {
            currentDirContents.push(fileName);
            this.directories[this.currentDirectory] = currentDirContents;
        }
    }
}

const main = new Main();
main.start();