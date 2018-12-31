# delivery-monitor

delivery-monitor is a graphical user interface (GUI) application written in Java that lets users register parcel informations and keep tracking the parcel shipping status. The parcel data are stored in an SQLite3 database. The service is ONLY available in South Korea.

### Prerequisites
- Java JDK : The version of Java JDK installed on your computer must be newer than 1.8.0. Java JDK can be downloaded from [here](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
- Maven: The version of Maven installed on your computer must be newer than 3.6.0. Download Maven from [here](https://maven.apache.org/download.cgi) and follow the [installation guides](https://maven.apache.org/install.html).

### Installation
Download delivery-monitor .jar file by downloading the [latest release](https://github.com/cylee-for-kuniv/cose102-delivery-monitor/releases/tag/1.0.1).

You may also download the source code and build yourself. 

To build the project, on the root directory of project folder, execute: 

<pre><code>$ mvn clean compile assembly:single</code></pre>

To run the program:

<pre><code>$ java -cp target/delivery_monitor-1.0.1-jar-with-dependencies.jar cose102.delivery_monitor.main.Main</code></pre>

### Usage

The default view of the application looks like following:

![Default View](https://t1.daumcdn.net/cfile/tistory/99505E3C5C121CF42C)

Visit [Sweet Tracker](https://tracking.sweettracker.co.kr/), register, login, and get a API key from [here](https://tracking.sweettracker.co.kr/templates/app.html#/apikey/add). Choose relevant type of key that matches your purpose of using this application. 

Copy your issued API key and paste it into "API key" text field. This should be done everytime before using any functions the application provides.

Click "Load Companies" button to get information about shipping companies. This __should__ be done if you have never done this before. 

Now, you are ready to use functions provided by the application.

#### Adding Parcels

Click "Add" button to register a new parcel shipping data to the database. 

![Parcel Adding Window](https://t1.daumcdn.net/cfile/tistory/99DA363C5C121CF531)

Select the company of the parcel, and input the invoice number of the parcel. Hit "Add" when the information relevantly has been input.

#### Deleting Parcels

Click "Delete" button to delete an existing parcel data. The application will disable the parcel and won't be shown in the future. This work cannot be undone.

#### Refreshing Parcels

Clicking "Refresh" button refreshes all existing active parcel data. It makes the application convenient to update the latest parcel shipping data. 

### Documentation and Live Demo

- You can check plan paper [here](https://drive.google.com/open?id=1IU-jh8RgiEkJKWn6oLtDjTTjp8i-qf0N) and report paper [here](https://drive.google.com/open?id=1GpJppDpYCSM3rhghUtUYIJTHq9tcQQQh). 
- You can view live demo video [here](https://drive.google.com/open?id=1DW_8yUV9zBUqSER105pWaaaAKQgBjqC3).

### Limitations

- The application is very vulnerable to exceptions. Further exception handling should be done properly in order to make the application work properly regardless of unexpected conditions.
- The API key is __not__ storable on the DB and therefore forces the user to copy-paste API key every time the user uses the application, which makes the application less convenient.
- Some parcel shipping data neither does not provide any parcel name nor proper one, which makes user be confused about their registered parcels. However, the application does not let the user change the name of the parcel. This function is needed to be added to the application.
