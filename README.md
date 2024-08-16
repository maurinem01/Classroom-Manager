# Classroom-Manager 
Classroom Manager is a program I developed to help manage student flow in and out of the classroom for local Kumon Centres.  

This program tracks students as they arrive and leave the centre.  When a student signs in, their name is added to the display projected to the classroom.  As the class progresses, the student's colour based the following:
- White: The student does not require special notice.
- Blue: The student needs to change subject.
- Yellow: The student's appointment time is almost elapsed and the student needs to start packing up.
- Red: The student has exceeded their appointment time and must leave as soon as possible.

These are the default colours and can be changed in the program's **Configuration** screen.

When a student signs in or out, a text is sent to their parent(s)/guardian(s) to notify them.  If a student arrives outside of their scheduled appointment, an email alert is sent to the instructor.

Here is a [video demonstration](Demo.mkv) of how the program works.


## v1.1 Updates
- A new log table in the database is required to make v1.1 run.  If updating from v1.0, run the query found in _install/kumon_db_v1.1_update.sql to create the new table.
- The original program was designed to send email notifications when students come outside of their appointment slots.  In v1.1, the email feature can be enabled by following the instructions in this README, and can be disabled simply by removing the email in credentials.properties.
- When the email feature is disabled, the attendance summary PDF opens at the end of the class session.
- The attendance summary PDF includes appointment information.  If the student attends outside of this appointment, it is highlighted in yellow.


## Requirements
This section covers the required installations for Classroom Manager to run.

### Java
Java is the language that Classroom Manager is written in and must be installed to run the program.  Go to https://www.oracle.com/java/technologies/downloads/#jdk20 to download Java 20.

Click on the link beside the x64 installer to download it. Once finished, open the .exe file and follow the prompts to install Java 20.

### MySQL
MySQL is the database used to hold student information, contact information, attendance logs, and configurations. This section outlines how to download, install, and configure MySQL.

**Step 1: Download MySQL.**  Download and install MySQL Community Server from https://dev.mysql.com/downloads/mysql/. Classroom Manager was created using MySQL Community Server 8.0.34 but you can choose the latest version. Select your version of choice, operating system, and preferred installer.
You will then be asked to log in or create an account. If you choose to continue the download without an account, click **“No thanks, just start my download”** on the bottom of the screen.

**Step 2: Launch the MySQL Server Setup Wizard.**  Make sure you are logged in as an Administrator as this step requires the installer to make changes to your computer.
- Open the file you downloaded in Step 1.
- On the welcome screen, click **Next**.
- On the End-User License Agreement screen, check **“I accept the terms in the License Agreement”** and click **Next**.
- On the Choose Setup Type screen, click **Typical** then click **Next**.
- Click **Install** on the next screen.
- Once the Wizard is complete, check **“Run MySQL Configurator”** and click **Finish**.

**Step 3: Configure MySQL.**  Now that MySQL is installed, it needs to be configured.
- On the welcome screen , click **Next >**.
- In the Type and Networking screen, the defaults can be used, using the following properties:
   - Config Type: Development Computer
   - Connectivity: TCP/IP, Port: 3306, X Protocol Port: 33060
   - Check "Open Windows Firewall ports for network access"
- In the Accounts and Roles screen, choose a password for the root (primary admin) user of the database. If additional users are needed, they can be added using the Add User button. **Keep this password in a safe place and do not forget it.**
- Open **credentials.properties** in the root folder of this repository. Add your password beside **pass=** to the root password for MySQL.
- On the Windows Service screen of the MySQL setup, the default settings can be kept:
   - Check "Configure MySQL Server as a Windows Service
   - Windows Service Name: keep default
   - Check "Start the MySQL Server at System Startup"
   - Run Windows As... Standard System Account
- In the Server File Permissions screen, the first option can be chosen (**“Yes, grant full access to the user running the Windows Service (if applicable) and the administrators group only. Other users and groups will not have access.”**). However, this assumes the computer that the program is running on is only accessed by trusted persons. Keep in mind this database will contain student name and contact information.
- In the next window, the Sample Databases are not needed. Leave them unchecked and click **Next >**.
- In the Apply Configuration screen, click **Execute**. Once it is complete, click **Next >**.
- Click **Finish** once the configuration is complete.

