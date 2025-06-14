# AI-Powered Research Assistant

This project builds a smart research assistant using Spring Boot and the Google Gemini API. It allows users to:
Summarize selected text from a web page: The extension sends the selected text to the backend, which utilizes the Gemini API to generate a concise summary. (1:01)
Take notes in the browser: Users can save summaries or other notes directly within the extension, using local storage for persistence.

To run this project:

Backend (Spring Boot):
1. Clone the repository:
git clone 

2. Navigate to the backend directory:
cd backend

3. Install Dependencies:
mvn clean install

4. Update Application Properties:
Open resources/application.properties.
Replace Google Gemini API URL.
Replace with your own Google Gemini API key (obtain one for free from the Google AI Studio).

5. Run the Server:
mvn spring-boot:run

Frontend (Chrome Extension):

6. Clone the repository (if not already done):
git clone 

7. Open the frontend directory in Visual Studio Code:
code frontend

8. Load Unpacked Extension:
Go to Chrome's Extensions page (chrome://extensions).
Click "Load unpacked".
Select the frontend directory.

How it Works:
The extension's frontend is built with HTML, CSS, and JavaScript. It includes buttons to summarize text and save notes.
When a user selects text and clicks "Summarize", the extension sends a request to the backend.
The backend uses a Spring WebClient to communicate with the Gemini API.
The API request includes the selected text and operation ("summarize").
The Gemini API generates a summary, which is returned to the backend.
The backend sends the summary back to the extension.
The extension displays the summary in the results pane.
Saved notes are stored in local storage using chrome.storage.local.
