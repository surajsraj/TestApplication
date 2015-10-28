# TestApplication

Application with the below features. 

1. Widget contains a class extending AppWidgetProvider , layout file for the widget, xml file containing info about widget, and the Manifest file updated with receiver class for the widget.

2. Notification contains a Notify class with a Notification Manager and NotifyBuilder.Compact to create a Inbox style notification.

3. Connection Settings implementing a Preference screen and SharedPreferences for the connection type i.e. Wifi or Any, Network Activity with a Connection Manager and Webview to display webpage based on preference.

4. A Broadcast receiver to detect changes to the connection and based on that - update IP address and the refresh the webpage.

5. Native capabilities like - SMS, Native dialler, Contacts.

6. Accelerometer to get the readings.

7. Using the Google API client & Play service get the latitude and longitude.