### HeidiSQL
HeidiSQL is a user interface used to access the MySQL database. If you prefer MySQL Workbench and are familiar with the process to install it, this section can be skipped. However, the rest of this guide will use HeidiSQL for importing the database.

**Step 1: Download and Install HeidiSQL.**  Download the latest version of HeidiSQL (https://www.heidisql.com/download.php).
- Open the downloaded .exe file and choose who to install HeidiSQL for.
   - If the program will be used on multiple accounts, choose “Install for all users (recommend)”.
   - If the program will only be used on one account, choose “Install for me only”.
- Accept the License Agreement and click **Next**.
- Choose the folder destination where HeidiSQL will be installed and click **Next**.
- A Start Menu folder is not required, but is optional.
- In the Set Additional Tasks window, check off **"Associate .SQL files with HeidiSQL"**.
- Click **Install** and then launch HeidiSQL.

**Step 2: Set up the database.**  After launching HeidiSQL, click the **New** button on the bottom-left of the screen.
- Enter the root user credentials from the MySQL installation and choose Port 3306.
- Click **Open**.
- In the next window, click **File > Run SQL File** and choose **_install/kumon_db.sql** from this repository's root folder.  This file generates the database used by the program.


## APIs Used
This section discusses the APIs used by Classroom Manager and how to link them to the program.  These services can be turned on and off through the **Configurations** window of the program.  Each API provides extensive documentation on how to create an account and where to find the necessary properties.  For brevity, this section will focus on outlining where to insert these properties to link them to Classroom Manager.

### Twilio 
Twilio allows the program to send text messages to parents when students arrive and are scheduled to leave the centre.

Create a Twilio account at https://www.twilio.com/

Log in to your new account.  On the dashboard, there is a pane called **Account Info** which contains an **Account SID** and **Auth Token**.  These two properties need to be entered in **credentials.properties** beside **twilio_account_sid=** and **twilio_auth_token=**, respectively.

In addition, a phone number must be purchased, and this property needs to be entered beside **twilio_from=**.

### Acuity Scheduling
Acuity Scheduling keeps track of when students are scheduled to be in the class and how long their appointments are.  When students arrive outside of their appointment slot, the instructor is alerted by email through the Gmail API (explained in the next section).

Create an Acuity Scheduuling account at https://acuityscheduling.com/

The API credentials needed can be found at https://developers.acuityscheduling.com/reference/quick-start under **Authentication**.  The User ID and API Key must be added to **acuity_user_id=** and **acuity_api_key=**, respectively.

### Gmail
Since there are limited slots in the classroom, the instructor needs to know immediately which students arrive outside of their appointment slots.  The Gmail API allows the programs to send email alerts to the instructor when a student's sign-in time does not match their appointment per Acuity Scheduling (discussed in the previous section).  The email address used to send and receive these messages will be the same Gmail account.

Go to https://console.cloud.google.com/apis/ and choose the Google account that the messages will be sent to and from.

Click on **Enabled APIs & Services**.  Search for **Gmail API** and click **Enable**.

Click on **Credentials**, **+ Create Credentials**, then **OAuth Client ID**.  The application type should be **Desktop app** and the name can be **Classroom Manager** to avoid confusion.  Once the OAuth client is created, a modal window will pop up with a button to **Download Json**.  Click this button and rename the file to **gmailClientSecret.json**.  This file needs to be moved to the **src\main\java\util** folder of this repository.

Back on the Google Cloud APIs & Services window, click on **OAuth consent screen**.  If this program is only for personal use or is used at a small scale, **External** User type will suffice.  Click **Create**.  On the next screen, click **Add or Remove Scopes**, then check off **../auth/gmail.compose**.  This will allow the program to send email messages to the connected Gmail account.

Finally, open **credentials.properties** and enter the email address beside **alert_email=**.
