## Description
- This is the backed of a web chat application where users can register, log in, search for other users to chat with and send/receive private messages with them.<br>
- In this group project my role was to develop this backend solution as a pair work with my fellow student Mirva.<br>
- I didn't take part in the frontend/UI development - that's why I've included only the codes for the backend.

<p></p>

## Technologies used
- Programming language: Java<br>
- Spring Framework<br>
- Messaging protocol: STOMP over WebSocket (used to handle 2-way messaging)<br>
- Database: MariaDB database in a cloud environment<br>
- Security features: SHA-256 hashing algorithm for password encryption
<p></p>

## Screenshots
- I've included a few screen shots for visualisation:

<p></p>

<img width="652" alt="Screenshot 2024-01-18 at 20 26 18" src="https://github.com/satukon/Chat-App-backend/assets/113008423/389820c5-9a19-4877-af61-1963b9183682"><p>
<img width="652" alt="Screenshot 2024-01-18 at 20 26 31" src="https://github.com/satukon/Chat-App-backend/assets/113008423/0bc03901-e4fb-4634-af15-82f1d72d570a"><p>
<img width="652" alt="Screenshot 2024-01-22 at 14 34 05" src="https://github.com/satukon/Chat-App-backend/assets/113008423/a4183309-c71e-41b0-9762-5b30bd567655"><p>

## Configuration / running the app
- Run from project root by using command: ``mvn spring-boot:run``<br>
- Please note thqt as application.properties file containing the onnection string to to the MariaDB datavase is not included, database access won't work.

<p></p>
