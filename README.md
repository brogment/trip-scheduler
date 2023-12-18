Title: 
My Vacation Scheduler Hedgehog

Purpose: 
This application will allow you to create a vacation and attach excursion activities to it. You can set alarms to occur on the start and end dates of your vacation as well 
as the date of an excursion. You can also share details about your vacation through email, text, or to your clipboard. This app represents the MVP of a vacation scheduling app. 
        
Link to repo: https://gitlab.com/wgu-gitlab-environment/student-repos/wbro294/d308-mobile-application-development-android.git
Android version signed APK is deployed to: SDK 34

NOTE: 

.1.a
Vacations are added by clicking the plus button on the vacation list screen, filling out the fields,
and then clicking save through the vacation details screen menu. NOTE: The refresh option in the vacation list screen will do as it states, but is not needed 
during normal application flow.

.1.b
Once you've added an excursion to a vacation, attempting to delete the vacation through the menu on the vacation details screen will be blocked
and an alert will be displayed.

.2
These details are graphically represented on the vacation details screen.

.3.a
The detailed view of the vacation is represented on the vacation details screen, and adding and updating the information is done through
the 'Save vacation' menu option. If save is selected you will remain on the vacation details screen.

.3.b
The fields can be edited on the vacation details screen, and the vacation can be saved or deleted through the menu. Upon deletion you will be 
brought back to the vacation list screen.

.3.c
You can set a date by clicking on the space to the right of the Start/End Date label. Validation is implemented by making a clickable DatePicker 
widget the only way to input a date, keyboard is disabled. If you try to save a vacation 
where the date has not been set, it will be blocked with an appropriate alert.

.3.d
This can be demonstrated by setting the end date before the start date, and then trying to save the vacation through the menu on the vacation details screen. 
The action will be blocked and a popup will display

.3.e
First, notifications must be enabled for the app in system settings.
Alarms can then be set through the menu on the vacation details screen. It will set a notification/alarm for the start/end date that is currently on
the screen. If it's the first time you set an alarm, you will receive a pop up asking you to enable alarm permissions, 
and the alarm should be set again, this is only needed the first time you set the alarm.
If the date you set the alarm for is the current day in your system, it will go off. You can also set the system date forward and an alarm you set
previously for that day will go off. The alarm is represented as both a system notification and as an alert at the bottom of the screen.

.3.f
This is done through the share option in the menu of the vacation details screen. Upon selecting you will be able to send through messages, email,
or copy to clipboard.

.3.g
First an excursion is added by clicking the plus button on the vacation details screen, next you fill out the details on the
excursion details screen and then choose save on its menu. Upon returning to the previous screen, your excursion will be displayed under
the vacation.

.3.h.
A new excursion is added through the plus button on the vacation details screen, and existing excursions can be updated or deleted by 
clicking on the excursions name on the vacation details screen. That will take you to the excursion details screen to change the fields 
and then you can save or delete through the excursion details screen menu.

.4
These details are graphically represented on the excursion details screen.

.5.a
These details can be viewed by clicking on the name of an excursion on the vacation details screen.

.5.b
Upon clicking on an existing excursion name or adding a new excursion through the plus button, its fields can be changed on the 
excursion details screen that is displayed. Once filled in or changed, the excursion can be saved or deleted. 

.5.c
You can set a date by clicking on the space to the right of the Excursion Date label. Validation is implemented by making a clickable DatePicker
widget the only way to input a date, keyboard is disabled. If you try to save an excursion
where the date has not been set, it will be blocked with an appropriate alert.

.5.d
First, notifications must be enabled for the app in system settings if not already done.
An alarm can then be set through the menu on the excursion details screen. It will set a notification/alarm for the date that is currently on
the screen. If it's the first time you set an alarm, you will receive a pop up asking you to enable alarm permissions,
and the alarm should be set again, this is only needed the first time you set the alarm.
If the date you set the alarm for is the current day in your system, it will go off. You can also set the system date forward and an alarm you set
previously for that day will go off. The alarm is represented as both a system notification and as an alert at the bottom of the screen.

.5.e
If you try to save an excursion through the menu option on the excursion details screen with a date that is not between its associated vacation start and 
end dates, your action will be blocked with an appropriate alert.