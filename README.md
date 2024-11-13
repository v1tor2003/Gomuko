# Gomuko RMI Java Program

This project demonstrates a Remote Method Invocation (RMI) application in Java, where a client-server architecture is implemented. The server hosts the game sarver that the players can, via the clients access remotely.

## Project Structure

- `console/`: Contains console-related classes.
- `src/game/`: Contains game-related logic.
- `src/lib/`: Contains additional library classes.
- `src/interfaces/`: Defines the RMI interfaces shared between server and client.
- `src/Server.java`: The main class for starting the server.
- `src/Client.java`: The main class for starting the client.

## Prerequisites

- Java Development Kit (JDK) installed
- Ensure `rmiregistry` is in your system path

## Compiling the Program

Clone the repo:
```bash
git clone git@github.com:v1tor2003/Gomuko.git
```

And then cd into the dir:
```bash
cd Gomuko
```

## Compiling the Program

To compile the program, run the following command:

```bash
javac -sourcepath ./src -d ./out console/*.java src/game/*.java src/lib/*.java src/interfaces/*.java src/Server.java src/Client.java
```

This command compiles all `.java` files, placing the output into the `out/` directory.

## Running the Program

To run the server and client, follow these steps:

1. Change to the output directory:

   ```bash
   cd out
   ```

2. Start the RMI registry in the background:

   ```bash
   start rmiregistry
   ```

3. In the same terminal, start the server:

   ```bash
   java src.Server
   ```

4. Open another terminal, navigate to the `out` directory, and start the client:

   ```bash
   java src.Client
   ```

The server will be listening for client requests, and the client can invoke remote methods hosted by the server.

## Notes

- Ensure that the `rmiregistry` process is running before starting the server.
- The server and client need to have the correct paths and package structures, so check that the `out/` directory reflects the package hierarchy from `src/`.

## License

This project is licensed under the MIT License.

```

Let me know if you’d like any additional sections or details.