# Classroom-Manager Demo
This is a demo version of the Classroom Manager App.  This program works by automatically sorting students throughout a "class session" as follows:
- Students who need to be signed out are at the top of the list.
- Students who are in the warning time zone must start packing up.  These students appear below those that need to sign out immediately.
- Students who need to change subjects are displayed next.
- All other students appear at the bottom.
- Within each grouping, students who have notes are displayed above those who don't.  This makes it easier for classroom staff to ensure the instructor's notes are addressed. 

## Requirements
- Java 17 or higher.  [Download Java here.](https://www.oracle.com/ca-en/java/technologies/downloads/)
- An internet connection.

## Try it out!
1. Download and extract ClassroomManagerDemo.zip.  Open Classroom-Manager.jar.
2. Click on **Configurations**.  In this window, background and foreground colours and be changed for each of the groups:
    -  Over time limit
    -  Warning time zone
    -  Subject change
    -  No status (generally, this should be left white to avoid confusion)
3. Click on **Edit Students** and add some fictional student info.  To test the texting feature, a valid phone number is required, and the Notifications checkbox must be checked under Contacts.  **NOTE: The database used for the demo version is publicly accessed.  Please do not enter actual student information and ensure you delete your contact information from the Edit Students window once you've finished trying the program out.**
4. From the main menu, click **Start Class**.
    - Sign in by double clicking  your fictional student or highlight the name and click the **>** button.  This should send a check in message to the contact phone number associated with this student.
    - Leave the window open.  The colours will change according to what is set in the Configurations window.  For students enrolled in two subjects, the background will change when it is time to change subjects.  For all students, the background will also change when the warning time is reached, and when the session time has elapsed.  These times are not editable in the demo version.
    - Closing this window or manually signing out the student will also send a check out message.
5.  From the main menu, click Send Text.  Double click on your fictional student and click **Send**.  This will send a text message to the contact phone number associated with this student.
6.  Before closing the program, remember to erase your phone number!  This can be done through the **Edit Students** window.

## Program features
- ~~Acuity~~ schedules students at specific time slots.  This is not applicable for the demo.
- ~~Gmail~~ sends an email to the instructor when students check in outside of their time slot.  This is not applicable for the demo.
- ~~PDF Writer~~ loads a PDF of all student notes to be printed at the start of a classroom session.  Since this is only a demo, this feature has been disabled to optimize program speed.
- Twilio sends automated messages to the associated phone number(s) when a student is checked in/out.
