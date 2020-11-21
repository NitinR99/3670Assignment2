# 3670Assignment2
Submission for group 11
## How to run?
Compile JobCreator.java and JobSeeker.java using command prompt.
```bash
javac JobCreator.java
javac JobSeeker.java
```
and then run it using these commands (in order).
```bash
java JobCreator
java JobSeeker
```
The line "java JobSeeker" can be run many times. (Each Jobseeker is a client)

## How to use?

**Step 1:** Run JobCreator

<img src="Screenshots/1-initial.png" width=500>

**Step 2:** Run JobSeeker in a new command prompt window

<img src="Screenshots/2-newClient.png" width=500>

**Step 3:** You can also run multiple JobSeekers to connect to the same JobCreator, or you can skip this step.

<img src="Screenshots/3-multipleClients.png" width=800>

**Step 4:** You can view the current connections to the JobCreator by choosing option "1" on the JobCreator command window.

<img src="Screenshots/4-checkConnection.png" width=500>

**Step 5:** Now, you can assign jobs to any connect JobSeeker by selecting option "2" and selecting a free client.

<img src="Screenshots/5-assigningJobs.png" width=800>

After any client is done with it's assigned job, it will send a confirmation string to the JobCreator to be displayed

<img src="Screenshots/6-gettingResult.png" width=500>

## Additional Information

At any time during the execution, the JobSeeker can terminate connection by entering ctrl-c on their command window. The JobCreator is also able to terminate any unwanted connection by using menu option "3".
