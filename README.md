# 💬 Java ChatApp

A desktop-based chat application built using **Java Swing**, **Sockets**, and **MySQL**, with a focus on real-time communication and message persistence. The app features both a client and server UI, with message history stored in a MySQL database.

![ChatApp UI Screenshot](https://github.com/Zenoguy/ChatApp-Java/blob/main/Chatting%20Application/src/chatapp.png) 

## 🚀 Features

- Real-time one-to-one messaging (Client ↔ Server)
- Message persistence with **MySQL**
- Modern GUI built with **Java Swing**
- Scrollable chat interface with custom scrollbar UI
- Timestamped messages
- Keyboard-friendly (press Enter to send)
- Local database integration (JDBC)

---

## 🛠️ Tech Stack

| Layer           | Technology        |
|----------------|-------------------|
| Frontend (UI)  | Java Swing        |
| Backend        | Java + Sockets    |
| Database       | MySQL             |
| JDBC Connector | `mysql-connector-java` |

---

## 🧑‍💻 How to Run

### ✅ Prerequisites

- Java JDK 8 or higher
- MySQL installed and running
- MySQL Connector/J (JDBC driver)

### 📦 Setup Steps

1. **Clone the repository**

   git clone https://github.com/Zenoguy/ChatApp-Java.git
   cd ChatApp-Java

2. **Create MySQL Database**
   
CREATE DATABASE chatapp;

USE chatapp;

CREATE TABLE messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender VARCHAR(50),
    content TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

3.**Update DB credentials**

Edit DBConnection.java and replace:

String url = "jdbc:mysql://localhost:3306/chatapp";
String username = "your_username";
String password = "your_password";

4. **Run the Server**

Open your IDE or terminal and run Server.java.

5. **Run the Client**

In a separate window, run Client.java.

**Demo**
A sample message exchange between Client and Server:

Client: Hello!
Server: Hi there, welcome to ChatApp!

| Client View                        | Server View                        |
| ---------------------------------- | ---------------------------------- |
| ![Client UI](https://github.com/Zenoguy/ChatApp-Java/blob/main/Chatting%20Application/src/clientside.png) | ![Server UI](https://github.com/Zenoguy/ChatApp-Java/blob/main/Chatting%20Application/src/Serverside.png) |



## 🙏 Acknowledgements

I would like to thank [Code for Interview](https://www.youtube.com/@CodeForInterview) for the original chat application tutorial. This project is heavily inspired by their work.

> I've extended the original project by:
> - Revamping the **UI** with custom design and color themes  
> - Adding **MySQL database integration** to store chat history  
> - Implementing **chat history loading** on both client and server  
> - Handling multiple client connections (server-side threading)  
> - Improving UX with auto-scroll and Enter-to-send functionality


