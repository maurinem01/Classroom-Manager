# Classroom-Manager Demo
This is a demo version of the Classroom Manager App.  This program works by automatically sorting students throughout a "class session" as follows:
- Students who must be signed out are at the top of the list.
- Students who are in the warning time zone must start packing up.  These students appear below those who need to sign out immediately.
- Students who need to change subjects are displayed next.
- All other students appear at the bottom.
- Within each grouping, students with notes are displayed above those without.  This makes it easier for classroom staff to ensure the instructor's notes are addressed. 

## Requirements
- Java 17 or higher.  [Download Java here.](https://www.oracle.com/ca-en/java/technologies/downloads/)
- An internet connection.

## Try it out!
1. Download and extract ClassroomManagerDemo.zip.  Open Classroom-Manager.jar.
2. Click on **Configurations**.  Only the time format and background/foreground colours can be changed in the demo version.
3. Click on **Edit Students** and add fictional student info.  To test the texting feature, a valid phone number is required, and the Notifications checkbox must be checked under Contacts.  **NOTE: The database used for the demo version is publicly accessed.  Please do not enter actual student information and ensure you delete your contact information from the Edit Students window once you've finished trying the program out.**
4. From the main menu, click **Start Class**.
    - Sign in by double-clicking a student or highlighting a name and clicking the **>** button.  Do this for a couple of students, leaving a few seconds between each sign-in.
    - Signing your fictional student in will send a check-in message to the contact phone number associated with your student.
    - Leave the window open.  The order of the students will be sorted according to the description in the introduction of this README, and the colours will change based on what is set in the Configurations window.
    - Closing this window or manually signing out the student will also send a check-out message.
5.  From the main menu, click Send Text.  Double-click on your fictional student and click **Send**.  This will send a text message to the contact phone number associated with this student.
6.  Before closing the program, remember to erase your phone number!  This can be done through the **Edit Students** window.

## Program features
- Twilio sends automated messages to the associated phone number(s) when a student is checked in/out.

The following are part of the full program but are not included in the demo:
- ~~Acuity and Gmail~~ are used to send an email to the instructor when students check in outside of their time slot.
- ~~PDF Writer~~ loads a PDF of all student notes to be printed at the start of a classroom session.

## Final Notes
- The original program was designed to run on a local MySQL database with a singleton connection for fast responsiveness.  This demo creates a new connection for each transaction and closes it once the transaction is complete.  This means that the actual program performs a lot faster than the demo; this is crucial when signing in multiple students within a short timeframe.
- The highest tier of Acuity is required to have access to its API for integration with ClassroomManager.
- As of creating this program, a free tier of the Gmail API was available and sufficient for what this program required.  There is no guarantee this will be the case in the future.
- As mentioned above, the database used for the demo version is publicly accessed.  Please do not enter actual student information and ensure you delete your contact information from the Edit Students window once you've finished trying the program out.
